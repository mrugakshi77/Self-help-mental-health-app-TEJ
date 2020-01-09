package com.example.mentalhealth;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

import androidx.lifecycle.LiveData;

public class FirestoreQueryLiveData extends LiveData<QuerySnapshot> {

    private static final String LOG_TAG = "FirestoreQueryLiveData";

    private final Query query;
    private final MyValueEventListener1 listener = new MyValueEventListener1();

    public FirestoreQueryLiveData(Query query) {
        this.query = query;
    }

    public FirestoreQueryLiveData(CollectionReference ref) {
        this.query = ref;
    }

    @Override
    protected void onActive() {
        Log.d(LOG_TAG, "onActive");
        query.addSnapshotListener(listener);
    }

    @Override
    protected void onInactive() {
        Log.d(LOG_TAG, "onInactive");

    }

    private class MyValueEventListener1 implements EventListener<QuerySnapshot> {

        @Override
        public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
            setValue(querySnapshot);
        }
    }
}
