package com.mycompany.catclinicproject.model;

import java.util.Date;

public class Invoice {
    private int InvoiceID;
    private int BookingID;
    private double TotalAmount;
    private Date CreatedDate;
    private String PaymentStatus;

    public Invoice() {
    }

    public Invoice(int InvoiceID, int BookingID, double TotalAmount, Date CreatedDate, String PaymentStatus) {
        this.InvoiceID = InvoiceID;
        this.BookingID = BookingID;
        this.TotalAmount = TotalAmount;
        this.CreatedDate = CreatedDate;
        this.PaymentStatus = PaymentStatus;
    }

    public int getInvoiceID() {
        return InvoiceID;
    }

    public void setInvoiceID(int InvoiceID) {
        this.InvoiceID = InvoiceID;
    }

    public int getBookingID() {
        return BookingID;
    }

    public void setBookingID(int BookingID) {
        this.BookingID = BookingID;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double TotalAmount) {
        this.TotalAmount = TotalAmount;
    }

    public Date getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(Date CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    public String getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(String PaymentStatus) {
        this.PaymentStatus = PaymentStatus;
    }
    
    
}
