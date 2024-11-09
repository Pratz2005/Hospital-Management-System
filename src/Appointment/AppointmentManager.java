package Appointment;

public interface AppointmentManager {
    String scheduleAppointment(String patientID);
    void rescheduleAppointment(String appointmentID);
    void cancelAppointment(String appointmentID);
    String viewAppointmentStatus(String appointmentID);
}
