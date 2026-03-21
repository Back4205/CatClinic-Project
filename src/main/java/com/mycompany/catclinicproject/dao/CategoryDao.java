/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.Category;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        public Category getCategoryIsActive(int id) {

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
    public List<Category> getCategory() {
        List<Category> list = new ArrayList<>();
        String sql = "select * from Categorys";
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
   public void insertCategory(Category s) {
        String sql = "INSERT INTO Categorys\n" +
"            (CategoryName, Banner, Description,IsActive)\n" +
"            VALUES (?, ?, ?, ?)"
            
        ;

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, s.getCategoryName());
            ps.setString(2, s.getBanner());
            ps.setString(3, s.getDescription());
            ps.setBoolean(4, s.isActive());

            ps.executeUpdate();
            System.out.println("INSERT SERVICE SUCCESS");

        } catch (Exception e) {
            System.out.println("INSERT SERVICE FAILED");
            e.printStackTrace();
        }
    }
    public Category getCategoryById(int id) {
        String sql = "SELECT * FROM Categorys WHERE CategoryID = ?";
        Category s = null;

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
             if (rs.next()) {
                s = new Category(
                        rs.getInt("CategoryID"),
                        rs.getString("CategoryName"),
                        rs.getString("Banner"),
                        rs.getString("Description"),
                        rs.getBoolean("IsActive")
                );
               
             }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }

        return s;
    }
     public void updateCategory(Category s) {
        String sql = "UPDATE Categorys\n" +
"            SET CategoryName = ?,\n" +
"                Banner = ?,\n" +
"                Description = ?\n" +
"            WHERE CategoryID = ?"          
        ;

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, s.getCategoryName());
            ps.setString(2, s.getBanner());
            ps.setString(3, s.getDescription());
            ps.setInt(4, s.getCategoryID());

            ps.executeUpdate();
            System.out.println("UPDATE SUCCESS");

        } catch (Exception e) {
            System.out.println("UPDATE FAILED");
            e.printStackTrace();
        }
    }
     public void updateCategoryStatus(int categoryID, boolean isActive) {
         String sql = "-- Update category\n"
                 + "UPDATE Categorys\n"
                 + "SET IsActive = ?\n"
                 + "WHERE CategoryID = ?;\n"
                 + "\n"
                 + "-- Update services thuộc category đó\n"
                 + "UPDATE Services\n"
                 + "SET IsActive = ?\n"
                 + "WHERE CategoryID = ?;";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setBoolean(1, isActive);
            ps.setInt(2, categoryID);
            ps.setBoolean(3, isActive);
            ps.setInt(4, categoryID);

            ps.executeUpdate();
            System.out.println("UPDATE SERVICE STATUS SUCCESS");

        } catch (Exception e) {
            System.out.println("UPDATE SERVICE STATUS FAILED");
            e.printStackTrace();
        }
    }

    public boolean isCategoryNameExists(String name) {
        String sql = "SELECT 1 FROM Categorys WHERE CategoryName = ?";
        try {
            PreparedStatement ps = this.c.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
                cat.setCategoryID(rs.getInt("CategoryID"));
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
