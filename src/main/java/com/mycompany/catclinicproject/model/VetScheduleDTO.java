/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.model;

import java.sql.Date;

/**
 *
 * @author Son
 */
public class VetScheduleDTO {
     private int slotID;
    private String startTime;
    private String endTime;
    private Date date;
    private Integer bookingID;
    private String fullName;
    private String catName;
    private String status;
    public VetScheduleDTO(){}
    public VetScheduleDTO(int slotID, String startTime, String endTime, Date date, Integer bookingID, String fullName, String catName, String status) {
        this.slotID = slotID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.bookingID = bookingID;
        this.fullName = fullName;
        this.catName = catName;
        this.status = status;
    }

    public void setSlotID(int slotID) {
        this.slotID = slotID;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setBookingID(Integer bookingID) {
        this.bookingID = bookingID;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSlotID() {
        return slotID;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public Date getDate() {
        return date;
    }

    public Integer getBookingID() {
        return bookingID;
    }

    public String getFullName() {
        return fullName;
    }

    public String getCatName() {
        return catName;
    }

    public String getStatus() {
        return status;
    }
}
