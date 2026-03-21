/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.model;

/**
 *
 * @author Son
 */
public class PrescriptionView {

    private int medicalRecordID;
    private int prescriptionID;
    private String note;

    private int drugID;
    private int quantity;
    private String instruction;

    public PrescriptionView() {
    }

    public PrescriptionView(int medicalRecordID, int prescriptionID, String note,
                            int drugID, int quantity, String instruction) {
        this.medicalRecordID = medicalRecordID;
        this.prescriptionID = prescriptionID;
        this.note = note;
        this.drugID = drugID;
        this.quantity = quantity;
        this.instruction = instruction;
    }

    public int getMedicalRecordID() {
        return medicalRecordID;
    }

    public void setMedicalRecordID(int medicalRecordID) {
        this.medicalRecordID = medicalRecordID;
    }

    public int getPrescriptionID() {
        return prescriptionID;
    }

    public void setPrescriptionID(int prescriptionID) {
        this.prescriptionID = prescriptionID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getDrugID() {
        return drugID;
    }

    public void setDrugID(int drugID) {
        this.drugID = drugID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}