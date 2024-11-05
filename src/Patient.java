import java.io.IOException;
import java.util.Scanner;
import java.lang.Math;

public class Patient extends User{
    private String gender;
    private String dob;
    private String contactNo;
    private String email;
    private String bloodType;
    private String pastTreatment;

    public Patient(String id, String password, String role,String name,String gender, String dob, String contactNo, String email, String bloodType, String pastTreatment) {
        super(id,password,role,name);
        this.gender = gender;
        this.dob = dob;
        this.contactNo = contactNo;
        this.email = email;
        this.bloodType = bloodType;
        this.pastTreatment = pastTreatment;
    }

    public Patient(String id, String name){
        super(id, name);
    }

    public void viewMedicalRecord(){
        System.out.println("Name: "+ getName());
        System.out.println("Gender: "+gender);
        System.out.println("DOB: "+dob);
        System.out.println("ContactNo: "+contactNo);
        System.out.println("Email: "+email);
        System.out.println("Blood Type: "+bloodType);
        System.out.println("Past Treatment: "+pastTreatment);
    }

    public void updateContactNo(String newContactNo){
        this.contactNo = newContactNo;
    }

    public void updateEmail(String newEmail){
        this.email = newEmail;
    }

    public void scheduleAppointment () throws IOException {
        Scanner doctID = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter Doctor ID: ");
        String doctorID = doctID.nextLine();

        Scanner datein = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter date in dd/mm/yy format: ");
        String date = datein.nextLine();

        Scanner timeslotin = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter time slot in hh:mm format: ");
        String timeslot = timeslotin.nextLine();

        double appID = Math.random();
        String status = "confirmed";
        Appointment a = new Appointment(appID, doctorID, this.getId(), date, timeslot, status);
        a.addToRecord(a);
    }

    public void reschedule(){

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter AppointmentID");
        double appointmentID = sc.nextDouble();
        sc.nextLine();

        Appointment appointmentToReschedule = null;
        for(Appointment appointment : Appointment.appointmentOutRecord){
            if(appointment.getAppointmentID() == appointmentID){
                appointmentToReschedule = appointment;
                break;
            }
        }

        if(appointmentToReschedule != null){
            System.out.println("Enter new time slot in hh:mm format: ");
            String newTimeSlot = sc.nextLine();

            appointmentToReschedule.setTimeSlot(newTimeSlot);

            appointmentToReschedule.setStatus("rescheduled");

            System.out.println("Appointment Scheduled Successfully");

            try {
                appointmentToReschedule.saveToCSV();
            } catch (IOException e) {
                System.out.println("Error saving appointment: " + e.getMessage());
            }
        }else{
            System.out.println("Appointment ID not found");
        }

    }

    public void cancelAppointment(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter AppointmentID");
        double appointmentID = sc.nextDouble();
        sc.nextLine();

        Appointment appointmentToCancel = null;
        for(Appointment appointment : Appointment.appointmentOutRecord){
            if(appointment.getAppointmentID() == appointmentID){
                appointmentToCancel = appointment;
                break;
            }
        }

        if(appointmentToCancel != null){
            appointmentToCancel.setStatus("cancelled");
            Appointment.appointmentOutRecord.remove(appointmentToCancel);
            System.out.println("Appointment Cancelled");
        }else{
            System.out.println("Appointment ID not found");
        }
    }

    public void viewScheduledAppointments() {
        boolean hasAppointments = false;

        System.out.println("Scheduled Appointments for " + this.getName() + " (Patient ID: " + this.getId() + "):");
        System.out.println("----------------------------------------------------");

        for (Appointment appointment : Appointment.appointmentOutRecord) {
            if (appointment.getPatientId().equals(this.getId()) && !appointment.getStatus().equalsIgnoreCase("cancelled")) {
                System.out.println("Appointment ID: " + appointment.getAppointmentID());
                System.out.println("Doctor ID: " + appointment.getDoctorId());
                System.out.println("Date: " + appointment.getDate());
                System.out.println("Time Slot: " + appointment.getTimeSlot());
                System.out.println("Status: " + appointment.getStatus());
                System.out.println("----------------------------------------------------");
                hasAppointments = true;
            }
        }

        if (!hasAppointments) {
            System.out.println("No scheduled appointments found.");
        }
    }

    public void viewPastRecords() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Appointment ID to view past record:");
        double appointmentID = sc.nextDouble();
        sc.nextLine(); // Consume newline

        boolean recordFound = false;

        for (Appointment appointment : Appointment.appointmentOutRecord) {
            if (appointment.getAppointmentID() == appointmentID && (appointment.getStatus().equalsIgnoreCase("completed") || appointment.getStatus().equalsIgnoreCase("cancelled"))) {
                System.out.println("Past Appointment Details:");
                System.out.println("Appointment ID: " + appointment.getAppointmentID());
                System.out.println("Doctor ID: " + appointment.getDoctorId());
                System.out.println("Date: " + appointment.getDate());
                System.out.println("Time Slot: " + appointment.getTimeSlot());
                System.out.println("Status: " + appointment.getStatus());
                System.out.println("Type of Service: " + appointment.getTypeOfService());
                System.out.println("Medication: " + appointment.getMedicationName());
                System.out.println("Medication Status: " + (appointment.isMedStatus() ? "Pending" : "Dispensed"));
                recordFound = true;
                break;
            }
        }

        if (!recordFound) {
            System.out.println("No past records found for the given Appointment ID.");
        }
    }


    public void logOut() {
        System.out.println("Patient " + getName() + " has logged out successfully.");
    }
}