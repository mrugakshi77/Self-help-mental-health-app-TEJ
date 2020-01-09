package com.example.mentalhealth.patient_navbar.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mentalhealth.LoginActivity;
import com.example.mentalhealth.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyAccountFragment extends Fragment {

    private MyAccountViewModel myAccountViewModel;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myAccountViewModel =
                ViewModelProviders.of(this).get(MyAccountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_myaccount, container, false);
        final TextView textView = root.findViewById(R.id.Username);
       // TextView test = root.findViewById(R.id.firestoretest);
        Button logOut = root.findViewById(R.id.LogOut);

        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseFirestore.collection("freeslots");




        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser curruser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String uname = dataSnapshot.child("User").child(curruser.getEmail().replace('.', '&')).child("mName").getValue().toString();
                textView.setText("Username : " + uname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAccountViewModel.signOut();
                Intent intent1 = new Intent(getActivity(), LoginActivity.class);
                getActivity().finish();
                startActivity(intent1);
            }
        });

        return root;
    }
}