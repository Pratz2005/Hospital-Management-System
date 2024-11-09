package Appointment;

import java.io.*;
import java.util.*;
import java.util.Random;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class AppointmentService extends DoctorAvailabilityService implements AppointmentManager {
    private static final String APPOINTMENT_FILE = "src/Files/Appointment.csv";
    private static final String USER_FILE = "src/Files/User.csv";
    private static final String DOCTOR_AVAILABILITY_FILE = "src/Files/DoctorAvailability.csv";

    @Override
    public String scheduleAppointment(String patientID) {
        Scanner scanner = new Scanner(System.in);
        String doctorID;
        String date;
        String timeSlot;

        // Step 1: Validate Doctor ID
        while (true) {
            System.out.print("Enter Doctor ID: ");
            doctorID = scanner.nextLine();
            if (isValidDoctorID(doctorID)) {
                break;
            } else {
                System.out.println("Invalid Doctor ID. Please enter a valid Doctor ID.");
            }
        }

        // Step 2: Validate Date and Check Availability
        while (true) {
            System.out.print("Enter the date (e.g., DD-MM-YY): ");
            date = scanner.nextLine();
            if (isValidDateFormat(date) && isDoctorAvailableOnDate(doctorID, date)) {
                break;
            } else if (!isValidDateFormat(date)) {
                System.out.println("Invalid date format. Please use DD-MM-YY.");
            } else {
                System.out.println("The doctor is not available on this date. Please choose another date.");
            }
        }

        // Step 3: Validate Time Slot
        while (true) {
            System.out.print("Enter the time slot (e.g., 09:00): ");
            timeSlot = scanner.nextLine();
            if (isValidTimeSlotFormat(timeSlot) && isAvailableSlot(doctorID, date, timeSlot)) {
                break;
            } else {
                System.out.println("Invalid time slot or unavailable. Please check available slots for this doctor.");
            }
        }

        // Generate and save appointment details
        String appointmentID = generateAppointmentID();
        saveAppointmentDetails(appointmentID, doctorID, patientID, date, timeSlot, "pending");

        return appointmentID;
    }


    // Helper method to check if Doctor ID is valid
    private boolean isValidDoctorID(String doctorID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].equals(doctorID) && fields[2].equalsIgnoreCase("Doctor")) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading User.csv: " + e.getMessage());
        }
        return false;
    }

    // Check if the date is in DD-MM-YY format
    private boolean isValidDateFormat(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Check if the doctor is available on the given date
    private boolean isDoctorAvailableOnDate(String doctorID, String date) {
        try (BufferedReader reader = new BufferedReader(new FileReader(DOCTOR_AVAILABILITY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].equals(doctorID) && fields[2].equals(date) && fields[4].equalsIgnoreCase("available")) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading DoctorAvailability.csv: " + e.getMessage());
        }
        return false;
    }

    // Check if the time slot is in HH:MM format
    private boolean isValidTimeSlotFormat(String timeSlot) {
        String timeSlotPattern = "^\\d{2}:\\d{2}$";
        return Pattern.matches(timeSlotPattern, timeSlot);
    }

    // Check availability in DoctorAvailability.csv
    private boolean isAvailableSlot(String doctorID, String date, String timeSlot) {
        try (BufferedReader reader = new BufferedReader(new FileReader(DOCTOR_AVAILABILITY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(doctorID) && data[2].equals(date) && data[4].equalsIgnoreCase("available")) {
                    String[] slotRange = data[3].split("-");
                    if (slotRange.length == 2 && slotRange[0].equals(timeSlot)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading DoctorAvailability.csv: " + e.getMessage());
        }
        return false;
    }

    // Generate a unique Appointment ID
    private String generateAppointmentID() {
        Random random = new Random();
        return "AP" + (random.nextInt(900) + 100);
    }

    // Save appointment details to Appointment.csv
    private void saveAppointmentDetails(String appointmentID, String doctorID, String patientID, String date, String timeSlot, String status) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(APPOINTMENT_FILE, true))) {
            String line = String.join(",", appointmentID, doctorID, patientID, date, timeSlot, status);
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving appointment: " + e.getMessage());
        }
    }

    // Method to get doctor's name by ID from User.csv
    private String getDoctorNameByID(String doctorID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].equals(doctorID) && fields[2].equalsIgnoreCase("Doctor")) {
                    return fields[3];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unknown Doctor";
    }

    // Load all appointments from Appointment.csv
    private List<String[]> loadAppointments() {
        List<String[]> appointments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(APPOINTMENT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                appointments.add(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    // Save all appointments back to Appointment.csv
    private void saveAppointments(List<String[]> appointments) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(APPOINTMENT_FILE))) {
            for (String[] appointment : appointments) {
                writer.write(String.join(",", appointment));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rescheduleAppointment(String appointmentID) {
        List<String[]> appointments = loadAppointments();
        boolean valid = false;
        Scanner scanner = new Scanner(System.in);

        for (String[] appointment : appointments) {
            if (appointment[0].equals(appointmentID)) {
                String doctorID = appointment[1];

                // Step 1: Validate New Date and Check Availability
                String newDate;
                while (true) {
                    System.out.print("Enter the new date (e.g., DD-MM-YY): ");
                    newDate = scanner.nextLine();
                    if (isValidDateFormat(newDate) && isDoctorAvailableOnDate(doctorID, newDate)) {
                        break;
                    } else if (!isValidDateFormat(newDate)) {
                        System.out.println("Invalid date format. Please use DD-MM-YY.");
                    } else {
                        System.out.println("The doctor is not available on this date. Please choose another date.");
                    }
                }

                // Step 2: Validate New Time Slot
                String newTimeSlot;
                while (true) {
                    System.out.print("Enter the new time slot (e.g., 09:00): ");
                    newTimeSlot = scanner.nextLine();
                    if (isValidTimeSlotFormat(newTimeSlot) && isAvailableSlot(doctorID, newDate, newTimeSlot)) {
                        break;
                    } else {
                        System.out.println("Invalid time slot or unavailable. Please check available slots for this doctor.");
                    }
                }

                String doctorName = getDoctorNameByID(doctorID);
                updateDoctorAvailability(doctorID, doctorName, appointment[3], appointment[4], true);
                appointment[3] = newDate;
                appointment[4] = newTimeSlot;
                appointment[5] = "pending";
                updateDoctorAvailability(doctorID, doctorName, newDate, newTimeSlot, false);
                valid = true;
                break;
            }
        }

        if (valid) {
            saveAppointments(appointments);
            System.out.println("Appointment rescheduled successfully.");
        } else {
            System.out.println("Appointment ID not found.");
        }
    }

    @Override
    public void cancelAppointment(String appointmentID) {
        List<String[]> appointments = loadAppointments();
        boolean appointmentFound = false;

        for (String[] appointment : appointments) {
            if (appointment[0].equals(appointmentID)) {
                String doctorID = appointment[1];
                String date = appointment[3];
                String timeSlot = appointment[4];

                // Update the appointment status to canceled
                appointment[5] = "canceled";
                appointmentFound = true;

                break;
            }
        }

        if (appointmentFound) {
            saveAppointments(appointments);
            System.out.println("Appointment canceled successfully.");
        } else {
            System.out.println("Appointment ID not found.");
        }
    }


    @Override
    public String viewAppointmentStatus(String appointmentID) {
        List<String[]> appointments = loadAppointments();
        for (String[] appointment : appointments) {
            if (appointment[0].equals(appointmentID)) {
                return appointment[5];
            }
        }
        return "Appointment not found.";
    }
}
