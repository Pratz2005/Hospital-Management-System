import java.io.IOException;
import java.util.Scanner;

public class PTest {
    public static void main(String[] args) throws IOException {
        // Creating a sample patient
        Patient patient = new Patient(
                "P123", "password", "Patient", "John Doe", "Male",
                "01/01/1985", "+1234567890", "johndoe@example.com", "O+", "No past treatments"
        );

        // Sample data for appointments
        Appointment.appointmentOutRecord.add(new Appointment(1, "D001", "P123", "10/11/2024", "10:00", "confirmed"));
        Appointment.appointmentOutRecord.add(new Appointment(2, "D002", "P123", "15/11/2024", "15:00", "completed"));
        Appointment.appointmentOutRecord.add(new Appointment(3, "D003", "P456", "16/11/2024", "14:00", "confirmed"));

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("Patient Menu:");
            System.out.println("1. View Medical Record");
            System.out.println("2. Update Contact Information");
            System.out.println("3. View Available Appointment Slots");
            System.out.println("4. Schedule Appointment");
            System.out.println("5. Reschedule Appointment");
            System.out.println("6. Cancel Appointment");
            System.out.println("7. View Scheduled Appointments");
            System.out.println("8. View Past Appointment Outcome Records");
            System.out.println("9. Log Out");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    patient.viewMedicalRecord();
                    break;
                case 2:
                    System.out.print("Enter new contact number: ");
                    String newContact = sc.nextLine();
                    System.out.print("Enter new email: ");
                    String newEmail = sc.nextLine();
                    patient.updateContactNo(newContact);
                    patient.updateEmail(newEmail);
                    System.out.println("Contact information updated successfully.");
                    break;
                case 3:
                    System.out.println("Available appointment slots:");
                    for (Appointment appt : Appointment.appointmentOutRecord) {
                        if (appt.getStatus().equals("confirmed")) {
                            System.out.println("Doctor ID: " + appt.getDoctorId() + ", Date: " + appt.getDate() + ", Time: " + appt.getTimeSlot());
                        }
                    }
                    break;
                case 4:
                    patient.scheduleAppointment();
                    break;
                case 5:
                    patient.reschedule();
                    break;
                case 6:
                    patient.cancelAppointment();
                    break;
                case 7:
                    patient.viewScheduledAppointments();
                    break;
                case 8:
                    patient.viewPastRecords();
                    break;
                case 9:
                    patient.logOut();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            System.out.println();
        } while (choice != 9);

        sc.close();
    }
}
