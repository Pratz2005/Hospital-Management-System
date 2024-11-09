package Appointment;

import java.io.*;
import java.util.*;
import java.util.Random;
import java.lang.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;


public class AppointmentService extends DoctorAvailabilityService implements AppointmentManager {
    private static final String APPOINTMENT_FILE = "src/Files/Appointment.csv";
    private static final String USER_FILE = "src/Files/User.csv";  // Add this line for the User.csv file
    private static final String DOCTOR_AVAILABILITY_FILE = "src/Files/DoctorAvailability.csv";

    // Method to validate date format, time slot format, and availability
    public boolean validateAppointmentDetails(String doctorID, String date, String timeSlot) {
        if (!isValidDateFormat(date)) {
            System.out.println("Invalid date format. Please use DD-MM-YY.");
            return false;
        }

        if (!isValidTimeSlotFormat(timeSlot)) {
            System.out.println("Invalid time slot format. Please use HH:MM-HH:MM.");
            return false;
        }

        if (!isAvailableSlot(doctorID, date, timeSlot)) {
            System.out.println("Invalid time slot or unavailable. Please check available slots for this doctor.");
            return false;
        }

        return true;
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

    // Check if the time slot is in HH:MM-HH:MM format
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
                if (data[0].equals(doctorID) && data[2].equals(date) && data[4].equals("available")) {
                    String[] slotRange = data[3].split("-");
                    if (slotRange.length == 2 && slotRange[0].equals(timeSlot)) {
                        return true;  // Match if the start time equals the input timeSlot
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading DoctorAvailability.csv: " + e.getMessage());
        }
        return false;
    }


    @Override
    public String scheduleAppointment(String doctorID, String patientID, String date, String timeSlot) {
        boolean isValid = validateAppointmentDetails(doctorID, date, timeSlot);
        if (!isValid) {
            System.out.println("Invalid appointment details. Please try again.");
            return null;  // Ensure early exit if validation fails
        }

        String appointmentID = null;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(APPOINTMENT_FILE, true))) {
            Random random = new Random();
            Integer randomNumber = random.nextInt(900) + 100;
            appointmentID = "AP" + randomNumber.toString();
            String status = "pending";
            String doctorName = getDoctorNameByID(doctorID); // Fetch the doctor's name from User.csv
            String line = String.join(",", appointmentID, doctorID, patientID, date, timeSlot, status);
            writer.write(line);
            writer.newLine();

            // Update doctor's availability
            updateDoctorAvailability(doctorID, doctorName, date, timeSlot, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appointmentID;
    }

    public void rescheduleAppointment(String appointmentID, String newDate, String newTimeSlot) {
        List<String[]> appointments = loadAppointments();
        boolean valid = false;

        for (String[] appointment : appointments) {
            if (appointment[0].equals(appointmentID)) {
                String doctorID = appointment[1];

                if (!validateAppointmentDetails(doctorID, newDate, newTimeSlot)) {
                    System.out.println("Invalid reschedule details. Please try again.");
                    return;
                }

                String doctorName = getDoctorNameByID(doctorID);  // Fetch the doctor's name
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
        for (String[] appointment : appointments) {
            if (appointment[0].equals(appointmentID)) {
                String doctorID = appointment[1];
                String doctorName = getDoctorNameByID(doctorID);  // Fetch the doctor's name
                updateDoctorAvailability(doctorID, doctorName, appointment[3], appointment[4], true);
                appointment[5] = "canceled";
                break;
            }
        }
        saveAppointments(appointments);
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

    // Method to get doctor's name by ID from User.csv
    private String getDoctorNameByID(String doctorID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].equals(doctorID) && fields[2].equals("doctor")) { // Check if the role is doctor
                    return fields[3];  // Return doctor's name
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unknown Doctor"; // If doctor ID is not found
    }
}
