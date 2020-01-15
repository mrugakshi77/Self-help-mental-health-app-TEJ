package com.example.mentalhealth.doctor_home.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private FirebaseAuth firebaseAuth;

    public NotificationsViewModel() {
        firebaseAuth = FirebaseAuth.getInstance();
        mText = new MutableLiveData<>();
        mText.setValue("username");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void signOut() {
        firebaseAuth.signOut();


    }
}
