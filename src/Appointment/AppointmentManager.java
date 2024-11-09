package Appointment;

public interface AppointmentManager {
    String scheduleAppointment(String doctorID, String patientID, String date, String timeSlot);
    void rescheduleAppointment(String appointmentID, String newDate, String newTimeSlot);
    void cancelAppointment(String appointmentID);
    String viewAppointmentStatus(String appointmentID);
}
