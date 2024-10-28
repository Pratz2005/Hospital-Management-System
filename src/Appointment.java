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
    private String status;
    private String typeOfService;
    private String medicationName;
    private boolean medStatus; //true = pending, false = dispensed
    public static List<Appointment> appointmentOutRecord;

    private static final String APPOINTMENT_FILE_PATH = "Appointments.csv"; // Update this path as needed

    public Appointment(double appointmentID, String doctorId, String patientId, String date, String timeSlot, String status) {
        this.appointmentID = appointmentID;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.date = date;
        this.timeSlot = timeSlot;
        this.status = status;
        this.typeOfService = "n/a";
        this.medicationName = "n/a";
        this.medStatus = true;

    }


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

    public double getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getTypeOfService() {
        return typeOfService;
    }

    public void setTypeOfService(String typeOfService) {
        this.typeOfService = typeOfService;
    }

    public String getMedicationName() {
        return medicationName;
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

    // Method to save the appointment details to the CSV file
    public void saveToCSV() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPOINTMENT_FILE_PATH, true))) {
            bw.write(String.join(",", doctorId, patientId, date, timeSlot, status));
            bw.newLine(); // Add a newline at the end
        }
    }
}
