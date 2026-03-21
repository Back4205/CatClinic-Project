package com.mycompany.catclinicproject.model;
public class FeedbackDTO {

    private int bookingID;
    private int rating;
    private String comment;
    private String createdAt;
    private String fullName;

    public FeedbackDTO(int bookingID, int rating, String comment, String createdAt, String fullName) {
        this.bookingID = bookingID;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.fullName = fullName;
    }

    public int getBookingID() {
        return bookingID;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getFullName() {
        return fullName;
    }
}