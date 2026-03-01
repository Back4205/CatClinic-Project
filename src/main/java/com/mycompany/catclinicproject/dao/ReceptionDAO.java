package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.ReceptionDashboardDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceptionDAO extends DBContext {

    // Lấy danh sách khách HÔM NAY chưa Check-in
    public List<ReceptionDashboardDTO> getPendingCheckinsToday() {
        List<ReceptionDashboardDTO> list = new ArrayList<>();
        String sql = "SELECT b.BookingID, u.FullName AS CustomerName, u.Phone, "
                + "c.Name AS CatName, b.AppointmentDate, b.AppointmentTime, b.Status "
                + "FROM Bookings b "
                + "JOIN Cats c ON b.CatID = c.CatID "
                + "JOIN Owners o ON c.OwnerID = o.OwnerID "
                + "JOIN Users u ON o.UserID = u.UserID "
                + "WHERE CAST(b.AppointmentDate AS DATE) = CAST(GETDATE() AS DATE) "
                + "AND b.Status IN ('Assigned', 'Confirmed', 'Pending') "
                + "ORDER BY b.AppointmentTime ASC";
        try {
            if (c == null || c.isClosed()) new DBContext();
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new ReceptionDashboardDTO(
                        rs.getInt("BookingID"), rs.getString("CustomerName"),
                        rs.getString("Phone"), rs.getString("CatName"), rs.getDate("AppointmentDate"),
                        rs.getTime("AppointmentTime"), rs.getString("Status")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Cập nhật trạng thái
    public boolean updateStatus(int bookingId, String status) {
        String sql = "UPDATE Bookings SET Status = ? WHERE BookingID = ?";
        try {
            if (c == null || c.isClosed()) new DBContext();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, bookingId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // Lấy danh sách lịch hẹn ĐANG CHỜ để Lễ tân có thể Hủy
    public List<ReceptionDashboardDTO> searchBookingsForCancellation(String search) {
        List<ReceptionDashboardDTO> list = new ArrayList<>();
        String sql = "SELECT b.BookingID, u.FullName AS CustomerName, u.Phone, "
                + "c.Name AS CatName, b.AppointmentDate, b.AppointmentTime, b.Status "
                + "FROM Bookings b "
                + "JOIN Cats c ON b.CatID = c.CatID "
                + "JOIN Owners o ON c.OwnerID = o.OwnerID "
                + "JOIN Users u ON o.UserID = u.UserID "
                + "WHERE b.Status IN ('Assigned', 'Confirmed', 'Pending', 'Waiting') ";

        // Nếu có nhập từ khóa tìm kiếm
        if (search != null && !search.trim().isEmpty()) {
            sql += "AND (u.Phone LIKE ? OR u.FullName LIKE ? OR c.Name LIKE ? OR CAST(b.BookingID AS VARCHAR) = ?) ";
        }
        sql += "ORDER BY b.AppointmentDate ASC, b.AppointmentTime ASC";

        try {
            if (c == null || c.isClosed()) new DBContext();
            PreparedStatement ps = c.prepareStatement(sql);
            if (search != null && !search.trim().isEmpty()) {
                String keyword = "%" + search + "%";
                ps.setString(1, keyword);
                ps.setString(2, keyword);
                ps.setString(3, keyword);
                ps.setString(4, search);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new ReceptionDashboardDTO(
                        rs.getInt("BookingID"), rs.getString("CustomerName"),
                        rs.getString("Phone"), rs.getString("CatName"), rs.getDate("AppointmentDate"),
                        rs.getTime("AppointmentTime"), rs.getString("Status")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    // 1. Lấy danh sách Dịch vụ (Services)
    public List<Map<String, Object>> getAllActiveServices() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT ServiceID, NameService, Price FROM Services WHERE IsActive = 1";
        try {
            if (c == null || c.isClosed()) { c = new DBContext().c; }
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("ServiceID", rs.getInt("ServiceID"));
                map.put("NameService", rs.getString("NameService"));
                map.put("Price", rs.getDouble("Price"));
                list.add(map);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. Lấy danh sách Bác sĩ (Veterinarians)
    public List<Map<String, Object>> getAllVeterinarians() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT v.VetID, u.FullName FROM Veterinarians v JOIN Users u ON v.UserID = u.UserID WHERE u.IsActive = 1";
        try {
            if (c == null || c.isClosed()) { c = new DBContext().c; }
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("VetID", rs.getInt("VetID"));
                map.put("FullName", rs.getString("FullName"));
                list.add(map);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 3. Lấy danh sách KHUNG GIỜ RẢNH của Bác sĩ (Dùng cho AJAX)
    public List<Map<String, Object>> getAvailableSlots(int vetId, String date) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT t.SlotID, t.StartTime FROM TimeSlots t " +
                "WHERE t.VetID = ? AND t.Date = ? AND t.Status = 'Available' " +
                "AND t.SlotID NOT IN (" +
                "    SELECT b.SlotID FROM Bookings b " +
                "    WHERE b.VetID = ? AND b.AppointmentDate = ? AND b.Status NOT IN ('Cancelled', 'Completed')" +
                ") ORDER BY t.StartTime";
        try {
            if (c == null || c.isClosed()) { c = new DBContext().c; }
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, vetId);
            ps.setString(2, date);
            ps.setInt(3, vetId);
            ps.setString(4, date);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("slotId", rs.getInt("SlotID"));
                String time = rs.getString("StartTime");
                if(time != null && time.length() >= 5) time = time.substring(0, 5);
                map.put("startTime", time);
                list.add(map);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 4. Tạo Booking một mạch cho Khách vãng lai (Tự tạo User, Owner, Cat, Booking)
    public boolean createFullCounterBooking(String phone, String fullName, String petName, String breed, int age, String gender, int vetId, int serviceId, String date, String time, String note, int slotId) {
        try {
            if (c == null || c.isClosed()) { c = new DBContext().c; }
            c.setAutoCommit(false);

            int userId = -1;
            int ownerId = -1;

            // KIỂM TRA SĐT ĐÃ CÓ TRONG HỆ THỐNG CHƯA
            PreparedStatement checkUser = c.prepareStatement("SELECT UserID FROM Users WHERE Phone = ?");
            checkUser.setString(1, phone);
            ResultSet rsUser = checkUser.executeQuery();

            if (rsUser.next()) {
                userId = rsUser.getInt("UserID");
                PreparedStatement checkOwner = c.prepareStatement("SELECT OwnerID FROM Owners WHERE UserID = ?");
                checkOwner.setInt(1, userId);
                ResultSet rsOwner = checkOwner.executeQuery();
                if (rsOwner.next()) ownerId = rsOwner.getInt("OwnerID");
            } else {
                // NẾU KHÁCH MỚI -> TẠO USER (RoleID 5) & OWNER
                String sqlInsertUser = "INSERT INTO Users (UserName, PassWord, FullName, Phone, RoleID, IsActive, Male) VALUES (?, '123456', ?, ?, 5, 1, 1)";
                PreparedStatement insertUser = c.prepareStatement(sqlInsertUser, java.sql.Statement.RETURN_GENERATED_KEYS);
                insertUser.setString(1, phone);
                insertUser.setString(2, fullName);
                insertUser.setString(3, phone);
                insertUser.executeUpdate();
                ResultSet rsUserKeys = insertUser.getGeneratedKeys();
                if (rsUserKeys.next()) userId = rsUserKeys.getInt(1);

                String sqlInsertOwner = "INSERT INTO Owners (UserID) VALUES (?)";
                PreparedStatement insertOwner = c.prepareStatement(sqlInsertOwner, java.sql.Statement.RETURN_GENERATED_KEYS);
                insertOwner.setInt(1, userId);
                insertOwner.executeUpdate();
                ResultSet rsOwnerKeys = insertOwner.getGeneratedKeys();
                if (rsOwnerKeys.next()) ownerId = rsOwnerKeys.getInt(1);
            }

            // TẠO MÈO
            String sqlInsertCat = "INSERT INTO Cats (OwnerID, Name, Breed, Age, Gender, IsActive) VALUES (?, ?, ?, ?, ?, 1)";
            PreparedStatement insertCat = c.prepareStatement(sqlInsertCat, java.sql.Statement.RETURN_GENERATED_KEYS);
            insertCat.setInt(1, ownerId);
            insertCat.setString(2, petName);
            insertCat.setString(3, breed);
            insertCat.setInt(4, age);
            insertCat.setString(5, gender);
            insertCat.executeUpdate();
            ResultSet rsCatKeys = insertCat.getGeneratedKeys();
            int catId = -1;
            if (rsCatKeys.next()) catId = rsCatKeys.getInt(1);

            // TẠO BOOKING
            String sqlBooking = "INSERT INTO Bookings (CatID, VetID, SlotID, AppointmentDate, AppointmentTime, BookingDate, EndDate, Status, Note) VALUES (?, ?, ?, ?, ?, GETDATE(), ?, 'Assigned', ?)";
            PreparedStatement psBooking = c.prepareStatement(sqlBooking, java.sql.Statement.RETURN_GENERATED_KEYS);
            psBooking.setInt(1, catId);
            psBooking.setInt(2, vetId);
            psBooking.setInt(3, slotId);
            psBooking.setString(4, date);
            psBooking.setString(5, time);
            psBooking.setString(6, date); // EndDate = AppointmentDate theo DB chuẩn
            psBooking.setString(7, note);
            psBooking.executeUpdate();
            ResultSet rsBookingKeys = psBooking.getGeneratedKeys();
            int bookingId = -1;
            if (rsBookingKeys.next()) bookingId = rsBookingKeys.getInt(1);

            // TẠO APPOINTMENT_SERVICE
            PreparedStatement psPrice = c.prepareStatement("SELECT Price FROM Services WHERE ServiceID = ?");
            psPrice.setInt(1, serviceId);
            ResultSet rsPrice = psPrice.executeQuery();
            double price = 0;
            if (rsPrice.next()) price = rsPrice.getDouble("Price");

            PreparedStatement psApptSvc = c.prepareStatement("INSERT INTO Appointment_Service (BookingID, ServiceID, PriceAtBooking) VALUES (?, ?, ?)");
            psApptSvc.setInt(1, bookingId);
            psApptSvc.setInt(2, serviceId);
            psApptSvc.setDouble(3, price);
            psApptSvc.executeUpdate();

            c.commit();
            c.setAutoCommit(true);
            return true;

        } catch (Exception e) {
            try { if (c != null) c.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        }
        return false;
    }

}