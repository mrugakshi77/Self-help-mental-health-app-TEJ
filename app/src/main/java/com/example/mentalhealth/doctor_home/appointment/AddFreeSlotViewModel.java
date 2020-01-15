package com.example.mentalhealth.doctor_home.appointment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddFreeSlotViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public AddFreeSlotViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
