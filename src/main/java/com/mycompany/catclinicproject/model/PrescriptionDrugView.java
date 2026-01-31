package com.mycompany.catclinicproject.model;

public class PrescriptionDrugView {

    private String drugName;
    private String unit;
    private int quantity;
    private String instruction;
    private double price ;

    public PrescriptionDrugView() {}

    public PrescriptionDrugView(String drugName, String unit, int quantity, String instruction , double price) {
        this.drugName = drugName;
        this.unit = unit;
        this.quantity = quantity;
        this.instruction = instruction;
        this.price = price;
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
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

}