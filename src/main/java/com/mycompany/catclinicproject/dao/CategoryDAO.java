/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.Category;
import com.mycompany.catclinicproject.model.Service;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class CategoryDAO extends DBContext{
    //Category
    public List<Category> getAllCategory() {
        List<Category> list = new ArrayList<>();

        String sql = "select * from Categorys";

        try (PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Category s = new Category();
                s.setCategoryID(rs.getInt("CategoryID"));
                s.setCategoryName(rs.getString("CategoryName"));
                s.setBanner(rs.getString("Banner"));
                s.setDescription(rs.getString("Description"));
                s.setActive(rs.getBoolean("IsActive"));
                list.add(s);
                
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
     public void updateCategoryStatus(int serviceID, boolean isActive) {
        String sql = "UPDATE Categorys SET IsActive = ? WHERE CategoryID = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setBoolean(1, isActive);
            ps.setInt(2, serviceID);

            ps.executeUpdate();
            System.out.println("UPDATE SERVICE STATUS SUCCESS");

        } catch (Exception e) {
            System.out.println("UPDATE SERVICE STATUS FAILED");
            e.printStackTrace();
        }
    }
}
