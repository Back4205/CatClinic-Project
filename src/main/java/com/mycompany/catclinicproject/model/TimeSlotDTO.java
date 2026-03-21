package com.mycompany.catclinicproject.model;

import java.sql.Time;

public class TimeSlotDTO {

    private int slotID;
    private Time startTime;
    private Time endTime;
    private boolean isActive;

    public TimeSlotDTO() {
    }

    public TimeSlotDTO(int slotID, Time startTime, Time endTime, boolean isActive) {
        this.slotID = slotID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isActive = isActive;
    }

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

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}