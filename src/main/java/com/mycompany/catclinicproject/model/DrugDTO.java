package com.mycompany.catclinicproject.model;

public class DrugDTO {

    private int drugID;
    private double price;
    private int stockQuantity;
    private String unit;
    private String name;
    private boolean isActive;

    public DrugDTO(int drugID, double price, int stockQuantity,
            String unit, String name, boolean isActive) {
        this.drugID = drugID;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.unit = unit;
        this.name = name;
        this.isActive = isActive;
    }

    public void setDrugID(int drugID) {
        this.drugID = drugID;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getDrugID() {
        return drugID;
    }

    public double getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public String getUnit() {
        return unit;
    }

    public String getName() {
        return name;
    }

    public boolean isIsActive() {
        return isActive;
    }

}
