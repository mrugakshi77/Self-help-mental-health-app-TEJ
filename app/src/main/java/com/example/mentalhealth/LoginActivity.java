package com.example.mentalhealth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;

import com.example.mentalhealth.databinding.ActivityLoginBinding;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    ;

    info.hoang8f.widget.FButton fButton;
    private Intent i;

    TextView signup;
    ProgressBar busy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        LoginViewModel loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        binding.setLoginViewModel(loginViewModel);
        binding.setLifecycleOwner(this);

        fButton =findViewById(R.id.button);
        fButton.setButtonColor(getResources().getColor(R.color.colorMidnightBlue));


        signup =(TextView) findViewById(R.id.signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });


        loginViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if (user.getEmail().length() > 0 || user.getPassword().length() > 0)
                   // Toast.makeText(getApplicationContext(), "email : " + user.getEmail() + " password " + user.getPassword(), Toast.LENGTH_SHORT).show();

                    checkUserCredentials(user);
            }
        });
    }

    private void checkUserCredentials(final User user) {
        firebaseAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(), "Successful Sign in", Toast.LENGTH_SHORT).show();
                            final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            //Intent i;

                            databaseReference.addValueEventListener(new ValueEventListener() {

                                String userType = "";

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    userType = dataSnapshot.child("User").child(currentUser.getEmail().replace('.', '&')).child("mType").getValue().toString();
                                   // Toast.makeText(getApplicationContext(), userType, Toast.LENGTH_SHORT).show();

                                    //REDIRECTING TO DASHBOARD

                                    if (userType.equals("Patient"))
                                    {
                                        i = new Intent(LoginActivity.this, Patient_feed.class);
                                    }
                                    if(userType.equals("Doctor"))
                                    {
                                        i = new Intent(LoginActivity.this, Doctor_MainActivity.class);
                                    }
                                   /* else
                                    {
                                        i = new Intent(LoginActivity.this, Volunteer_dashboard.class);
                                    }*/
                                    startActivity(i);


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Incorrect email or password", Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }



/////////////////////////////////////////////////////////////////
    /*@Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        Toast.makeText(getApplicationContext(), "User already logged in : " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
    }*/
}




