package com.mycompany.catclinicproject.model;

public class AppointmentService {
    private int bookingID;
    private int serviceID;

    public AppointmentService() {
    }

    public AppointmentService(int bookingID, int serviceID) {
        this.bookingID = bookingID;
        this.serviceID = serviceID;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }
    
    
}
