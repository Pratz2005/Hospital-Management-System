import java.util.Scanner;

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


}