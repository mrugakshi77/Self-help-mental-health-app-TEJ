package com.example.mentalhealth.patient_navbar.myaccount;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyAccountViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    public MyAccountViewModel() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void signOut() {
        firebaseAuth.signOut();


    }
}