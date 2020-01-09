package com.example.mentalhealth.patient_navbar.patient_doclist;

import android.util.Log;

import com.example.mentalhealth.FirebaseQueryLiveData;
import com.example.mentalhealth.Model.Doctor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class PatientDocListViewModel extends ViewModel {
    private List<Doctor> allDocs = new ArrayList<Doctor>();
    private DatabaseReference DOC_REF = FirebaseDatabase.getInstance().getReference("User");
    private FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(DOC_REF.orderByChild("mType").equalTo("Doctor"));

    public LiveData<List<Doctor>> getDocsLiveData(){
        LiveData<List<Doctor>> docsLiveData = Transformations.map(liveData, new Deserializer());
        return docsLiveData;
    }

    private class Deserializer implements Function<DataSnapshot, List<Doctor>> {

        @Override
        public List<Doctor> apply(DataSnapshot input) {

            allDocs.clear();
            for (DataSnapshot ds : input.getChildren()) {
                Doctor doc = ds.getValue(Doctor.class);
                  Log.e("testDL1",""+doc.getQualification()+"  sp:"+doc.getSpecialization());
                allDocs.add(doc);
            }
            return allDocs;
        }
    }

}
