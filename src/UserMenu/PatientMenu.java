package UserMenu;
import UserMain.Patient;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.*;
import Appointment.AppointmentService;

public class PatientMenu extends AbstractMenu {
    private Patient patient;
    private Scanner sc;
    private AppointmentService appointmentService;

    // Constructor to accept the patient object
    public PatientMenu(Patient patient) {
        this.patient = patient;
        this.sc = new Scanner(System.in);
        this.appointmentService = new AppointmentService();
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

            // Wrap input handling in try-catch to handle non-integer inputs
            try {
                choice = sc.nextInt();
                sc.nextLine(); // Clear the newline character from the buffer

                switch (choice) {
                    case 1:
                        patient.viewMedicalRecord();
                        break;
                    case 2:
                        updatePersonalInformation();
                        break;
                    case 3:
                        viewAvailableAppointmentSlots();
                        break;
                    case 4:
                        scheduleAppointment();
                        break;
                    case 5:
                        rescheduleAppointment();
                        break;
                    case 6:
                        cancelAppointment();
                        break;
                    case 7:
                        viewScheduledAppointments();
                        break;
                    case 8:
                        patient.viewPastAppointmentOutcome();
                        break;
                    case 9:
                        System.out.println("Logging out...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine(); // Clear invalid input from the scanner buffer
                choice = -1; // Reset choice to continue the loop without exiting
            }
        } while (choice != 9); // Repeat until logout
    }

    private void updatePersonalInformation() {
        String newEmail;
        String newContactNo;

        // Validate email format
        while (true) {
            System.out.print("Enter new email: ");
            newEmail = sc.nextLine();
            if (isValidEmail(newEmail)) {
                break;
            } else {
                System.out.println("Invalid email format. Please enter a valid email.");
            }
        }

        // Validate contact number format
        while (true) {
            System.out.print("Enter new contact number: ");
            newContactNo = sc.nextLine();
            if (isValidContactNumber(newContactNo)) {
                break;
            } else {
                System.out.println("Invalid contact number. Please enter an 8-digit number.");
            }
        }

        // Update personal information
        patient.updatePersonalInfo(newEmail, newContactNo);
    }

    // Helper method to validate email format
    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@[\\w-\\.]+\\.([a-z]{2,3})$"; // Simplified regex pattern for email validation
        return email.matches(emailRegex);
    }

    // Helper method to validate contact number format
    private boolean isValidContactNumber(String contactNo) {
        String contactNoRegex = "^\\d{8}$"; // Ensures exactly 8 digits
        return contactNo.matches(contactNoRegex);
    }


    private void viewAvailableAppointmentSlots() {
        Scanner scanner = new Scanner(System.in);
        String doctorID;
        String date;
        int flag = 0;

        // Step 1: Validate Doctor ID
        while (true) {
            System.out.print("Enter Doctor ID: ");
            doctorID = scanner.nextLine();
            if (appointmentService.isValidDoctorID(doctorID)) {
                break;
            } else {
                System.out.println("Invalid Doctor ID. Please enter a valid Doctor ID.");
            }
        }

        // Step 2: Validate Date and Check Availability
        while (true) {
            System.out.print("Enter the date (e.g., DD-MM-YY): ");
            date = scanner.nextLine();
            if (appointmentService.isValidDateFormat(date) && appointmentService.isDoctorAvailableOnDate(doctorID, date)) {
                break;
            } else if (!appointmentService.isValidDateFormat(date)) {
                System.out.println("Invalid date format. Please use DD-MM-YY.");
            } else {
                System.out.println("The doctor is not available on this date. Please choose another date.");
                flag=1;
                break;
            }
        }
        if (flag!=1){
            patient.viewAvailableAppointmentSlots(doctorID, date);
        }
    }

    private void scheduleAppointment() {
        String patientID = patient.getPatientID();
        patient.scheduleAppointment(patientID);
    }

    private void rescheduleAppointment() {
        String appointmentID;

        while (true) {
            System.out.print("Enter Appointment ID to cancel: ");
            appointmentID = sc.nextLine();

            // Check if the appointment ID exists and the status is not "completed"
            if (isValidAppointmentForCancellation(appointmentID)) {
                break; // Valid appointment ID, proceed to cancel
            } else {
                System.out.println("Invalid Appointment ID or the appointment is already completed. Please enter a valid Appointment ID.");
            }
        }
        patient.rescheduleAppointment(appointmentID);
    }

    private void cancelAppointment() {
        String appointmentID;

        while (true) {
            System.out.print("Enter Appointment ID to cancel: ");
            appointmentID = sc.nextLine();

            // Check if the appointment ID exists and the status is not "completed"
            if (isValidAppointmentForCancellation(appointmentID)) {
                break; // Valid appointment ID, proceed to cancel
            } else {
                System.out.println("Invalid Appointment ID or the appointment is already completed. Please enter a valid Appointment ID.");
            }
        }

        // Call the cancelAppointment method on the patient object
        patient.cancelAppointment(appointmentID);
    }

    // Helper method to validate the appointment ID and its status
    private boolean isValidAppointmentForCancellation(String appointmentID) {
        String appointmentFile = "src/Files/Appointment.csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(appointmentFile))) {
            String line = reader.readLine(); // Skip header line

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                // Check if the appointment ID matches and status is not "completed"
                if (fields[0].equals(appointmentID) && !fields[5].equalsIgnoreCase("completed")) {
                    return true; // Valid appointment ID for cancellation
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading Appointment.csv: " + e.getMessage());
        }

        return false; // Appointment ID is invalid or status is "completed"
    }


    private void viewScheduledAppointments() {
        String patientID = patient.getPatientID();
        String appointmentFile = "src/Files/Appointment.csv";
        String userFile = "src/Files/User.csv";
        boolean foundConfirmed = false;

        try (BufferedReader appointmentReader = new BufferedReader(new FileReader(appointmentFile))) {
            String line = appointmentReader.readLine(); // Skip header line

            while ((line = appointmentReader.readLine()) != null) {
                String[] fields = line.split(",");

                // Check if the line has the expected number of fields
                if (fields.length < 6) {
                    //System.out.println("Skipping malformed line in Appointment.csv: " + line);
                    continue;
                }

                String appointmentID = fields[0];
                String appointmentDoctorID = fields[1];
                String appointmentPatientID = fields[2];
                String date = fields[3];
                String timeSlot = fields[4];
                String status = fields[5];

                // Check if the appointment is for the current patient and is confirmed
                if (appointmentPatientID.equals(patientID) && status.equals("confirmed")) {
                    String doctorName = getDoctorName(appointmentDoctorID, userFile);

                    System.out.println("\n==== Upcoming Appointment ====");
                    System.out.println("Appointment ID: " + appointmentID);
                    System.out.println("Doctor Name: " + doctorName);
                    System.out.println("Date: " + date);
                    System.out.println("Time Slot: " + timeSlot);
                    System.out.println("Status: " + status);

                    foundConfirmed = true;
                }
            }

            if (!foundConfirmed) {
                System.out.println("No confirmed upcoming appointments found.");
            }
        } catch (IOException e) {
            System.err.println("Error reading Appointment.csv: " + e.getMessage());
        }
    }

    // Helper method to retrieve doctor name by doctor ID from User.csv
    private String getDoctorName(String doctorID, String userFilePath) {
        try (BufferedReader userReader = new BufferedReader(new FileReader(userFilePath))) {
            String line = userReader.readLine(); // Skip header line

            while ((line = userReader.readLine()) != null) {
                String[] userFields = line.split(",");

                // Check if the line has the expected number of fields
                if (userFields.length < 4) {
                    continue; // Skip malformed lines
                }

                String userID = userFields[0];
                String role = userFields[2];
                String name = userFields[3];

                // Match the doctor ID and role to find the doctor's name
                if (userID.equals(doctorID) && role.equalsIgnoreCase("Doctor")) {
                    return name;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading User.csv: " + e.getMessage());
        }

        return "Unknown Doctor"; // Return a default value if the doctor is not found
    }
}
