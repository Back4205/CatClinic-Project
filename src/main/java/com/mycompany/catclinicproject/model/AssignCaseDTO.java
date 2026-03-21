package com.mycompany.catclinicproject.model;

public class AssignCaseDTO {
    
    private int bookingID;
    private String catName;
    private String fullName;
    private String status;
    private int slot;
    
    public AssignCaseDTO() {
    }

    public AssignCaseDTO(int bookingID, String catName, String fullName, String status,int slot) {
        this.bookingID = bookingID;
        this.catName = catName;
        this.fullName = fullName;
        this.status = status;
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}