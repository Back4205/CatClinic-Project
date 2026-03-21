/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.model;

/**
 *
 * @author Son
 */
public class NotificationDTO {
    private int notificationID;
    private int vetID;
    private String message;
    private int refID;
    private String type;
    private boolean isRead;
    private String createdAt;

    public NotificationDTO() {
    }

    public NotificationDTO(int notificationID, int vetID, String message, int refID, String type, boolean isRead, String createdAt) {
        this.notificationID = notificationID;
        this.vetID = vetID;
        this.message = message;
        this.refID = refID;
        this.type = type;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }
    public int getNotificationID() {
        return notificationID;
    }
    public int getVetID() {
        return vetID;
    }
    public String getMessage() {
        return message;
    }
    public int getRefID() {
        return refID;
    }
    public String getType() {
        return type;
    }
    public boolean isIsRead() {
        return isRead;
    }
    public String getCreatedAt() {
        return createdAt;
    }
    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }
    public void setVetID(int vetID) {
        this.vetID = vetID;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setRefID(int refID) {
        this.refID = refID;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
