package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.BookingHistoryDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO extends DBContext {

    public List<BookingHistoryDTO> getHistoryByUserID(int userID) {
        List<BookingHistoryDTO> list = new ArrayList<>();
        
        // Thêm b.EndDate vào câu SELECT
        String sql = "SELECT b.BookingID, c.Name AS CatName, c.Breed, " +
                     "b.AppointmentDate, b.EndDate, b.AppointmentTime, b.Status, " + // <--- Thêm b.EndDate
                     "s.ServiceName, bd.PriceAtBooking " +
                     "FROM Bookings b " +
                     "JOIN Cats c ON b.CatID = c.CatID " +
                     "JOIN Owners o ON c.OwnerID = o.OwnerID " +
                     "JOIN BookingDetails bd ON b.BookingID = bd.BookingID " +
                     "JOIN Services s ON bd.ServiceID = s.ServiceID " +
                     "WHERE o.UserID = ? " +
                     "ORDER BY b.AppointmentDate DESC"; 
        
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new BookingHistoryDTO(
                    rs.getInt("BookingID"),
                    rs.getString("CatName"),
                    rs.getString("Breed"),
                    rs.getDate("AppointmentDate"),
                    rs.getDate("EndDate"), // <--- Lấy EndDate từ DB
                    rs.getTime("AppointmentTime"),
                    rs.getString("ServiceName"),
                    rs.getDouble("PriceAtBooking"),
                    rs.getString("Status")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}