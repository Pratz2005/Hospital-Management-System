package UserMain;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Appointment {
    private double appointmentID;
    private String doctorId;
    private String patientId;
    private String date;
    private String timeSlot;
    private String status; // e.g., "pending", "confirmed", "canceled", "completed"
    private String typeOfService;
    private String medicationName;
    private boolean medStatus; //true = pending, false = dispensed
    private String doctorNotes;
    private String treatmentDetails; // Added to link treatments with specific appointments
    public static List<Appointment> appointmentOutRecord;

    private static final String APPOINTMENT_FILE_PATH = "/Files/Appointments.csv"; // Update this path as needed

    public Appointment(double appointmentID, String doctorId, String patientId, String date, String timeSlot, String status) {
        this.appointmentID = appointmentID;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.date = date;
        this.timeSlot = timeSlot;
        this.status = "pending"; //default
        this.typeOfService = "n/a";
        this.medicationName = "n/a";
        this.medStatus = true;

    }

    //Getters
    public String getDoctorId() {
        return doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDate() {
        return date;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String newStatus) {
        status = newStatus;
    }

    public void setTimeSlot(String newTimeSlot) {
        timeSlot = newTimeSlot;
    }

    public double getAppointmentID() {
        return appointmentID;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public String getTreatmentDetails() {
        return treatmentDetails;
    }

    //Setters

    public String getTypeOfService() {
        return typeOfService;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public void setTypeOfService(String typeOfService) {
        this.typeOfService = typeOfService;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public boolean isMedStatus() {
        return medStatus;
    }

    public void setMedStatus(boolean medStatus) {
        this.medStatus = medStatus;
    }

    public void addToRecord(Appointment a) throws IOException {
        appointmentOutRecord.add(a);
        saveToCSV();
    }

    public void removeRecord (Appointment a){
        appointmentOutRecord.remove(a);
    }

    public void setTreatmentDetails(String treatmentDetails) {
        this.treatmentDetails = treatmentDetails;
    }

    // Method to reschedule the appointment (if required)
    public void reschedule(String newDate, String newTime) {
        this.date = newDate;
        this.timeSlot = newTime;
        this.status = "rescheduled";
    }

    // Additional method to cancel the appointment
    public void cancel() {
        this.status = "canceled";
    }

    // Method to save the appointment details to the CSV file
    public void saveToCSV() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPOINTMENT_FILE_PATH, true))) {
            bw.write(String.join(",", doctorId, patientId, date, timeSlot, doctorNotes, treatmentDetails,status));
            bw.newLine(); // Add a newline at the end
        }
    }
}