package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.RegisterDTO;
import com.mycompany.catclinicproject.model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO extends DBContext {

    public User checkLogin(String userOrEmail, String password) {
        String sql = "SELECT * FROM Users WHERE (UserName = ? OR Email = ?) AND PassWord = ?";
        try {
            if (c == null || c.isClosed()) {

                new DBContext();
            }
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, userOrEmail);
            ps.setString(2, userOrEmail);
            ps.setString(3, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("UserID"),
                        rs.getString("UserName"),
                        rs.getString("PassWord"),
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        rs.getString("Phone"),
                        rs.getBoolean("Male"),
                        rs.getInt("RoleID"),
                        rs.getBoolean("IsActive"),
                        rs.getString("GoogleID")
                );
            }
        } catch (SQLException e) {
            System.out.println("Login Error: " + e.getMessage());
        }
        return null;
    }

    public boolean checkUserExists(String username, String email) {
        String sql = "SELECT UserID FROM Users WHERE UserName = ? OR Email = ?";
        try {
            if (c == null || c.isClosed()) {
                new DBContext();
            }
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean registerCustomer(RegisterDTO dto) {
        String sqlUser = "INSERT INTO Users (UserName, PassWord, FullName, Email, Phone, RoleID, IsActive, Male) VALUES (?, ?, ?, ?, ?, ?, 1, 1)";
        String sqlOwner = "INSERT INTO Owners (UserID, Address) VALUES (?, ?)";

        try {
            if (c == null || c.isClosed()) {
                new DBContext();
            }

            c.setAutoCommit(false);

            PreparedStatement psUser = c.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS);
            psUser.setString(1, dto.getUsername());
            psUser.setString(2, dto.getPassword());
            psUser.setString(3, dto.getFullName());
            psUser.setString(4, dto.getEmail());
            psUser.setString(5, dto.getPhone());
            psUser.setInt(6, 5);

            int affectedRows = psUser.executeUpdate();
            if (affectedRows == 0) {
                c.rollback();
                return false;
            }

            int newUserID = 0;
            try (ResultSet generatedKeys = psUser.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newUserID = generatedKeys.getInt(1);
                } else {
                    c.rollback();
                    return false;
                }
            }

            PreparedStatement psOwner = c.prepareStatement(sqlOwner);
            psOwner.setInt(1, newUserID);
            psOwner.setString(2, dto.getAddress());

            psOwner.executeUpdate();

            c.commit();

            c.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            System.out.println("Register Error: " + e.getMessage());
            try {
                if (c != null) {
                    c.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }

    public User checkLoginGoogle(String email, String googleID) {
        String sql = "SELECT * FROM Users WHERE Email = ? OR GoogleID = ?";
        try {
            if (c == null || c.isClosed()) {
                new DBContext();
            }
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, googleID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {

                if (rs.getString("GoogleID") == null) {
                    String update = "UPDATE Users SET GoogleID = ? WHERE UserID = ?";
                    PreparedStatement psUp = c.prepareStatement(update);
                    psUp.setString(1, googleID);
                    psUp.setInt(2, rs.getInt("UserID"));
                    psUp.executeUpdate();
                }
                return new User(rs.getInt("UserID"), rs.getString("UserName"), rs.getString("PassWord"),
                        rs.getString("FullName"), rs.getString("Email"), rs.getString("Phone"),
                        rs.getBoolean("Male"), rs.getInt("RoleID"), rs.getBoolean("IsActive"), googleID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createGoogleUser(User u) {

        String sqlUser = "INSERT INTO Users (UserName, FullName, Email, RoleID, IsActive, GoogleID, PassWord) VALUES (?, ?, ?, 5, 1, ?, 'GoogleLogin')";

        String sqlOwner = "INSERT INTO Owners (UserID, Address) VALUES (?, ?)";

        try {
            if (c == null || c.isClosed()) {
                new DBContext();
            }
            c.setAutoCommit(false);

            PreparedStatement ps = c.prepareStatement(sqlUser, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, u.getEmail());
            ps.setString(2, u.getFullName());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getGoogleID());
            ps.executeUpdate();

            int newUserID = 0;
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                newUserID = rs.getInt(1);
            }

            PreparedStatement psOwner = c.prepareStatement(sqlOwner);
            psOwner.setInt(1, newUserID);
            psOwner.setNull(2, java.sql.Types.NVARCHAR);
            psOwner.executeUpdate();

            c.commit();
            c.setAutoCommit(true);
        } catch (Exception e) {
            try {
                c.rollback();
            } catch (Exception ex) {
            }
            e.printStackTrace();
        }
    }
    // Hàm kiểm tra xem Email có tồn tại trong hệ thống không
    public boolean checkEmailExists(String email) {
        String sql = "SELECT UserID FROM Users WHERE Email = ?";
        try {
            if (c == null || c.isClosed()) {
                new DBContext();
            }
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Hàm cập nhật mật khẩu mới theo Email
    public boolean updatePasswordByEmail(String email, String newPassword) {
        String sql = "UPDATE Users SET PassWord = ? WHERE Email = ?";
        try {
            if (c == null || c.isClosed()) {
                new DBContext();
            }
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, newPassword);
            ps.setString(2, email);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Hàm kích hoạt tài khoản (Update IsActive = 1)
    public boolean activateUser(String email) {
        String sql = "UPDATE Users SET IsActive = 1 WHERE Email = ?";
        try {
            if (c == null || c.isClosed()) {
                new DBContext();
            }
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, email);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    // Hàm lấy chức vụ (Position) của Staff dựa vào UserID
    public String getStaffPosition(int userId) {
        String sql = "SELECT Position FROM Staffs WHERE UserID = ?";
        try {
            if (c == null || c.isClosed()) {
                new DBContext();
            }
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("Position");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public User getUserByPhone(String phone) {
        String sql = "SELECT * FROM Users WHERE Phone = ? AND RoleID = 5 AND IsActive = 1";
        try {
            if (c == null || c.isClosed()) new DBContext();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("UserID"), rs.getString("UserName"), rs.getString("PassWord"),
                        rs.getString("FullName"), rs.getString("Email"), rs.getString("Phone"),
                        rs.getBoolean("Male"), rs.getInt("RoleID"), rs.getBoolean("IsActive"), rs.getString("GoogleID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Hàm lấy VetID từ UserID cho Bác sĩ
    public Integer getVetIDByUserId(int userId) {
        String sql = "SELECT VetID FROM Veterinarians WHERE UserID = ?";
        try {
            if (c == null || c.isClosed()) {
                new DBContext();
            }
            java.sql.PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, userId);
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("VetID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    // 1. Hàm lấy danh sách Nhân viên theo Chức vụ (Care / Technician)
    public java.util.List<java.util.Map<String, Object>> getAllStaffByPosition(String position) {
        java.util.List<java.util.Map<String, Object>> list = new java.util.ArrayList<>();
        String sql = "SELECT s.StaffID, u.FullName, s.Position FROM Staffs s " +
                "JOIN Users u ON s.UserID = u.UserID " +
                "WHERE s.Position = ? AND u.IsActive = 1";
        try {
            if (c == null || c.isClosed()) { new DBContext(); }
            java.sql.PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, position);
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                java.util.Map<String, Object> map = new java.util.HashMap<>();
                map.put("staffID", rs.getInt("StaffID"));
                map.put("fullName", rs.getString("FullName"));
                map.put("position", rs.getString("Position"));
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Hàm lấy danh sách tất cả Bác sĩ (Veterinarians)
    public java.util.List<java.util.Map<String, Object>> getAllVeterinarians() {
        java.util.List<java.util.Map<String, Object>> list = new java.util.ArrayList<>();
        String sql = "SELECT u.UserID, u.FullName, v.VetID FROM Veterinarians v " +
                "JOIN Users u ON v.UserID = u.UserID " +
                "WHERE u.IsActive = 1";
        try {
            if (c == null || c.isClosed()) { new DBContext(); }
            java.sql.PreparedStatement ps = c.prepareStatement(sql);
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                java.util.Map<String, Object> map = new java.util.HashMap<>();
                map.put("userID", rs.getInt("UserID")); // JSP đang cần UserID để hiển thị
                map.put("vetID", rs.getInt("VetID"));
                map.put("fullName", rs.getString("FullName"));
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
