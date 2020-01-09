package com.example.mentalhealth;

import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {

    public MutableLiveData<String> errorPassword = new MutableLiveData<>();
    public MutableLiveData<String> errorEmail = new MutableLiveData<>();
    public MutableLiveData<String> errorName = new MutableLiveData<>();
    public MutableLiveData<String> errorAge = new MutableLiveData<>();
    public MutableLiveData<String> errorType = new MutableLiveData<>();
    public MutableLiveData<String> errorConfirmPassword = new MutableLiveData<>();

    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> age = new MutableLiveData<>();
    public MutableLiveData<String> type = new MutableLiveData<>();
    public MutableLiveData<String> confirmpassword = new MutableLiveData<>();
    public MutableLiveData<Integer> busy;
    public MutableLiveData<Integer> flag = new MutableLiveData<>();

    public MutableLiveData<Integer> getBusy() {

        if (busy == null) {
            busy = new MutableLiveData<>();
            busy.setValue(8);
        }

        return busy;
    }


    public RegisterViewModel() {

    }

    private MutableLiveData<User> userMutableLiveData;

    LiveData<User> getUser() {
        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }

        return userMutableLiveData;
    }


    public void onRegisterClicked() {

        flag.setValue(0);
        getBusy().setValue(0); //View.VISIBLE
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                User user = new User(email.getValue(), password.getValue(),name.getValue(), age.getValue(),type.getValue(), confirmpassword.getValue());

                if (user.getEmail().isEmpty()) {
                    errorEmail.setValue("Email is required");
                    flag.setValue(1);
                } else {
                    if (!user.isEmailValid()) {
                        errorEmail.setValue("Enter valid email address");
                        flag.setValue(1);
                    } else {
                        errorEmail.setValue(null);
                    }
                }

                if (user.getmName().isEmpty()) {
                    errorName.setValue("Name is required");
                    flag.setValue(1);
                } else {
                    errorName.setValue(null);
                }

                if (user.getmAge().isEmpty()) {
                    errorAge.setValue("Age is required");
                    flag.setValue(1);
                } else {
                    errorAge.setValue(null);
                }

                if (user.getPassword().isEmpty()) {
                    errorPassword.setValue("Password is required");
                    flag.setValue(1);
                } else {
                    if (!user.isPasswordLengthGreaterThan5()) {
                        errorPassword.setValue("Password Length should be greater than 5");
                        flag.setValue(1);
                    } else {
                        errorPassword.setValue(null);
                    }
                }

                if (user.getmConfirmPassword().isEmpty()) {
                    errorConfirmPassword.setValue("Confirm Password is required");
                    flag.setValue(1);
                } else {
                    if (!user.isSame()) {
                        errorConfirmPassword.setValue("Confirm password does not match");
                        flag.setValue(1);
                    } else {
                        errorConfirmPassword.setValue(null);
                    }
                }


                if (flag.getValue() == 0) {
                    userMutableLiveData.setValue(user);
                }

                busy.setValue(8); //8 == View.GONE

            }
        }, 1000);
    }
}
