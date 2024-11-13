package UserMenu;
import UserMain.Pharmacist;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;

import enums.MedicineList;
import enums.PrescriptionStatus;

public class PharmacistMenu extends AbstractMenu {
    private Pharmacist pharmacist;
    private static final List<String> VALID_PRESCRIPTIONS = List.of(MedicineList.AMOXICILLIN.name(), MedicineList.IBUPROFEN.name(),MedicineList.PARACETAMOL.name());

    public PharmacistMenu(Pharmacist pharmacist) {
        this.pharmacist = pharmacist;
    }

    @Override
    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n==== Pharmacist Menu ====");
            System.out.println("(1) View Appointment Outcome Record");
            System.out.println("(2) Update Prescription Status");
            System.out.println("(3) View Medication Inventory");
            System.out.println("(4) Submit Replenishment Request");

            displayLogoutOption(5); // Call the common logout option method

            // Wrap input handling in a try-catch to handle non-integer inputs
            try {
                choice = sc.nextInt();
                sc.nextLine(); // Clear the newline character from the buffer

                switch (choice) {
                    case 1:
                        viewAppointmentOutcome();
                        break;
                    case 2:
                        updatePrescriptionStatus();
                        break;
                    case 3:
                        // View medication inventory logic
                        pharmacist.viewMedicationInventory();
                        break;
                    case 4:
                        SubmitReplenishmentRequest();
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
                choice = -1; // Reset choice to ensure loop continues
            }
        } while (choice != 5); // Repeat until logout
    }

    private void viewAppointmentOutcome() {
        String appointmentID;
        while (true) {
            System.out.print("Enter Appointment ID to view: ");
            appointmentID = sc.nextLine();

            // Check if the appointment ID exists and the status is "completed"
            if (isValidAppointmentForViewing(appointmentID)) {
                pharmacist.viewAppointmentOutcome(appointmentID);
                break;
            } else {
                System.out.println("Invalid Appointment ID or the appointment is not completed. Please enter a valid Appointment ID.");
            }
        }
    }

    private void updatePrescriptionStatus() {
        String appointmentID;
        while (true) {
            System.out.print("Enter Appointment ID to update prescription status: ");
            appointmentID = sc.nextLine();

            // Check if the appointment ID exists and the status is "pending"
            if (isValidAppointmentForUpdating(appointmentID)) {
                pharmacist.updatePrescriptionStatus(appointmentID);
                break;
            } else {
                System.out.println("Invalid Appointment ID or the medicines have already been dispensed. Please enter a valid Appointment ID.");
            }
        }
    }

    // Check if the appointment exists and is completed for viewing
    public boolean isValidAppointmentForViewing(String appointmentID) {
        String appointmentFile = "resources/Appointment.csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(appointmentFile))) {
            String line = reader.readLine(); // Skip header line

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                // Check if the appointment ID matches and status is "completed"
                if (fields[0].equals(appointmentID) && fields[5].equalsIgnoreCase("completed")) {
                    return true; // Valid appointment ID for viewing
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading Appointment.csv: " + e.getMessage());
        }

        return false; // Appointment ID is invalid or status is not "completed"
    }

    // Check if the appointment exists and is pending for updating
    public boolean isValidAppointmentForUpdating(String appointmentID) {
        String appointmentFile = "resources/AppointmentRecord.csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(appointmentFile))) {
            String line = reader.readLine(); // Skip header line

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                // Check if the appointment ID matches and status is "pending"
                if (fields[0].equals(appointmentID) && fields[4].equalsIgnoreCase(PrescriptionStatus.PENDING.name())) {
                    return true; // Valid appointment ID for updating
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading Appointment.csv: " + e.getMessage());
        }

        return false; // Appointment ID is invalid or status is not "pending"
    }

    private void SubmitReplenishmentRequest() {
        String prescription;
        while (true) {
            System.out.print("Enter medicine name for replenishment (Paracetamol, Ibuprofen, Amoxicillin): ");
            prescription = sc.nextLine();

            // Use the case-insensitive check
            if (isValidPrescription(prescription)) {
                prescription = prescription.toUpperCase(); // Convert to uppercase to match enum format
                break; // Valid medicine name entered, exit the loop
            } else {
                System.out.println("Invalid medicine name. Please enter a valid name.");
            }
        }

        int quantity = 0;
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Enter quantity required: ");
            if (sc.hasNextInt()) {
                quantity = sc.nextInt();
                sc.nextLine(); // Clear the newline character from the buffer
                if (quantity > 0) { // Ensure quantity is a positive number
                    validInput = true;
                } else {
                    System.out.println("Quantity must be a positive number. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number for quantity.");
                sc.nextLine(); // Clear the invalid input from the scanner buffer
            }
        }

        pharmacist.submitReplenishmentRequest(prescription, quantity);
    }

    private boolean isValidPrescription(String prescription) {
        // Convert the input to uppercase to match the enum constants
        return VALID_PRESCRIPTIONS.contains(prescription.toUpperCase());
    }

    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }
}
