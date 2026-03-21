package com.mycompany.catclinicproject.model;

import java.sql.Time;
import java.util.Date;

public class NotificationBooking {

    private int id;
    private String ownerName;
    private String phone ;
    private Date appointmentDate;
    private Time appointmentTime;
    private String status;
    public NotificationBooking() {

    }
    public NotificationBooking(String ownerName, String phone, Date appointmentDate, Time appointmentTime, String status) {
        this.ownerName = ownerName;
        this.phone = phone;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;

    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getOwnerName() {
        return ownerName;
    }
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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
    public Time getAppointmentTime() {
        return appointmentTime;
    }
    public void setAppointmentTime(Time appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
