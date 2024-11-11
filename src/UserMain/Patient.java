package UserMain;

import Appointment.AppointmentManager;
import Appointment.DoctorAvailabilityManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Patient extends User {
    private String patientID;
    private String dob;
    private String gender;
    private String contactNo;
    private String email;
    private String bloodType;
    private String pastTreatment;
    private AppointmentManager appointmentManager;
    private DoctorAvailabilityManager availabilityManager;

    public Patient(String patientID, String password, String role, String name, String dob, String gender, String contactNo, String email, String bloodType, String pastTreatment, AppointmentManager appointmentManager, DoctorAvailabilityManager availabilityManager) {
        super(patientID, password, role, name); // Call the User constructor to initialize ID, password, role, and name
        this.patientID = patientID;
        this.dob = dob;
        this.gender = gender;
        this.contactNo = contactNo;
        this.email = email;
        this.bloodType = bloodType;
        this.pastTreatment = pastTreatment;
        this.appointmentManager = appointmentManager;
        this.availabilityManager = availabilityManager;
    }

    public String getPatientID() {
        return patientID;
    }

    public void viewMedicalRecord() {
        updatePastTreatmentFromCSV(); // Update pastTreatment field before viewing

        System.out.println("Medical Record:");
        System.out.println("Patient ID: " + patientID);
        System.out.println("Name: " + getName());
        System.out.println("Date of Birth: " + dob);
        System.out.println("Gender: " + gender);
        System.out.println("Contact No: " + contactNo);
        System.out.println("Email: " + email);
        System.out.println("Blood Type: " + bloodType);
        System.out.println("Past Treatments: ");
        System.out.println("- Appointment ID - Diagnosis - Past Treatment");

        // Display each past treatment (AppointmentID, Diagnosis, and Treatment Plan)
        if (pastTreatment.equals("N/A") || pastTreatment.isEmpty()) {
            System.out.println("No past treatments recorded.");
        } else {
            String[] treatments = pastTreatment.split("; ");
            for (String treatment : treatments) {
                System.out.println(" - " + treatment); // Display each treatment in a new line
            }
        }
    }

    // Update pastTreatment field by reading from Patient_List.csv
    private void updatePastTreatmentFromCSV() {
        String filePath = "src/Files/Patient_List.csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine(); // Skip header line

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].equals(patientID)) {
                    this.pastTreatment = fields[8]; // PastTreatment field contains AppointmentID, Diagnosis, and Treatment Plan
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading Patient_List.csv: " + e.getMessage());
        }
    }


    public void updatePersonalInfo(String newEmail, String newContactNo) {
        this.email = newEmail;
        this.contactNo = newContactNo;

        // Update the information in the CSV file
        if (updatePatientInfoInCSV(this.patientID, newEmail, newContactNo)) {
            System.out.println("Personal information updated successfully.");
        } else {
            System.out.println("Failed to update personal information in CSV file.");
        }
    }

    // Helper method to update patient information in the CSV file
    private boolean updatePatientInfoInCSV(String patientID, String newEmail, String newContactNo) {
        String filePath = "src/Files/Patient_List.csv";
        List<String[]> records = new ArrayList<>();
        boolean isUpdated = false;

        // Read the CSV file and store each record in a list
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // Read header line
            if (line != null) {
                records.add(line.split(",")); // Add header to records
            }

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                // Check if this is the record to be updated
                if (fields[0].equals(patientID)) {
                    fields[5] = newContactNo; // Update contact number
                    fields[6] = newEmail; // Update email
                    isUpdated = true;
                }
                records.add(fields);
            }
        } catch (IOException e) {
            System.err.println("Error reading Patient_List.csv: " + e.getMessage());
            return false;
        }

        // Write the updated records back to the CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] record : records) {
                writer.write(String.join(",", record));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to Patient_List.csv: " + e.getMessage());
            return false;
        }

        return isUpdated;
    }


    public void viewAvailableAppointmentSlots(String doctorID, String date) {
        String[] availableSlots = availabilityManager.viewDoctorAvailability(doctorID, date);
        System.out.println("Available Slots:");
        for (String slot : availableSlots) {
            System.out.println(slot);  // Each slot includes doctor name, date, and time slot
        }
    }

    public void scheduleAppointment(String patientID) {
        String appointmentID = appointmentManager.scheduleAppointment(patientID);
        if (appointmentID != null) {
            System.out.println("Appointment scheduled successfully. The appointment ID is " + appointmentID);
        } else {
            System.out.println("Failed to schedule the appointment. Please check the details and try again.");
        }
    }


    public void rescheduleAppointment(String appointmentID) {
        appointmentManager.rescheduleAppointment(appointmentID);
    }

    public void cancelAppointment(String appointmentID) {
        appointmentManager.cancelAppointment(appointmentID);
    }

    public void viewScheduledAppointments(String appointmentID) {
        String status = appointmentManager.viewAppointmentStatus(appointmentID);
        System.out.println("Appointment Status: " + status);
    }

    public void viewPastAppointmentOutcome() {
        String appointmentFilePath = "src/Files/Appointment.csv";
        String recordFilePath = "src/Files/AppointmentRecord.csv";
        System.out.println("Past Appointment Outcomes for Patient ID: " + patientID);

        // List to store completed appointment IDs for this patient
        List<String> completedAppointments = new ArrayList<>();

        // Step 1: Read Appointment.csv to find completed appointments for this patient
        try (BufferedReader appointmentReader = new BufferedReader(new FileReader(appointmentFilePath))) {
            String line = appointmentReader.readLine(); // Skip header line

            while ((line = appointmentReader.readLine()) != null) {
                String[] fields = line.split(",");

                // Check if the line has the expected number of fields
                if (fields.length < 6) {
                    System.out.println("Skipping malformed line in Appointment.csv: " + line);
                    continue;
                }

                String appointmentID = fields[0];
                String appointmentPatientID = fields[2];
                String status = fields[5];

                // If the appointment is completed for the current patient, store the appointmentID
                if (appointmentPatientID.equals(patientID) && status.equalsIgnoreCase("completed")) {
                    completedAppointments.add(appointmentID);
                }
            }

            if (completedAppointments.isEmpty()) {
                System.out.println("No completed appointments found for this patient.");
                return;
            }
        } catch (IOException e) {
            System.err.println("Error reading Appointment.csv: " + e.getMessage());
            return;
        }

        // Step 2: Read AppointmentRecord.csv to print details for completed appointments
        try (BufferedReader recordReader = new BufferedReader(new FileReader(recordFilePath))) {
            String line;
            boolean hasRecord = false;

            while ((line = recordReader.readLine()) != null) {
                String[] fields = line.split(",");

                // Check if the line has the expected number of fields
                if (fields.length < 9) {
                    //System.out.println("Skipping malformed line in AppointmentRecord.csv: " + line);
                    continue;
                }

                String appointmentID = fields[0];

                // If the appointmentID is in the completedAppointments list, print the record
                if (completedAppointments.contains(appointmentID)) {
                    hasRecord = true;
                    System.out.println("Appointment ID: " + fields[0]);
                    System.out.println("Diagnosis: " + fields[1]);
                    System.out.println("Prescription Medicine: " + fields[2]);
                    System.out.println("Prescription Quantity: " + fields[3]);
                    System.out.println("Prescription Status: " + fields[4]);
                    System.out.println("Treatment Plan: " + fields[5]);
                    System.out.println("Date: " + fields[6]);
                    System.out.println("Type of Service: " + fields[7]);
                    System.out.println("Consultation Notes: " + fields[8]);
                    System.out.println("-------------------------");
                }
            }

            if (!hasRecord) {
                System.out.println("No past appointment records found in AppointmentRecord.csv for this patient.");
            }
        } catch (IOException e) {
            System.err.println("Error reading AppointmentRecord.csv: " + e.getMessage());
        }
    }

}
