package com.example.mentalhealth;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    public MutableLiveData<String> errorPassword = new MutableLiveData<>();
    public MutableLiveData<String> errorEmail = new MutableLiveData<>();

    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<Integer> busy;
    public LiveData<String> utype;
    public String utype1;
    Query query;
    FirebaseQueryLiveData fqlivedata;


    public MutableLiveData<Integer> getBusy() {

        if (busy == null) {
            busy = new MutableLiveData<>();
            busy.setValue(8);
        }

        return busy;
    }

    public String getType(String email){
        query = FirebaseDatabase.getInstance().getReference().child("User").orderByChild("email").equalTo(email);

       // Log.e("dataerror",query.getRef().ge+"");
        //fqlivedata = new FirebaseQueryLiveData(query);
        //utype = Transformations.map(fqlivedata,new Desearializer());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        Log.e("typeerr",ds.getValue(User.class).getmType()+"");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return utype1;
    }


    public LoginViewModel() {

    }

    private MutableLiveData<User> userMutableLiveData;

    LiveData<User> getUser() {
        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }

        return userMutableLiveData;
    }


    public void onLoginClicked() {

        getBusy().setValue(0); //View.VISIBLE
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                User user = new User(email.getValue(), password.getValue());

                if (!user.isEmailValid()) {
                    errorEmail.setValue("Enter a valid email address");
                } else {
                    errorEmail.setValue(null);
                }

                if (!user.isPasswordLengthGreaterThan5())
                    errorPassword.setValue("Password Length should be greater than 5");
                else {
                    errorPassword.setValue(null);
                }

                userMutableLiveData.setValue(user);
                //checkUserCredentials();
                busy.setValue(8); //8 == View.GONE

            }
        }, 3000);
    }


    private class Desearializer implements Function<DataSnapshot, String> {
        @Override
        public String apply(DataSnapshot input) {
            for(DataSnapshot ds : input.getChildren()){
                utype1.equals(ds.getValue(User.class).getmType());

            }
            return utype1;
        }
    }
}
