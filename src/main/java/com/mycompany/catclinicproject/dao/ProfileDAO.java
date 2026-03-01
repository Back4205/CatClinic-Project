package com.mycompany.catclinicproject.dao;


import com.mycompany.catclinicproject.model.UserDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProfileDAO extends DBContext {

    public UserDTO getUserProfile(int id) {
        String sql = "SELECT u.UserID, u.UserName, u.FullName, u.Email, u.Phone, o.Address \n" +
                "FROM Users u \n" +
                "LEFT JOIN Owners o ON u.UserID = o.UserID \n" +
                "WHERE u.UserID = ?";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                UserDTO u = new UserDTO();
                u.setFullName(rs.getString("FullName"));
                u.setUserID(rs.getInt("UserID"));
                u.setUserName(rs.getString("UserName"));
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
    public String checkDuplicate(String email, String phone, int userID) {
        // SQL: Tìm xem có AI KHÁC (UserID != ?) đang dùng Email hoặc Phone này không
        String sql = "SELECT Email, Phone FROM Users WHERE (Email = ? OR Phone = ?) AND UserID != ?";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, phone);
            ps.setInt(3, userID); // Đây là ID của chính cậu, để loại trừ mình ra

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Kiểm tra chính xác trùng cái nào để báo lỗi cho đúng
                if (email.equalsIgnoreCase(rs.getString("Email"))) {
                    return "Email is already in use by another account!";
                }
                if (phone.equals(rs.getString("Phone"))) {
                    return "Phone number is already in use by another account!";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Không trùng với ai cả -> Hợp lệ
    }
    public boolean updateProfile(UserDTO u) {
        try {
            String sqlUser = "UPDATE Users SET FullName = ?, Phone = ?, Email = ? WHERE UserID = ?";
            PreparedStatement psUser = c.prepareStatement(sqlUser);
            psUser.setString(1, u.getFullName());
            psUser.setString(2, u.getPhone());
            psUser.setString(3, u.getEmail());
            psUser.setInt(4, u.getUserID());

            int r1 = psUser.executeUpdate();
            String sqlOwnerUpdate = "UPDATE Owners SET Address = ? WHERE UserID = ?";
            PreparedStatement psOwner = c.prepareStatement(sqlOwnerUpdate);
            psOwner.setString(1, u.getAddress());
            psOwner.setInt(2, u.getUserID());

            int r2 = psOwner.executeUpdate();

            if (r2 == 0) {
                String sqlOwnerInsert = "INSERT INTO Owners (UserID, Address) VALUES (?, ?)";
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
