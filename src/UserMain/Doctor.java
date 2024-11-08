package UserMain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Doctor extends User {
    private String specialization;
    private List<Appointment> appointments;
    private Map<String, List<String>> availableSlots; //Mapping date to Available time slots
    private HashMap<String, Patient> patientRecords;

    public Doctor(String id, String password, String role, String name){
        super(id, password, role, name);
        this.specialization = specialization; 
        this.availableSlots = new HashMap<>();
        this.appointments = new ArrayList<>();
        this.patientRecords = new HashMap<>();
    }

     // Method to view upcoming appointments. Extra Feature??
     public List<Appointment> viewUpcomingAppointments() {
        List<Appointment> upcomingAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getStatus() != ) {
                upcomingAppointments.add(appointment);
            }
        }
        return upcomingAppointments;
    }

    public void addAvailableSlot(String slot, String TimeSlot){
        availableSlots.putIfAbsent(slot, new ArrayList<>());
        availableSlots.get(slot).add(TimeSlot);
    }

    public void removeAvailableSlot(String slot, String TimeSlot){
        availableSlots.get(slot).remove(TimeSlot);
    }

    public List<String> getAvailableSlots(String date){
        return availableSlots.getOrDefault(date, new ArrayList<>());
    }

    public void bookappointment(Appointment appointment){
        String date = appointment.getDate();
        String timeSlot = appointment.getTimeSlot();

        if(availableSlots.containsKey(date) && availableSlots.containsKey(timeSlot)){
            appointments.add(appointment);
            availableSlots.remove(date);
            System.out.println("UserMain.Appointment added successfully");
        }else {
            System.out.println("Slot not available");
        }
    }

    public void viewAppointments(){
        for(int i = 0; i<appointments.size(); i++){
            System.out.println(appointments.get(i));
        }
    }


    // Method to view medical records of a specific patient
    public Patient viewPatientRecord(String patientId) {
        return patientRecords.get(patientId);
    }

    // Method to update medical records by adding new diagnosis, prescription, or treatment plan
    public void updatePatientRecord(String patientId, String diagnosis, String prescription, String treatment, String appointmentId) {
        Patient patient = patientRecords.get(patientId);
        if (patient != null) {
            patient.addDiagnosis(diagnosis);
            patient.addPrescription(prescription);
            patient.addTreatment(appointmentId,treatment);
            System.out.println("Updated medical record for patient " + patientId);
        } else {
            System.out.println("Patient record not found.");
        }
    }
}