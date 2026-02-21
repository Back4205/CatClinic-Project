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

    // =========================
    // GET BOOKING HISTORY
    // =========================
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

    // =========================
    // GET CAT ID BY BOOKING ID
    // =========================
    public int getCatIdByBookingID(int bookingID) {
        String sql = "SELECT CatID FROM Bookings WHERE BookingID = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, bookingID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("CatID");
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getCatIdByBookingID", e);
        }

        return -1;
    }

    // =========================
    // CREATE BOOKING
    // =========================
    public int createBooking(Booking booking) {

        String sql = "INSERT INTO Bookings "
                + "(CatID, VetID, StaffID, SlotID, AppointmentDate, EndDate, AppointmentTime, Status, Note) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, booking.getCatID());
            ps.setInt(2, booking.getVeterinarianID());
            ps.setInt(3, booking.getStaffID());
            ps.setInt(4, booking.getSlotID());
            ps.setDate(5, new java.sql.Date(booking.getAppointmentDate().getTime()));
            ps.setDate(6, new java.sql.Date(booking.getEndDate().getTime()));
            ps.setTime(7, booking.getAppointmentTime());
            ps.setString(8, "PendingPayment");
            ps.setString(9, booking.getStatus());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error createBooking", e);
        }

        return -1;
    }

    // =========================
    // CHECK SLOT AVAILABLE
    // =========================
    public boolean checkSlotAvailable(int slotID) {

        String sql = "SELECT Status FROM TimeSlots WHERE SlotID = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, slotID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return "Available".equals(rs.getString("Status"));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checkSlotAvailable", e);
        }

        return false;
    }

    // =========================
    // UPDATE BOOKING STATUS
    // =========================
    public boolean updateBookingStatus(int bookingID, String status) {

        String sql = "UPDATE Bookings SET Status = ? WHERE BookingID = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, bookingID);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updateBookingStatus", e);
        }

        return false;
    }

    // =========================
    // LOCK SLOT
    // =========================
    public boolean lockSlot(int slotID) {

        String sql = "UPDATE TimeSlots SET Status = N'Booked' "
                + "WHERE SlotID = ? AND Status = N'Available'";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, slotID);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error lockSlot", e);
        }

        return false;
    }

    // =========================
    // RELEASE SLOT
    // =========================
    public boolean releaseSlot(int slotID) {

        String sql = "UPDATE TimeSlots SET Status = N'Available' WHERE SlotID = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, slotID);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error releaseSlot", e);
        }

        return false;
    }
}