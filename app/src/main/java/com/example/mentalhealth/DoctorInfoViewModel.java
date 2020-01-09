package com.example.mentalhealth;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DoctorInfoViewModel extends ViewModel {

    public MutableLiveData<String> qualification = new MutableLiveData<>();
    public MutableLiveData<String> specialization = new MutableLiveData<>();
    public MutableLiveData<String> describe = new MutableLiveData<>();

    public DoctorInfoViewModel() {

    }

    private MutableLiveData<DoctorInfo> doctorInfoMutableLiveData;

    LiveData<DoctorInfo> getDoctorInfo() {
        if (doctorInfoMutableLiveData == null) {
            doctorInfoMutableLiveData = new MutableLiveData<>();
        }

        return doctorInfoMutableLiveData;
    }

    public void onSubmitClicked()
    {
        //check if all the fields are filled or not

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                DoctorInfo doctorInfo = new DoctorInfo(specialization.getValue(), qualification.getValue(), describe.getValue());

                Log.w("docInfo", doctorInfo.getDescribe());

                /*if (doctorInfo.getDescribe().isEmpty()) {

                    //describe.setValue("Description is missing");

                }*/

                doctorInfoMutableLiveData.setValue(doctorInfo);


            }
        }, 1000);
    }

}
