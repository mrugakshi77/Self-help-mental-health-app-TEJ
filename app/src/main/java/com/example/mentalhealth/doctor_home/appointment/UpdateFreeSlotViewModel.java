package com.example.mentalhealth.doctor_home.appointment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UpdateFreeSlotViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public UpdateFreeSlotViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
