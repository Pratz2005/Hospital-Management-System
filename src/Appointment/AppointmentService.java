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

        // Step 1: Validate Doctor ID with slot availability check
        while (true) {
            System.out.print("Enter Doctor ID: ");
            doctorID = scanner.nextLine();
            if (isValidDoctorID(doctorID) && hasAvailableSlots(doctorID)) {
                break;
            } else {
                System.out.println("The selected doctor has no available slots. Please choose another doctor.");
            }
        }

        // Step 2: Validate Date and Check Slot Availability on the Date
        while (true) {
            System.out.print("Enter the date (e.g., DD-MM-YY): ");
            date = scanner.nextLine();
            if (isValidDateFormat(date) && isDoctorAvailableOnDate(doctorID, date)) {
                break;
            } else if (!isValidDateFormat(date)) {
                System.out.println("Invalid date format. Please use DD-MM-YY.");
            } else {
                System.out.println("The doctor is not available on this date or has no available slots. Please choose another date.");
            }
        }

        // Step 3: Validate Time Slot
        while (true) {
            System.out.print("Enter the time slot (e.g., 09:00): ");
            timeSlot = scanner.nextLine();
            String formattedTimeSlot = formatToHalfHourSlot(timeSlot);

            if (!formattedTimeSlot.isEmpty() && isValidTimeSlotFormat(formattedTimeSlot) && isAvailableSlot(doctorID, date, formattedTimeSlot)) {
                timeSlot = formattedTimeSlot; // Use the correctly formatted time slot
                break;
            } else {
                System.out.println("Invalid time slot or unavailable. Please check available slots for this doctor.");
            }
        }

        // Generate and save appointment details
        String appointmentID = generateAppointmentID();
        saveAppointmentDetails(appointmentID, doctorID, patientID, date, timeSlot, "pending");
        updateSlotStatus(doctorID, date, timeSlot, "booked");
        return appointmentID;
    }

    // Check if the doctor has any available slots
    private boolean hasAvailableSlots(String doctorID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(DOCTOR_AVAILABILITY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].equals(doctorID) && fields[4].equalsIgnoreCase("available")) {
                    return true; // Doctor has at least one available slot
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading DoctorAvailability.csv: " + e.getMessage());
        }
        return false; // All slots are booked
    }

    // Check if the doctor has available slots on the given date
    public boolean isDoctorAvailableOnDate(String doctorID, String date) {
        boolean hasAvailableSlots = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(DOCTOR_AVAILABILITY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].equals(doctorID) && fields[2].equals(date) && fields[4].equalsIgnoreCase("available")) {
                    hasAvailableSlots = true;
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading DoctorAvailability.csv: " + e.getMessage());
        }
        return hasAvailableSlots;
    }



    // Helper method to check if Doctor ID is valid
// Checks if the doctor exists in User.csv and DoctorAvailability.csv
    public boolean isValidDoctorID(String doctorID) {
        // Check if doctor ID exists in User.csv
        boolean doctorExistsInUser = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].equals(doctorID) && fields[2].equalsIgnoreCase("Doctor")) {
                    doctorExistsInUser = true;
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading User.csv: " + e.getMessage());
        }

        // Check if doctor ID exists in DoctorAvailability.csv
        if (doctorExistsInUser && isDoctorInAvailability(doctorID)) {
            return true;
        } else if (doctorExistsInUser) {
            System.out.println("The doctor is not available on any date.");
        } else {
            System.out.println("Doctor ID not found. Please enter a valid Doctor ID.");
        }
        return false;
    }

    // New helper method to check if the doctor ID exists in DoctorAvailability.csv
    private boolean isDoctorInAvailability(String doctorID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(DOCTOR_AVAILABILITY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].equals(doctorID)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading DoctorAvailability.csv: " + e.getMessage());
        }
        return false;
    }


    // Check if the date is in DD-MM-YY format
    public boolean isValidDateFormat(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Check if the time slot is in HH:MM-HH:MM format
    private boolean isValidTimeSlotFormat(String timeSlot) {
        String timeSlotPattern = "^\\d{2}:\\d{2}-\\d{2}:\\d{2}$";
        return Pattern.matches(timeSlotPattern, timeSlot);
    }


    // Check availability in DoctorAvailability.csv
    private boolean isAvailableSlot(String doctorID, String date, String timeSlot) {
        try (BufferedReader reader = new BufferedReader(new FileReader(DOCTOR_AVAILABILITY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(doctorID) && data[2].equals(date) && data[3].equals(timeSlot) && data[4].equalsIgnoreCase("available")) {
                    return true;
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
                // Save the old date and time slot before updating
                String oldDate = appointment[3];
                String oldTimeSlot = appointment[4];

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
                    newTimeSlot = formatToHalfHourSlot(newTimeSlot);
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
                rescheduleSlotStatus(doctorID, oldDate, oldTimeSlot, newDate, newTimeSlot);
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
                updateSlotStatus(doctorID, date, timeSlot, "available");

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

    // Method to update the status of a specific slot
    public void updateSlotStatus(String doctorID, String date, String timeSlot, String newStatus) {
        List<String[]> availabilityData = loadDoctorAvailability();
        boolean slotUpdated = false;

        for (String[] entry : availabilityData) {
            if (entry[0].equals(doctorID) && entry[2].equals(date) && entry[3].equals(timeSlot)) {
                entry[4] = newStatus; // Update status to either "available" or "booked"
                slotUpdated = true;
                break;
            }
        }

        if (slotUpdated) {
            saveDoctorAvailability(availabilityData);
        } else {
            System.out.println("Slot not found in DoctorAvailability.csv.");
        }
    }

    // Method to update the status for rescheduling an appointment
    private void rescheduleSlotStatus(String doctorID, String oldDate, String oldTimeSlot, String newDate, String newTimeSlot) {
        List<String[]> availabilityData = loadDoctorAvailability();
        boolean oldSlotUpdated = false;
        boolean newSlotUpdated = false;

        for (String[] entry : availabilityData) {
            if (entry[0].equals(doctorID) && entry[2].equals(oldDate) && entry[3].equals(oldTimeSlot)) {
                entry[4] = "available"; // Set the old slot to "available"
                oldSlotUpdated = true;
            }
            if (entry[0].equals(doctorID) && entry[2].equals(newDate) && entry[3].equals(newTimeSlot)) {
                entry[4] = "booked"; // Set the new slot to "unavailable"
                newSlotUpdated = true;
            }
        }

        if (oldSlotUpdated && newSlotUpdated) {
            saveDoctorAvailability(availabilityData);
        } else {
            //System.out.println("One or both slots not found in DoctorAvailability.csv.");
        }
    }

    // Helper method to load DoctorAvailability.csv into a List of String arrays
    private List<String[]> loadDoctorAvailability() {
        List<String[]> availabilityData = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DOCTOR_AVAILABILITY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                availabilityData.add(line.split(","));
            }
        } catch (IOException e) {
            System.err.println("Error reading DoctorAvailability.csv: " + e.getMessage());
        }
        return availabilityData;
    }

    // Helper method to save the updated availability data back to DoctorAvailability.csv
    private void saveDoctorAvailability(List<String[]> availabilityData) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DOCTOR_AVAILABILITY_FILE))) {
            for (String[] entry : availabilityData) {
                writer.write(String.join(",", entry));
                writer.newLine();
            }
            //System.out.println("Doctor availability saved successfully."); // Debugging output
        } catch (IOException e) {
            System.err.println("Error saving DoctorAvailability.csv: " + e.getMessage());
        }
    }

    private String formatToHalfHourSlot(String time) {
        try {
            String[] parts = time.split(":");
            int hour = Integer.parseInt(parts[0]); // Attempt to parse hour
            int minute = Integer.parseInt(parts[1]); // Attempt to parse minute

            String start = String.format("%02d:%02d", hour, minute);
            String end;

            if (minute == 0) {
                end = String.format("%02d:%02d", hour, 30);
            } else {
                end = String.format("%02d:%02d", (hour + 1) % 24, 0);
            }

            return start + "-" + end;

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            //System.out.println("Invalid time format. Please enter time in HH:MM format.");
            return ""; // Return an empty string to indicate invalid format
        }
    }

}
