package UserMenu;
import UserMain.Administrator;

public class AdministratorMenu extends AbstractMenu {
    private Administrator admin;

    public AdministratorMenu(Administrator admin){
        this.admin = admin;
    }
    @Override
    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n==== UserMain.Administrator Menu ====");
            System.out.println("(1) View and Manage Hospital Staff");
            System.out.println("(2) View Appointments details");
            System.out.println("(3) View and Manage Medication Inventory");
            System.out.println("(4) Approve Replenishment Requests");

            displayLogoutOption(5); // Call the common logout option method

            choice = sc.nextInt();
            sc.nextLine(); // Clear the newline character from the buffer

            switch (choice) {
                case 1:
                    // View and manage hospital staff logic
                    System.out.println("A1");
                    break;
                case 2:
                    // View appointment details logic
                    System.out.println("A2");
                    break;
                case 3:
                    // View/manage medication inventory logic
                    System.out.println("A3");
                    break;
                case 4:
                    // Approve replenishment requests logic
                    System.out.println("A4");
                    break;
                case 5:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5); // Repeat until logout
    }
}