package com.mycompany.catclinicproject.model;

import java.sql.Date;
import java.sql.Time;

public class BookingHistoryDTO {

      private int bookingID;
    private int slotID;
    private String catName;
    private String catBreed;
    private Date appointmentDate;
    private Date endDate;
    private Time appointmentTime;
    private String serviceName;
    private String serviceType;
    private double price;
    private String status;
    private String vetName;
    private String note;
    private String ownerName;
    private String ownerPhone;
    private String customerName;
    private String customerPhone;

    public BookingHistoryDTO() {
    }

    public BookingHistoryDTO(int bookingID, int slotID, String catName, String catBreed, Date appointmentDate, Date endDate, Time appointmentTime, String serviceName, double price, String status, String vetName, String note, String ownerName, String ownerPhone) {
        this.bookingID = bookingID;
        this.slotID = slotID;
        this.catName = catName;
        this.catBreed = catBreed;
        this.appointmentDate = appointmentDate;
        this.endDate = endDate;
        this.appointmentTime = appointmentTime;
        this.serviceName = serviceName;
        this.price = price;
        this.status = status;
        this.vetName = vetName;
        this.note = note;
        this.ownerName = ownerName;
        this.ownerPhone = ownerPhone;

        if (serviceName != null && (serviceName.toLowerCase().contains("spa") || serviceName.toLowerCase().contains("grooming") || serviceName.toLowerCase().contains("hotel"))) {
            this.serviceType = "Spa";
        } else {
            this.serviceType = "Clinic";
        }
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getSlotID() {
        return slotID;
    }

    public void setSlotID(int slotID) {
        this.slotID = slotID;
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

    public String getCatBreed() {
        return catBreed;
    }

    public void setCatBreed(String catBreed) {
        this.catBreed = catBreed;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Time getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Time appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVetName() {
        return vetName;
    }

    public void setVetName(String vetName) {
        this.vetName = vetName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
}
