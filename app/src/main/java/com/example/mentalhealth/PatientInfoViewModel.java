package com.example.mentalhealth;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PatientInfoViewModel extends ViewModel {

    public MutableLiveData<String> marital_status = new MutableLiveData<>();
    public MutableLiveData<String> profession = new MutableLiveData<>();
    public MutableLiveData<String> describe = new MutableLiveData<>();

    public PatientInfoViewModel() {

    }

    private MutableLiveData<PatientInfo> patientInfoMutableLiveData;

    LiveData<PatientInfo> getPatientInfo() {
        if (patientInfoMutableLiveData == null) {
            patientInfoMutableLiveData = new MutableLiveData<>();
        }

        return patientInfoMutableLiveData;
    }

    public void onSubmitClicked()
    {
        //check if all the fields are filled or not

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                PatientInfo patientInfo = new PatientInfo(marital_status.getValue(), profession.getValue(), describe.getValue());

                Log.w("patientInfo", patientInfo.getDescribe());

                /*if (patientInfo.getDescribe().isEmpty()) {

                    //describe.setValue("Description is missing");

                }*/

                patientInfoMutableLiveData.setValue(patientInfo);


            }
        }, 1000);
    }
}
