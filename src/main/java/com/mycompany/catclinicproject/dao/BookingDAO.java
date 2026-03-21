package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.BillingBookingDTO;
import com.mycompany.catclinicproject.model.Booking;
import com.mycompany.catclinicproject.model.BookingHistoryDTO;
import com.mycompany.catclinicproject.model.TimeSlotVet;

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


    public int createBookingWithInvoice(Booking booking,
                                        List<Integer> serviceIDs,
                                        List<Double> prices,
                                        double totalAmount) {

        int bookingID = -1;

        String lockSlotSQL =
                "SELECT Status FROM TimeSlot_Vet WITH (UPDLOCK, ROWLOCK) " +
                        "WHERE VetID=? AND SlotID=? AND Date=?";

        String insertBookingSQL =
                "INSERT INTO Bookings (CatID, VetID, SlotID, AppointmentDate, BookingDate, EndDate, AppointmentTime, Status, Note, StaffID) " +
                        "VALUES (?, ?, ?, ?, GETDATE(), ?, ?, ?, ?, ?)";

        String insertSlotSQL =
                "INSERT INTO TimeSlot_Vet (VetID, SlotID, Date, Status) VALUES (?, ?, ?, 'Booked')";

        String updateSlotSQL =
                "UPDATE TimeSlot_Vet SET Status='Booked' WHERE VetID=? AND SlotID=? AND Date=?";

        String insertDetailSQL =
                "INSERT INTO Appointment_Service (BookingID, ServiceID, PriceAtBooking) VALUES (?, ?, ?)";

        String insertInvoiceSQL =
                "INSERT INTO Invoices (BookingID, TotalAmount, PaymentStatus, CreatedDate) VALUES (?, ?, 'Unpaid', GETDATE())";

        try {

            c.setAutoCommit(false);

            // convert date an toàn cho JDBC
            java.sql.Date apptDate = new java.sql.Date(booking.getAppointmentDate().getTime());
            java.sql.Date endDate  = new java.sql.Date(booking.getEndDate().getTime());

            if (booking.getVeterinarianID() > 0 && booking.getSlotID() > 0) {

                try (PreparedStatement ps = c.prepareStatement(lockSlotSQL)) {

                    ps.setInt(1, booking.getVeterinarianID());
                    ps.setInt(2, booking.getSlotID());
                    ps.setDate(3, apptDate);

                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {

                        String status = rs.getString("Status");

                        if ("Booked".equals(status)) {
                            c.rollback();
                            return -2; // slot đã bị đặt
                        }

                        try (PreparedStatement psUpdate = c.prepareStatement(updateSlotSQL)) {

                            psUpdate.setInt(1, booking.getVeterinarianID());
                            psUpdate.setInt(2, booking.getSlotID());
                            psUpdate.setDate(3, apptDate);

                            psUpdate.executeUpdate();
                        }

                    } else {

                        try (PreparedStatement psInsert = c.prepareStatement(insertSlotSQL)) {

                            psInsert.setInt(1, booking.getVeterinarianID());
                            psInsert.setInt(2, booking.getSlotID());
                            psInsert.setDate(3, apptDate);

                            psInsert.executeUpdate();
                        }
                    }
                }
            }

            try (PreparedStatement ps = c.prepareStatement(insertBookingSQL, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, booking.getCatID());

                if (booking.getVeterinarianID() <= 0)
                    ps.setNull(2, Types.INTEGER);
                else
                    ps.setInt(2, booking.getVeterinarianID());

                if (booking.getSlotID() <= 0)
                    ps.setNull(3, Types.INTEGER);
                else
                    ps.setInt(3, booking.getSlotID());

                ps.setDate(4, apptDate);
                ps.setDate(5, endDate);
                ps.setTime(6, booking.getAppointmentTime());
                ps.setString(7, "PendingPayment");
                ps.setString(8, booking.getNote());

                if (booking.getStaffID() <= 0)
                    ps.setNull(9, Types.INTEGER);
                else
                    ps.setInt(9, booking.getStaffID());

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    bookingID = rs.getInt(1);
                }
            }

            if (bookingID == -1) {
                c.rollback();
                return -1;
            }

            try (PreparedStatement ps = c.prepareStatement(insertDetailSQL)) {

                for (int i = 0; i < serviceIDs.size(); i++) {

                    ps.setInt(1, bookingID);
                    ps.setInt(2, serviceIDs.get(i));
                    ps.setDouble(3, prices.get(i));

                    ps.addBatch();
                }

                ps.executeBatch();
            }

            try (PreparedStatement ps = c.prepareStatement(insertInvoiceSQL)) {

                ps.setInt(1, bookingID);
                ps.setDouble(2, totalAmount);

                ps.executeUpdate();
            }

            c.commit();

            System.out.println(" Booking Successfuly ID: " + bookingID);

            return bookingID;

        } catch (Exception e) {

            try { c.rollback(); } catch (Exception ex) {}

            e.printStackTrace();
            return -4;

        } finally {

            try { c.setAutoCommit(true); } catch (Exception e) {}
        }
    }

    public boolean confirmBooking(int bookingID) {

        String updateBookingSQL =
                "UPDATE Bookings SET Status = 'Confirmed' " +
                        "WHERE BookingID = ? AND Status = 'PendingPayment'";

        try {

            c.setAutoCommit(false);

            try (PreparedStatement ps = c.prepareStatement(updateBookingSQL)) {

                ps.setInt(1, bookingID);
                int rowsUpdated = ps.executeUpdate();

                if (rowsUpdated > 0) {
                    c.commit();
                    System.out.println(" Booking confirmed: ID=" + bookingID);
                    return true;
                } else {
                    c.rollback();
                    System.out.println(" Booking not found or not pending: ID=" + bookingID);
                    return false;
                }

            }

        } catch (Exception e) {

            try { c.rollback(); } catch (Exception ex) {}

            System.out.println(" ERROR in confirmBooking: " + e.getMessage());
            e.printStackTrace();

        } finally {

            try { c.setAutoCommit(true); } catch (Exception e) {}

        }

        return false;
    }




    public void autoCancelExpiredBookings() {

        try {

            c.setAutoCommit(false);


            String selectSql =
                    "SELECT BookingID, VetID, SlotID, AppointmentDate " +
                            "FROM Bookings " +
                            "WHERE Status = 'PendingPayment' " +
                            "AND DATEDIFF(MINUTE, BookingDate, GETDATE()) >= 5";

            PreparedStatement psSelect = c.prepareStatement(selectSql);
            ResultSet rs = psSelect.executeQuery();

            List<TimeSlotVet> slots = new ArrayList<>();

            while (rs.next()) {

                TimeSlotVet t = new TimeSlotVet();

                t.setVetID(rs.getInt("VetID"));
                t.setSlotID(rs.getInt("SlotID"));
                t.setDate(rs.getDate("AppointmentDate"));

                slots.add(t);
            }

            psSelect.close();


            String cancelSql =
                    "UPDATE Bookings SET Status = 'Cancelled' " +
                            "WHERE Status = 'PendingPayment' " +
                            "AND DATEDIFF(MINUTE, BookingDate, GETDATE()) >= 5";

            PreparedStatement psCancel = c.prepareStatement(cancelSql);

            psCancel.close();

            if (!slots.isEmpty()) {

                String releaseSql =
                        "UPDATE TimeSlot_Vet SET Status = 'Available' " +
                                "WHERE VetID = ? AND SlotID = ? AND Date = ?";

                PreparedStatement psRelease = c.prepareStatement(releaseSql);

                for (TimeSlotVet t : slots) {

                    psRelease.setInt(1, t.getVetID());
                    psRelease.setInt(2, t.getSlotID());
                    psRelease.setDate(3, new java.sql.Date(t.getDate().getTime()));

                    psRelease.executeUpdate();

                    System.out.println(" Slot released: VetID=" + t.getVetID() +
                            ", SlotID=" + t.getSlotID() + ", Date=" + t.getDate());
                }

                System.out.println(" Released " + slots.size() + " slots");

                psRelease.close();
            }

            c.commit();
            System.out.println(" Auto-cancel transaction completed!");

        } catch (Exception e) {

            try { c.rollback(); } catch (Exception ex) {}

            System.out.println(" ERROR in autoCancelExpiredBookings: " + e.getMessage());
            e.printStackTrace();

        } finally {

            try { c.setAutoCommit(true); } catch (Exception e) {}

        }
    }




    public boolean isCatHotelConflict(int catID, Date newStart, Date newEnd) {

        String sql =
                "SELECT 1 FROM Bookings " +
                        "WHERE CatID = ? " +
                        "AND Status IN ('PendingPayment','Confirmed') " +
                        "AND NOT (EndDate <= ? OR AppointmentDate >= ?)";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, catID);
            ps.setDate(2, newStart);
            ps.setDate(3, newEnd);

            ResultSet rs = ps.executeQuery();
            return rs.next(); // true = có trùng

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public boolean isCatBusyAtSlot(int catID,
                                   java.sql.Date appointmentDate,
                                   int slotID) {

        String sql =
                "SELECT 1 FROM Bookings " +
                        "WHERE CatID = ? " +
                        "AND AppointmentDate = ? " +
                        "AND SlotID = ? " +
                        "AND Status IN ('PendingPayment','Confirmed','Completed')";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, catID);
            ps.setDate(2, appointmentDate);
            ps.setInt(3, slotID);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public boolean isCatBusyAtTime(int catID, java.sql.Date date, java.sql.Time time) {

        String sql = "SELECT 1 FROM Bookings "
                + "WHERE CatID = ? "
                + "AND AppointmentDate = ? "
                + "AND AppointmentTime = ? "
                + "AND Status IN ('PendingPayment','Confirmed','Completed')";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, catID);
            ps.setDate(2, date);
            ps.setTime(3, time);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public int getLatestBookingID() {
        String sql = "SELECT MAX(BookingID) FROM Bookings";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public List<BillingBookingDTO> getNewBookings(int lastSeenID) {
        List<BillingBookingDTO> list = new ArrayList<>();

        String sql = "SELECT \n" +
                "    b.BookingID,\n" +
                "    u.FullName AS ownerName,\n" +
                "    u.Phone,\n" +
                "    b.AppointmentDate,\n" +
                "    b.AppointmentTime\n" +
                "FROM Bookings b\n" +
                "JOIN Cats c ON b.CatID = c.CatID\n" +
                "JOIN Owners o ON c.OwnerID = o.OwnerID\n" +
                "JOIN Users u ON o.UserID = u.UserID\n" +
                "WHERE b.BookingID > ?\n" +
                "  AND b.Status IN ('Confirmed', 'Completed')\n" +
                "ORDER BY b.BookingID DESC";

        try (
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, lastSeenID);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                BillingBookingDTO b = new BillingBookingDTO();

                b.setBookingID(rs.getInt("BookingID"));
                b.setOwnerName(rs.getString("ownerName"));
                b.setPhone(rs.getString("Phone"));
                b.setAppointmentDate(rs.getDate("AppointmentDate"));
                b.setAppointmentTime(rs.getTimestamp("AppointmentTime"));

                list.add(b);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
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

}