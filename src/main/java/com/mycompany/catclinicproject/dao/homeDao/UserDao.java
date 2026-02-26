/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.dao.homeDao;

import com.mycompany.catclinicproject.dao.DBContext;
import com.mycompany.catclinicproject.model.User;
import com.mycompany.catclinicproject.model.UserDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Son
 */
public class UserDao extends DBContext {

    public List<UserDTO> getAllUser() {
        List<UserDTO> list = new ArrayList<>();

        String sql = " SELECT u.UserID, u.UserName, u.FullName,r.RoleName, u.Email, u.Phone "
                + "FROM Users u JOIN Roles r ON u.RoleID = r.RoleID WHERE IsActive = 1";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UserDTO u = new UserDTO();
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

    public void DeleteUserById(int userID) {
        String sql = "UPDATE Users SET IsActive = 0 WHERE UserID = ? ";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, userID);
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

    public User getUserById(int userId) {
        String sql = "SELECT * FROM Users WHERE UserID = ?";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User u = new User();
                u.setUserID(rs.getInt("UserID"));
                u.setUserName(rs.getString("UserName"));
                u.setPassword(rs.getString("PassWord"));
                u.setFullName(rs.getString("FullName"));
                u.setMale(rs.getBoolean("Male"));
                u.setEmail(rs.getString("Email"));
                u.setPhone(rs.getString("Phone"));
                u.setGoogleID(rs.getString("GoogleID"));
                u.setActive(rs.getBoolean("Active"));
                u.setRoleID(rs.getInt("RoleID"));
                return u;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isEmailExist(String email) {
        String sql = "SELECT 1 FROM Users WHERE Email = ?";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isPhoneExist(String phone) {
        String sql = "SELECT 1 FROM Users WHERE Phone = ?";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<UserDTO> getUserByRole(int roleID) {
        List<UserDTO> list = new ArrayList<>();

        String sql = "SELECT u.UserID, u.UserName, u.FullName, r.RoleName, u.Email, u.Phone "
                + "FROM Users u JOIN Roles r ON u.RoleID = r.RoleID "
                + "WHERE u.IsActive = 1 AND u.RoleID = ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, roleID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UserDTO u = new UserDTO();
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

    public List<UserDTO> getUserByString(String search) {
        List<UserDTO> list = new ArrayList<>();

        String sql = "SELECT u.UserID, u.UserName, u.FullName, r.RoleName, u.Email, u.Phone "
                + "FROM Users u JOIN Roles r ON u.RoleID = r.RoleID "
                + "WHERE u.IsActive = 1 "
                + "AND (u.UserName LIKE ? OR u.Email LIKE ? OR u.Phone LIKE ?)";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            String key = "%" + search + "%";
            ps.setString(1, key);
            ps.setString(2, key);
            ps.setString(3, key);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UserDTO u = new UserDTO();
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

    public int countAllUser() {
        String sql = "SELECT COUNT(*) FROM Users WHERE IsActive = 1";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
public List<UserDTO> getAllUserPaging(int page, int pageSize) {
    List<UserDTO> list = new ArrayList<>();

    String sql = "SELECT u.UserID, u.UserName, u.FullName, r.RoleName, u.Email, u.Phone "
               + "FROM Users u JOIN Roles r ON u.RoleID = r.RoleID "
               + "WHERE u.IsActive = 1 "
               + "ORDER BY u.UserID DESC "
               + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    try {
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, (page - 1) * pageSize);
        ps.setInt(2, pageSize);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            UserDTO u = new UserDTO();
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

public int countAll() {
    String sql = "SELECT COUNT(*) FROM Users WHERE IsActive = 1";
    try {
        PreparedStatement ps = c.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt(1);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return 0;
}
public List<UserDTO> getPaging(int page, int pageSize) {
    List<UserDTO> list = new ArrayList<>();

    String sql =
        "SELECT u.UserID, u.UserName, u.FullName, r.RoleName, u.Email, u.Phone "
      + "FROM Users u "
      + "JOIN Roles r ON u.RoleID = r.RoleID "
      + "WHERE u.IsActive = 1 "
      + "ORDER BY u.UserID DESC "
      + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";


    try {
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, (page - 1) * pageSize);
        ps.setInt(2, pageSize);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            UserDTO u = new UserDTO();
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

public List<User> searchUser(String keyword) {
    List<User> list = new ArrayList<>();

    String sql = "SELECT * FROM Users " +
                 "WHERE UserName LIKE ? " +
                 "OR FullName LIKE ? " +
                 "OR Email LIKE ? " +
                 "OR Phone LIKE ? " +
                 "ORDER BY UserID DESC ";

    try {
        PreparedStatement ps = c.prepareStatement(sql);

        String search = "%" + keyword + "%";

        ps.setString(1, search);
        ps.setString(2, search);
        ps.setString(3, search);
        ps.setString(4, search);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            User u = new User();
            u.setUserID(rs.getInt("UserID"));
            u.setUserName(rs.getString("UserName"));
            u.setFullName(rs.getString("FullName"));
            u.setEmail(rs.getString("Email"));
            u.setPhone(rs.getString("Phone"));
            u.setRoleID(rs.getInt("RoleID"));
            list.add(u);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}

public List<UserDTO> searchFilterPaging(String keyword, String roleName, int page, int pageSize) {

    List<UserDTO> list = new ArrayList<>();

    String sql = "SELECT u.UserID, u.UserName, u.FullName, r.RoleName, u.Email, u.Phone "
            + "FROM Users u JOIN Roles r ON u.RoleID = r.RoleID "
            + "WHERE u.IsActive = 1 ";

    if (keyword != null && !keyword.trim().isEmpty()) {
        sql += "AND (u.UserName LIKE ? "
             + "OR u.FullName LIKE ? "
             + "OR u.Email LIKE ? "
             + "OR u.Phone LIKE ?) ";
    }

    if (roleName != null && !roleName.trim().isEmpty()) {
        sql += "AND r.RoleName = ? ";
    }

    sql += "ORDER BY u.UserID DESC "
         + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    try {
        PreparedStatement ps = c.prepareStatement(sql);

        int index = 1;

        if (keyword != null && !keyword.trim().isEmpty()) {
            String key = "%" + keyword + "%";
            ps.setString(index++, key);
            ps.setString(index++, key);
            ps.setString(index++, key);
            ps.setString(index++, key);
        }

        if (roleName != null && !roleName.trim().isEmpty()) {
            ps.setString(index++, roleName);
        }

        ps.setInt(index++, (page - 1) * pageSize);
        ps.setInt(index++, pageSize);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            UserDTO u = new UserDTO();
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
public int countSearchFilter(String keyword, String roleName) {

    String sql = "SELECT COUNT(*) "
            + "FROM Users u JOIN Roles r ON u.RoleID = r.RoleID "
            + "WHERE u.IsActive = 1 ";

    if (keyword != null && !keyword.trim().isEmpty()) {
        sql += "AND (u.UserName LIKE ? "
             + "OR u.FullName LIKE ? "
             + "OR u.Email LIKE ? "
             + "OR u.Phone LIKE ?) ";
    }

    if (roleName != null && !roleName.trim().isEmpty()) {
        sql += "AND r.RoleName = ? ";
    }

    try {
        PreparedStatement ps = c.prepareStatement(sql);

        int index = 1;

        if (keyword != null && !keyword.trim().isEmpty()) {
            String key = "%" + keyword + "%";
            ps.setString(index++, key);
            ps.setString(index++, key);
            ps.setString(index++, key);
            ps.setString(index++, key);
        }

        if (roleName != null && !roleName.trim().isEmpty()) {
            ps.setString(index++, roleName);
        }

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}

}
