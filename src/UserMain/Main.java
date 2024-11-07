package UserMain;

import java.io.*;
import java.util.*;
import UserMenu.PatientMenu;
import UserMenu.DoctorMenu;
import UserMenu.PharmacistMenu;
import UserMenu.AdministratorMenu;
import UserMenu.AbstractMenu;


public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        String id, password,name;
        String role = null;

        // Ask for user login details
        System.out.println("==== Login ====");
        System.out.print("Enter your user ID: ");
        id = sc.nextLine();
        System.out.print("Enter your password: ");
        password = sc.nextLine();

        // Verify credentials and get the role from the CSV file
        boolean authenticated = false;
        Object user = null; // To hold the user object (UserMain.Patient, UserMain.Doctor, etc.)

        // Path to the UserMain.User.csv file
        String filePath = "C:\\Users\\USER\\Downloads\\User.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Skip header
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String userId = data[0];
                String userPassword = data[1];
                role = data[2];
                name=data[3];

                // Check if the entered ID and password match
                if (userId.equals(id) && userPassword.equals(password)) {
                    authenticated = true;
                    // Create the user object based on the role
                    if (role.equals("patient"))
                    {
                        String filePath_1 = "C:\\Users\\USER\\Downloads\\Patient_List.csv"; // path to your CSV file
                        try (BufferedReader br_1 = new BufferedReader(new FileReader(filePath_1))) {
                            String line_1;
                            // Skip the header line if present
                            br_1.readLine();

                            while ((line_1 = br_1.readLine()) != null) {
                                String[] data_1 = line_1.split(",");

                                // Assign values based on column order in CSV file
                                String patientId = data_1[0];
                                String patientPassword = data_1[1];
                                String name_1 = data_1[2];
                                String gender = data_1[3];
                                String dob = data_1[4];
                                String contactNo = data_1[5];
                                String email = data_1[6];
                                String bloodType = data_1[7];
                                String pastTreatment = data_1[8];

                                user = new Patient(patientId, patientPassword, role, name_1, gender, dob, contactNo, email, bloodType, pastTreatment);
                            }
                        }    // Create UserMain.Patient object
                    }else if(role.equals("Doctor")) {
                        user = new Doctor(id, password, role,name); // Create UserMain.Doctor object
                    } else if (role.equals("Pharmacist")) {
                        user = new Pharmacist(id, password, role, name); // Create UserMain.Pharmacist object
                    } else if (role.equals("Administrator")) {
                        user = new Administrator(); // Create UserMain.Administrator object
                    }
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        if (!authenticated) {
            System.out.println("Invalid user ID or password.");
            return;
        }

        // Once authenticated, use the role to show the corresponding menu
        AbstractMenu Menu = null;

        switch (role) {
            case "patient":
                Menu = new PatientMenu((Patient) user); // Pass the UserMain.Patient object to the PatientMenu
                break;
            case "Doctor":
                Menu = new DoctorMenu((Doctor) user); // Pass the UserMain.Doctor object to the DoctorMenu
                break;
            case "Pharmacist":
                Menu = new PharmacistMenu((Pharmacist) user); // Pass the UserMain.Pharmacist object to the PharmacistMenu
                break;
            case "Administrator":
                Menu = new AdministratorMenu((Administrator) user); // Pass the UserMain.Administrator object to the AdministratorMenu
                break;
            default:
                System.out.println("Role not recognized.");
                return;
        }

        // Display the appropriate menu based on the role
        Menu.displayMenu();
    }
}