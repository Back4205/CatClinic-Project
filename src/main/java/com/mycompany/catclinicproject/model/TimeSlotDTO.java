package com.mycompany.catclinicproject.model;

import java.sql.Date;
import java.sql.Time;

public class TimeSlotDTO {

    private int vetID;
    private int slotID;
    private Date slotDate;
    private Time startTime;
    private Time endTime;
    private boolean status;

    public TimeSlotDTO() {
    }
    public TimeSlotDTO(int vetID, int slotID, Date slotDate, Time startTime, Time endTime, boolean status) {
        this.vetID = vetID;
        this.slotID = slotID;
        this.slotDate = slotDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;

    }

    public int getVetID() {
        return vetID;
    }

    public void setVetID(int vetID) {
        this.vetID = vetID;
    }

    public int getSlotID() {
        return slotID;
    }

    public void setSlotID(int slotID) {
        this.slotID = slotID;
    }

    public Date getSlotDate() {
        return slotDate;
    }

    public void setSlotDate(Date slotDate) {
        this.slotDate = slotDate;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}