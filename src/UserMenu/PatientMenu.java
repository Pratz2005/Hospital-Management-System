package UserMenu;
import UserMain.Patient;

public class PatientMenu extends AbstractMenu {
    private Patient patient;

    // Constructor to accept the patient object
    public PatientMenu(Patient patient) {
        this.patient = patient;
    }

    @Override
    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n==== Patient Menu ====");
            System.out.println("(1) View Medical Record");
            System.out.println("(2) Update Personal Information");
            System.out.println("(3) View Available Appointment Slots");
            System.out.println("(4) Schedule an Appointment");
            System.out.println("(5) Reschedule an Appointment");
            System.out.println("(6) Cancel an Appointment");
            System.out.println("(7) View Scheduled Appointments");
            System.out.println("(8) View Past Appointment Outcome Records");

            displayLogoutOption(9); // Call the common logout option method

            choice = sc.nextInt();
            sc.nextLine(); // Clear the newline character from the buffer

            switch (choice) {
                case 1:
                    // Call the corresponding method (view medical record)
                    patient.viewPatientInfo(); // Assuming you have a UserMain.Patient object
                    System.out.println("P1");
                    break;
                case 2:
                    System.out.print("Enter new contact number: ");
                    String newContact = sc.nextLine();
                    System.out.print("Enter new email: ");
                    String newEmail = sc.nextLine();
                    patient.updateContactNo(newContact);
                    patient.updateEmail(newEmail);
                    System.out.println("Contact information updated successfully.");
                    break;
                case 3:
                    // View available appointment slots logic
                    System.out.println("P3");
                    break;
                case 4:
                    // Schedule an appointment logic
                    System.out.println("P4");
                    break;
                case 5:
                    // Reschedule an appointment logic
                    System.out.println("P5");
                    break;
                case 6:
                    // Cancel appointment logic
                    System.out.println("P6");
                    break;
                case 7:
                    // View scheduled appointments logic
                    System.out.println("P7");
                    break;
                case 8:
                    // View past appointment outcome records logic
                    System.out.println("P8");
                    break;
                case 9:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 9); // Repeat until logout
    }
}