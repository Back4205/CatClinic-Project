package com.mycompany.catclinicproject.model;

public class VeterinarianInfo {
    private int userID;
    private String fullName;
    private int vetID;

    public VeterinarianInfo() {
    }

    public VeterinarianInfo(int userID, String fullName, int vetID) {
        this.userID = userID;
        this.fullName = fullName;
        this.vetID = vetID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getVetID() {
        return vetID;
    }

    public void setVetID(int vetID) {
        this.vetID = vetID;
    }
}