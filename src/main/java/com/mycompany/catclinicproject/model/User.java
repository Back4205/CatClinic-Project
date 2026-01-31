package com.mycompany.catclinicproject.model;

public class User {

    private int userID;
    private String userName;
    private String fullName;
    private String roleName;
    private String email;
    private String phone;

    public User() {
    }

    public User(int userID, String userName, String fullName,
                String roleName, String email, String phone) {
        this.userID = userID;
        this.userName = userName;
        this.fullName = fullName;
        this.roleName = roleName;
        this.email = email;
        this.phone = phone;
    }

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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
