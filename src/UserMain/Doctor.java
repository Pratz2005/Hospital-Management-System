package UserMain;

import Appointment.AppointmentManager;
import Appointment.DoctorAvailabilityManager;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Doctor extends User {
    private String doctorID;
    private AppointmentManager appointmentManager;
    private DoctorAvailabilityManager availabilityManager;

    private static final String APPOINTMENT_FILE = "src/Files/Appointment.csv";
    private static final String APPOINTMENT_RECORD_FILE = "src/Files/AppointmentRecord.csv";

    public Doctor(String doctorID, String password, String role, String name, AppointmentManager appointmentManager, DoctorAvailabilityManager availabilityManager) {
        super(doctorID, password, role, name); // Call the User constructor to initialize ID, password, role, and name
        this.doctorID = doctorID;
        this.appointmentManager = appointmentManager;
        this.availabilityManager = availabilityManager;
    }

    public String getDoctorID(){
        return doctorID;
    }

    public void viewPatientMedicalRecord(Patient patient) {
        patient.viewMedicalRecord();
    }

    public void updatePatientMedicalRecord(String appointmentID, String newDiagnosis, String newPrescription, int newPrescriptionQuantity, String newTreatmentPlan, String newConsultationNotes) {
        List<String[]> records = loadAppointmentRecords();
        boolean updated = false;

        for (String[] record : records) {
            if (record[0].equals(appointmentID)) {
                record[1] = newDiagnosis;
                record[2] = newPrescription;
                record[3] = String.valueOf(newPrescriptionQuantity);
                record[5] = newTreatmentPlan;
                record[8] = newConsultationNotes;
                updated = true;
                break;
            }
        }

        if (updated) {
            saveAppointmentRecords(records);
            System.out.println("Patient medical record updated successfully.");
        } else {
            System.out.println("Appointment ID not found.");
        }
    }

    public void viewPersonalSchedule(String date) {
        String[] availableSlots = availabilityManager.viewDoctorAvailability(doctorID, date);
        System.out.println("Available Slots:");
        for (String slot : availableSlots) {
            System.out.println(slot);
        }
    }

    public void setAvailability(String date, String[] availableSlots) {
        availabilityManager.setDoctorAvailability(doctorID, getName(), date, availableSlots);
        System.out.println("Availability set successfully.");
    }

    public void acceptAppointment(String appointmentID) {
        updateAppointmentStatus(appointmentID, "confirmed");
    }

    public void declineAppointment(String appointmentID) {
        updateAppointmentStatus(appointmentID, "canceled");
    }

    public void viewUpcomingAppointments() {
        System.out.println("Upcoming Appointments for Doctor ID: " + doctorID);

        try (BufferedReader reader = new BufferedReader(new FileReader(APPOINTMENT_FILE))) {
            String line;
            boolean hasAppointments = false;

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                if (fields[1].equals(doctorID) && fields[5].equals("confirmed")) {
                    hasAppointments = true;
                    System.out.println("Appointment ID: " + fields[0]);
                    System.out.println("Patient ID: " + fields[2]);
                    System.out.println("Date: " + fields[3]);
                    System.out.println("Time Slot: " + fields[4]);
                    System.out.println("Status: " + fields[5]);
                    System.out.println("-------------------------");
                }
            }

            if (!hasAppointments) {
                System.out.println("No upcoming confirmed appointments found for this doctor.");
            }
        } catch (IOException e) {
            System.err.println("Error reading Appointment.csv: " + e.getMessage());
        }
    }

    public void recordAppointmentOutcome(String appointmentID, String diagnosis, String prescriptionMedicine, int quantity, String treatmentPlan, String date, String typeOfService, String notes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(APPOINTMENT_RECORD_FILE, true))) {
            String line = String.join(",",
                    appointmentID,
                    diagnosis,
                    prescriptionMedicine,
                    String.valueOf(quantity),
                    "pending", // Prescription status initially set to "pending"
                    treatmentPlan,
                    date,
                    typeOfService,
                    notes
            );
            writer.write(line);
            writer.newLine();
            System.out.println("Appointment outcome recorded successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to AppointmentRecord.csv: " + e.getMessage());
        }

        // Update the appointment status to "completed" in Appointment.csv
        updateAppointmentStatus(appointmentID, "completed");
    }

    private void updateAppointmentStatus(String appointmentID, String newStatus) {
        List<String[]> appointments = loadAppointments();
        boolean updated = false;

        for (String[] appointment : appointments) {
            if (appointment[0].equals(appointmentID)) {
                appointment[5] = newStatus;
                updated = true;
                break;
            }
        }

        if (updated) {
            saveAppointments(appointments);
            System.out.println("Appointment status updated to " + newStatus + " for Appointment ID: " + appointmentID);
        } else {
            System.out.println("Appointment ID not found.");
        }
    }

    private List<String[]> loadAppointments() {
        List<String[]> appointments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(APPOINTMENT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                appointments.add(line.split(","));
            }
        } catch (IOException e) {
            System.err.println("Error reading Appointment.csv: " + e.getMessage());
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
            System.err.println("Error writing to Appointment.csv: " + e.getMessage());
        }
    }

    private List<String[]> loadAppointmentRecords() {
        List<String[]> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(APPOINTMENT_RECORD_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                records.add(line.split(","));
            }
        } catch (IOException e) {
            System.err.println("Error reading AppointmentRecord.csv: " + e.getMessage());
        }
        return records;
    }

    private void saveAppointmentRecords(List<String[]> records) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(APPOINTMENT_RECORD_FILE))) {
            for (String[] record : records) {
                writer.write(String.join(",", record));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to AppointmentRecord.csv: " + e.getMessage());
        }
    }

    public void viewAppointmentsByDate(String date) {
        System.out.println("Confirmed Appointments for Doctor ID: " + doctorID + " on " + date);

        try (BufferedReader reader = new BufferedReader(new FileReader(APPOINTMENT_FILE))) {
            String line;
            boolean hasAppointments = false;

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                // Check if the appointment is for this doctor, on the specified date, and confirmed
                if (fields[1].equals(doctorID) && fields[3].equals(date) && fields[5].equals("confirmed")) {
                    hasAppointments = true;
                    System.out.println("Appointment ID: " + fields[0]);
                    System.out.println("Patient ID: " + fields[2]);
                    System.out.println("Date: " + fields[3]);
                    System.out.println("Time Slot: " + fields[4]);
                    System.out.println("Status: " + fields[5]);
                    System.out.println("-------------------------");
                }
            }

            if (!hasAppointments) {
                System.out.println("No confirmed appointments found for this doctor on " + date + ".");
            }
        } catch (IOException e) {
            System.err.println("Error reading Appointment.csv: " + e.getMessage());
        }
    }

}
