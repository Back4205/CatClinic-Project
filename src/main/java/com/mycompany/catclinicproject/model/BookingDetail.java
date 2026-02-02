package com.mycompany.catclinicproject.model;

public class BookingDetail {
    private int bookingID;
    private int serviceID;
    private double priceAtBooking;

    public BookingDetail() {
    }

    public BookingDetail(int bookingID, int serviceID , int priceAtBooking) {
        this.bookingID = bookingID;
        this.serviceID = serviceID;
        this.priceAtBooking = priceAtBooking;
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
    public double getPriceAtBooking() {
        return priceAtBooking;
    }
    public void setPriceAtBooking(double priceAtBooking) {
        this.priceAtBooking = priceAtBooking;
    }
    
    
}
