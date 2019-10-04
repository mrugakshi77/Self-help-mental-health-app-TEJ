package com.example.mentalhealth.patient_navbar.myaccount;

import com.google.firebase.auth.FirebaseAuth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyAccountViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private FirebaseAuth firebaseAuth;

    public MyAccountViewModel() {
        firebaseAuth = FirebaseAuth.getInstance();
        mText = new MutableLiveData<>();
        mText.setValue("username");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void signOut(){
        firebaseAuth.signOut();


    }
}