/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.model;

/**
 *
 * @author Son
 */
public class AddServiceDTO {
 



    private int serviceID;
    private String nameService;
    private double price;
    private String description;
    private int timeService;
    private boolean isActive;
    private int CategoryID;
    private String ImgURL;

    public AddServiceDTO() {
    }

    public AddServiceDTO(int serviceID, String nameService, double price,
                   String description, int timeService, boolean isActive,int CategoryID,String ImgURL) {
        this.serviceID = serviceID;
        this.nameService = nameService;
        this.price = price;
        this.description = description;
        this.timeService = timeService;
        this.isActive = isActive;
        this.CategoryID = CategoryID;
        this.ImgURL = ImgURL;
    }

    public void setCategoryID(int CategoryID) {
        this.CategoryID = CategoryID;
    }
    
    public int getCategoryID() {
        return CategoryID;
    }

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public String getNameService() {
        return nameService;
    }

    public void setNameService(String nameService) {
        this.nameService = nameService;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTimeService() {
        return timeService;
    }

    public void setTimeService(int timeService) {
        this.timeService = timeService;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getImgURL() {
        return ImgURL;
    }

    public void setImgURL(String ImgURL) {
        this.ImgURL = ImgURL;
    }
    
}

