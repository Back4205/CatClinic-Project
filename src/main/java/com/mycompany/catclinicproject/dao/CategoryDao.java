/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.Category;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Son
 */
public class CategoryDao extends DBContext{
    
        public List<Category> getAllCategory() {
        List<Category> list = new ArrayList<>();
        String sql = "select * from Categorys where IsActive = 1 ";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               Category cg = new Category();
               cg.setCategoryID(rs.getInt("CategoryID"));
               cg.setCategoryName(rs.getString("CategoryName"));
               cg.setBanner(rs.getString("Banner"));
               cg.setDescription(rs.getString("Description"));
               cg.setActive(rs.getBoolean("IsActive"));
               list.add(cg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
        public Category getCategoryById(int id) {

    String sql = "SELECT * FROM Categorys WHERE CategoryID = ? AND IsActive = 1";

    try (PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setInt(1, id);

        try (ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return new Category(
                        rs.getInt("CategoryID"),
                        rs.getString("CategoryName"),
                        rs.getString("Banner"),
                        rs.getString("Description"),
                        rs.getBoolean("IsActive")
                );
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}
        
    
}
