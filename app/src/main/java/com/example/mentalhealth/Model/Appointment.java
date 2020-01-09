package com.example.mentalhealth.Model;

import androidx.annotation.NonNull;

public class Appointment extends FreeSlot {
    String patientEmail;


    public Appointment(){
        patientEmail="";
    }
    public Appointment(FreeSlot freeSlot, String patientEmail){
        this.setFromOther(freeSlot);
        this.patientEmail = patientEmail;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public Appointment(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    @NonNull
    @Override
    public String toString() {

        return this.patientEmail+"  "+this.doctorEmail+"  "+this.date+"  "+this.time+" "+this.location;
    }
}
