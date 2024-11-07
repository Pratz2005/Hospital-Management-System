package UserMenu;
import UserMain.Doctor;

public class DoctorMenu extends AbstractMenu {
    private Doctor doctor;

    public DoctorMenu(Doctor doctor){
        this.doctor = doctor;
    }
    @Override
    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n==== Doctor Menu ====");
            System.out.println("(1) View Patient Medical Records");
            System.out.println("(2) Update Patient Medical Records");
            System.out.println("(3) View Personal Schedule");
            System.out.println("(4) Set Availability for Appointments");
            System.out.println("(5) Accept or Decline Appointment Requests");
            System.out.println("(6) View Upcoming Appointments");
            System.out.println("(7) Record Appointment Outcome");

            displayLogoutOption(8); // Call the common logout option method

            choice = sc.nextInt();
            sc.nextLine(); // Clear the newline character from the buffer

            switch (choice) {
                case 1:
                    // View patient medical records logic
                    System.out.println("D1");
                    break;
                case 2:
                    // Update patient medical records logic
                    System.out.println("D2");
                    break;
                case 3:
                    // View personal schedule logic
                    System.out.println("D3");
                    break;
                case 4:
                    // Set availability logic
                    System.out.println("D4");
                    break;
                case 5:
                    // Accept/decline appointment requests logic
                    System.out.println("D5");
                    break;
                case 6:
                    // View upcoming appointments logic
                    System.out.println("D6");
                    break;
                case 7:
                    // Record appointment outcome logic
                    System.out.println("D7");
                    break;
                case 8:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 8); // Repeat until logout
    }
}