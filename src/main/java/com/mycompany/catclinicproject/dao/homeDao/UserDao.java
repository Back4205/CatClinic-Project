/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.dao.homeDao;

import com.mycompany.catclinicproject.dao.DBContext;
import com.mycompany.catclinicproject.model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Son
 */
public class UserDao extends DBContext{
      public List<User> getAllUser() {
        List<User> list = new ArrayList<>();

        String sql = " SELECT u.UserID, u.UserName, u.FullName,r.RoleName, u.Email, u.Phone "
                + "FROM Users u JOIN Roles r ON u.RoleID = r.RoleID WHERE Active = 1 ";

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
    String sql = "UPDATE Users SET Active = 0 WHERE UserID = ? ";

    try {
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, userID);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}



    
}
