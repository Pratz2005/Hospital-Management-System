import java.io.IOException;
import java.util.Scanner;
import java.lang.Math;


public class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String id, password;
        int option =1;
        System.out.println("==== Login ====");
        System.out.println("[1] Patient\n"
        + "[2] Doctor\n"
        + "[3] Pharmacist\n"
        + "[4] Administrator\n");
        System.out.println("Enter your choice: ");
        option = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter your user ID: ");
        id = sc.nextLine();
        System.out.println("Enter your password: ");
        password = sc.nextLine();

        switch (option){
            case 1:
                Patient patient = new Patient(id, password);

                System.out.println("==== Patient Menu ====");
                System.out.println("(1) View Medical Record\n"
                        + "(2) Update Personal Information\n"
                        + "(3) View Available Appointment Slots\n"
                        + "(4) Schedule Appointment\n"
                        + "(5) Reschedule Appointment\n"
                        + "(6) Cancel Appointment \n"
                        + "(7) View Scheduled Appointments\n"
                        + "(8) Logout");
                int pChoice;
                do {
                    System.out.println("Enter your choice: ");
                    pChoice = sc.nextInt();
                    switch (pChoice) {
                        case 1:

                    }

                } while (option == 8);

                break;
            case 2:
                System.out.println("==== Doctor Menu ====");
                System.out.println("(1) View Patient Medical Records\n"
                        + "(2) Update Patient Medical Records\n"
                        + "(3) View Personal Schedule\n"
                        + "(4) Set Availability for Appointments\n"
                        + "(5) Accept or Decline Appointment Requests\n"
                        + "(6) View Upcoming Appointments\n"
                        + "(7) Record Appointment Outcome\n"
                        + "(8) Logout");
                break;

            case 3:
                System.out.println("==== Pharmacist Menu ====");
                System.out.println("(1) View Appointmnet Outcome Record\n"
                        + "(2) Update Prescription Status\n"
                        + "(3) View Medication Inventory\n"
                        + "(4) Submit Replenishment Request\n"
                        + "(8) Logout");
                break;

            case 4:
                System.out.println("==== Administrator Menu ====");
                System.out.println("(1) View and Manage Hospital Staff\n"
                        + "(2) View Appointments Details\n"
                        + "(3) View and Manage Medication Inventory\n"
                        + "(4) Approve Replenishment Requests\n"
                        + "(8) Logout");
                break;

            case 5:
                break;

        }

    }
}