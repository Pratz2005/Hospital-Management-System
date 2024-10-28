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

        Scanner timeslotin = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter new time slot in hh:mm format: ");
        String timeslot = timeslotin.nextLine();


    }



}