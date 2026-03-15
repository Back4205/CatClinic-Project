package com.mycompany.catclinicproject.model; // Kiểm tra lại package cho đúng của cậu nhé

public class BoardingRecordDTO {
    private String checkInTime;
    private String checkInCondition;
    private String checkOutTime;
    private String checkOutCondition;

    public String getCheckInTime() { return checkInTime; }
    public void setCheckInTime(String checkInTime) { this.checkInTime = checkInTime; }

    public String getCheckInCondition() { return checkInCondition; }
    public void setCheckInCondition(String checkInCondition) { this.checkInCondition = checkInCondition; }

    public String getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(String checkOutTime) { this.checkOutTime = checkOutTime; }

    public String getCheckOutCondition() { return checkOutCondition; }
    public void setCheckOutCondition(String checkOutCondition) { this.checkOutCondition = checkOutCondition; }
}