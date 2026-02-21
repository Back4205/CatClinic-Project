    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
     */
    package com.mycompany.catclinicproject.model;

    /**
     *
     * @author Son
     */
    public class UserDTO {
        private int userID;
        private String userName;
        private String fullName;
        private String roleName;
        private String email;
        private String phone;
        private String address;
        private String password;
        private String phoneNumber;
        private int roleID;
        private String type ;
        private Integer experienceYear;

        public UserDTO() {
        }

        public UserDTO(int userID, String userName, String fullName,
                       String roleName, String email, String phone) {
            this.userID = userID;
            this.userName = userName;
            this.fullName = fullName;
            this.roleName = roleName;
            this.email = email;
            this.phone = phone;
        }
        public UserDTO(int userID, String fullName, String email, String password, String phoneNumber, String address, int roleID) {
            this.userID = userID;
            this.fullName = fullName;
            this.email = email;
            this.password = password;
            this.phoneNumber = phoneNumber;
            this.address = address;
            this.roleID = roleID;
        }

    public UserDTO(int userID, String userName, String fullName, String roleName, String email, String phone, String address, String password, String phoneNumber, int roleID, String type, Integer experienceYear) {
        this.userID = userID;
        this.userName = userName;
        this.fullName = fullName;
        this.roleName = roleName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.roleID = roleID;
        this.type = type;
        this.experienceYear = experienceYear;
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public int getRoleID() {
            return roleID;
        }

        public void setRoleID(int roleID) {
            this.roleID = roleID;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

    public Integer getExperienceYear() {
        return experienceYear;
    }

    public void setExperienceYear(Integer experienceYear) {
        this.experienceYear = experienceYear;
    }

       


    }