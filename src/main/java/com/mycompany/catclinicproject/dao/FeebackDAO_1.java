/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.Feedback3DTO;
import com.mycompany.catclinicproject.model.ServiceFeedback;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Son
 */
public class FeebackDAO_1 extends DBContext{
    
    public List<Feedback3DTO> getAllFeedback() {
    List<Feedback3DTO> list = new ArrayList<>();

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
            Feedback3DTO f = new Feedback3DTO(
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
