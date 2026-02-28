package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.Booking;
import com.mycompany.catclinicproject.model.BookingHistoryDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO extends DBContext {


    public List<BookingHistoryDTO> getHistoryByUserID(int userID) {

        List<BookingHistoryDTO> list = new ArrayList<>();

        String sql = "SELECT b.BookingID, c.Name AS CatName, c.Breed, "
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
                            rs.getString("CatName"),
                            rs.getString("Breed"),
                            rs.getDate("AppointmentDate"),
                            rs.getDate("EndDate"),
                            rs.getTime("AppointmentTime"),
                            rs.getString("NameService"),
                            rs.getDouble("PriceAtBooking"),
                            rs.getString("Status")
                    ));
                }
            }

        } catch (SQLException e) {

        }

        return list;
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


    // tao Booking ,  Booking detail và invoice
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
            c.setAutoCommit(false); // tắt chế độ lưu vào DB tự động . mọi thay đổi chỉ được lưu trong bộ nhớ
            //  LOCK SLOT (if medical service)
            if (booking.getSlotID() > 0) {
                try (PreparedStatement psLock = c.prepareStatement(lockSlotSQL)) {
                    psLock.setInt(1, booking.getSlotID());
                    int affected = psLock.executeUpdate();

                    if (affected == 0) {
                        c.rollback(); // hủy tất cả những thay đổi từ khi setAutoCommit(false)
                        return -1;
                    }
                }
            }

            //  tạo Bôking                                                   yêu cầu JDBC trả về các data mà DB tự sinh ra
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
            // khong tạo được booking
            if (bookingID == -1) {
                c.rollback(); // hủy transaction
                return -1;
            }

            //  tạo bookingDetail
            try (PreparedStatement psDetail = c.prepareStatement(insertDetailSQL)) {

                for (int i = 0; i < serviceIDs.size(); i++) {
                    psDetail.setInt(1, bookingID);
                    psDetail.setInt(2, serviceIDs.get(i));
                    psDetail.setDouble(3, prices.get(i));
                    psDetail.addBatch(); // Thu gom vào batch để insert nhiều dòng 1 lần
                }

                psDetail.executeBatch();// Thực thi toàn bộ batch cùng lúc
            }

            //  tạo hóa đơn
            try (PreparedStatement psInvoice = c.prepareStatement(insertInvoiceSQL)) {
                psInvoice.setInt(1, bookingID);
                psInvoice.setDouble(2, totalAmount);
                psInvoice.setString(3, "Unpaid");
                psInvoice.executeUpdate();
            }

            c.commit(); // update toàn bộ thay đổi lên trên DB

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