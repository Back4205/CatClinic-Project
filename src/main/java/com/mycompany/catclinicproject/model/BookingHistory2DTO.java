package com.mycompany.catclinicproject.model;

import java.sql.Timestamp;

public class BookingHistory2DTO {

    private int bookingID;
    private String catName;
    private String ownerName;
    private String ownerPhone;
    private Timestamp checkInTime;

    public BookingHistory2DTO() {
    }

    public BookingHistory2DTO(int bookingID, String catName, String ownerName, String ownerPhone, Timestamp checkInTime) {
        this.bookingID = bookingID;
        this.catName = catName;
        this.ownerName = ownerName;
        this.ownerPhone = ownerPhone;
        this.checkInTime = checkInTime;
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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public Timestamp getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Timestamp checkInTime) {
        this.checkInTime = checkInTime;
    }
}