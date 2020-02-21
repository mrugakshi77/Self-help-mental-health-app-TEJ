package com.example.mentalhealth.doctor_home.appointment;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.mentalhealth.FirebaseQueryLiveData;
import com.example.mentalhealth.Model.FreeSlot;
import com.example.mentalhealth.patient_navbar.patient_freeslots.PatientFreeSlotsViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FreeSlotViewModel extends ViewModel {
    private List<FreeSlot> allFreeSlots = new ArrayList<FreeSlot>();
    private String docEmail = "";
    private DatabaseReference FREESLOTS_REF;
    private FirebaseQueryLiveData liveData;
    @SuppressLint("NewApi")
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public String getDocEmail() {
        return docEmail;
    }

    public void setDocEmail(String docEmail) {
        this.docEmail = docEmail;
        Log.e("testfs3", "" + this.getDocEmail());
    }

    public void setFreeSlotData() {
        FREESLOTS_REF = FirebaseDatabase.getInstance().getReference("freeslots");
        liveData = new FirebaseQueryLiveData(FREESLOTS_REF.orderByChild("doctorEmail").equalTo(getDocEmail()));
    }


    public LiveData<List<FreeSlot>> getFreeSlots() {
        LiveData<List<FreeSlot>> freeSlotsLiveData = Transformations.map(liveData, new FreeSlotViewModel.Deserializer());
        return freeSlotsLiveData;
    }

    private class Deserializer implements Function<DataSnapshot, List<FreeSlot>> {

        @SuppressLint("NewApi")
        @Override
        public List<FreeSlot> apply(DataSnapshot input) {

            allFreeSlots.clear();
            for (DataSnapshot ds : input.getChildren()) {
                FreeSlot freeSlot = ds.getValue(FreeSlot.class);
                Log.e("testfs4", "" + freeSlot.getDate().toString());
                if (((LocalDate.now().isBefore(LocalDate.parse(freeSlot.getDate(), formatter)))
                        || (LocalDate.now().equals(LocalDate.parse(freeSlot.getDate(), formatter)))) && freeSlot.getStatusBooked() == false) {
                    allFreeSlots.add(freeSlot);
                }
            }
            return allFreeSlots;
        }
    }
}
