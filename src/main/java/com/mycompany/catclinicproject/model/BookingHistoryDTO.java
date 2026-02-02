package com.mycompany.catclinicproject.model;

import java.sql.Date;
import java.sql.Time;

public class BookingHistoryDTO {
    private int bookingID;
    private String catName;
    private String catBreed;
    private Date appointmentDate; 
    private Date endDate;       
    private Time appointmentTime;
    private String serviceName;
    private String serviceType;
    private double price;
    private String status;

    public BookingHistoryDTO() {
    }

    public BookingHistoryDTO(int bookingID, String catName, String catBreed, Date appointmentDate, Date endDate, Time appointmentTime, String serviceName, double price, String status) {
        this.bookingID = bookingID;
        this.catName = catName;
        this.catBreed = catBreed;
        this.appointmentDate = appointmentDate;
        this.endDate = endDate; 
        this.appointmentTime = appointmentTime;
        this.serviceName = serviceName;
        this.price = price;
        this.status = status;
        
        if (serviceName != null && (serviceName.toLowerCase().contains("spa") || serviceName.toLowerCase().contains("grooming") || serviceName.toLowerCase().contains("hotel"))) {
            this.serviceType = "Spa";
        } else {
            this.serviceType = "Clinic";
        }
    }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    
    public int getBookingID() { return bookingID; }
    public void setBookingID(int bookingID) { this.bookingID = bookingID; }
    public String getCatName() { return catName; }
    public void setCatName(String catName) { this.catName = catName; }
    public String getCatBreed() { return catBreed; }
    public void setCatBreed(String catBreed) { this.catBreed = catBreed; }
    public Date getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(Date appointmentDate) { this.appointmentDate = appointmentDate; }
    public Time getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(Time appointmentTime) { this.appointmentTime = appointmentTime; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}