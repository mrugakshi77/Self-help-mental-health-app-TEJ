package com.example.mentalhealth.Model;

public class Patient {
    private String email;
    private String mPassword;
    private String mName;
    private String mAge;
    private String mType;
    private String mConfirmPassword;
    private String marital_status;
    private String Profession;
    private String Description;

    Boolean ageEmpty, confPassEmpty, emailEmpty, emailValid, nameEmpty, passEmpty, passwordLengthGreaterThan5, same;

    public Patient() {
        this.email = "";
        this.mAge = "10";
        this.mConfirmPassword = "";
        this.mName = "";
        this.mType = "";
        this.mPassword = "";
        this.marital_status = "";
        this.Profession = "";
        this.Description = "";
        this.ageEmpty = Boolean.FALSE;
        this.confPassEmpty = Boolean.FALSE;
        this.emailEmpty = Boolean.FALSE;
        this.emailValid = Boolean.FALSE;
        this.nameEmpty = Boolean.FALSE;
        this.passEmpty = Boolean.FALSE;
        this.passwordLengthGreaterThan5 = Boolean.TRUE;
        this.same = Boolean.FALSE;
    }

    public Patient(String mEmail, String mPassword, String mName, String mAge, String mType, String mConfirmPassword,
                   String marital_status, String profession, String describe, Boolean ageEmpty, Boolean confPassEmpty,
                   Boolean emailEmpty, Boolean emailValid, Boolean nameEmpty, Boolean passEmpty,
                   Boolean passwordLengthGreaterThan5, Boolean same) {
        this.email = mEmail;
        this.mPassword = mPassword;
        this.mName = mName;
        this.mAge = mAge;
        this.mType = mType;
        this.mConfirmPassword = mConfirmPassword;
        this.marital_status = marital_status;
        this.Profession = profession;
        this.Description = describe;
        this.ageEmpty = ageEmpty;
        this.confPassEmpty = confPassEmpty;
        this.emailEmpty = emailEmpty;
        this.emailValid = emailValid;
        this.nameEmpty = nameEmpty;
        this.passEmpty = passEmpty;
        this.passwordLengthGreaterThan5 = passwordLengthGreaterThan5;
        this.same = same;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String mEmail) {
        this.email = mEmail;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmAge() {
        return mAge;
    }

    public void setmAge(String mAge) {
        this.mAge = mAge;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public String getmConfirmPassword() {
        return mConfirmPassword;
    }

    public void setmConfirmPassword(String mConfirmPassword) {
        this.mConfirmPassword = mConfirmPassword;
    }

    public String getMarital_status() {
        return marital_status;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
    }

    public String getProfession() {
        return Profession;
    }

    public void setProfession(String profession) {
        this.Profession = profession;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String describe) {
        this.Description = describe;
    }

    public Boolean getAgeEmpty() {
        return ageEmpty;
    }

    public void setAgeEmpty(Boolean ageEmpty) {
        this.ageEmpty = ageEmpty;
    }

    public Boolean getConfPassEmpty() {
        return confPassEmpty;
    }

    public void setConfPassEmpty(Boolean confPassEmpty) {
        this.confPassEmpty = confPassEmpty;
    }

    public Boolean getEmailEmpty() {
        return emailEmpty;
    }

    public void setEmailEmpty(Boolean emailEmpty) {
        this.emailEmpty = emailEmpty;
    }

    public Boolean getEmailValid() {
        return emailValid;
    }

    public void setEmailValid(Boolean emailValid) {
        this.emailValid = emailValid;
    }

    public Boolean getNameEmpty() {
        return nameEmpty;
    }

    public void setNameEmpty(Boolean nameEmpty) {
        this.nameEmpty = nameEmpty;
    }

    public Boolean getPassEmpty() {
        return passEmpty;
    }

    public void setPassEmpty(Boolean passEmpty) {
        this.passEmpty = passEmpty;
    }

    public Boolean getPasswordLengthGreaterThan5() {
        return passwordLengthGreaterThan5;
    }

    public void setPasswordLengthGreaterThan5(Boolean passwordLengthGreaterThan5) {
        this.passwordLengthGreaterThan5 = passwordLengthGreaterThan5;
    }

    public Boolean getSame() {
        return same;
    }

    public void setSame(Boolean same) {
        this.same = same;
    }
}
