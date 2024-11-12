package UserMain;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.util.*;

public class Administrator {

    private static final String STAFF_FILE_PATH = "src/Files/Staff.csv";
    private static final String APPOINTMENT_FILE_PATH = "src/Files/Appointment.csv";
    private static final String MEDICINE_FILE_PATH = "src/Files/Medicine_List.csv";
    private static final String APPOINTMENT_RECORD_FILE = "src/Files/AppointmentRecord.csv";
    private static final String REPLENISHMENT_REQUEST_FILE = "src/Files/ReplenishmentRequest.csv";

    // Method to view the staff list
    public void viewStaffList() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(STAFF_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    // Method to view appointment details
    public void viewAppointmentDetails(String appointmentID) {
        boolean appointmentFound = false;

        // Read from Appointment.csv
        try (BufferedReader reader = new BufferedReader(new FileReader(APPOINTMENT_FILE_PATH))) {
            String line;
            System.out.println("Appointment Details:");
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                // Check if the appointment ID matches
                if (data[0].equals(appointmentID)) {
                    System.out.println("Appointment ID: " + data[0]);
                    System.out.println("Doctor ID: " + data[1]);
                    System.out.println("Patient ID: " + data[2]);
                    System.out.println("Date: " + data[3]);
                    System.out.println("Time Slot: " + data[4]);
                    System.out.println("Status: " + data[5]);
                    appointmentFound = true;
                    break;
                }
            }
            if (!appointmentFound) {
                System.out.println("No appointment found with ID: " + appointmentID);
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read from AppointmentRecord.csv
        try (BufferedReader reader = new BufferedReader(new FileReader(APPOINTMENT_RECORD_FILE))) {
            String line;
            System.out.println("\nAppointment Record Details:");
            boolean recordFound = false;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                // Check if the appointment ID matches
                if (data[0].equals(appointmentID)) {
                    System.out.println("Diagnosis: " + data[1]);
                    System.out.println("Prescription Medicine: " + data[2]);
                    System.out.println("Prescription Quantity: " + data[3]);
                    System.out.println("Prescription Status: " + data[4]);
                    System.out.println("Treatment Plan: " + data[5]);
                    System.out.println("Date: " + data[6]);
                    System.out.println("Type of Service: " + data[7]);
                    System.out.println("Consultation Notes: " + data[8]);
                    recordFound = true;
                    break;
                }
            }
            if (!recordFound) {
                System.out.println("No appointment record found with ID: " + appointmentID);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to add a staff member
    public void addStaff(String id, String name, String role, String gender, int age) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(STAFF_FILE_PATH, true))) {
            bw.write(id + "," + name + "," + role + "," + gender + "," + age);
            bw.newLine();  // Add a newline at the end
            System.out.println("Staff member added successfully.");
        }
    }

    // Method to update a staff member
    public void updateStaff(String id, String newName, String newRole, String newGender, int newAge) throws IOException {
        List<String[]> staffList = readCSV(STAFF_FILE_PATH);
        boolean staffFound = false;

        // Find the staff member by ID and update details
        for (String[] staff : staffList) {
            if (staff[0].equals(id)) {
                staff[1] = newName;
                staff[2] = newRole;
                staff[3] = newGender;
                staff[4] = String.valueOf(newAge);
                staffFound = true;
                break;
            }
        }

        if (staffFound) {
            writeStaffCSV(staffList, STAFF_FILE_PATH);
            System.out.println("Staff member updated successfully.");
        } else {
            System.out.println("Staff member with ID " + id + " not found.");
        }
    }

    // Method to remove a staff member by ID
    public void removeStaff(String id) throws IOException {
        List<String[]> staffList = readCSV(STAFF_FILE_PATH);
        boolean staffFound = false;

        // Find and remove the staff member by ID
        for (int i = 0; i < staffList.size(); i++) {
            if (staffList.get(i)[0].equals(id)) {
                staffList.remove(i);
                staffFound = true;
                break;
            }
        }

        if (staffFound) {
            writeStaffCSV(staffList, STAFF_FILE_PATH);
            System.out.println("Staff member removed successfully.");
        } else {
            System.out.println("Staff member with ID " + id + " not found.");
        }
    }

    // Method to view the medication inventory
    public void viewMedicationInventory() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(MEDICINE_FILE_PATH))) {
            String line;
            boolean isFirstLine = true; // To skip the header line
            System.out.println("Medication Inventory:");
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }
                System.out.println(line);
            }
        }
    }

    // Method to update medication stock level
    public void updateMedicationStock(String medicineName, int newStockLevel) throws IOException {
        List<String[]> medicineList = readCSV(MEDICINE_FILE_PATH);
        boolean medicineFound = false;

        for (String[] medicine : medicineList) {
            if (medicine[0].equalsIgnoreCase(medicineName)) {
                medicine[1] = String.valueOf(newStockLevel); // Update stock level
                medicineFound = true;
                break;
            }
        }

        if (medicineFound) {
            writeMedicineCSV(medicineList, MEDICINE_FILE_PATH);
            System.out.println("Stock level for " + medicineName + " updated to " + newStockLevel);
        } else {
            System.out.println("Medication " + medicineName + " not found.");
        }
    }

    public void updateLowStockLevel(String medicineName, int newLowStockLevel) throws IOException {
        List<String[]> medicineList = readCSV(MEDICINE_FILE_PATH);
        boolean medicineFound = false;

        for (String[] medicine : medicineList) {
            if (medicine[0].equalsIgnoreCase(medicineName)) {
                medicine[2] = String.valueOf(newLowStockLevel); // Update low stock level
                medicineFound = true;
                break;
            }
        }

        if (medicineFound) {
            writeMedicineCSV(medicineList, MEDICINE_FILE_PATH);
            System.out.println("Low stock level for " + medicineName + " updated to " + newLowStockLevel);
        } else {
            System.out.println("Medication " + medicineName + " not found.");
        }
    }

    // Helper method to read the CSV file into a list
    private List<String[]> readCSV(String filePath) throws IOException {
        List<String[]> staffList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true; // Skip the header

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header
                }
                String[] staff = line.split(",");
                staffList.add(staff);
            }
        }
        return staffList;
    }

    // Helper method to write the updated medicine list back to the CSV file
    private void writeMedicineCSV(List<String[]> medicineList, String filePath) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            // Write header first for the medicine list
            bw.write("Medicine Name,Initial Stock,Low Stock Level Alert");
            bw.newLine();

            // Write the rest of the medicine list
            for (String[] medicine : medicineList) {
                bw.write(String.join(",", medicine));
                bw.newLine();
            }
        }
    }


    // Helper method to write the updated staff list back to the CSV file
    private void writeStaffCSV(List<String[]> staffList, String filePath) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            // Write header first for the staff list
            bw.write("Staff ID,Name,Role,Gender,Age");
            bw.newLine();

            // Write the rest of the staff list
            for (String[] staff : staffList) {
                bw.write(String.join(",", staff));
                bw.newLine();
            }
        }
    }
    // Method to approve replenishment requests
    public void approveReplenishmentRequests() {
        List<String[]> replenishmentRequests = new ArrayList<>();
        List<String[]> updatedRequests = new ArrayList<>();
        Map<String, Integer> medicineStock = loadMedicineStock();

        // Load pending replenishment requests
        try (BufferedReader reader = new BufferedReader(new FileReader(REPLENISHMENT_REQUEST_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[3].equalsIgnoreCase("pending")) {
                    replenishmentRequests.add(data);
                }
                updatedRequests.add(data); // Store for later updates
            }
        } catch (IOException e) {
            System.err.println("Error reading ReplenishmentRequest.csv: " + e.getMessage());
            return;
        }

        // Process each pending request
        Scanner scanner = new Scanner(System.in);
        for (String[] request : replenishmentRequests) {
            String requestId = request[0];
            String medicineName = request[1];
            int quantity = Integer.parseInt(request[2]);
            String status = request[3];

            System.out.println("\nReplenishment Request:");
            System.out.println("Request ID: " + requestId);
            System.out.println("Medicine: " + medicineName);
            System.out.println("Quantity: " + quantity);
            System.out.println("Status: " + status);

            // Prompt for approval
            String approval;
            while (true) {
                System.out.print("Approve request? (Y/N): ");
                approval = scanner.nextLine().trim().toUpperCase();
                if (approval.equals("Y") || approval.equals("N")) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 'Y' for Yes or 'N' for No.");
                }
            }

            if (approval.equals("Y")) {
                request[3] = "approved"; // Update status to approved

                // Update the medicine stock by adding the requested quantity to the original stock
                if (medicineStock.containsKey(medicineName)) {
                    int currentStock = medicineStock.get(medicineName);
                    medicineStock.put(medicineName, currentStock + quantity);// Add replenishment quantity
                    System.out.println("Request approved successfully for " + medicineName + " with quantity " + quantity + ".");
                } else {
                    System.out.println("Medicine " + medicineName + " not found in the stock list.");
                }
            }
        }

        // Write updated requests back to ReplenishmentRequest.csv
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(REPLENISHMENT_REQUEST_FILE))) {
            for (String[] data : updatedRequests) {
                writer.write(String.join(",", data));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to ReplenishmentRequest.csv: " + e.getMessage());
        }

        // Write updated stock back to Medicine_List.csv
        saveUpdatedMedicineStock(medicineStock);
    }

    // Method to load medicine stock from Medicine_List.csv
    private Map<String, Integer> loadMedicineStock() {
        Map<String, Integer> stock = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(MEDICINE_FILE_PATH))) {
            String line;
            reader.readLine(); // Skip header line
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String medicineName = data[0].trim();
                int initialStock = Integer.parseInt(data[1].trim());
                stock.put(medicineName, initialStock);
            }
        } catch (IOException e) {
            System.err.println("Error reading Medicine_List.csv: " + e.getMessage());
        }
        return stock;
    }

    // Method to save updated medicine stock to Medicine_List.csv
    private void saveUpdatedMedicineStock(Map<String, Integer> medicineStock) {
        List<String[]> updatedStock = new ArrayList<>();

        // Read existing data and update stock values
        try (BufferedReader reader = new BufferedReader(new FileReader(MEDICINE_FILE_PATH))) {
            String line;
            updatedStock.add(reader.readLine().split(",")); // Add header
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String medicineName = data[0].trim();
                if (medicineStock.containsKey(medicineName)) {
                    data[1] = String.valueOf(medicineStock.get(medicineName)); // Update stock value by adding replenishment quantity
                }
                updatedStock.add(data);
            }
        } catch (IOException e) {
            System.err.println("Error reading Medicine_List.csv for updates: " + e.getMessage());
            return;
        }

        // Write the updated data back to Medicine_List.csv
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MEDICINE_FILE_PATH))) {
            for (String[] data : updatedStock) {
                writer.write(String.join(",", data));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to Medicine_List.csv: " + e.getMessage());
        }
    }

}
