package com.mycompany.catclinicproject.model;

public class Cat {
    private int catID;
    private int ownerID;
    private String name;
    private int gender;
    private String breed;
    private int age;
    private boolean hasBooking;
    public Cat() {

    }
    public Cat(int catID, int ownerID, String name, int gender, String breed, int age) {
        this.catID = catID;
        this.ownerID = ownerID;
        this.name = name;
        this.gender = gender;
        this.breed = breed;
        this.age = age;
    }
    public int getCatID() {
        return catID;
    }
    public void setCatID(int catID) {
        this.catID = catID;
    }
    public int getOwnerID() {
        return ownerID;
    }
    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;

    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getGender() {
        return gender;
    }
    public void setGender(int gender) {
        this.gender = gender;
    }
    public String getBreed() {
        return breed;
    }
    public void setBreed(String breed) {
        this.breed = breed;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public boolean isHasBooking() {
        return hasBooking;
    }
    public void setHasBooking(boolean hasBooking) {
        this.hasBooking = hasBooking;
    }


}
