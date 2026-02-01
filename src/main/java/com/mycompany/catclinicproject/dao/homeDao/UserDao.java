/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.dao.homeDao;

import com.mycompany.catclinicproject.dao.DBContext;
import com.mycompany.catclinicproject.model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Son
 */
public class UserDao extends DBContext {

    public List<User> getAllUser() {
        List<User> list = new ArrayList<>();

        String sql = " SELECT u.UserID, u.UserName, u.FullName,r.RoleName, u.Email, u.Phone "
                + "FROM Users u JOIN Roles r ON u.RoleID = r.RoleID WHERE IsActive = 1";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User u = new User();
                u.setUserID(rs.getInt("UserID"));
                u.setUserName(rs.getString("UserName"));
                u.setFullName(rs.getString("FullName"));
                u.setRoleName(rs.getString("RoleName"));
                u.setEmail(rs.getString("Email"));
                u.setPhone(rs.getString("Phone"));

                list.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void DeleteUserById(String userID) {
        String sql = "UPDATE Users SET IsActive = 0 WHERE UserID = ? ";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, userID);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   public int getRoleID(String role) {
    switch (role) {
        case "Admin":
            return 1;
        case "Veterinarian":
            return 2;
        case "Receptionist":
            return 3;
        case "Staff":
            return 4;
        case "Customer":
            return 5;
        default:
            return -1;
    }
}

    public boolean isUsernameExist(String username) {
        String sql = "SELECT UserID FROM Users WHERE UserName = ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addUser(String username, String password, String fullName,
            boolean male, String email, int roleID,
            String phone, String googleID) {

        String sql = " INSERT INTO Users (UserName, PassWord, FullName, Male, Email, RoleID, Phone, IsActive, GoogleID) VALUES (?, ?, ?, ?, ?, ?, ?, 1, ?)";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, fullName);
            ps.setBoolean(4, male);
            ps.setString(5, email);
            ps.setInt(6, roleID);
            ps.setString(7, phone);
            ps.setString(8, googleID); // null OK

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

     public boolean updateUserRole(int userID, int roleID) {
    String sql = "UPDATE Users SET RoleID = ? WHERE UserID = ?";
    try (PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setInt(1, roleID);
        ps.setInt(2, userID);

        int rows = ps.executeUpdate();
        return rows > 0; // true nếu update thành công
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}



    public int getRoleIdByUserId(int userId) {
        String sql = "SELECT RoleID FROM Users WHERE UserID = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("RoleID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

}
