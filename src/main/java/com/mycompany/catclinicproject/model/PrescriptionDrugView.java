package com.mycompany.catclinicproject.model;

public class PrescriptionDrugView {

    private String drugName;
    private String unit;
    private int quantity;
    private String instruction;

    public PrescriptionDrugView() {}

    public PrescriptionDrugView(String drugName, String unit, int quantity, String instruction) {
        this.drugName = drugName;
        this.unit = unit;
        this.quantity = quantity;
        this.instruction = instruction;
    }
    public String getDrugName() {
        return drugName;
    }
    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
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