package com.example.mentalhealth;

class PatientInfo {

    private String marital_status;
    private String profession;
    private String describe;

    public PatientInfo(String marital_status, String profession, String describe) {

        this.marital_status = marital_status;
        this.profession = profession;
        this.describe = describe;
    }

    public String getMarital_status() {
        if (marital_status == null) {
            return "";
        }
        return marital_status;
    }

    public String getProfession() {
        if (profession == null) {
            return "";
        }
        return profession;
    }

    public String getDescribe() {
        if (describe == null) {
            return "";
        }
        return describe;
    }
}
