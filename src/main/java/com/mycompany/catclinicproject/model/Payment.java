package com.mycompany.catclinicproject.model;

import java.time.LocalDateTime;

public class Payment {

    private int paymentID;
    private int invoiceID;
    private double amountPaid;
    private String paymentMethod;
    private String transactionCode;
    private LocalDateTime paymentDate;

    public Payment() {
    }

    public Payment(int paymentID, int invoiceID, double amountPaid, String paymentMethod, String transactionCode, LocalDateTime paymentDate) {
        this.paymentID = paymentID;
        this.invoiceID = invoiceID;
        this.amountPaid = amountPaid;
        this.paymentMethod = paymentMethod;
        this.transactionCode = transactionCode;
        this.paymentDate = paymentDate;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public int getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(int invoiceID) {
        this.invoiceID = invoiceID;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }


}