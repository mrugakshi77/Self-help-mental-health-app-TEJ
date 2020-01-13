package com.example.mentalhealth.Model;

import androidx.annotation.NonNull;

public class Appointment extends FreeSlot {
    private String patientEmail;
    private String visitNumber;


    public Appointment(FreeSlot freeSlot, String patientEmail, String visitNumber) {
        this.setFromOther(freeSlot);
        this.patientEmail = patientEmail;
        this.visitNumber = visitNumber;
    }

    public Appointment(){
        patientEmail="";
        visitNumber = "0";
    }

    public Appointment(String patientEmail, String visitNumber) {
        this.patientEmail = patientEmail;
        this.visitNumber = visitNumber;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }


    public String getVisitNumber() {
        return visitNumber;
    }

    public void setVisitNumber(String visitNumber) {
        this.visitNumber = visitNumber;
    }

    @NonNull
    @Override
    public String toString() {

        return this.patientEmail + "  " + this.doctorEmail + "  " + this.date + "  " + this.time + " " + this.location
                + " " + this.visitNumber;
    }
}
