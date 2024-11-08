package Appointment;

public interface DoctorAvailabilityManager {
    void setDoctorAvailability(String doctorID,String doctorName, String date, String[] availableSlots);
    String[] viewDoctorAvailability(String doctorID, String date);
}
