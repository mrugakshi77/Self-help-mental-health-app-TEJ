package com.example.mentalhealth.doctor_home.patientprofile;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.mentalhealth.FirebaseQueryLiveData;
import com.example.mentalhealth.Model.Patient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class PatientProfileViewModel extends ViewModel {
    private List<Patient> patients = new ArrayList<Patient>();
    private String patientEmail = "";
    private DatabaseReference Patient_REF;
    private FirebaseQueryLiveData liveData;

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
        Log.e("testfs3", "" + this.getPatientEmail());
    }

    public void setPatientData() {
        Patient_REF = FirebaseDatabase.getInstance().getReference("User");
        liveData = new FirebaseQueryLiveData(Patient_REF.orderByChild("email").equalTo(getPatientEmail()));
    }


    public LiveData<List<Patient>> getPatients() {
        LiveData<List<Patient>> patientsLiveData = Transformations.map(liveData, new PatientProfileViewModel.Deserializer());
        return patientsLiveData;
    }

    private class Deserializer implements Function<DataSnapshot, List<Patient>> {

        @Override
        public List<Patient> apply(DataSnapshot input) {

            patients.clear();
            for (DataSnapshot ds : input.getChildren()) {
                Patient patient1 = ds.getValue(Patient.class);
                Log.e("testfs4", "" + patient1.getEmail());
                patients.add(patient1);
            }
            return patients;
        }
    }
}
