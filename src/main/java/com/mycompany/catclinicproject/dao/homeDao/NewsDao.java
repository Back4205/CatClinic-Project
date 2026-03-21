/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.dao.homeDao;

import com.mycompany.catclinicproject.dao.DBContext;
import com.mycompany.catclinicproject.model.NewsDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Son
 */
public class NewsDao extends DBContext {

    public List<NewsDTO> getNewsByPage(int page, int pageSize) {

        List<NewsDTO> list = new ArrayList<>();

        String sql
                = "SELECT * FROM News "
                + "WHERE IsActive = 1 "
                + "ORDER BY NewID DESC "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (
                PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, (page - 1) * pageSize);
            ps.setInt(2, pageSize);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                NewsDTO news = new NewsDTO(
                        rs.getInt("NewID"),
                        rs.getString("Img"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getBoolean("IsActive")
                );

                list.add(news);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public List<NewsDTO> getNews() {

        List<NewsDTO> list = new ArrayList<>();

        String sql
                = "SELECT * FROM News "
                + "WHERE IsActive = 1 "
                + "ORDER BY NewID DESC ";

        try (
                PreparedStatement ps = c.prepareStatement(sql)) {

            
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                NewsDTO news = new NewsDTO(
                        rs.getInt("NewID"),
                        rs.getString("Img"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getBoolean("IsActive")
                );

                list.add(news);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<NewsDTO> getLastNews() {
        List<NewsDTO> list = new ArrayList<>();

        String sql = "SELECT TOP 5 * FROM News WHERE IsActive = 1 ORDER BY NewID DESC";

        try (
                PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery();) {

            while (rs.next()) {
                NewsDTO news = new NewsDTO(
                        rs.getInt("NewID"),
                        rs.getString("Img"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getBoolean("IsActive")
                );
                list.add(news);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int getTotalNews() {
        String sql = "SELECT COUNT(*) FROM News WHERE IsActive = 1";

        try (
                PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery();) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    public List<NewsDTO> searchNews(String keyword, int page, int pageSize) {

    List<NewsDTO> list = new ArrayList<>();

    String sql = "SELECT * FROM News "
            + "WHERE IsActive = 1 "
            + "AND (Title LIKE ? OR Description LIKE ?) "
            + "ORDER BY NewID DESC "
            + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    try (PreparedStatement ps = c.prepareStatement(sql)) {

        if (keyword == null) {
            keyword = "";
        }

        ps.setString(1, "%" + keyword + "%");
        ps.setString(2, "%" + keyword + "%");
        ps.setInt(3, (page - 1) * pageSize);
        ps.setInt(4, pageSize);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            NewsDTO news = new NewsDTO(
                    rs.getInt("NewID"),
                    rs.getString("Img"),
                    rs.getString("Title"),
                    rs.getString("Description"),
                    rs.getBoolean("IsActive")
            );
            list.add(news);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
    public int countSearchNews(String keyword) {

    String sql = "SELECT COUNT(*) FROM News "
            + "WHERE IsActive = 1 "
            + "AND (Title LIKE ? OR Description LIKE ?)";

    try (PreparedStatement ps = c.prepareStatement(sql)) {

        if (keyword == null) {
            keyword = "";
        }

        ps.setString(1, "%" + keyword + "%");
        ps.setString(2, "%" + keyword + "%");

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}
    public NewsDTO getNewsByID(int id) {

    String sql = "SELECT * FROM News WHERE NewID = ? AND IsActive = 1";

    try (PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new NewsDTO(
                    rs.getInt("NewID"),
                    rs.getString("Img"),
                    rs.getString("Title"),
                    rs.getString("Description"),
                    rs.getBoolean("IsActive")
            );
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}
}
