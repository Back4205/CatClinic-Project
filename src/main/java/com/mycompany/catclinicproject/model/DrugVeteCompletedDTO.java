/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.model;

/**
 *
 * @author Son
 */
public class DrugVeteCompletedDTO {
    
    private int drugID;
    private int quantity;

    public DrugVeteCompletedDTO() {
    }

    public DrugVeteCompletedDTO(int drugID, int quantity) {
        this.drugID = drugID;
        this.quantity = quantity;
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
}
