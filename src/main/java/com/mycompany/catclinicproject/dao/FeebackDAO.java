/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.FeedbackDTO;
import com.mycompany.catclinicproject.model.ServiceFeedback;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Son
 */
public class FeebackDAO extends DBContext{
    
    public List<FeedbackDTO> getAllFeedback() {
    List<FeedbackDTO> list = new ArrayList<>();

    String sql = "SELECT f.BookingID, f.Rating, f.Comment, f.CreatedAt, u.FullName "
            + "FROM Feedbacks f "
            + "JOIN Bookings b ON f.BookingID = b.BookingID "
            + "JOIN Cats c ON b.CatID = c.CatID "
            + "JOIN Owners o ON c.OwnerID = o.OwnerID "
            + "JOIN Users u ON o.UserID = u.UserID";

    try {
        PreparedStatement ps = c.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            FeedbackDTO f = new FeedbackDTO(
                    rs.getInt("BookingID"),
                    rs.getInt("Rating"),
                    rs.getString("Comment"),
                    rs.getString("CreatedAt"),
                    rs.getString("FullName")
            );
            list.add(f);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
    public List<ServiceFeedback> getAllBookingServices() {
    List<ServiceFeedback> list = new ArrayList<>();

    String sql = "SELECT a.BookingID, s.NameService "
               + "FROM Appointment_Service a "
               + "JOIN Services s ON a.ServiceID = s.ServiceID";

    try {
        PreparedStatement ps = c.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ServiceFeedback bs = new ServiceFeedback(
                    rs.getInt("BookingID"),
                    rs.getString("NameService")
            );
            list.add(bs);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
}
