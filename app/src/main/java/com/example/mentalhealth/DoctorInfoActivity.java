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

import com.example.mentalhealth.databinding.ActivityDoctorInfoBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DoctorInfoActivity extends AppCompatActivity {

    info.hoang8f.widget.FButton fButton_setDp, fButton_submit;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    Spinner specialization;
    Spinner qualification;
    EditText describe_doctor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_info);

        ActivityDoctorInfoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_info);
        final DoctorInfoViewModel doctorInfoViewModel = ViewModelProviders.of(this).get(DoctorInfoViewModel.class);
        binding.setDoctorInfoViewModel(doctorInfoViewModel);
        binding.setLifecycleOwner(this);

        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.getWindow().setBackgroundDrawableResource(R.color.fbutton_color_transparent);

        fButton_setDp =findViewById(R.id.setDp_doctor);
        fButton_submit =findViewById(R.id.submit_doctor);
        fButton_setDp.setButtonColor(getResources().getColor(R.color.colorWhite));
        fButton_submit.setButtonColor(getResources().getColor(R.color.colorWhite));

        specialization = (Spinner) findViewById(R.id.specialization);
        ArrayAdapter<String> myAdapter1 = new ArrayAdapter<String>(DoctorInfoActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.specialization));
        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialization.setAdapter(myAdapter1);

        qualification = (Spinner) findViewById(R.id.qualification);
        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<String>(DoctorInfoActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.qualification));
        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qualification.setAdapter(myAdapter2);

        describe_doctor =(EditText) findViewById(R.id.describe_doctor);


        doctorInfoViewModel.getDoctorInfo().observe(this, new Observer<DoctorInfo>() {
            @Override
            public void onChanged(@Nullable DoctorInfo doctorInfo) {
                //if (doctorInfo.getDescribe().length() > 0)
                    saveDoctorInformation(doctorInfo);
            }
        });
    }

    private void saveDoctorInformation(final DoctorInfo doctorInfo) {
        //save info

        final FirebaseUser curr_user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child("User").child(curr_user.getEmail().replace('.','&')).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "Some error has occurred", Toast.LENGTH_LONG).show();
                } else {
                    databaseReference.child("User").child(curr_user.getEmail().replace('.','&')).child("Specialization").setValue(specialization.getSelectedItem().toString());
                    databaseReference.child("User").child(curr_user.getEmail().replace('.','&')).child("Qualification").setValue(qualification.getSelectedItem().toString());
                    databaseReference.child("User").child(curr_user.getEmail().replace('.','&')).child("Description").setValue(doctorInfo.getDescribe());

                    Toast.makeText(getApplicationContext(), "Saving Doc Info", Toast.LENGTH_LONG).show();

                    redirect(curr_user);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void redirect(FirebaseUser curr_user)
    {
        Intent i = new Intent(DoctorInfoActivity.this, Doctor_MainActivity.class);
        startActivity(i);
    }
}
