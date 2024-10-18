import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Doctor extends User{
    private Map<String, List<String>> availableSlots; //Mapping date to Available time slots
    private List<Appointment> appointments;

    public Doctor(String id, String password, String role, String name){
        super(id, password, role, name);
        this.availableSlots = new HashMap<>();
        this.appointments = new ArrayList<>();
    }

    public void addAvailableSlot(String slot, String TimeSlot){
        availableSlots.putIfAbsent(slot, new ArrayList<>());
        availableSlots.get(slot).add(TimeSlot);
    }

    public void removeAvailableSlot(String slot, String TimeSlot){
        availableSlots.get(slot).remove(TimeSlot);
    }

}