package com.mycompany.catclinicproject.model;

public class Feedback2DTO {
    private int bookingID;
    private int rating;
    private String comment;
    private String createdAt;
    private String fullName;

    public Feedback2DTO() {
    }
    
    public Feedback2DTO(int bookingID, int rating, String comment, String createdAt, String fullName) {
        this.bookingID = bookingID;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.fullName = fullName;
    }
    public int getBookingID() {
        return bookingID;
    }
    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}