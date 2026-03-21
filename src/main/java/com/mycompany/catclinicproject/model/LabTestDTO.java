package com.mycompany.catclinicproject.model;

public class LabTestDTO {
    private int testOrderID;
    private String catName;
    private String testName;
    private String resultName;
    private String result;
    private String status;
    private String doctorName;

    public LabTestDTO() {
    }

    public LabTestDTO(int testOrderID, String catName, String testName, String resultName, String result, String status, String doctorName) {
        this.testOrderID = testOrderID;
        this.catName = catName;
        this.testName = testName;
        this.resultName = resultName;
        this.result = result;
        this.status = status;
        this.doctorName = doctorName;
    }

    public int getTestOrderID() {
        return testOrderID;
    }

    public void setTestOrderID(int testOrderID) {
        this.testOrderID = testOrderID;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getResultName() {
        return resultName;
    }

    public void setResultName(String resultName) {
        this.resultName = resultName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}