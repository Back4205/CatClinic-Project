package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.Booking;
import com.mycompany.catclinicproject.model.BookingHistoryDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookingDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(BookingDAO.class.getName());

    // =========================================================
    // GET BOOKING HISTORY BY USER
    // =========================================================
    public List<BookingHistoryDTO> getHistoryByUserID(int userID) {

        List<BookingHistoryDTO> list = new ArrayList<>();

        String sql = "SELECT b.BookingID, c.Name AS CatName, c.Breed, "
                + "b.AppointmentDate, b.EndDate, b.AppointmentTime, b.Status, "
                + "s.ServiceName, bd.PriceAtBooking "
                + "FROM Bookings b "
                + "JOIN Cats c ON b.CatID = c.CatID "
                + "JOIN Owners o ON c.OwnerID = o.OwnerID "
                + "JOIN BookingDetails bd ON b.BookingID = bd.BookingID "
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
                            rs.getString("ServiceName"),
                            rs.getDouble("PriceAtBooking"),
                            rs.getString("Status")
                    ));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getHistoryByUserID", e);
        }

        return list;
    }


    // =========================================================
// GET CAT ID BY BOOKING ID
// =========================================================
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
            LOGGER.log(Level.SEVERE, "Error getCatIdByBookingID", e);
        }

        return catID;
    }

    // =========================================================
    // CREATE BOOKING + DETAILS + INVOICE (TRANSACTION SAFE)
    // =========================================================
    public int createBookingWithInvoice(Booking booking,
                                        List<Integer> serviceIDs,
                                        List<Double> prices,
                                        double totalAmount) {

        int bookingID = -1;

        String lockSlotSQL = "UPDATE TimeSlots SET Status = N'Pending' "
                + "WHERE SlotID = ? AND Status = N'Available'";

        String insertBookingSQL = "INSERT INTO Bookings "
                + "(CatID, VetID, StaffID, SlotID, AppointmentDate, EndDate, AppointmentTime, Status, Note) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String insertDetailSQL = "INSERT INTO BookingDetails "
                + "(BookingID, ServiceID, PriceAtBooking) VALUES (?, ?, ?)";

        String insertInvoiceSQL = "INSERT INTO Invoices "
                + "(BookingID, TotalAmount, PaymentStatus) VALUES (?, ?, ?)";

        try {
            c.setAutoCommit(false);

            // 1️⃣ LOCK SLOT (if medical service)
            if (booking.getSlotID() > 0) {
                try (PreparedStatement psLock = c.prepareStatement(lockSlotSQL)) {
                    psLock.setInt(1, booking.getSlotID());
                    int affected = psLock.executeUpdate();

                    if (affected == 0) {
                        c.rollback();
                        return -1; // Slot already taken
                    }
                }
            }

            // 2️⃣ INSERT BOOKING
            try (PreparedStatement ps = c.prepareStatement(insertBookingSQL, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, booking.getCatID());
                ps.setObject(2, booking.getVeterinarianID() <= 0 ? null : booking.getVeterinarianID());
                ps.setObject(3, booking.getStaffID() <= 0 ? null : booking.getStaffID());
                ps.setObject(4, booking.getSlotID() <= 0 ? null : booking.getSlotID());
                ps.setDate(5, new java.sql.Date(booking.getAppointmentDate().getTime()));
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

            // 3️⃣ INSERT BOOKING DETAILS (MULTI SERVICE)
            try (PreparedStatement psDetail = c.prepareStatement(insertDetailSQL)) {

                for (int i = 0; i < serviceIDs.size(); i++) {
                    psDetail.setInt(1, bookingID);
                    psDetail.setInt(2, serviceIDs.get(i));
                    psDetail.setDouble(3, prices.get(i));
                    psDetail.addBatch();
                }

                psDetail.executeBatch();
            }

            // 4️⃣ INSERT INVOICE
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
                LOGGER.log(Level.SEVERE, "Rollback failed", ex);
            }
            LOGGER.log(Level.SEVERE, "Error createBookingWithInvoice", e);
            bookingID = -1;

        } finally {
            try {
                c.setAutoCommit(true);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Reset autoCommit failed", e);
            }
        }

        return bookingID;
    }

    // =========================================================
    // CONFIRM BOOKING AFTER SUCCESS PAYMENT
    // =========================================================
    public boolean confirmBooking(int bookingID, int slotID) {

        String updateBookingSQL = "UPDATE Bookings SET Status = N'Confirmed' WHERE BookingID = ?";
        String updateSlotSQL = "UPDATE TimeSlots SET Status = N'Booked' WHERE SlotID = ?";

        try {
            c.setAutoCommit(false);

            try (PreparedStatement ps1 = c.prepareStatement(updateBookingSQL)) {
                ps1.setInt(1, bookingID);
                ps1.executeUpdate();
            }

            if (slotID > 0) {
                try (PreparedStatement ps2 = c.prepareStatement(updateSlotSQL)) {
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
                LOGGER.log(Level.SEVERE, "Rollback failed", ex);
            }
            LOGGER.log(Level.SEVERE, "Error confirmBooking", e);
        } finally {
            try {
                c.setAutoCommit(true);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Reset autoCommit failed", e);
            }
        }

        return false;
    }

    // =========================================================
    // CANCEL BOOKING (RELEASE SLOT)
    // =========================================================
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
                LOGGER.log(Level.SEVERE, "Rollback failed", ex);
            }
            LOGGER.log(Level.SEVERE, "Error cancelBooking", e);
        } finally {
            try {
                c.setAutoCommit(true);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Reset autoCommit failed", e);
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

            // 1️⃣ Lấy các slot của booking quá 5 phút
            String selectSql = " SELECT SlotID FROM Bookings WHERE Status = 'PendingPayment' AND DATEDIFF(MINUTE, BookingDate, GETDATE()) >= 5 AND SlotID IS NOT NULL ";

            PreparedStatement psSelect = c.prepareStatement(selectSql);
            ResultSet rs = psSelect.executeQuery();

            List<Integer> slotIds = new ArrayList<>();

            while (rs.next()) {
                slotIds.add(rs.getInt("SlotID"));
            }

            // 2️⃣ Cancel booking
            String cancelSql = " UPDATE Bookings SET Status = 'Cancelled' WHERE Status = 'PendingPayment' AND DATEDIFF(MINUTE, BookingDate, GETDATE()) >= 5";


            PreparedStatement psCancel = c.prepareStatement(cancelSql);
            psCancel.executeUpdate();

            // 3️⃣ Nhả slot
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


}