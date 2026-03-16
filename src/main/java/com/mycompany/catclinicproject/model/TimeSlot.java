/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.model;

import java.sql.Date;
import java.sql.Time;

/**
 *
 * @author Vuong Bach
 */
public class TimeSlot {

    private int slotID;

    private Time startTime;
    private Time endTime;
    private boolean isActive ;

    public TimeSlot() {
    }

    public TimeSlot(int slotID, Time startTime, Time endTime, boolean isActive) {
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

    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean active) {
        isActive = active;
    }





}