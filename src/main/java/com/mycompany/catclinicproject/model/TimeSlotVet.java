package com.mycompany.catclinicproject.model;

import java.util.Date;

public class TimeSlotVet {
    private int VetID ;
    private int slotID ;
    private Date date ;
    private boolean status ;

    public TimeSlotVet() {

    }
    public TimeSlotVet(int VetID, int slotID, Date date, boolean status) {
        this.VetID = VetID;
        this.slotID = slotID;
        this.date = date;
        this.status = status;

    }
    public int getVetID() {
        return VetID;

    }
    public void setVetID(int VetID) {
        this.VetID = VetID;
    }
    public int getSlotID() {
        return slotID;
    }
    public void setSlotID(int slotID) {
        this.slotID = slotID;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }

}
