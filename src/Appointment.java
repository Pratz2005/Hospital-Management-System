public class Appointment {
    private String doctorId;
    private String patientId;
    private String date;
    private String timeSlot;
    private String status;

    public Appointment(String doctorId, String patientId, String date, String timeSlot, String status) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.date = date;
        this.timeSlot = timeSlot;
        this.status = status;
    }

    public String getdoctorId(){
        return doctorId;
    }

    public String getpatientId(){
        return patientId;
    }

    public String getdate(){
        return date;
    }

    public String gettimeSlot(){
        return timeSlot;
    }

    public String getstatus(){
        return status;
    }

    public void setstatus(String newStatus){
        status = newStatus;
    }
}