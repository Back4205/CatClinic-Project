package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.Booking;
import com.mycompany.catclinicproject.model.BookingHistoryDTO;
import com.mycompany.catclinicproject.model.CancelBookingDTO;
import com.mycompany.catclinicproject.model.BoardingRecordDTO;
import com.mycompany.catclinicproject.model.FeedbackDTO;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class BookingDAO extends DBContext {


    public List<BookingHistoryDTO> getHistoryByUserID(int userID) {
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
                    list.add(new BookingHistoryDTO(
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
                    ));
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

        String sql = "SELECT Status, COUNT(*) as Total FROM Bookings GROUP BY Status";

        try (PreparedStatement st = c.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                String status = rs.getString("Status");
                int total = rs.getInt("Total");
                if (stats.containsKey(status)) {
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
        // Thêm b.AppointmentTime vào SELECT
        String sql = "SELECT b.BookingID, b.AppointmentDate, b.AppointmentTime, b.Status, " +
                "u_owner.FullName AS CustomerName, u_owner.Phone AS CustomerPhone, " +
                "c.Name AS CatName, s.NameService " +
                "FROM Bookings b " +
                "JOIN Cats c ON b.CatID = c.CatID " +
                "JOIN Owners o ON c.OwnerID = o.OwnerID " +
                "JOIN Users u_owner ON o.UserID = u_owner.UserID " +
                "LEFT JOIN Appointment_Service ad ON b.BookingID = ad.BookingID " +
                "LEFT JOIN Services s ON ad.ServiceID = s.ServiceID " +
                "WHERE 1=1 ";

        // ... (giữ nguyên phần keyword) ...

        try (PreparedStatement st = c.prepareStatement(sql)) {
            // ... (giữ nguyên phần setString) ...
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    BookingHistoryDTO d = new BookingHistoryDTO();
                    d.setBookingID(rs.getInt("BookingID"));
                    d.setAppointmentDate(rs.getDate("AppointmentDate"));
                    d.setAppointmentTime(rs.getTime("AppointmentTime")); // QUAN TRỌNG: Lấy giờ ở đây
                    d.setStatus(rs.getString("Status"));
                    d.setCustomerName(rs.getString("CustomerName"));
                    d.setCustomerPhone(rs.getString("CustomerPhone"));
                    d.setCatName(rs.getString("CatName"));
                    d.setServiceName(rs.getString("NameService"));

                    list.add(d);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    public void updateBookingStatusSimple(int id, String status) {
        String sql = "UPDATE Bookings SET Status = ? WHERE BookingID = ?";
        try (PreparedStatement st = c.prepareStatement(sql)) { // Biến 'c' là connection của cậu
            st.setString(1, status);
            st.setInt(2, id);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BookingHistoryDTO getBookingDetailByID(int bookingID) {
        String sql = "SELECT b.BookingID, b.SlotID, c.Name AS CatName, c.Breed, b.AppointmentDate, "
                + "b.EndDate, b.AppointmentTime, b.Status, b.Note, s.NameService, "
                + "ad.PriceAtBooking, u_vet.FullName AS VetName, "
                + "u_owner.FullName AS OwnerName, u_owner.Phone, "
                + "br.CheckInTime, br.CheckOutTime "
                + "FROM Bookings b "
                + "JOIN Cats c ON b.CatID = c.CatID "
                + "JOIN Owners o ON c.OwnerID = o.OwnerID "
                + "JOIN Users u_owner ON o.UserID = u_owner.UserID "
                + "LEFT JOIN Veterinarians v ON b.VetID = v.VetID "
                + "LEFT JOIN Users u_vet ON v.UserID = u_vet.UserID "
                + "LEFT JOIN Appointment_Service ad ON b.BookingID = ad.BookingID "
                + "LEFT JOIN Services s ON ad.ServiceID = s.ServiceID "
                + "LEFT JOIN BoardingRecords br ON b.BookingID = br.BookingID "
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
                    d.setCheckInTime(rs.getTimestamp("CheckInTime"));
                    d.setCheckOutTime(rs.getTimestamp("CheckOutTime"));
                    return d;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean cancelAndRequestRefund(int bookingID, String refundNote, int slotID) {
        String updateBookingSQL = "UPDATE Bookings SET Status = N'PendingCancel', Note = ? WHERE BookingID = ?";
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
            try {
                c.rollback();
            } catch (SQLException ex) {
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                c.setAutoCommit(true);
            } catch (SQLException e) {
            }
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
                    return false;
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
            try {
                c.rollback();
            } catch (Exception ex) {
            }
        } finally {
            try {
                c.setAutoCommit(true);
            } catch (Exception e) {
            }
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

    public boolean isCatBusyAtSlot(int catID, int slotID) {

        String sql = "SELECT 1 FROM Bookings b " +
                "JOIN TimeSlots ts_new ON ts_new.SlotID = ? " +
                "JOIN TimeSlots ts_old ON b.SlotID = ts_old.SlotID " +
                "WHERE b.CatID = ? " +
                "AND b.Status IN ('PendingPayment', 'Confirmed') " +
                "AND ts_old.Date = ts_new.Date " +
                "AND ts_old.StartTime = ts_new.StartTime";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, slotID);
            ps.setInt(2, catID);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Nếu true nghĩa là mèo đã bận
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<CancelBookingDTO> getAllCancelBookings() {
        List<CancelBookingDTO> list = new ArrayList<>();

        String sql = "SELECT b.BookingID, b.Status, b.Note, a.PriceAtBooking "
                + "FROM Bookings b "
                + "INNER JOIN Appointment_Service a "
                + "ON b.BookingID = a.BookingID "
                + "WHERE b.Status = 'PendingCancel'";

        try {
            PreparedStatement ps = c.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CancelBookingDTO dto = new CancelBookingDTO(
                        rs.getInt("BookingID"),
                        rs.getString("Status"),
                        rs.getString("Note"),
                        rs.getDouble("PriceAtBooking")
                );

                list.add(dto);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public CancelBookingDTO getPendingCancelBookingById(int bookingID) {

        String sql = "SELECT b.BookingID, b.Status, b.Note, a.PriceAtBooking "
                + "FROM Bookings b "
                + "INNER JOIN Appointment_Service a "
                + "ON b.BookingID = a.BookingID "
                + "WHERE b.Status = 'PendingCancel' AND b.BookingID = ?";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, bookingID);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                CancelBookingDTO dto = new CancelBookingDTO(
                        rs.getInt("BookingID"),
                        rs.getString("Status"),
                        rs.getString("Note"),
                        rs.getDouble("PriceAtBooking")
                );

                rs.close();
                ps.close();

                return dto;
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // nếu không tìm thấy
    }

    public void rejectRefund(int bookingID) {
        String sql = "UPDATE Bookings SET Status = 'Reject', "
                + "Note = 'Please verify your refund details and try again.' "
                + "WHERE BookingID = ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, bookingID);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void approveRefund(int bookingID, String imageUrl) {
        String sql = "UPDATE Bookings SET Status = 'Cancelled', "
                + "Note = ? "
                + "WHERE BookingID = ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, imageUrl);
            ps.setInt(2, bookingID);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int countCompleted() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Bookings WHERE Status = 'Completed'";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public int countConfirmed() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Bookings WHERE Status = 'Confirmed'";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public int countPendingPayment() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Bookings WHERE Status = 'PendingPayment'";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public int countPendingCancelRefund() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Bookings WHERE Status = 'PendingCancelRefund'";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public int countCancelled() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Bookings WHERE Status = 'Cancelled'";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public List<BookingHistoryDTO> getBookingHistory(String search, String status, int offset, int pageSize) {
        List<BookingHistoryDTO> list = new ArrayList<>();
        String sql =
                "SELECT "
                        + "b.BookingID, "
                        + "b.SlotID, "
                        + "c.Name AS CatName, "
                        + "c.Breed, "
                        + "b.AppointmentDate, "
                        + "b.EndDate, "
                        + "b.AppointmentTime, "
                        + "b.Status, "
                        + "b.Note, "
                        + "u.FullName AS OwnerName, "
                        + "u.Phone AS OwnerPhone, "
                        + "vetUser.FullName AS VetName, "
                        + "s.NameService " // Lấy tên dịch vụ
                        + "FROM Bookings b "
                        + "JOIN Cats c ON b.CatID = c.CatID "
                        + "JOIN Owners o ON c.OwnerID = o.OwnerID "
                        + "JOIN Users u ON o.UserID = u.UserID "
                        + "LEFT JOIN Veterinarians v ON b.VetID = v.VetID "
                        + "LEFT JOIN Users vetUser ON v.UserID = vetUser.UserID "
                        + "LEFT JOIN TimeSlots t ON b.SlotID = t.SlotID "
                        + "LEFT JOIN Appointment_Service ad ON b.BookingID = ad.BookingID "
                        + "LEFT JOIN Services s ON ad.ServiceID = s.ServiceID "
                        + "WHERE 1=1 ";

        if (search != null && !search.trim().isEmpty()) {
            sql += " AND (c.Name LIKE ? OR u.FullName LIKE ? OR u.Phone LIKE ?) ";
        }
        if (status != null && !status.trim().isEmpty()) {
            sql += " AND b.Status = ? ";
        }
        sql += " ORDER BY b.AppointmentDate DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            int index = 1;
            if (search != null && !search.trim().isEmpty()) {
                ps.setString(index++, "%" + search + "%");
                ps.setString(index++, "%" + search + "%");
                ps.setString(index++, "%" + search + "%");
            }
            if (status != null && !status.trim().isEmpty()) {
                ps.setString(index++, status);
            }
            ps.setInt(index++, offset);
            ps.setInt(index++, pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BookingHistoryDTO dto = new BookingHistoryDTO();
                    dto.setBookingID(rs.getInt("BookingID"));
                    dto.setCatName(rs.getString("CatName"));
                    dto.setAppointmentDate(rs.getDate("AppointmentDate"));

                    // --- ĐOẠN QUAN TRỌNG NHẤT ĐÂY CẬU ---
                    dto.setAppointmentTime(rs.getTime("AppointmentTime")); // Gán giờ vào DTO
                    dto.setServiceName(rs.getString("NameService"));       // Gán tên dịch vụ vào DTO
                    // -------------------------------------

                    dto.setOwnerName(rs.getString("OwnerName"));
                    dto.setOwnerPhone(rs.getString("OwnerPhone"));
                    dto.setStatus(rs.getString("Status"));
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public int countBookings(String search, String status) {

        int total = 0;

        String sql =
                "SELECT COUNT(*) "
                        + "FROM Bookings b "
                        + "JOIN Cats c ON b.CatID = c.CatID "
                        + "JOIN Owners o ON c.OwnerID = o.OwnerID "
                        + "JOIN Users u ON o.UserID = u.UserID "
                        + "WHERE 1=1 ";

        if (search != null && !search.trim().isEmpty()) {
            sql += " AND (c.Name LIKE ? OR u.FullName LIKE ? OR u.Phone LIKE ?) ";
        }

        if (status != null && !status.trim().isEmpty()) {
            sql += " AND b.Status = ? ";
        }

        try {

            PreparedStatement ps = c.prepareStatement(sql);

            int index = 1;

            if (search != null && !search.trim().isEmpty()) {
                ps.setString(index++, "%" + search + "%");
                ps.setString(index++, "%" + search + "%");
                ps.setString(index++, "%" + search + "%");
            }

            if (status != null && !status.trim().isEmpty()) {
                ps.setString(index++, status);
            }

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    public void cancelNoShowBookings() {
        String sql = "UPDATE Bookings SET Status = 'Cancelled', Note = 'System: No-show auto-cancel' " +
                "WHERE Status = 'Confirmed' " +
                "AND CAST(AppointmentDate AS DATETIME) + CAST(AppointmentTime AS DATETIME) < GETDATE()";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkInWithRecord(int bookingID, String condition) {
        String sqlUpdate = "UPDATE Bookings SET Status = 'Completed' WHERE BookingID = ?";

        String sqlInsert = "INSERT INTO BoardingRecords (BookingID, CheckInTime, CheckInCondition) VALUES (?, GETDATE(), ?)";

        try {
            c.setAutoCommit(false); // Bắt đầu giao dịch (Transaction)

            try (PreparedStatement ps1 = c.prepareStatement(sqlUpdate)) {
                ps1.setInt(1, bookingID);
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = c.prepareStatement(sqlInsert)) {
                ps2.setInt(1, bookingID);
                ps2.setString(2, (condition == null || condition.trim().isEmpty()) ? "Normal" : condition);
                ps2.executeUpdate();
            }

            c.commit();
            return true;
        } catch (SQLException e) {
            try {
                c.rollback();
            } catch (SQLException ex) {
            }
            e.printStackTrace();
        } finally {
            try {
                c.setAutoCommit(true);
            } catch (SQLException e) {
            }
        }
        return false;
    }

    public boolean processCheckOut(int bookingID, String releaseCondition) {
        String sql = "UPDATE BoardingRecords SET CheckOutTime = GETDATE(), CheckOutCondition = ? " +
                "WHERE BookingID = ? AND CheckOutTime IS NULL";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, releaseCondition);
            ps.setInt(2, bookingID);

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public BoardingRecordDTO getBoardingRecordByBookingID(int bookingID) {
        String sql = "SELECT * FROM BoardingRecords WHERE BookingID = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, bookingID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BoardingRecordDTO d = new BoardingRecordDTO();
                    d.setCheckInTime(rs.getString("CheckInTime"));
                    d.setCheckInCondition(rs.getString("CheckInCondition"));
                    d.setCheckOutTime(rs.getString("CheckOutTime")); // Sẽ null nếu chưa check-out
                    d.setCheckOutCondition(rs.getString("CheckOutCondition"));
                    return d;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<BookingHistoryDTO> getClientHistoryPaging(int userID, String search, String status, int offset, int pageSize) {
        List<BookingHistoryDTO> list = new ArrayList<>();
        // SỬA LẠI SQL: Lấy thêm NameService và PriceAtBooking
        String sql = "SELECT DISTINCT b.BookingID, b.SlotID, c.Name AS CatName, c.Breed, b.AppointmentDate, b.EndDate, "
                + "b.AppointmentTime, b.Status, b.Note, s.NameService, ad.PriceAtBooking, br.CheckOutTime " // Thêm 2 trường này
                + "FROM Bookings b "
                + "JOIN Cats c ON b.CatID = c.CatID "
                + "JOIN Owners o ON c.OwnerID = o.OwnerID "
                + "LEFT JOIN Appointment_Service ad ON b.BookingID = ad.BookingID "
                + "LEFT JOIN Services s ON ad.ServiceID = s.ServiceID "
                + " LEFT JOIN BoardingRecords br ON b.BookingID = br.BookingID "
                + "WHERE o.UserID = ? ";

        if (search != null && !search.trim().isEmpty()) {
            sql += " AND (c.Name LIKE ? OR s.NameService LIKE ?) ";
        }
        if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("ALL")) {
            sql += " AND b.Status = ? ";
        }

        sql += " ORDER BY b.AppointmentDate DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            int index = 1;
            ps.setInt(index++, userID);
            if (search != null && !search.trim().isEmpty()) {
                String p = "%" + search.trim() + "%";
                ps.setString(index++, p);
                ps.setString(index++, p);
            }
            if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("ALL")) {
                ps.setString(index++, status);
            }
            ps.setInt(index++, offset);
            ps.setInt(index++, pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BookingHistoryDTO d = new BookingHistoryDTO();
                    d.setBookingID(rs.getInt("BookingID"));
                    d.setCatName(rs.getString("CatName"));
                    d.setCatBreed(rs.getString("Breed"));
                    d.setAppointmentDate(rs.getDate("AppointmentDate"));
                    d.setAppointmentTime(rs.getTime("AppointmentTime"));
                    d.setStatus(rs.getString("Status"));
                    d.setServiceName(rs.getString("NameService")); // GÁN DỮ LIỆU
                    d.setPrice(rs.getDouble("PriceAtBooking"));
                    d.setCheckOutTime(rs.getTimestamp("CheckOutTime"));
// GÁN DỮ LIỆU
                    list.add(d);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public int countClientBookings(int userID, String search, String status) {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM Bookings b "
                + "JOIN Cats c ON b.CatID = c.CatID "
                + "JOIN Owners o ON c.OwnerID = o.OwnerID "
                + "JOIN Appointment_Service ad ON b.BookingID = ad.BookingID "
                + "JOIN Services s ON ad.ServiceID = s.ServiceID "
                + "WHERE o.UserID = ? ";

        if (search != null && !search.trim().isEmpty()) {
            sql += " AND (c.Name LIKE ? OR s.NameService LIKE ?) ";
        }
        if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("ALL")) {
            sql += " AND b.Status = ? ";
        }

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            int index = 1;
            ps.setInt(index++, userID);
            if (search != null && !search.trim().isEmpty()) {
                String p = "%" + search.trim() + "%";
                ps.setString(index++, p);
                ps.setString(index++, p);
            }
            if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("ALL")) {
                ps.setString(index++, status);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public FeedbackDTO getFeedbackByBookingID(int bookingID) {
        String sql = "SELECT FeedbackID, BookingID, Rating, Comment FROM Feedbacks WHERE BookingID = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, bookingID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    FeedbackDTO fb = new FeedbackDTO();
                    fb.setFeedbackID(rs.getInt("FeedbackID"));
                    fb.setBookingID(rs.getInt("BookingID"));
                    fb.setRating(rs.getInt("Rating"));
                    fb.setComment(rs.getString("Comment"));
                    return fb;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertFeedback(int bookingID, int rating, String comment) {
        String sql = "INSERT INTO Feedbacks (BookingID, Rating, Comment) VALUES (?, ?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, bookingID);
            ps.setInt(2, rating);
            ps.setString(3, (comment == null) ? "" : comment);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean hasCheckout(int bookingId) {
        String sql = "SELECT checkout_time FROM BoardingRecord WHERE booking_id = ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getTimestamp("checkout_time") != null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
