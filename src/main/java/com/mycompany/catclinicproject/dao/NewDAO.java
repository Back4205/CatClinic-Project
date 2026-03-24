/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.News;
import com.mycompany.catclinicproject.model.NewsImages;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class NewDAO extends DBContext {

    public List<News> getAllNew() {
        List<News> list = new ArrayList<>();
        String sql = "select * from News ORDER BY NewID DESC;";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                News cg = new News();
                cg.setNewsId(rs.getInt("NewID"));
                cg.setBanner(rs.getString("Banner"));
                cg.setTitle(rs.getString("Title"));
                cg.setDescription(rs.getString("Description"));
                cg.setCreatedDate(rs.getDate("CreatedDate"));
                cg.setIsActive(rs.getBoolean("IsActive"));
                list.add(cg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public List<News> getActiveNew() {
        List<News> list = new ArrayList<>();
        String sql = "SELECT  *\n"
                + "FROM News\n"
                + "WHERE IsActive = 1\n"
                + "ORDER BY NewID DESC;";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                News cg = new News();
                cg.setNewsId(rs.getInt("NewID"));
                cg.setBanner(rs.getString("Banner"));
                cg.setTitle(rs.getString("Title"));
                cg.setDescription(rs.getString("Description"));
                cg.setCreatedDate(rs.getDate("CreatedDate"));
                cg.setIsActive(rs.getBoolean("IsActive"));
                list.add(cg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public News getNewsById(int id) {
        String sql = "SELECT * FROM News WHERE NewID=?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                News n = new News();
                n.setNewsId(rs.getInt("NewID"));
                n.setBanner(rs.getString("Banner"));
                n.setTitle(rs.getString("Title"));
                n.setDescription(rs.getString("Description"));
                n.setCreatedDate(rs.getDate("CreatedDate"));
                n.setIsActive(rs.getBoolean("IsActive"));
                return n;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<NewsImages> getNewsImagesByNewsId(int newsId) {

    List<NewsImages> list = new ArrayList<>();

    String sql = "SELECT * FROM NewsImages WHERE NewID = ?";

    try (PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setInt(1, newsId);

        try (ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                NewsImages img = new NewsImages(
                        rs.getInt("ImageID"),
                        rs.getInt("NewID"),
                        rs.getString("ImgUrl")
                );

                list.add(img);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
    public void createImage(NewsImages n ) {
        String sql = "INSERT INTO NewsImages\n" +
"            (NewID,ImgUrl)\n" +
"            VALUES (?, ?)"
            
        ;

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, n.getNewId());
            ps.setString(2, n.getImgUrl());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteImage(int imageId) {
        String sql = "DELETE FROM NewsImages WHERE ImageID = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {  
            ps.setInt(1, imageId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public int insertNews(News n) {
        String sql = "INSERT INTO News (Banner,Title, Description, IsActive) VALUES (?,?,?,?)";

        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, n.getBanner());
            ps.setString(2, n.getTitle());
            ps.setString(3, n.getDescription());
            ps.setBoolean(4, n.isIsActive());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void updateNews(News n,int newId) {
        String sql = "UPDATE News SET Banner=?, Title=?, Description=?, IsActive=? WHERE NewID=?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, n.getBanner());
            ps.setString(2, n.getTitle());
            ps.setString(3, n.getDescription());
            ps.setBoolean(4, n.isIsActive());
            ps.setInt(5,newId );
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteByNewsId(int newsId) {
        String sql = "DELETE FROM NewsImages WHERE NewID=?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, newsId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateStatus(int newId, boolean status) {
        String sql = "UPDATE News SET IsActive = ? WHERE NewID = ?";
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setBoolean(1, status);
            ps.setInt(2, newId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<News> getTop3LatestNews() {
    List<News> list = new ArrayList<>();
    String sql = "SELECT TOP 3 * FROM News WHERE IsActive = 1 ORDER BY NewID DESC";

    try {
        PreparedStatement ps = c.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            News n = new News();
            n.setNewsId(rs.getInt("NewID"));
            n.setBanner(rs.getString("Banner"));
            n.setTitle(rs.getString("Title"));
            n.setDescription(rs.getString("Description"));
            n.setCreatedDate(rs.getDate("CreatedDate"));
            n.setIsActive(rs.getBoolean("IsActive"));
            list.add(n);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
}
