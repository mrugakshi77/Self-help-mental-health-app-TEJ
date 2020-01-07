package com.example.mentalhealth;

class DoctorInfo {

    private String specialization;
    private String qualification;
    private String describe;

    public DoctorInfo(String specialization, String qualification, String describe) {

        this.specialization = specialization;
        this.qualification = qualification;
        this.describe = describe;
    }

    public String getSpecialization() {
        if (specialization == null) {
            return "";
        }
        return specialization;
    }

    public String getQualification() {
        if (qualification == null) {
            return "";
        }
        return qualification;
    }

    public String getDescribe() {
        if (describe == null) {
            return "";
        }
        return describe;
    }
}
