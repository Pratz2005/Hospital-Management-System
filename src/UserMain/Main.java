package UserMain;

import java.io.*;
import java.util.*;
import UserMenu.PatientMenu;
import UserMenu.DoctorMenu;
import UserMenu.PharmacistMenu;
import UserMenu.AdministratorMenu;
import UserMenu.AbstractMenu;
import Appointment.AppointmentService;
import Appointment.DoctorAvailabilityService;

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
        String filePath = "/Files/User.csv";

        try (InputStream is = Main.class.getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            String line;
            // Skip header
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String userId = data[0];
                String userPassword = data[1];
                role = data[2];
                name = data[3];

                // Check if the entered ID and password match
                if (userId.equals(id) && userPassword.equals(password)) {
                    authenticated = true;
                    // Create the user object based on the role
                    if (role.equals("patient")) {
                        String patientFilePath = "/Files/Patient_List.csv";
                        try (InputStream isPatient = Main.class.getResourceAsStream(patientFilePath);
                             BufferedReader brPatient = new BufferedReader(new InputStreamReader(isPatient))) {

                            String linePatient;
                            // Skip the header line if present
                            brPatient.readLine();

                            while ((linePatient = brPatient.readLine()) != null) {
                                String[] dataPatient = linePatient.split(",");

                                // Assign values based on column order in CSV file
                                String patientId = dataPatient[0];
                                String patientPassword = dataPatient[1];
                                String name_1 = dataPatient[2];
                                String gender = dataPatient[4];
                                String dob = dataPatient[3];
                                String contactNo = dataPatient[5];
                                String email = dataPatient[6];
                                String bloodType = dataPatient[7];
                                String pastTreatment = dataPatient[8];

                                AppointmentService appointmentService = new AppointmentService();
                                DoctorAvailabilityService doctorAvailabilityService = new DoctorAvailabilityService();

                                user = new Patient(patientId, patientPassword, role, name_1, gender, dob, contactNo, email, bloodType, pastTreatment,appointmentService,doctorAvailabilityService);
                            }
                        }
                    } else if (role.equals("Doctor")) {
                        // Instantiate AppointmentService and DoctorAvailabilityService for Doctor
                        AppointmentService appointmentService = new AppointmentService();
                        DoctorAvailabilityService doctorAvailabilityService = new DoctorAvailabilityService();

                        user = new Doctor(id,password,role, name, appointmentService, doctorAvailabilityService);
                    } else if (role.equals("Pharmacist")) {
                        user = new Pharmacist(id, password, role, name);
                    } else if (role.equals("Administrator")) {
                        user = new Administrator();
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