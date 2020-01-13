package com.example.mentalhealth.dataholder;

import com.example.mentalhealth.Model.FreeSlot;

import java.io.Serializable;

public class FreeSlotDataHolder extends FreeSlot implements Serializable {

    public FreeSlotDataHolder(String doctorEmail, String location, Boolean statusBooked, String date, String time, String slotId) {
        super(doctorEmail, location, statusBooked, date, time, slotId);
    }
}
