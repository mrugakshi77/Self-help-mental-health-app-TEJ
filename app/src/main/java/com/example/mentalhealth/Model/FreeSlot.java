package com.example.mentalhealth.Model;

import com.google.firebase.Timestamp;

public class FreeSlot {

    protected String doctorEmail;
    protected String location;
    protected Boolean statusBooked;
    protected String date;
    protected String time;
    protected String slotId;

    public FreeSlot(){
        doctorEmail="";
        location="";
        date="";
        time="";
        slotId="";
    }

    public void setFromOther(FreeSlot other) {
        this.doctorEmail = other.doctorEmail;
        this.location = other.location;
        this.statusBooked = other.statusBooked;
        this.date = other.date;
        this.time = other.time;
        this.slotId = other.slotId;
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
