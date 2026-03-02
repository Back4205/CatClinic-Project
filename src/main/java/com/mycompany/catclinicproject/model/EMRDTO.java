package com.mycompany.catclinicproject.model;

import java.time.LocalDateTime;

public class EMRDTO {

    private int medicalRecordID;
    private String diagnosis;
    private String symptoms;
    private String treatmentPlan;
    private LocalDateTime createdAt;

    private int bookingID;
    private LocalDateTime appointmentDate;
    private String status;
    private String note;

    private String catName;
    private String breed;
    private int age;

    private String ownerName;
    private String phone;

    public EMRDTO() {
    }

    public EMRDTO(int medicalRecordID, String diagnosis, String symptoms, String treatmentPlan, LocalDateTime createdAt, int bookingID, LocalDateTime appointmentDate, String status, String note, String catName, String breed, int age, String ownerName, String phone) {
        this.medicalRecordID = medicalRecordID;
        this.diagnosis = diagnosis;
        this.symptoms = symptoms;
        this.treatmentPlan = treatmentPlan;
        this.createdAt = createdAt;
        this.bookingID = bookingID;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.note = note;
        this.catName = catName;
        this.breed = breed;
        this.age = age;
        this.ownerName = ownerName;
        this.phone = phone;
    }

    public int getMedicalRecordID() {
        return medicalRecordID;
    }

    public void setMedicalRecordID(int medicalRecordID) {
        this.medicalRecordID = medicalRecordID;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getTreatmentPlan() {
        return treatmentPlan;
    }

    public void setTreatmentPlan(String treatmentPlan) {
        this.treatmentPlan = treatmentPlan;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
