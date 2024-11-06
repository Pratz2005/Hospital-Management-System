import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Doctor extends User{
    private Map<String, List<String>> availableSlots; //Mapping date to Available time slots
    private List<Appointment> appointments;
<<<<<<< Updated upstream
    private List<Patient> patientsUnderCare;
=======
    private List<Patient> patients;
>>>>>>> Stashed changes

    public Doctor(String id, String password, String role, String name){
        super(id, password, role, name);
        this.availableSlots = new HashMap<>();
        this.appointments = new ArrayList<>();
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

    public void acceptAppointment(Appointment appointment) {
        String date = appointment.getDate();
        String timeSlot = appointment.getTimeSlot();

        if (availableSlots.containsKey(date) && availableSlots.get(date).contains(timeSlot)) {
            appointments.add(appointment);
            availableSlots.get(date).remove(timeSlot); // Remove the specific time slot
            if (availableSlots.get(date).isEmpty()) {
                availableSlots.remove(date); // Remove the date if no more slots are available
            }
            System.out.println("Appointment added successfully.");
        } else {
            System.out.println("Slot not available.");
        }
    }

    public void declineAppointment(Appointment appointment) {
        appointment.setStatus("declined");
        System.out.println("Appointment declined successfully.");
    }


    public void viewAppointments(){
        System.out.println("Upcoming Appointments:");
        for(int i = 0; i<appointments.size(); i++){
            System.out.println(appointments.get(i));
        }
    }

<<<<<<< Updated upstream
    public void addPatient(Patient patient){
        patientsUnderCare.add(patient);
        System.out.println("Patient added successfully.");
    }


    public void viewPatientRecord(Patient patient, Appointment appointment){
        if(patientsUnderCare.contains(patient)){
            patient.viewMedicalRecord();
        }else{
            System.out.println("Patient not under care.");
        }
    }

    public void updatePatientRecord(Patient patient, Appointment appointment){
        if(!patientsUnderCare.contains(patient)){
            System.out.println("Patient not under care.");
            return;
        }

        String treatmentRecord = "Service: " + appointment.getTypeOfService() +
                ", Medication: " + appointment.getMedicationName() +
                ", Status: " + (appointment.isMedStatus() ? "Pending" : "Dispensed") +
                ", Notes: " + appointment.getConsultationNotes();

        // Add the treatment record to the patient's past treatments
        System.out.println("Patient record updated with new treatment entry.");
=======
    public void viewPatientRecord(Patient patient){
        patient.viewMedicalRecord();
>>>>>>> Stashed changes
    }
}