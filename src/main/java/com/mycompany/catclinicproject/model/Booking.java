/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.model;


import java.sql.Time;
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
    private int StaffID;
    private int SlotID;
    private String status;
    private Date appointmentDate;
    private Date endDate;
    private Time appointmentTime;
    private String note ; 



    public Booking() {
    }

    public Booking(int bookingID, int catID, int veterinarianID, int StaffID, int SlotID, String status, Date appointmentDate, Date endDate, Time appointmentTime, String note) {
        this.bookingID = bookingID;
        this.catID = catID;
        this.veterinarianID = veterinarianID;
        this.StaffID = StaffID;
        this.SlotID = SlotID;
        this.status = status;
        this.appointmentDate = appointmentDate;
        this.endDate = endDate;
        this.appointmentTime = appointmentTime;
        this.note = note;
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

    public int getStaffID() {
        return StaffID;
    }

    public void setStaffID(int StaffID) {
        this.StaffID = StaffID;
    }

    public int getSlotID() {
        return SlotID;
    }

    public void setSlotID(int SlotID) {
        this.SlotID = SlotID;
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

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Time getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Time appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

   
    

}
