package com.mycompany.catclinicproject.model;

import java.sql.Date;
import java.util.List;

public class CareTaskDTO {
    private int careJID;
    private int bookingID;
    private String status; // Pending, In Progress, Completed
    private Date endDate;
    private String bookingStatus; // Để check xem đã bấm Ready for Checkout chưa

    private int catID;
    private String catName;
    private int catAge;
    private String breed;
    private String gender;
    private String catImage;

    private String ownerName;
    private String ownerPhone;
    private String address;
    private String note;

    private List<Integer> completedTaskIds; // Lưu các ID công việc đã tích
    private int totalTasks; // Tổng số công việc (4)
    private List<String> careHistory;

    public CareTaskDTO() {}

    // Bạn dùng Alt + Insert để Generate Getter và Setter nhé!
    public int getCareJID() { return careJID; }
    public void setCareJID(int careJID) { this.careJID = careJID; }
    public int getBookingID() { return bookingID; }
    public void setBookingID(int bookingID) { this.bookingID = bookingID; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }
    public int getCatID() { return catID; }
    public void setCatID(int catID) { this.catID = catID; }
    public String getCatName() { return catName; }
    public void setCatName(String catName) { this.catName = catName; }
    public int getCatAge() { return catAge; }
    public void setCatAge(int catAge) { this.catAge = catAge; }
    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getCatImage() { return catImage; }
    public void setCatImage(String catImage) { this.catImage = catImage; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public String getOwnerPhone() { return ownerPhone; }
    public void setOwnerPhone(String ownerPhone) { this.ownerPhone = ownerPhone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public List<Integer> getCompletedTaskIds() { return completedTaskIds; }
    public void setCompletedTaskIds(List<Integer> completedTaskIds) { this.completedTaskIds = completedTaskIds; }
    public int getTotalTasks() { return totalTasks; }
    public void setTotalTasks(int totalTasks) { this.totalTasks = totalTasks; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    private java.sql.Timestamp checkOutTime;
    public java.sql.Timestamp getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(java.sql.Timestamp checkOutTime) { this.checkOutTime = checkOutTime;}


    // Thêm Getter và Setter này vào cuối file
    public List<String> getCareHistory() { return careHistory; }
    public void setCareHistory(List<String> careHistory) { this.careHistory = careHistory; }
}