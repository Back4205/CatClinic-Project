package com.mycompany.catclinicproject.model;

public class TestOrderViewDTO {

    private int testOrderID;
    private String fullName;
    private String catName;
    private String testName;
    private String status;
    private int medicalRecordID;

    public void setMedicalRecordID(int medicalRecordID) {
        this.medicalRecordID = medicalRecordID;
    }

    public int getMedicalRecordID() {
        return medicalRecordID;
    }

    public TestOrderViewDTO() {
    }

    public TestOrderViewDTO(int testOrderID, String fullName, String testName, String status,String catName) {
        this.testOrderID = testOrderID;
        this.fullName = fullName;
        this.testName = testName;
        this.status = status;
        this.catName = catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatName() {
        return catName;
    }

    public int getTestOrderID() {
        return testOrderID;
    }

    public void setTestOrderID(int testOrderID) {
        this.testOrderID = testOrderID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}