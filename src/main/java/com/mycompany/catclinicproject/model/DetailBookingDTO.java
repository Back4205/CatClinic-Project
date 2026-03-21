package com.mycompany.catclinicproject.model;

public class DetailBookingDTO {

    private int bookingID;
    private String ownerName;
    private String phone;
    private String catName;
    private String breed;
    private String note;
    private String appointmentDate;
    private String status;
    public DetailBookingDTO() {}
    public DetailBookingDTO(int bookingID, String ownerName, String phone,
            String catName, String breed, String note,
            String appointmentDate, String status) {

        this.bookingID = bookingID;
        this.ownerName = ownerName;
        this.phone = phone;
        this.catName = catName;
        this.breed = breed;
        this.note = note;
        this.appointmentDate = appointmentDate;
        this.status = status;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
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

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}