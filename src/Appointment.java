import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Appointment {
    private String doctorId;
    private String patientId;
    private String date;
    private String timeSlot;
    private String status;

    private static final String APPOINTMENT_FILE_PATH = "Appointments.csv"; // Update this path as needed

    public Appointment(String doctorId, String patientId, String date, String timeSlot, String status) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.date = date;
        this.timeSlot = timeSlot;
        this.status = status;
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

    // Method to save the appointment details to the CSV file
    public void saveToCSV() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPOINTMENT_FILE_PATH, true))) {
            bw.write(String.join(",", doctorId, patientId, date, timeSlot, status));
            bw.newLine(); // Add a newline at the end
        }
    }
}
