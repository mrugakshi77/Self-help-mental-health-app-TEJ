package com.example.mentalhealth.Model;



public class FreeSlot {

    protected String doctorEmail;
    protected String location;
    protected Boolean statusBooked;
    protected String date;
    protected String time;
    protected String slotId;

    public FreeSlot(FreeSlot appointment) {
        doctorEmail="";
        location="";
        date="";
        time="";
        slotId="";
    }

    public FreeSlot() {
    }

    public void setFromOther(FreeSlot other) {
        this.doctorEmail = other.doctorEmail;
        this.location = other.location;
        this.statusBooked = other.statusBooked;
        this.date = other.date;
        this.time = other.time;
        this.slotId = other.slotId;
    }

    public FreeSlot(String doctorEmail, String location, Boolean statusBooked, String date, String time, String slotId) {
        this.doctorEmail = doctorEmail;
        this.location = location;
        this.statusBooked = statusBooked;
        this.date = date;
        this.time = time;
        this.slotId = slotId;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getStatusBooked() {
        return statusBooked;
    }

    public void setStatusBooked(Boolean statusBooked) {
        this.statusBooked = statusBooked;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }
}
