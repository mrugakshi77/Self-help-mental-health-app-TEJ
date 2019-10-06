package com.example.mentalhealth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {

            databaseReference.addValueEventListener(new ValueEventListener() {

                String userType = "";

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userType = dataSnapshot.child("User").child(currentUser.getEmail().replace('.', '&')).child("mType").getValue().toString();

                    /*  REDIRECTING TO DASHBOARD*/

                    if (userType.equals("Patient")) {
                        Intent i = new Intent(SplashActivity.this, Patient_feed.class);
                        finish();
                        startActivity(i);
                    } else if (userType.equals("Doctor")) {
                        //i = new Intent(LoginActivity.this, Doctor_dashboard.class)
                    } else {
                        //i = new Intent(LoginActivity.this, Volunteer_dashboard.class)
                    }
                    //startActivity(i);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
