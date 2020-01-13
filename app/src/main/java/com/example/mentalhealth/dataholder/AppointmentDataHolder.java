package com.example.mentalhealth.dataholder;

import com.example.mentalhealth.Model.Appointment;
import com.example.mentalhealth.Model.FreeSlot;

import java.io.Serializable;

public class AppointmentDataHolder extends Appointment implements Serializable {

    public AppointmentDataHolder(FreeSlot freeSlot, String patientEmail, String visitNumber) {
        super(freeSlot, patientEmail, visitNumber);
    }


}
