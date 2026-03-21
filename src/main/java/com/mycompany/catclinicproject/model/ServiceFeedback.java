/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.model;

/**
 *
 * @author Son
 */
public class ServiceFeedback {
    private int bookingID;
    private String nameService;

    public ServiceFeedback(){}
    public ServiceFeedback(int bookingID, String nameService) {
        this.bookingID = bookingID;
        this.nameService = nameService;
    }
    public int getBookingID() {
        return bookingID;
    }
    public String getNameService() {
        return nameService;
    }
    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }
    public void setNameService(String nameService) {
        this.nameService = nameService;
    }
    
}
