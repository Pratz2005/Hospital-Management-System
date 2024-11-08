package UserMain;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Doctor extends User {
    private String specialization;
    private HashMap<String, Patient> patientRecords;

    public Doctor(String id, String password, String role, String name){
        super(id, password, role, name);
        this.specialization = specialization;
        this.patientRecords = new HashMap<>();
    }

    private static final String CSV_FILE_PATH = "doctor_availability.csv";
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final List<String> AVAILABLE_TIME_SLOTS = Arrays.asList(
            "09:00-09:30", "09:30-10:00", "10:00-10:30", "10:30-11:00",
            "11:00-11:30", "11:30-12:00", "13:00-13:30", "13:30-14:00",
            "14:00-14:30", "14:30-15:00", "15:00-15:30", "15:30-16:00",
            "16:00-16:30", "16:30-17:00"
    );

    public static void main(String[] args) {
        initializeCSV();
        captureAvailability();
    }

    // Initialize the CSV file with headers if it doesn't exist
    private static void initializeCSV() {
        try (FileWriter writer = new FileWriter(CSV_FILE_PATH, true)) {
            writer.append("DoctorID,Date,TimeSlot\n");
        } catch (IOException e) {
            System.out.println("Error initializing CSV: " + e.getMessage());
        }
    }

    // Capture availability information from the doctor
    private static void captureAvailability() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your Doctor ID: ");
        String doctorId = scanner.nextLine();

        System.out.print("Enter the date of availability (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        // Validate date format
        if (!validateDate(date)) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            return;
        }

        List<String> selectedSlots = new ArrayList<>();
        while (true) {
            displayAvailableTimeSlots();
            System.out.print("Select a time slot by entering the number (or type 'done' to finish): ");
            String input = scanner.nextLine();
            if ("done".equalsIgnoreCase(input)) {
                break;
            }

            try {
                int slotNumber = Integer.parseInt(input) - 1; // Adjust for 0-based index
                if (slotNumber >= 0 && slotNumber < AVAILABLE_TIME_SLOTS.size()) {
                    String chosenSlot = AVAILABLE_TIME_SLOTS.get(slotNumber);
                    if (!selectedSlots.contains(chosenSlot)) {
                        selectedSlots.add(chosenSlot);
                        System.out.println("Time slot " + chosenSlot + " added.");
                    } else {
                        System.out.println("Time slot " + chosenSlot + " is already selected.");
                    }
                } else {
                    System.out.println("Invalid slot number. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        // Add selected slots to CSV
        addDoctorAvailability(doctorId, date, selectedSlots);
        System.out.println("Availability added successfully.");
    }

    // Display available time slots
    private static void displayAvailableTimeSlots() {
        System.out.println("Available Time Slots:");
        for (int i = 0; i < AVAILABLE_TIME_SLOTS.size(); i++) {
            System.out.println((i + 1) + ". " + AVAILABLE_TIME_SLOTS.get(i));
        }
    }

    // Validate date format
    private static boolean validateDate(String date) {
        try {
            LocalDate.parse(date, dateFormatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Append doctor availability to CSV file
    private static void addDoctorAvailability(String doctorId, String date, List<String> timeSlots) {
        try (FileWriter writer = new FileWriter(CSV_FILE_PATH, true)) {
            for (String slot : timeSlots) {
                writer.append(doctorId).append(",")
                        .append(date).append(",")
                        .append(slot).append("\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing to CSV: " + e.getMessage());
        }
    }


     // Method to view upcoming appointments. Extra Feature??
     public List<Appointment> viewUpcomingAppointments() {
        List<Appointment> upcomingAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getStatus() != ) {
                upcomingAppointments.add(appointment);
            }
        }
        return upcomingAppointments;
    }

    public void bookappointment(Appointment appointment){
        String date = appointment.getDate();
        String timeSlot = appointment.getTimeSlot();

        if(availableSlots.containsKey(date) && availableSlots.containsKey(timeSlot)){
            appointments.add(appointment);
            availableSlots.remove(date);
            System.out.println("UserMain.Appointment added successfully");
        }else {
            System.out.println("Slot not available");
        }
    }

    public void viewAppointments(){
        for(int i = 0; i<appointments.size(); i++){
            System.out.println(appointments.get(i));
        }
    }


    // Method to view medical records of a specific patient
    public Patient viewPatientRecord(String patientId) {
        return patientRecords.get(patientId);
    }

    // Method to update medical records by adding new diagnosis, prescription, or treatment plan
    public void updatePatientRecord(String patientId, String diagnosis, String prescription, String treatment, String appointmentId) {
        Patient patient = patientRecords.get(patientId);
        if (patient != null) {
            patient.addDiagnosis(diagnosis);
            patient.addPrescription(prescription);
            patient.addTreatment(appointmentId,treatment);
            System.out.println("Updated medical record for patient " + patientId + "for Appointment" + appointmentId);
        } else {
            System.out.println("Patient record not found.");
        }
    }
}