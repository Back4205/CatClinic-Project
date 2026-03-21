package com.mycompany.catclinicproject.model;

import java.sql.Time;
import java.sql.Date;

public class TimeSlotDetailDTO {

    private int slotID;
    private Time startTime;
    private Time endTime;
    private Date date;
    private String status;
    private boolean isActive;

    public TimeSlotDetailDTO() {
    }

    public TimeSlotDetailDTO(int slotID, Time startTime, Time endTime, Date date, String status, boolean isActive) {
        this.slotID = slotID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.status = status;
        this.isActive = isActive;
    }

    // Getters & Setters
    public int getSlotID() {
        return slotID;
    }

    public void setSlotID(int slotID) {
        this.slotID = slotID;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
