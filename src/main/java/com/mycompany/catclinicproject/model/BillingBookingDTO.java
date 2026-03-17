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
    private String phone;
    private Date appointmentDate;
    private Timestamp appointmentTime;

    public BillingBookingDTO() {
    }

   public BillingBookingDTO(int bookingID, String catName, String ownerName, Timestamp checkInTime,Timestamp checkOutTime, String invoiceStatus , String phone , Date appointmentDate , Timestamp appointmentTime) {
        this.bookingID = bookingID;
        this.catName = catName;
        this.ownerName = ownerName;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.invoiceStatus = invoiceStatus;
        this.phone = phone;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;

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
   public String getPhone() {
        return phone;
   }
   public void setPhone(String phone) {
        this.phone = phone;
   }
    public Date getAppointmentDate() {
        return appointmentDate;
    }
    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    public Timestamp getAppointmentTime() {
        return appointmentTime;
    }
    public void setAppointmentTime(Timestamp appointmentTime) {
        this.appointmentTime = appointmentTime;
    }



}
