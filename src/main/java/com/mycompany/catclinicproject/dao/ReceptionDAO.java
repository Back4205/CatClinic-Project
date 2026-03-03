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

    // 2. Cập nhật trạng thái
    public boolean updateStatus(int bookingId, String status) {
        String sql = "UPDATE Bookings SET Status = ? WHERE BookingID = ?";
        try {
            if (c == null || c.isClosed()) new DBContext();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, bookingId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3. Lấy danh sách lịch hẹn ĐANG CHỜ để Lễ tân có thể Hủy (CHỈ LẤY CA ĐÃ THANH TOÁN)
    public List<ReceptionDashboardDTO> searchBookingsForCancellation(String search) {
        List<ReceptionDashboardDTO> list = new ArrayList<>();
        String sql = "SELECT b.BookingID, u.FullName AS CustomerName, u.Phone, "
                + "c.Name AS CatName, b.AppointmentDate, b.AppointmentTime, b.Status "
                + "FROM Bookings b "
                + "JOIN Cats c ON b.CatID = c.CatID "
                + "JOIN Owners o ON c.OwnerID = o.OwnerID "
                + "JOIN Users u ON o.UserID = u.UserID "
                // CHỈ LỌC NHỮNG CA ĐÃ THANH TOÁN ONLINE (Confirmed)
                + "WHERE b.Status = 'Confirmed' ";

        // Nếu có nhập từ khóa tìm kiếm
        if (search != null && !search.trim().isEmpty()) {
            sql += "AND (u.Phone LIKE ? OR u.FullName LIKE ? OR c.Name LIKE ? OR CAST(b.BookingID AS VARCHAR) = ?) ";
        }
        sql += "ORDER BY b.AppointmentDate ASC, b.AppointmentTime ASC";

        try {
            if (c == null || c.isClosed()) { c = new DBContext().c; }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 4. Lấy danh sách Dịch vụ (Services)
    public List<Map<String, Object>> getAllActiveServices() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT ServiceID, NameService, Price FROM Services WHERE IsActive = 1";
        try {
            if (c == null || c.isClosed()) new DBContext();
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("ServiceID", rs.getInt("ServiceID"));
                map.put("NameService", rs.getString("NameService"));
                map.put("Price", rs.getDouble("Price"));
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 5. Lấy danh sách Bác sĩ (Veterinarians)
    public List<Map<String, Object>> getAllVeterinarians() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT v.VetID, u.FullName FROM Veterinarians v JOIN Users u ON v.UserID = u.UserID WHERE u.IsActive = 1";
        try {
            if (c == null || c.isClosed()) new DBContext();
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("VetID", rs.getInt("VetID"));
                map.put("FullName", rs.getString("FullName"));
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 6. Lấy danh sách KHUNG GIỜ RẢNH của Bác sĩ (Dùng cho AJAX)
    public List<Map<String, Object>> getAvailableSlots(int vetId, String date) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT t.SlotID, t.StartTime FROM TimeSlots t " +
                "WHERE t.VetID = ? AND t.Date = ? AND t.Status = 'Available' " +
                "AND t.SlotID NOT IN (" +
                "    SELECT b.SlotID FROM Bookings b " +
                "    WHERE b.VetID = ? AND b.AppointmentDate = ? AND b.Status NOT IN ('Cancelled', 'Completed')" +
                ") ORDER BY t.StartTime";
        try {
            if (c == null || c.isClosed()) new DBContext();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ==============================================================================
    // 7. TẠO BOOKING MỘT MẠCH CHO KHÁCH VÃNG LAI (BẢN CẬP NHẬT CÓ STAFF_ID)
    // ==============================================================================
    public boolean createFullCounterBooking(String phone, String fullName, String petName, String breed,
                                            int age, String gender, int vetId, int serviceId,
                                            String date, String time, String note, int slotId, int staffId) {
        try {
            if (c == null || c.isClosed()) new DBContext();
            // TẮT AUTO COMMIT ĐỂ DÙNG TRANSACTION (LỖI THÌ QUAY LẠI TỪ ĐẦU)
            c.setAutoCommit(false);

            int userId = -1;
            int ownerId = -1;

            // BƯỚC 1: KIỂM TRA SĐT ĐÃ CÓ TRONG HỆ THỐNG CHƯA
            PreparedStatement checkUser = c.prepareStatement("SELECT UserID FROM Users WHERE Phone = ? AND RoleID = 5");
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
                String sqlInsertUser = "INSERT INTO Users (UserName, PassWord, FullName, Phone, Email, RoleID, IsActive, Male) VALUES (?, '123456', ?, ?, ?, 5, 1, 1)";
                PreparedStatement insertUser = c.prepareStatement(sqlInsertUser, java.sql.Statement.RETURN_GENERATED_KEYS);
                insertUser.setString(1, phone);
                insertUser.setString(2, fullName);
                insertUser.setString(3, phone);
                insertUser.setString(4, phone + "@catclinic.com"); // Cấp đại một email để thỏa mãn DB
                insertUser.executeUpdate();
                ResultSet rsUserKeys = insertUser.getGeneratedKeys();
                if (rsUserKeys.next()) userId = rsUserKeys.getInt(1);

                String sqlInsertOwner = "INSERT INTO Owners (UserID, Address) VALUES (?, 'Walk-in Customer')";
                PreparedStatement insertOwner = c.prepareStatement(sqlInsertOwner, java.sql.Statement.RETURN_GENERATED_KEYS);
                insertOwner.setInt(1, userId);
                insertOwner.executeUpdate();
                ResultSet rsOwnerKeys = insertOwner.getGeneratedKeys();
                if (rsOwnerKeys.next()) ownerId = rsOwnerKeys.getInt(1);
            }

            // BƯỚC 2: TẠO MÈO
            String sqlInsertCat = "INSERT INTO Cats (OwnerID, Name, Breed, Age, Gender, IsActive) VALUES (?, ?, ?, ?, ?, 1)";
            PreparedStatement insertCat = c.prepareStatement(sqlInsertCat, java.sql.Statement.RETURN_GENERATED_KEYS);
            insertCat.setInt(1, ownerId);
            insertCat.setString(2, petName);
            insertCat.setString(3, breed);
            insertCat.setInt(4, age);
            // Gender lưu chuẩn BIT (1: Male, 0: Female) theo bản update mới nhất của DB
            insertCat.setBoolean(5, "Male".equalsIgnoreCase(gender));
            insertCat.executeUpdate();

            ResultSet rsCatKeys = insertCat.getGeneratedKeys();
            int catId = -1;
            if (rsCatKeys.next()) catId = rsCatKeys.getInt(1);

            // BƯỚC 3: TẠO BOOKING
            // Thêm cột StaffID vào lệnh INSERT
            String sqlBooking = "INSERT INTO Bookings (CatID, VetID, SlotID, AppointmentDate, AppointmentTime, BookingDate, EndDate, Status, Note, StaffID) VALUES (?, ?, ?, ?, ?, GETDATE(), ?, 'Assigned', ?, ?)";
            PreparedStatement psBooking = c.prepareStatement(sqlBooking, java.sql.Statement.RETURN_GENERATED_KEYS);
            psBooking.setInt(1, catId);

            // Xử lý an toàn VetID và SlotID khi Lễ tân không chọn
            if (vetId > 0) psBooking.setInt(2, vetId); else psBooking.setNull(2, java.sql.Types.INTEGER);
            if (slotId > 0) psBooking.setInt(3, slotId); else psBooking.setNull(3, java.sql.Types.INTEGER);

            psBooking.setString(4, date);

            // Ép chuỗi "HH:mm" thành "HH:mm:ss" để DB nhận đúng định dạng TIME
            if (time != null && time.length() == 5) {
                psBooking.setString(5, time + ":00");
            } else {
                psBooking.setString(5, time);
            }

            psBooking.setString(6, date); // EndDate tạm để = AppointmentDate
            psBooking.setString(7, note);

            // Lưu StaffID vào Booking
            if (staffId > 0) psBooking.setInt(8, staffId); else psBooking.setNull(8, java.sql.Types.INTEGER);

            psBooking.executeUpdate();

            ResultSet rsBookingKeys = psBooking.getGeneratedKeys();
            int bookingId = -1;
            if (rsBookingKeys.next()) bookingId = rsBookingKeys.getInt(1);

            // BƯỚC 4: TẠO APPOINTMENT_SERVICE (Lấy giá từ Service lưu xuống)
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

            // BƯỚC 5: CẬP NHẬT TRẠNG THÁI KHUNG GIỜ CỦA BÁC SĨ THÀNH 'BOOKED'
            if (slotId > 0) {
                PreparedStatement psSlot = c.prepareStatement("UPDATE TimeSlots SET Status = 'Booked' WHERE SlotID = ?");
                psSlot.setInt(1, slotId);
                psSlot.executeUpdate();
            }

            // HOÀN TẤT GIAO DỊCH
            c.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (c != null) c.rollback(); // Bất kỳ lỗi nào cũng hoàn tác, không để rác data
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (c != null) c.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }
    // Lấy danh sách Nhân viên theo Chức vụ
    public List<Map<String, Object>> getStaffByPosition(String position) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT s.StaffID, u.FullName FROM Staffs s JOIN Users u ON s.UserID = u.UserID WHERE s.Position = ? AND u.IsActive = 1";
        try {
            // Chữ 'c' ở đây sẽ không bị đỏ vì ReceptionDAO kế thừa DBContext
            if (c == null || c.isClosed()) { c = new DBContext().c; }
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, position);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("staffID", rs.getInt("StaffID"));
                map.put("fullName", rs.getString("FullName"));
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}