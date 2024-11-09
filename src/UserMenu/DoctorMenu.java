package UserMenu;
import UserMain.Doctor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DoctorMenu extends AbstractMenu {
    private Doctor doctor;
    private Scanner sc;

    public DoctorMenu(Doctor doctor) {
        this.doctor = doctor;
        this.sc = new Scanner(System.in);
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
                    viewPatientMedicalRecords();
                    break;
                case 2:
                    updatePatientMedicalRecords();
                    break;
                case 3:
                    viewPersonalSchedule();
                    break;
                case 4:
                    setAvailabilityForAppointments();
                    break;
                case 5:
                    acceptOrDeclineAppointmentRequests();
                    break;
                case 6:
                    viewUpcomingAppointments();
                    break;
                case 7:
                    recordAppointmentOutcome();
                    break;
                case 8:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 8); // Repeat until logout
    }

    private void viewPatientMedicalRecords() {
        System.out.print("Enter Patient ID to view medical records: ");
        String patientID = sc.nextLine();

        String patientFile = "src/Files/Patient_List.csv";
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(patientFile))) {
            String line = reader.readLine(); // Skip header line

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].equals(patientID)) {
                    System.out.println("\n==== Patient Medical Record ====");
                    System.out.println("Patient ID: " + fields[0]);
                    System.out.println("Name: " + fields[2]);
                    System.out.println("Gender: " + fields[3]);
                    System.out.println("Date of Birth: " + fields[4]);
                    System.out.println("Contact Number: " + fields[5]);
                    System.out.println("Email: " + fields[6]);
                    System.out.println("Blood Type: " + fields[7]);
                    System.out.println("Past Treatments: " + fields[8]);
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("No patient found with ID: " + patientID);
            }
        } catch (IOException e) {
            System.err.println("Error reading Patient_List.csv: " + e.getMessage());
        }
    }

    private void updatePatientMedicalRecords() {
        System.out.print("Enter Appointment ID to update records: ");
        String appointmentID = sc.nextLine();
        System.out.print("Enter new diagnosis: ");
        String diagnosis = sc.nextLine();
        System.out.print("Enter new prescription: ");
        String prescription = sc.nextLine();
        System.out.print("Enter prescription quantity: ");
        int quantity = sc.nextInt();
        sc.nextLine(); // Clear newline
        System.out.print("Enter new treatment plan: ");
        String treatmentPlan = sc.nextLine();
        System.out.print("Enter new consultation notes: ");
        String notes = sc.nextLine();

        doctor.updatePatientMedicalRecord(appointmentID, diagnosis, prescription, quantity, treatmentPlan, notes);
    }

    private void viewPersonalSchedule() {
        System.out.print("Enter date to view schedule (e.g., YYYY-MM-DD): ");
        String date = sc.nextLine();
        doctor.viewPersonalSchedule(date);
    }

    private void setAvailabilityForAppointments() {
        System.out.print("Enter date for availability (e.g., DD-MM-YY): ");
        String date = sc.nextLine();

        // Generate time slots from 9:00 to 17:00 in half-hour intervals
        List<String> timeSlots = new ArrayList<>();
        int startHour = 9;
        int endHour = 17;

        System.out.println("Available time slots:");
        int slotNumber = 1;
        for (int hour = startHour; hour < endHour; hour++) {
            String slot1 = String.format("%02d:00-%02d:30", hour, hour);
            String slot2 = String.format("%02d:30-%02d:00", hour, hour + 1);
            timeSlots.add(slot1);
            timeSlots.add(slot2);
            System.out.printf("(%d) %s\n", slotNumber++, slot1);
            System.out.printf("(%d) %s\n", slotNumber++, slot2);
        }

        System.out.print("Enter the numbers for available time slots (e.g., 1,3,5): ");
        String[] selectedSlots = sc.nextLine().split(",");
        List<String> availableSlots = new ArrayList<>();

        // Add the selected time slots based on input numbers
        for (String slot : selectedSlots) {
            try {
                int slotIndex = Integer.parseInt(slot.trim()) - 1;
                if (slotIndex >= 0 && slotIndex < timeSlots.size()) {
                    availableSlots.add(timeSlots.get(slotIndex));
                } else {
                    System.out.println("Invalid slot number: " + slot);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + slot);
            }
        }

        // Pass the selected slots to the doctor's availability method
        doctor.setAvailability(date, availableSlots.toArray(new String[0]));
    }

    private void acceptOrDeclineAppointmentRequests() {
        String doctorID = doctor.getDoctorID();
        String appointmentFile = "src/Files/Appointment.csv";
        boolean foundPending = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(appointmentFile))) {
            String line = reader.readLine(); // Skip header line

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                // Check if the line has the expected number of fields to avoid ArrayIndexOutOfBoundsException
                if (fields.length < 6) {
                    System.out.println("Skipping malformed line in Appointment.csv: " + line);
                    continue;
                }

                String appointmentID = fields[0];
                String appointmentDoctorID = fields[1];
                String patientID = fields[2];
                String date = fields[3];
                String timeSlot = fields[4];
                String status = fields[5];

                // Check if the appointment is for this doctor and has a pending status
                if (appointmentDoctorID.equals(doctorID) && status.equals("pending")) {
                    foundPending = true;
                    System.out.println("\n==== Pending Appointment ====");
                    System.out.println("Appointment ID: " + appointmentID);
                    System.out.println("Patient ID: " + patientID);
                    System.out.println("Date: " + date);
                    System.out.println("Time Slot: " + timeSlot);

                    // Loop until a valid response (1 or 2) is provided
                    int response;
                    while (true) {
                        System.out.print("Do you want to (1) Accept or (2) Decline this appointment? Enter 1 or 2: ");
                        if (sc.hasNextInt()) {
                            response = sc.nextInt();
                            sc.nextLine(); // Clear newline

                            if (response == 1) {
                                doctor.acceptAppointment(appointmentID);
                                System.out.println("Appointment accepted.");
                                break;
                            } else if (response == 2) {
                                doctor.declineAppointment(appointmentID);
                                System.out.println("Appointment declined.");
                                break;
                            } else {
                                System.out.println("Invalid option. Please enter 1 to accept or 2 to decline.");
                            }
                        } else {
                            System.out.println("Invalid input. Please enter a number (1 to accept or 2 to decline).");
                            sc.nextLine(); // Clear invalid input
                        }
                    }
                }
            }

            if (!foundPending) {
                System.out.println("No pending appointments found.");
            }
        } catch (IOException e) {
            System.err.println("Error reading Appointment.csv: " + e.getMessage());
        }
    }


    private void viewUpcomingAppointments() {
        doctor.viewUpcomingAppointments();
    }

    private void recordAppointmentOutcome() {
        System.out.print("Enter Appointment ID: ");
        String appointmentID = sc.nextLine();

        // Confirm appointment date from Appointment.csv
        String appointmentFile = "src/Files/Appointment.csv";
        String correctDate = null;
        boolean appointmentFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(appointmentFile))) {
            String line = reader.readLine(); // Skip header line

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                // Check if the line has the expected number of fields
                if (fields.length < 6) {
                    System.out.println("Skipping malformed line in Appointment.csv: " + line);
                    continue;
                }

                String currentAppointmentID = fields[0];
                String date = fields[3];

                if (currentAppointmentID.equals(appointmentID)) {
                    correctDate = date;
                    appointmentFound = true;
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading Appointment.csv: " + e.getMessage());
            return;
        }

        if (!appointmentFound) {
            System.out.println("No appointment found with ID: " + appointmentID);
            return;
        }

        System.out.print("Enter diagnosis: ");
        String diagnosis = sc.nextLine();
        System.out.print("Enter prescription medicine: ");
        String prescriptionMedicine = sc.nextLine();

        // Validate prescription quantity
        int quantity;
        while (true) {
            System.out.print("Enter prescription quantity: ");
            if (sc.hasNextInt()) {
                quantity = sc.nextInt();
                sc.nextLine(); // Clear newline
                if (quantity > 0) {
                    break;
                } else {
                    System.out.println("Quantity must be a positive integer. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Quantity must be a positive integer.");
                sc.nextLine(); // Clear invalid input
            }
        }

        System.out.print("Enter treatment plan: ");
        String treatmentPlan = sc.nextLine();

        // Validate date format and confirm with Appointment.csv
        String date;
        while (true) {
            System.out.print("Enter date of appointment (e.g., DD-MM-YY): ");
            date = sc.nextLine();
            if (isValidDateFormat(date)) {
                if (date.equals(correctDate)) {
                    break;
                } else {
                    System.out.println("The entered date does not match the scheduled appointment date. Please enter the correct date.");
                }
            } else {
                System.out.println("Invalid date format. Please use DD-MM-YY format.");
            }
        }

        System.out.print("Enter type of service (e.g., consultation, X-ray): ");
        String typeOfService = sc.nextLine();
        System.out.print("Enter consultation notes: ");
        String notes = sc.nextLine();

        doctor.recordAppointmentOutcome(appointmentID, diagnosis, prescriptionMedicine, quantity, treatmentPlan, date, typeOfService, notes);
    }

    // Helper method to validate date format
    private boolean isValidDateFormat(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

}
