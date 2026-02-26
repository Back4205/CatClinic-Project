package com.mycompany.catclinicproject.model;

public class NewsDTO {
    private int newID;
    private String img;
    private String title;
    private String description;
    private boolean isActive;

    public NewsDTO() {
    }

    public NewsDTO(int newID, String img, String title, String description, boolean isActive) {
        this.newID = newID;
        this.img = img;
        this.title = title;
        this.description = description;
        this.isActive = isActive;
    }

    public int getNewID() {
        return newID;
    }

    public void setNewID(int newID) {
        this.newID = newID;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}