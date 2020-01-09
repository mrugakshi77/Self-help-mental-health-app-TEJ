package com.example.mentalhealth.patient_navbar.myaccount;

import com.example.mentalhealth.FirestoreQueryLiveData;
import com.example.mentalhealth.Model.FreeSlot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyAccountViewModel extends ViewModel {
    private FreeSlot freeSlottest = new FreeSlot();
    private MutableLiveData<String> mText;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("freeslots");
    private FirestoreQueryLiveData livedata = new FirestoreQueryLiveData(collectionReference);
   /* public LiveData<List<FreeSlot>> getLiveData(){
      //  LiveData<List<FreeSlot>> freeslotsLiveData = Transformations.map(livedata, new Deserializer());
        return freeslotsLiveData;
    }*/

    public MyAccountViewModel() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void signOut() {
        firebaseAuth.signOut();


    }
    private class Deserializer implements Function<DocumentSnapshot, List<FreeSlot>> {

        @Override
        public List<FreeSlot> apply(DocumentSnapshot input) {

          //  for(DocumentSnapshot ds : input.getClass())
            return null;
        }
    }

}