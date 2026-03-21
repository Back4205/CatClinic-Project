package com.mycompany.catclinicproject.model;

import java.sql.Time;
import java.sql.Date;

public class ReceptionDashboardDTO {
    private int bookingID;
    private String customerName;
    private String phone;
    private String catName;
    private Date appointmentDate;
    private Time appointmentTime;
    private String status;

    public ReceptionDashboardDTO(int bookingID, String customerName, String phone, String catName, Date appointmentDate, Time appointmentTime, String status) {
        this.bookingID = bookingID;
        this.customerName = customerName;
        this.phone = phone;
        this.catName = catName;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

    public int getBookingID() { return bookingID; }
    public String getCustomerName() { return customerName; }
    public String getPhone() { return phone; }
    public String getCatName() { return catName; }
    public Date getAppointmentDate() { return appointmentDate; }
    public Time getAppointmentTime() { return appointmentTime; }
    public String getStatus() { return status; }
}