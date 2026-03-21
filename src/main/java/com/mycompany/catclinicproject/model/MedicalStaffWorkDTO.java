package com.mycompany.catclinicproject.model;

public class MedicalStaffWorkDTO {
    private String staffName;
    private String staffRole;

    private String testName;
    private String result;
    private String resultName ;

    public MedicalStaffWorkDTO() {
    }

    public MedicalStaffWorkDTO(String staffName, String staffRole, String testName, String result , String resultName) {
        this.staffName = staffName;
        this.staffRole = staffRole;
        this.testName = testName;
        this.result = result;
        this.resultName = resultName;
    }

    public String getStaffName() {
        return staffName;
    }
    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffRole() {
        return staffRole;
    }
    public void setStaffRole(String staffRole) {
        this.staffRole = staffRole;
    }

    public String getTestName() {
        return testName;
    }
    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public String getResultName() {
        return resultName;
    }
    public void setResultName(String resultName) {
        this.resultName = resultName;
    }
}