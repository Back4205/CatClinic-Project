package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.Booking;
import com.mycompany.catclinicproject.model.BookingHistoryDTO;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class BookingDAO extends DBContext {


    public List<BookingHistoryDTO> getHistoryByUserID(int userID) {
        // 1. Tự động dọn dẹp các ca No-show hoặc quá hạn thanh toán trước khi lấy dữ liệu
        cancelNoShowBookings();
        autoCancelExpiredBookings();

        List<BookingHistoryDTO> list = new ArrayList<>();
        String sql = "SELECT b.BookingID, b.SlotID, c.Name AS CatName, c.Breed, "
                + "b.AppointmentDate, b.EndDate, b.AppointmentTime, b.Status, "
                + "s.NameService, bd.PriceAtBooking "
                + "FROM Bookings b "
                + "JOIN Cats c ON b.CatID = c.CatID "
                + "JOIN Owners o ON c.OwnerID = o.OwnerID "
                + "JOIN Appointment_Service bd ON b.BookingID = bd.BookingID "
                + "JOIN Services s ON bd.ServiceID = s.ServiceID "
                + "WHERE o.UserID = ? "
                + "ORDER BY b.AppointmentDate DESC";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Tạo đối tượng DTO
                    BookingHistoryDTO dto = new BookingHistoryDTO(
                            rs.getInt("BookingID"),
                            rs.getInt("SlotID"),
                            rs.getString("CatName"),
                            rs.getString("Breed"),
                            rs.getDate("AppointmentDate"),
                            rs.getDate("EndDate"),
                            rs.getTime("AppointmentTime"),
                            rs.getString("NameService"),
                            rs.getDouble("PriceAtBooking"),
                            rs.getString("Status"),
                            null, "", "", ""
                    );

                    // 2. Logic tính toán mốc 2 tiếng để cho phép khách Cancel
                    // Kết hợp Ngày + Giờ hẹn từ Database
                    long apptMillis = rs.getDate("AppointmentDate").getTime() + rs.getTime("AppointmentTime").getTime();
                    long nowMillis = System.currentTimeMillis();

                    // Hiệu số thời gian tính bằng giờ
                    double diffHours = (apptMillis - nowMillis) / (1000.0 * 60 * 60);

                    // Gán quyền Cancel: Chỉ khi 'Confirmed' và còn >= 2 tiếng
                    dto.setIsCancellable(dto.getStatus().equals("Confirmed") && diffHours >= 2.0);

                    list.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public Map<String, Integer> getBookingDashboardStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("Pending", 0);
        stats.put("Confirmed", 0);
        stats.put("Waiting", 0);
        stats.put("In Treatment", 0);
        stats.put("Completed", 0);
        stats.put("Cancelled", 0);
        stats.put("Pending Refund", 0);

        String sql = "SELECT Status, COUNT(*) as Total FROM Bookings GROUP BY Status";

        try (PreparedStatement st = c.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                String status = rs.getString("Status");
                int total = rs.getInt("Total");
                if(stats.containsKey(status)) {
                    stats.put(status, total);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stats;
    }

    public List<BookingHistoryDTO> getReceptionBookingList(String keyword) {
        List<BookingHistoryDTO> list = new ArrayList<>();
        String sql = "SELECT b.BookingID, b.AppointmentDate, b.AppointmentTime, b.Status, " +
                "u_owner.FullName AS CustomerName, u_owner.Phone AS CustomerPhone, " +
                "c.Name AS CatName " +
                "FROM Bookings b " +
                "JOIN Cats c ON b.CatID = c.CatID " +
                "JOIN Owners o ON c.OwnerID = o.OwnerID " +
                "JOIN Users u_owner ON o.UserID = u_owner.UserID " +
                "WHERE 1=1 ";
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql += " AND (u_owner.FullName LIKE ? OR u_owner.Phone LIKE ? OR c.Name LIKE ?) ";
        }
        sql += " ORDER BY b.AppointmentDate DESC, b.AppointmentTime DESC";

        try (PreparedStatement st = c.prepareStatement(sql)) { // Sử dụng connection 'c' của cậu
            if (keyword != null && !keyword.trim().isEmpty()) {
                String p = "%" + keyword.trim() + "%";
                st.setString(1, p);
                st.setString(2, p);
                st.setString(3, p);
            }
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    BookingHistoryDTO d = new BookingHistoryDTO();
                    d.setBookingID(rs.getInt("BookingID"));
                    d.setAppointmentDate(rs.getDate("AppointmentDate"));
                    d.setAppointmentTime(rs.getTime("AppointmentTime"));
                    d.setStatus(rs.getString("Status"));
                    d.setCustomerName(rs.getString("CustomerName"));
                    d.setCustomerPhone(rs.getString("CustomerPhone"));
                    d.setCatName(rs.getString("CatName"));
                    list.add(d);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public void updateBookingStatusSimple(int id, String status) {
        String sql = "UPDATE Bookings SET Status = ? WHERE BookingID = ?";
        try (PreparedStatement st = c.prepareStatement(sql)) { // Biến 'c' là connection của cậu
            st.setString(1, status);
            st.setInt(2, id);
            st.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
    public BookingHistoryDTO getBookingDetailByID(int bookingID) {
        String sql = "SELECT b.BookingID, b.SlotID, c.Name AS CatName, c.Breed, b.AppointmentDate, "
                + "b.EndDate, b.AppointmentTime, b.Status, b.Note, s.NameService, "
                + "ad.PriceAtBooking, u_vet.FullName AS VetName, "
                + "u_owner.FullName AS OwnerName, u_owner.Phone "
                + "FROM Bookings b "
                + "JOIN Cats c ON b.CatID = c.CatID "
                + "JOIN Owners o ON c.OwnerID = o.OwnerID "
                + "JOIN Users u_owner ON o.UserID = u_owner.UserID "
                + "LEFT JOIN Veterinarians v ON b.VetID = v.VetID "
                + "LEFT JOIN Users u_vet ON v.UserID = u_vet.UserID "
                + "LEFT JOIN Appointment_Service ad ON b.BookingID = ad.BookingID "
                + "LEFT JOIN Services s ON ad.ServiceID = s.ServiceID "
                + "WHERE b.BookingID = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, bookingID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BookingHistoryDTO d = new BookingHistoryDTO();
                    d.setBookingID(rs.getInt("BookingID"));
                    d.setCatName(rs.getString("CatName"));
                    d.setCatBreed(rs.getString("Breed"));
                    d.setAppointmentDate(rs.getDate("AppointmentDate"));
                    d.setAppointmentTime(rs.getTime("AppointmentTime"));
                    d.setStatus(rs.getString("Status"));
                    d.setNote(rs.getString("Note"));

                    d.setServiceName(rs.getString("NameService"));
                    d.setPrice(rs.getDouble("PriceAtBooking"));

                    d.setVetName(rs.getString("VetName"));
                    d.setCustomerName(rs.getString("OwnerName"));
                    d.setCustomerPhone(rs.getString("Phone"));
                    return d;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
    public boolean cancelAndRequestRefund(int bookingID, String refundNote, int slotID) {
        String updateBookingSQL = "UPDATE Bookings SET Status = N'Cancelled', Note = ? WHERE BookingID = ?";
        String releaseSlotSQL = "UPDATE TimeSlots SET Status = N'Available' WHERE SlotID = ?";

        try {
            c.setAutoCommit(false);
            try (PreparedStatement ps1 = c.prepareStatement(updateBookingSQL)) {
                ps1.setString(1, refundNote);
                ps1.setInt(2, bookingID);
                ps1.executeUpdate();
            }
            if (slotID > 0) {
                try (PreparedStatement ps2 = c.prepareStatement(releaseSlotSQL)) {
                    ps2.setInt(1, slotID);
                    ps2.executeUpdate();
                }
            }
            c.commit();
            return true;
        } catch (SQLException e) {
            try { c.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
            return false;
        } finally {
            try { c.setAutoCommit(true); } catch (SQLException e) {}
        }
    }
    public int getCatIdByBookingID(int bookingID) {

        String sql = "SELECT CatID FROM Bookings WHERE BookingID = ?";
        int catID = -1;

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, bookingID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    catID = rs.getInt("CatID");
                }
            }

        } catch (SQLException e) {

        }

        return catID;
    }


    public int createBookingWithInvoice(Booking booking, List<Integer> serviceIDs, List<Double> prices, double totalAmount) {

        int bookingID = -1;

        String lockSlotSQL = "UPDATE TimeSlots SET Status = N'Pending' "
                + "WHERE SlotID = ? AND Status = N'Available'";

        String insertBookingSQL = "INSERT INTO Bookings "
                + "(CatID, VetID, SlotID, AppointmentDate, BookingDate, EndDate, AppointmentTime, Status, Note) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String insertDetailSQL = "INSERT INTO Appointment_Service "
                + "(BookingID, ServiceID, PriceAtBooking) VALUES (?, ?, ?)";

        String insertInvoiceSQL = "INSERT INTO Invoices "
                + "(BookingID, TotalAmount, PaymentStatus, CreatedDate) VALUES (?, ?, ?,GETDATE())";

        try {
            c.setAutoCommit(false);
            if (booking.getSlotID() > 0) {
                try (PreparedStatement psLock = c.prepareStatement(lockSlotSQL)) {
                    psLock.setInt(1, booking.getSlotID());
                    int affected = psLock.executeUpdate();

                    if (affected == 0) {
                        c.rollback();
                        return -1;
                    }
                }
            }

            try (PreparedStatement ps = c.prepareStatement(insertBookingSQL, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, booking.getCatID());
                ps.setObject(2, booking.getVeterinarianID() <= 0 ? null : booking.getVeterinarianID());
                ps.setObject(3, booking.getSlotID() <= 0 ? null : booking.getSlotID());
                ps.setDate(4, new java.sql.Date(booking.getAppointmentDate().getTime()));
                ps.setDate(5, new java.sql.Date(System.currentTimeMillis()));
                ps.setDate(6, new java.sql.Date(booking.getEndDate().getTime()));
                ps.setTime(7, booking.getAppointmentTime());
                ps.setString(8, "PendingPayment");
                ps.setString(9, booking.getNote() == null ? "" : booking.getNote());

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        bookingID = rs.getInt(1);
                    }
                }
            }
            if (bookingID == -1) {
                c.rollback();
                return -1;
            }

            try (PreparedStatement psDetail = c.prepareStatement(insertDetailSQL)) {

                for (int i = 0; i < serviceIDs.size(); i++) {
                    psDetail.setInt(1, bookingID);
                    psDetail.setInt(2, serviceIDs.get(i));
                    psDetail.setDouble(3, prices.get(i));
                    psDetail.addBatch();
                }

                psDetail.executeBatch();
            }

            try (PreparedStatement psInvoice = c.prepareStatement(insertInvoiceSQL)) {
                psInvoice.setInt(1, bookingID);
                psInvoice.setDouble(2, totalAmount);
                psInvoice.setString(3, "Unpaid");
                psInvoice.executeUpdate();
            }

            c.commit();

        } catch (SQLException e) {
            try {
                c.rollback();
            } catch (SQLException ex) {
            }

            bookingID = -1;

        } finally {
            try {
                c.setAutoCommit(true);
            } catch (SQLException e) {
            }
        }

        return bookingID;
    }

    public boolean confirmBooking(int bookingID) {

        String getSlotSQL = "SELECT SlotID FROM Bookings WHERE BookingID = ? AND Status = 'PendingPayment'";
        String updateBookingSQL =
                "UPDATE Bookings SET Status = 'Confirmed' WHERE BookingID = ? AND Status = 'PendingPayment'";
        String updateSlotSQL =
                "UPDATE TimeSlots SET Status = 'Booked' WHERE SlotID = ?";

        try {
            c.setAutoCommit(false);

            int slotID = 0;

            try (PreparedStatement ps = c.prepareStatement(getSlotSQL)) {
                ps.setInt(1, bookingID);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    slotID = rs.getInt("SlotID");
                } else {
                    c.rollback();
                    return false; // không còn PendingPayment
                }
            }

            try (PreparedStatement ps = c.prepareStatement(updateBookingSQL)) {
                ps.setInt(1, bookingID);
                ps.executeUpdate();
            }

            if (slotID > 0) {
                try (PreparedStatement ps = c.prepareStatement(updateSlotSQL)) {
                    ps.setInt(1, slotID);
                    ps.executeUpdate();
                }
            }

            c.commit();
            return true;

        } catch (Exception e) {
            try { c.rollback(); } catch (Exception ex) {}
        } finally {
            try { c.setAutoCommit(true); } catch (Exception e) {}
        }

        return false;
    }


    public boolean cancelBooking(int bookingID, int slotID) {

        String updateBookingSQL = "UPDATE Bookings SET Status = N'Cancelled' WHERE BookingID = ?";
        String releaseSlotSQL = "UPDATE TimeSlots SET Status = N'Available' WHERE SlotID = ?";

        try {
            c.setAutoCommit(false);

            try (PreparedStatement ps1 = c.prepareStatement(updateBookingSQL)) {
                ps1.setInt(1, bookingID);
                ps1.executeUpdate();
            }

            if (slotID > 0) {
                try (PreparedStatement ps2 = c.prepareStatement(releaseSlotSQL)) {
                    ps2.setInt(1, slotID);
                    ps2.executeUpdate();
                }
            }

            c.commit();
            return true;

        } catch (SQLException e) {
            try {
                c.rollback();
            } catch (SQLException ex) {
            }
        } finally {
            try {
                c.setAutoCommit(true);
            } catch (SQLException e) {
            }
        }

        return false;
    }

    public boolean updateBookingStatus(int bookingID, String status) {
        String sql = "UPDATE Bookings SET status = ? WHERE bookingID = ?";

        try (
                PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, bookingID);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public int getSlotIDByBookingID(int bookingID) {

        String sql = "SELECT SlotID FROM Bookings WHERE BookingID = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, bookingID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("SlotID");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void autoCancelExpiredBookings() {

        try {

            //  Lấy các slot của booking quá 5 phút
            String selectSql = " SELECT SlotID FROM Bookings WHERE Status = 'PendingPayment' AND DATEDIFF(MINUTE, BookingDate, GETDATE()) >= 5 AND SlotID IS NOT NULL ";

            PreparedStatement psSelect = c.prepareStatement(selectSql);
            ResultSet rs = psSelect.executeQuery();

            List<Integer> slotIds = new ArrayList<>();

            while (rs.next()) {
                slotIds.add(rs.getInt("SlotID"));
            }

            //  Cancel booking
            String cancelSql = " UPDATE Bookings SET Status = 'Cancelled' WHERE Status = 'PendingPayment' AND DATEDIFF(MINUTE, BookingDate, GETDATE()) >= 5";


            PreparedStatement psCancel = c.prepareStatement(cancelSql);
            psCancel.executeUpdate();

            //  Nhả slot
            if (!slotIds.isEmpty()) {

                String releaseSql = " UPDATE TimeSlots SET Status = 'Available' WHERE SlotID = ?";

                PreparedStatement psRelease = c.prepareStatement(releaseSql);

                for (Integer id : slotIds) {
                    psRelease.setInt(1, id);
                    psRelease.executeUpdate();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void cancelNoShowBookings() {
        // 1. Tìm các SlotID của những ca 'Confirmed' đã quá giờ
        String selectSql = "SELECT SlotID FROM Bookings " +
                "WHERE Status = 'Confirmed' " +
                "AND (CAST(AppointmentDate AS DATETIME) + CAST(AppointmentTime AS DATETIME)) < GETDATE()";

        // 2. Cập nhật trạng thái Booking thành Cancelled (Không hoàn tiền)
        String updateBookingSql = "UPDATE Bookings SET Status = 'Cancelled', Note = N'System: No-show (Non-refundable)' " +
                "WHERE Status = 'Confirmed' " +
                "AND (CAST(AppointmentDate AS DATETIME) + CAST(AppointmentTime AS DATETIME)) < GETDATE()";

        try {
            c.setAutoCommit(false); // Dùng Transaction cho an toàn

            List<Integer> slotIds = new ArrayList<>();
            try (PreparedStatement psSelect = c.prepareStatement(selectSql);
                 ResultSet rs = psSelect.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("SlotID");
                    if (id > 0) slotIds.add(id);
                }
            }

            // Cập nhật Booking
            try (PreparedStatement psUpdate = c.prepareStatement(updateBookingSql)) {
                psUpdate.executeUpdate();
            }

            // Nhả Slot về Available để khách khác có thể đặt
            if (!slotIds.isEmpty()) {
                String releaseSql = "UPDATE TimeSlots SET Status = 'Available' WHERE SlotID = ?";
                try (PreparedStatement psRelease = c.prepareStatement(releaseSql)) {
                    for (Integer id : slotIds) {
                        psRelease.setInt(1, id);
                        psRelease.executeUpdate();
                    }
                }
            }

            c.commit();
        } catch (Exception e) {
            try { c.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
        } finally {
            try { c.setAutoCommit(true); } catch (SQLException e) {}
        }
    }
    //    public boolean isCatTimeOverlapping(int catID, int slotID) {
//
//        String sql = " SELECT 1 FROM Bookings b JOIN TimeSlots ts_old ON b.SlotID = ts_old.SlotID JOIN TimeSlots ts_new ON ts_new.SlotID = ? WHERE b.CatID = ? AND b.Status IN ('PendingPayment','Confirmed') AND ts_old.Date = ts_new.Date AND ts_old.StartTime < ts_new.EndTimeAND ts_old.EndTime > ts_new.StartTime";
//
//        try (PreparedStatement ps = c.prepareStatement(sql)) {
//            ps.setInt(1, slotID);
//            ps.setInt(2, catID);
//
//            ResultSet rs = ps.executeQuery();
//            return rs.next(); // true = có trùng
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }
    public boolean isCatMedicalConflict(int catID, int newSlotID) {

        String sql = " SELECT 1 FROM Bookings b JOIN TimeSlots ts_old ON b.SlotID = ts_old.SlotID JOIN TimeSlots ts_new ON ts_new.SlotID = ? WHERE b.CatID = ? AND b.Status IN ('PendingPayment','Confirmed','Assigned','Intreatment')AND ts_old.SlotDate = ts_new.SlotDate AND ts_old.StartTime < ts_new.EndTime AND ts_old.EndTime > ts_new.StartTime ";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, newSlotID);
            ps.setInt(2, catID);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean isCatHotelConflict(int catID, Date newStart, Date newEnd) {

        String sql = " SELECT 1 FROM Bookings WHERE CatID = ?AND Status IN ('PendingPayment','Confirmed','Assigned') AND NOT (EndDate < ? OR AppointmentDate > ?) ";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, catID);
            ps.setDate(2, newStart);
            ps.setDate(3, newEnd);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


}