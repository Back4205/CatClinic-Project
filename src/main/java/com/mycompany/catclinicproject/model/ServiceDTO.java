/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.model;

/**
 *
 * @author Son
 */
public class ServiceDTO {
     private int serviceID;
    private String nameService;
    private double price;
    private String description;
    private int timeService;
    private boolean isActive;
    private int categoryID;
    private String imgUrl;

    public ServiceDTO() {
    }

    public ServiceDTO(int serviceID, String nameService, double price,
                      String description, int timeService,
                      boolean isActive, int categoryID, String imgUrl) {
        this.serviceID = serviceID;
        this.nameService = nameService;
        this.price = price;
        this.description = description;
        this.timeService = timeService;
        this.isActive = isActive;
        this.categoryID = categoryID;
        this.imgUrl= imgUrl;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
