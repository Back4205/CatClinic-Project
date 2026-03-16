package com.mycompany.catclinicproject.model;

import java.sql.Timestamp;
import java.util.Date;

public class BillingBookingDTO {

    private int bookingID;
    private String catName;
    private String ownerName;
    private Timestamp checkInTime;
    private Timestamp checkOutTime;
    private String invoiceStatus;

    public BillingBookingDTO() {
    }

   public BillingBookingDTO(int bookingID, String catName, String ownerName, Timestamp checkInTime,Timestamp checkOutTime, String invoiceStatus) {
        this.bookingID = bookingID;
        this.catName = catName;
        this.ownerName = ownerName;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.invoiceStatus = invoiceStatus;

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
   public Timestamp getCheckInTime() {
        return checkInTime;
   }
   public void setCheckInTime(Timestamp checkInTime) {
        this.checkInTime = checkInTime;
   }
   public Timestamp getCheckOutTime() {
        return checkOutTime;
   }
   public void setCheckOutTime(Timestamp checkOutTime) {
        this.checkOutTime = checkOutTime;
   }
   public String getInvoiceStatus() {
        return invoiceStatus;
   }
   public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
   }


}
