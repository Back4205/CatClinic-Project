package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.Category;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO extends DBContext{

    public Category getCategoryById(int categoryId) {
        String sql = "SELECT CategoryID, CategoryName, Banner, Description, IsActive " +
                "FROM Categorys WHERE CategoryID = ?";

        try (
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Category cat = new Category();
                    cat.setCategoryId(rs.getInt("CategoryID"));
                    cat.setCategoryName(rs.getString("CategoryName"));
                    cat.setBanner(rs.getString("Banner"));
                    cat.setDescription(rs.getString("Description"));
                    cat.setActive(rs.getBoolean("IsActive"));
                    return cat;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Thay bằng logging thực tế
        }
        return null; // Không tìm thấy
    }

  
    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT CategoryID, CategoryName, Banner, Description, IsActive " +
                "FROM Categorys WHERE IsActive = 1 ORDER BY CategoryID";

        try (
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Category cat = new Category();
                cat.setCategoryId(rs.getInt("CategoryID"));
                cat.setCategoryName(rs.getString("CategoryName"));
                cat.setBanner(rs.getString("Banner"));
                cat.setDescription(rs.getString("Description"));
                cat.setActive(rs.getBoolean("IsActive"));
                list.add(cat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
