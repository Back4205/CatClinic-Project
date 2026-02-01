package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProfileDAO extends DBContext {

    // 1. LẤY THÔNG TIN
    public User getUserProfile(int id) {
        String sql = "SELECT u.*, o.Address " +
                     "FROM Users u " +
                     "LEFT JOIN Owners o ON u.UserID = o.UserID " +
                     "WHERE u.UserID = ?";
        try {
            // SỬA Ở ĐÂY: Dùng 'c' thay vì 'connection'
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setUserID(rs.getInt("UserID"));
                u.setUserName(rs.getString("FullName"));
                u.setEmail(rs.getString("Email"));
                u.setPhone(rs.getString("Phone"));
                
                String address = rs.getString("Address");
                u.setAddress(address != null ? address : ""); 
                
                return u;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 2. CẬP NHẬT
    public boolean updateProfile(User u) {
        try {
            // Bước 1: Update bảng Users
            String sqlUser = "UPDATE Users SET FullName = ?, Phone = ?, Email = ? WHERE UserID = ?";
            // SỬA Ở ĐÂY: Dùng 'c'
            PreparedStatement psUser = c.prepareStatement(sqlUser);
            psUser.setString(1, u.getUserName());
            psUser.setString(2, u.getPhone());
            psUser.setString(3, u.getEmail());
            psUser.setInt(4, u.getUserID());
            
            int r1 = psUser.executeUpdate();

            // Bước 2: Update bảng Owners
            String sqlOwnerUpdate = "UPDATE Owners SET Address = ? WHERE UserID = ?";
            // SỬA Ở ĐÂY: Dùng 'c'
            PreparedStatement psOwner = c.prepareStatement(sqlOwnerUpdate);
            psOwner.setString(1, u.getAddress());
            psOwner.setInt(2, u.getUserID());
            
            int r2 = psOwner.executeUpdate();
            
            // Nếu chưa có trong Owners -> Insert mới
            if (r2 == 0) {
                String sqlOwnerInsert = "INSERT INTO Owners (UserID, Address) VALUES (?, ?)";
                // SỬA Ở ĐÂY: Dùng 'c'
                PreparedStatement psInsert = c.prepareStatement(sqlOwnerInsert);
                psInsert.setInt(1, u.getUserID());
                psInsert.setString(2, u.getAddress());
                psInsert.executeUpdate();
            }

            return r1 > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // 3. CHECK PASS
    public boolean checkPassword(int userID, String oldPass) {
        String sql = "SELECT * FROM Users WHERE UserID = ? AND Password = ?";
        try {
            PreparedStatement ps = c.prepareStatement(sql); // SỬA: 'c'
            ps.setInt(1, userID);
            ps.setString(2, oldPass);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4. CHANGE PASS
    public void changePassword(int userID, String newPass) {
        String sql = "UPDATE Users SET Password = ? WHERE UserID = ?";
        try {
            PreparedStatement ps = c.prepareStatement(sql); // SỬA: 'c'
            ps.setString(1, newPass);
            ps.setInt(2, userID);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}