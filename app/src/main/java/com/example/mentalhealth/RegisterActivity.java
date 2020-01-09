package com.example.mentalhealth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mentalhealth.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    Spinner spinner;
    TextView login;

    info.hoang8f.widget.FButton fButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActivityRegisterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        final RegisterViewModel registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        binding.setRegisterViewModel(registerViewModel);
        binding.setLifecycleOwner(this);

        login = (TextView) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        fButton =findViewById(R.id.button);
        fButton.setButtonColor(getResources().getColor(R.color.colorMidnightBlue));

        spinner = (Spinner) findViewById(R.id.type);
        ArrayAdapter<String> myAdapter1 = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.type));
        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter1);

        registerViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                user.setmType(spinner.getSelectedItem().toString());
                saveUserInformation(user);
            }
        });

    }

    private void saveUserInformation(final User user) {
        //user.getEmail().replace('.','&');
        databaseReference.child("User").child(user.getEmail().replace('.','&')).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "User with this email already exists", Toast.LENGTH_LONG).show();
                } else {
                    databaseReference.child("User").child(user.getEmail().replace('.','&')).setValue(user);
                    register(user);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void register(final User user) {
        final String mail = user.getEmail();
        final String pass = user.getPassword();

        firebaseAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Intent i;

                    Toast.makeText(getApplicationContext(), "Successful User creation", Toast.LENGTH_SHORT).show();

                    Toast.makeText(getApplicationContext(), user.getmType(), Toast.LENGTH_SHORT).show();

                    if(user.getmType().equals("Doctor"))
                    {
                        i = new Intent(getApplicationContext(), DoctorInfoActivity.class);
                        i.getBundleExtra(user.getEmail());
                        i.putExtra("user_type", "Doctor");
                        Toast.makeText(getApplicationContext(), "Doc Info", Toast.LENGTH_SHORT).show();
                        startActivity(i);
                    }

                    else if(user.getmType().equals("Patient"))
                    {
                        i = new Intent(getApplicationContext(), PatientInfoActivity.class);
                        i.getBundleExtra(user.getEmail());
                        i.putExtra("user_type", "Patient");
                        Toast.makeText(getApplicationContext(), "Patient Info", Toast.LENGTH_SHORT).show();
                        startActivity(i);
                    }

                    /* Redirecting to dashboards

                    else if(user.getmType().equals("Doctor"))
                    {
                        //i = new Intent(RegisterActivity.this,doctor_dashboard.class);
                    }
                    else
                    {
                        //i = new Intent(RegisterActivity.this,volunteer_dashboard.class);
                    }

                   */

                } else {

                    if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "User with this email already exists", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            }

        });


    }
}
