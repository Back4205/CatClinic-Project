package com.mycompany.catclinicproject.model;


public class Service {

    private int serviceID;
    private String nameService;
    private double price;
    private String description;
    private int timeService;
    private boolean isActive;

    public Service() {
    }

    public Service(int serviceID, String nameService, double price,
                   String description, int timeService, boolean isActive) {
        this.serviceID = serviceID;
        this.nameService = nameService;
        this.price = price;
        this.description = description;
        this.timeService = timeService;
        this.isActive = isActive;
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

    // BOOLEAN GETTER CHUẨN JAVA BEAN
    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
