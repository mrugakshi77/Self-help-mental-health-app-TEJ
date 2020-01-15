package com.example.mentalhealth.Model;

public class Doctor{

    String email;
    String mAge;
    String mConfirmPassword;
    String mName;
    String mType;
    String password;

    String Qualification;
    String Specialization;

    Boolean ageEmpty, confPassEmpty, emailEmpty, emailValid, nameEmpty, passEmpty, passwordLengthGreaterThan5, same;

    public Doctor() {
        this.email = "";
        this.mAge = "10";
        this.mConfirmPassword = "";
        this.mName = "";
        this.mType = "";
        this.password = "";
        this.Qualification = "";
        this.Specialization = "";
        this.ageEmpty = Boolean.FALSE;
        this.confPassEmpty = Boolean.FALSE;
        this.emailEmpty = Boolean.FALSE;
        this.emailValid = Boolean.FALSE;
        this.nameEmpty = Boolean.FALSE;
        this.passEmpty = Boolean.FALSE;
        this.passwordLengthGreaterThan5 = Boolean.TRUE;
        this.same = Boolean.FALSE;
    }

    public Doctor(String email, String mAge, String mConfirmPassword, String mName, String mType, String password, String qualification, String specialization) {
        this.email = email;
        this.mAge = mAge;
        this.mConfirmPassword = mConfirmPassword;
        this.mName = mName;
        this.mType = mType;
        this.password = password;
        this.Qualification = qualification;
        this.Specialization = specialization;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getmAge() {
        return mAge;
    }

    public void setmAge(String mAge) {
        this.mAge = mAge;
    }

    public String getmConfirmPassword() {
        return mConfirmPassword;
    }

    public void setmConfirmPassword(String mConfirmPassword) {
        this.mConfirmPassword = mConfirmPassword;
    }



    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQualification() {
        return Qualification;
    }

    public void setQualification(String qualification) {
        this.Qualification = qualification;
    }

    public String getSpecialization() {
        return Specialization;
    }

    public void setSpecialization(String specialization) {
        this.Specialization = specialization;
    }





}
