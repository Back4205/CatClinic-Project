package com.mycompany.catclinicproject.model;

public class Owner {
    private int userID ;
    private int ownerID ;
    private String address;

    public Owner() {
    }

    public Owner(int userID, int ownerID, String address) {
        this.userID = userID;
        this.ownerID = ownerID;
        this.address = address;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    

    
}
