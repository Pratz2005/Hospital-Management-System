package UserMain;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Patient extends User {
    private String gender;
    private String dob;
    private String contactNo;
    private String email;
    private String bloodType;
    private String pastTreatment;

    private static final Scanner scanner = new Scanner(System.in);

    public Patient(String id, String password, String role, String name, String gender, String dob,
                   String contactNo, String email, String bloodType, String pastTreatment) {
        super(id, password, role, name);
        this.gender = gender;
        this.dob = dob;
        this.contactNo = contactNo;
        this.email = email;
        this.bloodType = bloodType;
        this.pastTreatment = pastTreatment;
    }

    public Patient(String id, String name){
        super(id,name);
    }

    public void viewMedicalRecord() {
        System.out.println("Name: " + getName());
        System.out.println("Gender: " + gender);
        System.out.println("DOB: " + dob);
        System.out.println("Contact No: " + contactNo);
        System.out.println("Email: " + email);
        System.out.println("Blood Type: " + bloodType);
        System.out.println("Past Treatment: " + pastTreatment);
    }

    public void updateContactNo(String newContactNo) {
        this.contactNo = newContactNo;
    }

    public void updateEmail(String newEmail) {
        this.email = newEmail;
    }

    public void scheduleAppointment() throws IOException {
        String doctorID;
        String date;
        String timeSlot;

        while (true) {
            System.out.println("Enter UserMain.Doctor ID: ");
            doctorID = scanner.nextLine();
            if (doctorID.isEmpty()) {
                System.out.println("UserMain.Doctor ID cannot be empty. Please enter a valid UserMain.Doctor ID.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.println("Enter date in dd/mm/yy format: ");
            date = scanner.nextLine();
            if (!isValidDate(date)) {
                System.out.println("Invalid date format. Please enter the date in dd/mm/yy format.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.println("Enter time slot in hh:mm format: ");
            timeSlot = scanner.nextLine();
            if (!isValidTime(timeSlot)) {
                System.out.println("Invalid time format. Please enter the time in hh:mm format.");
                continue;
            }
            break;
        }

        // Generate a random integer for AppointmentID
        Random random = new Random();
        int appID = random.nextInt(100000); // Generates a random integer between 0 and 99,999
        String status = "confirmed";
        Appointment a = new Appointment(appID, doctorID, this.getId(), date, timeSlot, status);
        a.addToRecord(a);
    }

    // Method to validate date format dd/mm/yy
    private boolean isValidDate(String date) {
        String datePattern = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(\\d{2})$";
        Pattern pattern = Pattern.compile(datePattern);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }

    // Method to validate time format hh:mm
    private boolean isValidTime(String time) {
        String timePattern = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$";
        Pattern pattern = Pattern.compile(timePattern);
        Matcher matcher = pattern.matcher(time);
        return matcher.matches();
    }

    public void reschedule() {
        System.out.println("Enter UserMain.Appointment ID: ");
        String appointmentID = scanner.nextLine();

        Appointment appointmentToReschedule = findAppointmentById(appointmentID);

        if (appointmentToReschedule != null) {
            System.out.println("Enter new time slot in hh:mm format: ");
            String newTimeSlot = scanner.nextLine();

            appointmentToReschedule.setTimeSlot(newTimeSlot);
            appointmentToReschedule.setStatus("rescheduled");

            System.out.println("UserMain.Appointment Rescheduled Successfully");

            try {
                appointmentToReschedule.saveToCSV();
            } catch (IOException e) {
                System.out.println("Error saving appointment: " + e.getMessage());
            }
        } else {
            System.out.println("UserMain.Appointment ID not found. Please check and try again.");
        }
    }

    public void cancelAppointment() {
        System.out.println("Enter UserMain.Appointment ID: ");
        String appointmentID = scanner.nextLine();

        Appointment appointmentToCancel = findAppointmentById(appointmentID);

        if (appointmentToCancel != null) {
            appointmentToCancel.setStatus("cancelled");
            Appointment.appointmentOutRecord.remove(appointmentToCancel);
            System.out.println("UserMain.Appointment Cancelled");
        } else {
            System.out.println("UserMain.Appointment ID not found. Please check and try again.");
        }
    }

    public void viewScheduledAppointments() {
        boolean hasAppointments = false;

        System.out.println("Scheduled Appointments for " + this.getName() + " (UserMain.Patient ID: " + this.getId() + "):");
        System.out.println("----------------------------------------------------");

        for (Appointment appointment : Appointment.appointmentOutRecord) {
            if (appointment.getPatientId().equals(this.getId()) && !appointment.getStatus().equalsIgnoreCase("cancelled")) {
                System.out.println("UserMain.Appointment ID: " + appointment.getAppointmentID());
                System.out.println("UserMain.Doctor ID: " + appointment.getDoctorId());
                System.out.println("Date: " + appointment.getDate());
                System.out.println("Time Slot: " + appointment.getTimeSlot());
                System.out.println("Status: " + appointment.getStatus());
                System.out.println("----------------------------------------------------");
                hasAppointments = true;
            }
        }

        if (!hasAppointments) {
            System.out.println("No scheduled appointments found.");
        }
    }

    public void viewPastRecords() {
        System.out.println("Enter UserMain.Appointment ID to view past record: ");
        String appointmentID = scanner.nextLine();

        Appointment appointment = findAppointmentById(appointmentID);

        if (appointment != null && (appointment.getStatus().equalsIgnoreCase("completed") || appointment.getStatus().equalsIgnoreCase("cancelled"))) {
            System.out.println("Past UserMain.Appointment Details:");
            System.out.println("UserMain.Appointment ID: " + appointment.getAppointmentID());
            System.out.println("UserMain.Doctor ID: " + appointment.getDoctorId());
            System.out.println("Date: " + appointment.getDate());
            System.out.println("Time Slot: " + appointment.getTimeSlot());
            System.out.println("Status: " + appointment.getStatus());
            System.out.println("Type of Service: " + appointment.getTypeOfService());
            System.out.println("Medication: " + appointment.getMedicationName());
            System.out.println("Medication Status: " + (appointment.isMedStatus() ? "Pending" : "Dispensed"));
        } else {
            System.out.println("No past records found for the given UserMain.Appointment ID.");
        }
    }

    private Appointment findAppointmentById(String appointmentID) {
        for (Appointment appointment : Appointment.appointmentOutRecord) {
            if (String.valueOf(appointment.getAppointmentID()).equals(appointmentID)) {
                return appointment;
            }
        }
        return null;
    }

    public void logOut() {
        System.out.println("UserMain.Patient " + getName() + " has logged out successfully.");
    }
}