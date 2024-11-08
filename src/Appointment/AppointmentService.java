package Appointment;

import java.io.*;
import java.util.*;

public class AppointmentService extends DoctorAvailabilityService implements AppointmentManager {
    private static final String APPOINTMENT_FILE = "src/Files/Appointment.csv";
    private static final String USER_FILE = "src/Files/User.csv";  // Add this line for the User.csv file

    @Override
    public void scheduleAppointment(String doctorID, String patientID, String date, String timeSlot) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(APPOINTMENT_FILE, true))) {
            String appointmentID = UUID.randomUUID().toString();
            String status = "pending";
            String doctorName = getDoctorNameByID(doctorID); // Fetch the doctor's name from User.csv
            String line = String.join(",", appointmentID, doctorID, patientID, date, timeSlot, status);
            writer.write(line);
            writer.newLine();
            updateDoctorAvailability(doctorID, doctorName, date, timeSlot, false);  // Pass the doctor's name
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rescheduleAppointment(String appointmentID, String newDate, String newTimeSlot) {
        List<String[]> appointments = loadAppointments();
        for (String[] appointment : appointments) {
            if (appointment[0].equals(appointmentID)) {
                String doctorID = appointment[1];
                String doctorName = getDoctorNameByID(doctorID);  // Fetch the doctor's name
                updateDoctorAvailability(doctorID, doctorName, appointment[3], appointment[4], true);
                appointment[3] = newDate;
                appointment[4] = newTimeSlot;
                appointment[5] = "pending";
                updateDoctorAvailability(doctorID, doctorName, newDate, newTimeSlot, false);
                break;
            }
        }
        saveAppointments(appointments);
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
