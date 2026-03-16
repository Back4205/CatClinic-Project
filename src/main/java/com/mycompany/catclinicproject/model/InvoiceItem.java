package com.mycompany.catclinicproject.model;

public class InvoiceItem {

    private int id;
    private String itemName;
    private String type;
    private int quantity;
    private double price;
    private double total;

    public InvoiceItem() {
    }

    public InvoiceItem(int id, String itemName, String type, int quantity, double price, double total) {
        this.id = id;
        this.itemName = itemName;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
