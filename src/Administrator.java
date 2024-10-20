import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Administrator {

    private static final String STAFF_FILE_PATH = "Staff_List.csv";
    private static final String APPOINTMENT_FILE_PATH = "Appointments.csv";
    private static final String MEDICINE_FILE_PATH = "Medicine_List.csv"; 

    // Method to manage hospital staff (add, remove, update)
    public void manageHospitalStaff() throws IOException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Manage Hospital Staff:");
            System.out.println("1. View Staff List");
            System.out.println("2. Add Staff");
            System.out.println("3. Update Staff");
            System.out.println("4. Remove Staff");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewStaffList();
                    break;
                case 2:
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter Staff ID (e.g., D001): ");
                    String id = scanner.nextLine();
                    System.out.print("Enter Staff Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Staff Role: ");
                    String role = scanner.nextLine();
                    System.out.print("Enter Staff Gender: ");
                    String gender = scanner.nextLine();
                    System.out.print("Enter Staff Age: ");
                    int age = scanner.nextInt();
                    addStaff(id, name, role, gender, age);
                    break;
                case 3:
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter Staff ID to update: ");
                    String updateId = scanner.nextLine();
                    System.out.print("Enter new Staff Name: ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter new Staff Role: ");
                    String newRole = scanner.nextLine();
                    System.out.print("Enter new Staff Gender: ");
                    String newGender = scanner.nextLine();
                    System.out.print("Enter new Staff Age: ");
                    int newAge = scanner.nextInt();
                    updateStaff(updateId, newName, newRole, newGender, newAge);
                    break;
                case 4:
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter Staff ID to remove: ");
                    String removeId = scanner.nextLine();
                    removeStaff(removeId);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

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
    public void viewAppointmentDetails() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(APPOINTMENT_FILE_PATH))) {
            String line;
            boolean isFirstLine = true; // Skip the header

            System.out.println("Appointment Details:");
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header
                }
                String[] appointmentDetails = line.split(",");
                System.out.printf("Patient ID: %s, Doctor ID: %s, Date: %s, Time Slot: %s, Status: %s%n",
                        appointmentDetails[1], appointmentDetails[0], appointmentDetails[2],
                        appointmentDetails[3], appointmentDetails[4]);
            }
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

    // Method to view and manage medication inventory with separate options for view and update
    public void viewAndManageMedicationInventory() throws IOException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("View and Manage Medication Inventory:");
            System.out.println("1. View Medication Inventory");
            System.out.println("2. Update Medication Stock");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewMedicationInventory(); // Call method to view the inventory
                    break;
                case 2:
                    System.out.print("Enter the name of the medication to update stock: ");
                    String medicineName = scanner.nextLine();
                    System.out.print("Enter the new stock level: ");
                    int newStockLevel = scanner.nextInt();
                    updateMedicationStock(medicineName, newStockLevel); // Update stock level for medication
                    break;
                case 3:
                    System.out.println("Exiting medication inventory management...");
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
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

    // Method to approve replenishment requests
    public void approveReplenishmentRequests(Pharmacist pharmacist, Scanner scanner) throws IOException {
        List<String[]> requests = pharmacist.getReplenishmentRequests();

        // Check if there are any requests to approve
        if (requests.isEmpty()) {
            System.out.println("No replenishment requests available for approval.");
            return;
        }

        System.out.println("Replenishment Requests:");
        // Display existing requests
        for (int i = 0; i < requests.size(); i++) {
            System.out.printf("%d: Medication: %s, Quantity: %s%n", i, requests.get(i)[0], requests.get(i)[1]);
        }

        System.out.print("Enter the request index to approve: ");
        int requestIndex = scanner.nextInt();

        if (requestIndex >= 0 && requestIndex < requests.size()) {
            String[] request = requests.get(requestIndex);
            String medicineName = request[0];
            int requestedQuantity = Integer.parseInt(request[1]);

            // Call the method to update the stock in the medicine inventory
            updateMedicationStock(medicineName, requestedQuantity); // Update stock with requested quantity
            pharmacist.removeReplenishmentRequest(requestIndex); // Remove the approved request
            System.out.println("Replenishment request approved for " + medicineName);
        } else {
            System.out.println("Invalid request index.");
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
}
