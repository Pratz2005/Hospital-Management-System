package UserMenu;
import UserMain.Administrator;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class AdministratorMenu extends AbstractMenu {
    private Administrator admin;

    public AdministratorMenu(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n==== Administrator Menu ====");
            System.out.println("(1) View and Manage Hospital Staff");
            System.out.println("(2) View Appointment Details");
            System.out.println("(3) View and Manage Medication Inventory");
            System.out.println("(4) Approve Replenishment Requests");

            displayLogoutOption(5); // Call the common logout option method

            // Wrap input handling in a try-catch to handle non-integer inputs
            try {
                choice = sc.nextInt();
                sc.nextLine(); // Clear the newline character from the buffer

                switch (choice) {
                    case 1:
                        // View and manage hospital staff logic
                        try {
                            manageHospitalStaff();
                        } catch (IOException e) {
                            System.out.println("An error occurred while managing hospital staff: " + e.getMessage());
                        }
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
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine(); // Clear the invalid input from the scanner buffer
                choice = -1; // Reset choice to continue the loop without exiting
            }
        } while (choice != 5); // Repeat until logout
    }

    // Method to manage hospital staff (add, remove, update)


    public void manageHospitalStaff() throws IOException {
        Scanner scanner = new Scanner(System.in);
        final List<String> VALID_ROLES = List.of("Doctor", "Pharmacist", "Administrator");

        while (true) {
            System.out.println("Manage Hospital Staff:");
            System.out.println("1. View Staff List");
            System.out.println("2. Add Staff");
            System.out.println("3. Update Staff");
            System.out.println("4. Remove Staff");
            System.out.println("5. Exit");

            int choice;

            while (true) {
                System.out.print("Enter your choice: ");
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    if (choice >= 1 && choice <= 5) {
                        break; // Exit loop if choice is valid
                    } else {
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine(); // Clear invalid input
                }
            }

            switch (choice) {
                case 1:
                    admin.viewStaffList();
                    break;
                case 2:
                    String[] newStaffDetails = getValidatedStaffDetails(0);
                    admin.addStaff(newStaffDetails[0], newStaffDetails[1], newStaffDetails[2], newStaffDetails[3], Integer.parseInt(newStaffDetails[4]));
                    break;
                case 3:
                    String[] updatedStaffDetails = getValidatedStaffDetails(1);
                    admin.updateStaff(updatedStaffDetails[0], updatedStaffDetails[1], updatedStaffDetails[2], updatedStaffDetails[3], Integer.parseInt(updatedStaffDetails[4]));
                    break;
                case 4:
                    // Loop until a valid Staff ID is entered for removal
                    String removeId;
                    while (true) {
                        System.out.print("Enter Staff ID to remove: ");
                        removeId = scanner.nextLine().trim();

                        // Check if the ID exists in the system
                        if (isProperFormat(removeId) && isStaffIDExists(removeId)) {
                            admin.removeStaff(removeId);
                            System.out.println("Staff member with ID " + removeId + " has been removed.");
                            break; // Exit the loop after successful removal
                        } else {
                            System.out.println("Invalid Staff ID or ID does not exist. Please try again.");
                        }
                    }
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    // Method to get validated staff details for adding or updating
    private String[] getValidatedStaffDetails(int operationType) {
        Scanner scanner = new Scanner(System.in);
        final List<String> VALID_ROLES = List.of("Doctor", "Pharmacist", "Administrator");

        // Choose validation method for Staff ID based on operation type (0 for add, 1 for update)
        String id = (operationType == 0) ? getValidatedNewStaffID(scanner) : getValidatedExistingStaffID(scanner);

        String name = getValidatedName(scanner);
        String role = getValidatedRole(scanner, VALID_ROLES);
        String gender = getValidatedGender(scanner);
        int age = getValidatedAge(scanner);

        return new String[]{id, name, role, gender, String.valueOf(age)};
    }

    // Method to get a valid new Staff ID (for adding new staff)
    private String getValidatedNewStaffID(Scanner scanner) {
        String id;
        while (true) {
            System.out.print("Enter Staff ID (e.g., D001): ");
            id = scanner.nextLine().trim();
            if (isProperFormat(id) && !isStaffIDExists(id)) {
                break;
            } else {
                System.out.println("Invalid Staff ID or ID already exists.");
            }
        }
        return id;
    }

    // Method to get a valid existing Staff ID (for updating existing staff)
    // Method to get a valid existing Staff ID (for updating existing staff)
    private String getValidatedExistingStaffID(Scanner scanner) {
        String id;
        while (true) {
            System.out.print("Enter Staff ID to update (e.g., D001): ");
            id = scanner.nextLine().trim();
            if (isProperFormat(id) && isStaffIDExists(id)) {
                break;
            } else {
                System.out.println("Invalid Staff ID or ID does not exist. Please try again.");
            }
        }
        return id;
    }


    // Check if the ID format is correct (e.g., one letter followed by three digits)
    private boolean isProperFormat(String id) {
        return id.matches("^[A-Z]\\d{3}$");
    }


    private String getValidatedName(Scanner scanner) {
        String name;
        while (true) {
            System.out.print("Enter Staff Name: ");
            name = scanner.nextLine().trim();
            if (isValidName(name)) {
                break;
            } else {
                System.out.println("Invalid name. Please enter a valid name without numbers.");
            }
        }
        return name;
    }

    private String getValidatedRole(Scanner scanner, List<String> validRoles) {
        String role;
        while (true) {
            System.out.print("Enter Staff Role (Doctor, Pharmacist, Administrator): ");
            role = scanner.nextLine().trim();
            if (validRoles.contains(role)) {
                break;
            } else {
                System.out.println("Invalid role. Please enter one of the following: Doctor, Pharmacist, Administrator.");
            }
        }
        return role;
    }

    private String getValidatedGender(Scanner scanner) {
        String gender;
        while (true) {
            System.out.print("Enter Staff Gender (M/F): ");
            gender = scanner.nextLine().trim().toUpperCase();
            if (gender.equals("M") || gender.equals("F")) {
                break;
            } else {
                System.out.println("Invalid gender. Please enter 'M' or 'F'.");
            }
        }
        return gender;
    }

    private int getValidatedAge(Scanner scanner) {
        int age;
        while (true) {
            System.out.print("Enter Staff Age (18-100): ");
            if (scanner.hasNextInt()) {
                age = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (age >= 18 && age <= 100) {
                    break;
                } else {
                    System.out.println("Invalid age. Please enter a value between 18 and 100.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number for age.");
                scanner.nextLine(); // Clear invalid input
            }
        }
        return age;
    }

    private boolean isStaffIDExists(String id) {
        // Reads Staff.csv and checks if the given ID already exists
        try (BufferedReader reader = new BufferedReader(new FileReader("src/Files/Staff.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].equals(id)) {
                    return true; // ID already exists
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading Staff.csv: " + e.getMessage());
        }
        return false; // ID does not exist
    }

    private boolean isValidName(String name) {
        // Check if the name contains only alphabetic characters and spaces
        return Pattern.matches("^[A-Za-z ]+$", name);
    }

}
