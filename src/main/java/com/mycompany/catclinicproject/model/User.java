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

    // --- PHẦN BẠN THÊM VÀO (Để phục vụ Profile) ---
    private String address; 

    public User() {
    }

    // Constructor CŨ của bạn mình (GIỮ NGUYÊN ĐỂ KHÔNG LỖI LOGIN)
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

    // --- GETTER & SETTER CŨ (GIỮ NGUYÊN) ---
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getGoogleID() {
        return googleID;
    }

    public void setGoogleID(String googleID) {
        this.googleID = googleID;
    }

    // --- GETTER & SETTER MỚI CHO ADDRESS (THÊM VÀO CUỐI) ---
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}