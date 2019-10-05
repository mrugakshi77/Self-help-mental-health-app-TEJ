package com.example.mentalhealth;

import android.util.Patterns;

public class User {

    private String mEmail;
    private String mPassword;
    private String mName;
    private String mAge;
    private String mType;
    private String mConfirmPassword;

    public User(String mEmail, String mPassword) {
        this.mEmail = mEmail;
        this.mPassword = mPassword;
    }

    public User(String mEmail, String mPassword, String mName, String mAge, String mType, String mConfirmPassword) {
        this.mEmail = mEmail;
        this.mPassword = mPassword;
        this.mName = mName;
        this.mAge = mAge;
        this.mType = mType;
        this.mConfirmPassword = mConfirmPassword;
    }

    public String getEmail() {
        if (mEmail == null) {
            return "";
        }
        return mEmail;
    }


    public String getPassword() {

        if (mPassword == null) {
            return "";
        }
        return mPassword;
    }

    public String getmName() {
        if (mName == null) {
            return "";
        }
        return mName;
    }

    public String getmAge() {
        if (mAge == null) {
            return "";
        }
        return mAge;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public String getmType() {
        if (mType == null) {
            return "";
        }
        return mType;
    }

    public String getmConfirmPassword() {
        if (mConfirmPassword == null) {
            return "";
        }
        return mConfirmPassword;
    }

    public boolean isEmailValid() {
        return Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches();
    }

    public boolean isPasswordLengthGreaterThan5() {
        return getPassword().length() > 5;
    }

    public boolean isEmailEmpty() { return getEmail().isEmpty();}

    public boolean isNameEmpty() { return getmName().isEmpty();}

    public boolean isAgeEmpty() { return getmAge().isEmpty();}

    public boolean isPassEmpty() { return getPassword().isEmpty();}

    public boolean isConfPassEmpty() { return getmConfirmPassword().isEmpty();}

    public boolean isSame() { return getmConfirmPassword().equals(getPassword());}

}
