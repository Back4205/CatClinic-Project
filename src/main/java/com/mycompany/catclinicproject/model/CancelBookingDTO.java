/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.model;

import java.sql.Date;

/**
 *
 * @author ADMIN
 */
public class CancelBookingDTO {
    public int bookingID;
    public String nameService;
    public String status;
    public String note;
    public Date refundDate;
    public double priceAtBooking;

    public CancelBookingDTO() {
    }

    public CancelBookingDTO(int bookingID, String nameService, String status, String note, Date refundDate, double priceAtBooking) {
        this.bookingID = bookingID;
        this.nameService = nameService;
        this.status = status;
        this.note = note;
        this.refundDate = refundDate;
        this.priceAtBooking = priceAtBooking;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public String getNameService() {
        return nameService;
    }

    public void setNameService(String nameService) {
        this.nameService = nameService;
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

    public Date getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(Date refundDate) {
        this.refundDate = refundDate;
    }

    public double getPriceAtBooking() {
        return priceAtBooking;
    }

    public void setPriceAtBooking(double priceAtBooking) {
        this.priceAtBooking = priceAtBooking;
    }

    
    
}
