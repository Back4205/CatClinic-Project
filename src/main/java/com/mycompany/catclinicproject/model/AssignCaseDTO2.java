/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.model;

/**
 *
 * @author Son
 */
public class AssignCaseDTO2 {
    private int medicalRecordID;
    private String catName;
    private String breed;
    private String ownerName;
    private String phone;
    private String note;
    private String appointmentDate;
    private String status;
    private String diagnosis;
    public AssignCaseDTO2(){}
    public AssignCaseDTO2(int medicalRecordID, String catName, String breed,
            String ownerName, String phone, String note,
            String appointmentDate, String status, String diagnosis) {
        this.medicalRecordID = medicalRecordID;
        this.catName = catName;
        this.breed = breed;
        this.ownerName = ownerName;
        this.phone = phone;
        this.note = note;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.diagnosis = diagnosis;
    }

    public int getMedicalRecordID() {
        return medicalRecordID;
    }

    public String getCatName() {
        return catName;
    }

    public String getBreed() {
        return breed;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getPhone() {
        return phone;
    }

    public String getNote() {
        return note;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setMedicalRecordID(int medicalRecordID) {
        this.medicalRecordID = medicalRecordID;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
    
}
