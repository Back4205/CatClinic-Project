package com.mycompany.catclinicproject.model;

public class User {

    private int userID;
    private String userName;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private boolean male;
    private int roleID;
    private boolean isActive;
    private String googleID;

    public User() {
    }

    public User(int userID, String userName, String password, String fullName, String email, String phone, boolean male, int roleID, boolean isActive, String googleID) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.male = male;
        this.roleID = roleID;
        this.isActive = isActive;
        this.googleID = googleID;

    }



    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isMale() {
        return male;
    }

    public int getRoleID() {
        return roleID;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getGoogleID() {
        return googleID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setGoogleID(String googleID) {
        this.googleID = googleID;
    }

}
