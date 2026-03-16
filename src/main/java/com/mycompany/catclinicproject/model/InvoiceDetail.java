package com.mycompany.catclinicproject.model;

import java.util.Date;
import java.util.List;

public class InvoiceDetail {

    private String invoiceCode;
    private String invoiceStatus;
    private Date createdDate;

    private String customerName;
    private String phone;

    private String petName;
    private String bookingCode;

    private double totalAmount;
    private String status;

    private List<InvoiceItem> items;

    public InvoiceDetail() {
    }

    public InvoiceDetail(String invoiceCode, String invoiceStatus, Date createdDate,
                         String customerName, String phone,
                         String petName, String bookingCode,String status,
                         double totalAmount, List<InvoiceItem> items) {
        this.invoiceCode = invoiceCode;
        this.invoiceStatus = invoiceStatus;
        this.createdDate = createdDate;
        this.customerName = customerName;
        this.phone = phone;
        this.petName = petName;
        this.bookingCode = bookingCode;
        this.totalAmount = totalAmount;
        this.status = status;
        this.items = items;
    }

    // Getter Setter

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }
}