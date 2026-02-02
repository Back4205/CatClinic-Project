/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.model;


import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author Vuong Bach
 */
public class Booking {

    private int bookingID;
    private int catID;
    private int veterinarianID;
    private String status;
    private Date appointmentDate;

    public Booking() {
    }

    public Booking(int bookingID, int catID, int veterinarianID, String status, Date appointmentDate) {
        this.bookingID = bookingID;
        this.catID = catID;
        this.veterinarianID = veterinarianID;
        this.status = status;
        this.appointmentDate = appointmentDate;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public int getCatID() {
        return catID;
    }

    public void setCatID(int catID) {
        this.catID = catID;
    }

    public int getVeterinarianID() {
        return veterinarianID;
    }

    public void setVeterinarianID(int veterinarianID) {
        this.veterinarianID = veterinarianID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    

}
