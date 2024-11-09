package UserMenu;
import UserMain.Patient;
import java.util.Scanner;
import java.io.*;

public class PatientMenu extends AbstractMenu {
    private Patient patient;
    private Scanner sc;

    // Constructor to accept the patient object
    public PatientMenu(Patient patient) {
        this.patient = patient;
        this.sc = new Scanner(System.in);
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
        } while (choice != 9); // Repeat until logout
    }

    private void updatePersonalInformation() {
        System.out.print("Enter new email: ");
        String newEmail = sc.nextLine();
        System.out.print("Enter new contact number: ");
        String newContactNo = sc.nextLine();
        patient.updatePersonalInfo(newEmail, newContactNo);
    }

    private void viewAvailableAppointmentSlots() {
        System.out.print("Enter Doctor ID: ");
        String doctorID = sc.nextLine();
        System.out.print("Enter date (e.g., DD-MM-YY): ");
        String date = sc.nextLine();
        patient.viewAvailableAppointmentSlots(doctorID, date);
    }

    private void scheduleAppointment() {
        System.out.print("Enter Doctor ID: ");
        String doctorID = sc.nextLine();
        System.out.print("Enter date (e.g., DD-MM-YY): ");
        String date = sc.nextLine();
        System.out.print("Enter time slot (e.g., 09:00): ");
        String timeSlot = sc.nextLine();
        String patientID= patient.getPatientID();
        patient.scheduleAppointment(doctorID,patientID,date, timeSlot);
    }

    private void rescheduleAppointment() {
        System.out.print("Enter Appointment ID to reschedule: ");
        String appointmentID = sc.nextLine();
        System.out.print("Enter new date (e.g., DD-MM-YY): ");
        String newDate = sc.nextLine();
        System.out.print("Enter new time slot (e.g., 09:00): ");
        String newTimeSlot = sc.nextLine();
        patient.rescheduleAppointment(appointmentID, newDate, newTimeSlot);
    }

    private void cancelAppointment() {
        System.out.print("Enter Appointment ID to cancel: ");
        String appointmentID = sc.nextLine();
        patient.cancelAppointment(appointmentID);
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
                    System.out.println("Skipping malformed line in Appointment.csv: " + line);
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
