/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.model;

/**
 *
 * @author ADMIN
 */
public class TestOrders {
    private int testOrderID;     
    private int medicalRecordID;      
    private String testName;     
    private String resultName;   
    private String result;       
    private String status;       
    private int staffID;

    public TestOrders() {
    }

    public TestOrders(int testOrderID, int medicalRecordID, String testName, String resultName, String result, String status, int staffID) {
        this.testOrderID = testOrderID;
        this.medicalRecordID = medicalRecordID;
        this.testName = testName;
        this.resultName = resultName;
        this.result = result;
        this.status = status;
        this.staffID = staffID;
    }

    public int getTestOrderID() {
        return testOrderID;
    }

    public void setTestOrderID(int testOrderID) {
        this.testOrderID = testOrderID;
    }

    public int getMedicalRecordID() {
        return medicalRecordID;
    }

    public void setMedicalRecordID(int medicalRecordID) {
        this.medicalRecordID = medicalRecordID;
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

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    

    
    
}
