package Appointment;

import java.io.*;
import java.util.*;

public class AppointmentRecordService {
    private static final String APPOINTMENT_RECORD_FILE = "src/Files/AppointmentRecord.csv";
    public void addAppointmentOutcomeRecord(
            String appointmentID,
            String diagnosis,
            String prescriptionMedicine,
            int prescriptionQuantity,
            String treatmentPlan,
            String date,
            String typeOfService,
            String consultationNotes) {

        // Step 1: Add the outcome record to AppointmentRecord.csv
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(APPOINTMENT_RECORD_FILE, true))) {
            String line = String.join(",",
                    appointmentID,
                    diagnosis,
                    prescriptionMedicine,
                    String.valueOf(prescriptionQuantity),
                    "pending", // Prescription status is initially set to "pending"
                    treatmentPlan,
                    date,
                    typeOfService,
                    consultationNotes
            );
            writer.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Step 2: Update the status in Appointment.csv to "completed" for the given AppointmentID
        List<String[]> appointments = loadAppointments();

        for (String[] appointment : appointments) {
            if (appointment[0].equals(appointmentID)) {
                appointment[5] = "completed"; // Update the Status column (index 5) to "completed"
                break;
            }
        }

        // Step 3: Save the updated appointments back to Appointment.csv
        saveAppointments(appointments);
    }

    /**
     * Loads all appointments from Appointment.csv.
     */
    private List<String[]> loadAppointments() {
        List<String[]> appointments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/Files/Appointment.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                appointments.add(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    /**
     * Saves the updated appointments back to Appointment.csv.
     */
    private void saveAppointments(List<String[]> appointments) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/Files/Appointment.csv"))) {
            for (String[] appointment : appointments) {
                writer.write(String.join(",", appointment));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
