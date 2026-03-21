package com.mycompany.catclinicproject.model;

public class Owner {
    private int userID ;
    private int ownerID ;
    private String fullName ;
    private String address;
    private String email;
    private String phone;


    public Owner() {
    }

    public Owner(int userID, int ownerID, String address, String fullName, String email, String phone) {
        this.userID = userID;
        this.ownerID = ownerID;
        this.address = address;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
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
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }


    

    
}
