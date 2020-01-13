package com.example.mentalhealth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mentalhealth.databinding.ActivityPatientInfoBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PatientInfoActivity extends AppCompatActivity {

    info.hoang8f.widget.FButton fButton_setDp, fButton_submit;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    Spinner marital_status;
    Spinner profession;
    EditText describe_patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);

        ActivityPatientInfoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_patient_info);
        final PatientInfoViewModel patientInfoViewModel = ViewModelProviders.of(this).get(PatientInfoViewModel.class);
        binding.setPatientInfoViewModel(patientInfoViewModel);
        binding.setLifecycleOwner(this);

        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.getWindow().setBackgroundDrawableResource(R.color.fbutton_color_transparent);

        fButton_setDp =findViewById(R.id.setDp_patient);
        fButton_submit =findViewById(R.id.submit_patient);
        fButton_setDp.setButtonColor(getResources().getColor(R.color.colorWhite));
        fButton_submit.setButtonColor(getResources().getColor(R.color.colorWhite));

        marital_status = (Spinner) findViewById(R.id.marital_status);
        ArrayAdapter<String> myAdapter1 = new ArrayAdapter<String>(PatientInfoActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.marital_status));
        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        marital_status.setAdapter(myAdapter1);

        profession = (Spinner) findViewById(R.id.profession);
        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<String>(PatientInfoActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.profession));
        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profession.setAdapter(myAdapter2);

        describe_patient =(EditText) findViewById(R.id.describe_patient);

        patientInfoViewModel.getPatientInfo().observe(this, new Observer<PatientInfo>() {
            @Override
            public void onChanged(@Nullable PatientInfo patientInfo) {
                //if (PatientInfo.getDescribe().length() > 0)
                //{
                    savePatientInformation(patientInfo);
                //}
            }
        });
    }

    private void savePatientInformation(final PatientInfo patientInfo) {

        //save info

        final FirebaseUser curr_user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child("User").child(curr_user.getEmail().replace('.','&')).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "Some error has occurred", Toast.LENGTH_LONG).show();
                } else {
                    databaseReference.child("User").child(curr_user.getEmail().replace('.','&')).child("Marital Status").setValue(marital_status.getSelectedItem().toString());
                    databaseReference.child("User").child(curr_user.getEmail().replace('.','&')).child("Profession").setValue(profession.getSelectedItem().toString());
                    databaseReference.child("User").child(curr_user.getEmail().replace('.','&')).child("Description").setValue(patientInfo.getDescribe());

                    Toast.makeText(getApplicationContext(), "Saving Patient Info", Toast.LENGTH_LONG).show();

                    redirect(curr_user);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void redirect(FirebaseUser curr_user) {

        Intent i = new Intent(PatientInfoActivity.this, Questionnaire.class);
        startActivity(i);
    }
}
