package Appointment;

import java.io.*;
import java.util.*;

public class DoctorAvailabilityService implements DoctorAvailabilityManager {
    private static final String DOCTOR_AVAILABILITY_FILE = "src/Files/DoctorAvailability.csv";

    // Method to set the availability of a doctor with specified time slots
    @Override
    public void setDoctorAvailability(String doctorID, String doctorName, String date, String[] availableSlots) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DOCTOR_AVAILABILITY_FILE, true))) {
            for (String slot : availableSlots) {
                String line = String.join(",", doctorID, doctorName, date, slot, "available");
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to view available slots for a specific doctor on a specific date
    @Override
    public String[] viewDoctorAvailability(String doctorID, String date) {
        List<String> availableSlots = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DOCTOR_AVAILABILITY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(doctorID) && data[2].equals(date) && data[4].equals("available")) {
                    availableSlots.add(data[3]);  // Add the available time slot
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return availableSlots.toArray(new String[0]);
    }

    // Method to update the availability status of a specific time slot for a doctor
    public void updateDoctorAvailability(String doctorID, String doctorName, String date, String timeSlot, boolean isAvailable) {
        List<String[]> availabilityData = new ArrayList<>();

        // Load current availability data
        try (BufferedReader reader = new BufferedReader(new FileReader(DOCTOR_AVAILABILITY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(doctorID) && data[2].equals(date) && data[3].equals(timeSlot)) {
                    data[4] = isAvailable ? "available" : "booked"; // Update the status
                }
                availabilityData.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save the updated availability data back to doctor_availability.csv
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DOCTOR_AVAILABILITY_FILE))) {
            for (String[] data : availabilityData) {
                writer.write(String.join(",", data));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
