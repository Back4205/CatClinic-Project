package com.mycompany.catclinicproject.dao;


import com.mycompany.catclinicproject.model.BillingBookingDTO;
import com.mycompany.catclinicproject.model.BoardingRecordDTO;
import com.mycompany.catclinicproject.model.Booking;
import com.mycompany.catclinicproject.model.BookingHistory2DTO;
import com.mycompany.catclinicproject.model.BookingHistoryDTO;
import com.mycompany.catclinicproject.model.CancelBookingDTO;
import com.mycompany.catclinicproject.model.FeedbackDTO;
import com.mycompany.catclinicproject.model.TimeSlotVet;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;


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
                            null, "", "", "" // 4 tham số bổ sung cho DTO
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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


        String lockSlotSQL
                = "SELECT Status FROM TimeSlot_Vet WITH (UPDLOCK, ROWLOCK) "
                + "WHERE VetID=? AND SlotID=? AND Date=?";


        String insertBookingSQL
                = "INSERT INTO Bookings (CatID, VetID, SlotID, AppointmentDate, BookingDate, EndDate, AppointmentTime, Status, Note, StaffID) "
                + "VALUES (?, ?, ?, ?, GETDATE(), ?, ?, ?, ?, ?)";


        String insertSlotSQL
                = "INSERT INTO TimeSlot_Vet (VetID, SlotID, Date, Status) VALUES (?, ?, ?, 'Booked')";


        String updateSlotSQL
                = "UPDATE TimeSlot_Vet SET Status='Booked' WHERE VetID=? AND SlotID=? AND Date=?";


        String insertDetailSQL
                = "INSERT INTO Appointment_Service (BookingID, ServiceID, PriceAtBooking) VALUES (?, ?, ?)";


        String insertInvoiceSQL
                = "INSERT INTO Invoices (BookingID, TotalAmount, PaymentStatus, CreatedDate) VALUES (?, ?, 'Unpaid', GETDATE())";


        try {


            c.setAutoCommit(false);


            // convert date an toàn cho JDBC
            java.sql.Date apptDate = new java.sql.Date(booking.getAppointmentDate().getTime());
            java.sql.Date endDate = new java.sql.Date(booking.getEndDate().getTime());


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


                if (booking.getVeterinarianID() <= 0) {
                    ps.setNull(2, Types.INTEGER);
                } else {
                    ps.setInt(2, booking.getVeterinarianID());
                }


                if (booking.getSlotID() <= 0) {
                    ps.setNull(3, Types.INTEGER);
                } else {
                    ps.setInt(3, booking.getSlotID());
                }


                ps.setDate(4, apptDate);
                ps.setDate(5, endDate);
                ps.setTime(6, booking.getAppointmentTime());
                ps.setString(7, "PendingPayment");
                ps.setString(8, booking.getNote());


                if (booking.getStaffID() <= 0) {
                    ps.setNull(9, Types.INTEGER);
                } else {
                    ps.setInt(9, booking.getStaffID());
                }


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


            try {
                c.rollback();
            } catch (Exception ex) {
            }


            e.printStackTrace();
            return -4;


        } finally {


            try {
                c.setAutoCommit(true);
            } catch (Exception e) {
            }
        }
    }
    public boolean confirmBooking2(int bookingID) {


        String updateBookingSQL
                = "UPDATE Bookings SET Status = 'Completed' "
                + "WHERE BookingID = ? AND Status = 'PendingPayment'";


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


            try {
                c.rollback();
            } catch (Exception ex) {
            }


            System.out.println(" ERROR in confirmBooking: " + e.getMessage());
            e.printStackTrace();


        } finally {


            try {
                c.setAutoCommit(true);
            } catch (Exception e) {
            }


        }


        return false;
    }
    public boolean confirmBooking(int bookingID) {


        String updateBookingSQL
                = "UPDATE Bookings SET Status = 'Confirmed' "
                + "WHERE BookingID = ? AND Status = 'PendingPayment'";


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


            try {
                c.rollback();
            } catch (Exception ex) {
            }


            System.out.println(" ERROR in confirmBooking: " + e.getMessage());
            e.printStackTrace();


        } finally {


            try {
                c.setAutoCommit(true);
            } catch (Exception e) {
            }


        }


        return false;
    }


    public void autoCancelExpiredBookings() {


        try {


            c.setAutoCommit(false);


            String selectSql
                    = "SELECT BookingID, VetID, SlotID, AppointmentDate "
                    + "FROM Bookings "
                    + "WHERE Status = 'PendingPayment' "
                    + "AND DATEDIFF(MINUTE, BookingDate, GETDATE()) >= 5";


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


            String cancelSql
                    = "UPDATE Bookings SET Status = 'Cancelled' "
                    + "WHERE Status = 'PendingPayment' "
                    + "AND DATEDIFF(MINUTE, BookingDate, GETDATE()) >= 5";


            PreparedStatement psCancel = c.prepareStatement(cancelSql);


            psCancel.close();


            if (!slots.isEmpty()) {


                String releaseSql
                        = "UPDATE TimeSlot_Vet SET Status = 'Available' "
                        + "WHERE VetID = ? AND SlotID = ? AND Date = ?";


                PreparedStatement psRelease = c.prepareStatement(releaseSql);


                for (TimeSlotVet t : slots) {


                    psRelease.setInt(1, t.getVetID());
                    psRelease.setInt(2, t.getSlotID());
                    psRelease.setDate(3, new java.sql.Date(t.getDate().getTime()));


                    psRelease.executeUpdate();


                    System.out.println(" Slot released: VetID=" + t.getVetID()
                            + ", SlotID=" + t.getSlotID() + ", Date=" + t.getDate());
                }


                System.out.println(" Released " + slots.size() + " slots");


                psRelease.close();
            }


            c.commit();
            System.out.println(" Auto-cancel transaction completed!");


        } catch (Exception e) {


            try {
                c.rollback();
            } catch (Exception ex) {
            }


            System.out.println(" ERROR in autoCancelExpiredBookings: " + e.getMessage());
            e.printStackTrace();


        } finally {


            try {
                c.setAutoCommit(true);
            } catch (Exception e) {
            }


        }
    }


    public boolean isCatHotelConflict(int catID, Date newStart, Date newEnd) {


        String sql
                = "SELECT 1 FROM Bookings "
                + "WHERE CatID = ? "
                + "AND Status IN ('PendingPayment','Confirmed') "
                + "AND NOT (EndDate <= ? OR AppointmentDate >= ?)";


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


        String sql
                = "SELECT 1 FROM Bookings "
                + "WHERE CatID = ? "
                + "AND AppointmentDate = ? "
                + "AND SlotID = ? "
                + "AND Status IN ('PendingPayment','Confirmed','Completed')";


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
                + "AND CAST(AppointmentTime AS TIME) = CAST(? AS TIME) "
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


        String sql = "SELECT \n"
                + "    b.BookingID,\n"
                + "    u.FullName AS ownerName,\n"
                + "    u.Phone,\n"
                + "    b.AppointmentDate,\n"
                + "    b.AppointmentTime\n"
                + "FROM Bookings b\n"
                + "JOIN Cats c ON b.CatID = c.CatID\n"
                + "JOIN Owners o ON c.OwnerID = o.OwnerID\n"
                + "JOIN Users u ON o.UserID = u.UserID\n"
                + "WHERE b.BookingID > ?\n"
                + "  AND b.Status IN ('Confirmed', 'Completed')\n"
                + "ORDER BY b.BookingID DESC";


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





    public boolean updateStatusPendingCancelRefund(int bookingID) {
        String sql = "UPDATE Bookings SET Status = ? WHERE BookingID = ?";


        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, "PendingCancel");
            ps.setInt(2, bookingID);


            return ps.executeUpdate() > 0;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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


    public boolean updateCheckoutStatus(int bookingId, String condition) {


        String sql = "UPDATE BoardingRecords "
                + "SET CheckOutTime = GETDATE(), "
                + "    CheckOutCondition = ? "
                + "WHERE BookingID = ? AND CheckOutTime IS NULL";


        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, (condition == null || condition.trim().isEmpty()) ? "Healthy" : condition);
            ps.setInt(2, bookingId);


            int rowAffected = ps.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public int countCheckoutQueue(String search, String dateFilter) {
        int total = 0;
        String sql = "SELECT COUNT(DISTINCT b.BookingID) FROM Bookings b "
                + "JOIN BoardingRecords br ON b.BookingID = br.BookingID "
                + "LEFT JOIN MedicalRecords m ON b.BookingID = m.BookingID "
                + "LEFT JOIN TestOrders t ON m.MedicalRecordID = t.MedicalRecordID "
                + "LEFT JOIN Staffs s ON b.StaffID = s.StaffID "
                + "JOIN Cats c ON b.CatID = c.CatID "
                + "JOIN Users u ON (SELECT OwnerID FROM Cats WHERE CatID = b.CatID) = u.UserID "
                + "WHERE br.CheckInTime IS NOT NULL AND br.CheckOutTime IS NULL "
                + "AND CAST(br.CheckInTime AS DATE) = ? ";


        if (search != null && !search.trim().isEmpty()) {
            sql += " AND (c.Name LIKE ? OR u.Phone LIKE ?)";
        }


        sql += " AND ((b.VetID IS NOT NULL AND m.Status = 'Completed') OR (s.Position IN ('Technician','Care') AND t.Status = 'Completed'))";


        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, dateFilter);
            if (search != null && !search.trim().isEmpty()) {
                String p = "%" + search.trim() + "%";
                ps.setString(2, p);
                ps.setString(3, p);
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


    public List<BookingHistory2DTO> getCheckoutPaging(String search, String dateFilter, int offset, int pageSize) {
        List<BookingHistory2DTO> list = new ArrayList<>();
        String sql = "SELECT DISTINCT b.BookingID, c.Name AS CatName, u.FullName AS OwnerName, u.Phone AS OwnerPhone, br.CheckInTime "
                + "FROM Bookings b "
                + "JOIN Cats c ON b.CatID = c.CatID "
                + "JOIN Owners o ON c.OwnerID = o.OwnerID "
                + "JOIN Users u ON o.UserID = u.UserID "
                + "JOIN BoardingRecords br ON b.BookingID = br.BookingID "
                + "LEFT JOIN MedicalRecords m ON b.BookingID = m.BookingID "
                + "LEFT JOIN TestOrders t ON m.MedicalRecordID = t.MedicalRecordID "
                + "LEFT JOIN Staffs s ON b.StaffID = s.StaffID "
                + " LEFT JOIN CareJourneys cj ON b.BookingID = cj.BookingID "
                + "WHERE br.CheckInTime IS NOT NULL AND br.CheckOutTime IS NULL "
                + "AND CAST(br.CheckInTime AS DATE) = ? ";


        if (search != null && !search.trim().isEmpty()) {
            sql += " AND (c.Name LIKE ? OR u.Phone LIKE ?)";
        }


        sql += "  AND (\n" +
                "        (b.VetID IS NOT NULL AND m.Status = 'Completed') \n" +
                "        OR \n" +
                "        (s.Position IN ('Technician') AND t.Status = 'Completed')\n" +
                "\t\t OR \n" +
                "        (s.Position IN ('Care') AND cj.Status = 'Completed')\n" +
                "    ) ";
        sql += " ORDER BY br.CheckInTime DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";


        try (PreparedStatement ps = c.prepareStatement(sql)) {
            int idx = 1;
            ps.setString(idx++, dateFilter);
            if (search != null && !search.trim().isEmpty()) {
                String p = "%" + search.trim() + "%";
                ps.setString(idx++, p);
                ps.setString(idx++, p);
            }
            ps.setInt(idx++, offset);
            ps.setInt(idx++, pageSize);


            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BookingHistory2DTO b = new BookingHistory2DTO();
                b.setBookingID(rs.getInt("BookingID"));
                b.setCatName(rs.getString("CatName"));
                b.setOwnerName(rs.getString("OwnerName"));
                b.setOwnerPhone(rs.getString("OwnerPhone"));
                b.setCheckInTime(rs.getTimestamp("CheckInTime"));
                list.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public int countTotalBookings(String dateFilter) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Bookings WHERE CAST(AppointmentDate AS DATE) = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, dateFilter);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }


    public boolean checkInWithRecord(int bookingID, String condition) {
        String sqlUpdate = "UPDATE Bookings SET Status = 'Completed' WHERE BookingID = ?";


        String sqlInsert = "INSERT INTO BoardingRecords (BookingID, CheckInTime, CheckInCondition) VALUES (?, GETDATE(), ?)";


        try {
            c.setAutoCommit(false);


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


    public List<BookingHistoryDTO> getBookingHistory(String search, String status, String dateFilter, int offset, int pageSize) {
        List<BookingHistoryDTO> list = new ArrayList<>();
        String sql
                = "SELECT "
                + "b.BookingID, b.SlotID, c.Name AS CatName, c.Breed, "
                + "b.AppointmentDate, b.EndDate, b.AppointmentTime, b.Status, b.Note, "
                + "u.FullName AS OwnerName, u.Phone AS OwnerPhone, "
                + "vetUser.FullName AS VetName, s.NameService "
                + "FROM Bookings b "
                + "JOIN Cats c ON b.CatID = c.CatID "
                + "JOIN Owners o ON c.OwnerID = o.OwnerID "
                + "JOIN Users u ON o.UserID = u.UserID "
                + "LEFT JOIN Veterinarians v ON b.VetID = v.VetID "
                + "LEFT JOIN Users vetUser ON v.UserID = vetUser.UserID "
                + "LEFT JOIN Appointment_Service ad ON b.BookingID = ad.BookingID "
                + "LEFT JOIN Services s ON ad.ServiceID = s.ServiceID "
                + "WHERE 1=1 ";


        if (search != null && !search.trim().isEmpty()) {
            sql += " AND (c.Name LIKE ? OR u.FullName LIKE ? OR u.Phone LIKE ?) ";
        }
        if (status != null && !status.trim().isEmpty()) {
            sql += " AND b.Status = ? ";
        }
        if (dateFilter != null && !dateFilter.trim().isEmpty()) {
            sql += " AND CAST(b.AppointmentDate AS DATE) = ? ";
        }


        sql += " ORDER BY b.AppointmentDate DESC, b.AppointmentTime DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";


        try (PreparedStatement ps = c.prepareStatement(sql)) {
            int index = 1;
            if (search != null && !search.trim().isEmpty()) {
                String p = "%" + search.trim() + "%";
                ps.setString(index++, p);
                ps.setString(index++, p);
                ps.setString(index++, p);
            }
            if (status != null && !status.trim().isEmpty()) {
                ps.setString(index++, status);
            }
            if (dateFilter != null && !dateFilter.trim().isEmpty()) {
                ps.setString(index++, dateFilter);
            }
            ps.setInt(index++, offset);
            ps.setInt(index++, pageSize);


            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BookingHistoryDTO dto = new BookingHistoryDTO();
                    dto.setBookingID(rs.getInt("BookingID"));
                    dto.setCatName(rs.getString("CatName"));
                    dto.setCatBreed(rs.getString("Breed"));
                    dto.setAppointmentDate(rs.getDate("AppointmentDate"));
                    dto.setAppointmentTime(rs.getTime("AppointmentTime"));
                    dto.setServiceName(rs.getString("NameService"));
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


    public int countBookings(String search, String status, String dateFilter) {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM Bookings b "
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
        if (dateFilter != null && !dateFilter.trim().isEmpty()) {
            sql += " AND CAST(b.AppointmentDate AS DATE) = ? ";
        }


        try (PreparedStatement ps = c.prepareStatement(sql)) {
            int index = 1;
            if (search != null && !search.trim().isEmpty()) {
                String p = "%" + search.trim() + "%";
                ps.setString(index++, p);
                ps.setString(index++, p);
                ps.setString(index++, p);
            }
            if (status != null && !status.trim().isEmpty()) {
                ps.setString(index++, status);
            }
            if (dateFilter != null && !dateFilter.trim().isEmpty()) {
                ps.setString(index++, dateFilter);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }
    public int countPendingPayment(String dateFilter) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Bookings WHERE Status = 'PendingPayment' AND CAST(AppointmentDate AS DATE) = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, dateFilter);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) count = rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }
    public int countConfirmed(String dateFilter) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Bookings WHERE Status = 'Confirmed' AND CAST(AppointmentDate AS DATE) = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, dateFilter);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) count = rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }
    public int countCompleted(String dateFilter) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Bookings WHERE Status = 'Completed' AND CAST(AppointmentDate AS DATE) = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, dateFilter);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) count = rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }
    public int countCancelled(String dateFilter) {
        int count = 0;


        String sql = "SELECT COUNT(*) FROM Bookings WHERE Status IN ('Cancelled', 'CancelRefund', 'RejectedCancelRefund', 'RejectedCancel') AND CAST(AppointmentDate AS DATE) = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, dateFilter);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) count = rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }    public int countPendingCancelRefund(String dateFilter) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Bookings WHERE Status = 'PendingCancel' AND CAST(AppointmentDate AS DATE) = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, dateFilter);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) count = rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return count;
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
    public List<BookingHistoryDTO> getClientHistoryPaging(int userID, String search, String status, String dateFilter, int offset, int pageSize) {
        List<BookingHistoryDTO> list = new ArrayList<>();
        String sql = "SELECT DISTINCT b.BookingID, b.SlotID, c.Name AS CatName, c.Breed, b.AppointmentDate, b.EndDate, "
                + "b.AppointmentTime, b.Status, b.Note, s.NameService, ad.PriceAtBooking, br.CheckOutTime "
                + "FROM Bookings b "
                + "JOIN Cats c ON b.CatID = c.CatID "
                + "JOIN Owners o ON c.OwnerID = o.OwnerID "
                + "LEFT JOIN Appointment_Service ad ON b.BookingID = ad.BookingID "
                + "LEFT JOIN Services s ON ad.ServiceID = s.ServiceID "
                + "LEFT JOIN BoardingRecords br ON b.BookingID = br.BookingID "
                + "WHERE o.UserID = ? ";


        if (search != null && !search.trim().isEmpty()) {
            sql += " AND (c.Name LIKE ? OR s.NameService LIKE ?) ";
        }
        if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("ALL")) {
            sql += " AND b.Status = ? ";
        }
        if (dateFilter != null && !dateFilter.trim().isEmpty()) {
            sql += " AND b.AppointmentDate = ? ";
        }


        sql += " ORDER BY b.BookingID DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";


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
            if (dateFilter != null && !dateFilter.trim().isEmpty()) {
                ps.setString(index++, dateFilter);
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
                    d.setServiceName(rs.getString("NameService"));
                    d.setPrice(rs.getDouble("PriceAtBooking"));
                    d.setCheckOutTime(rs.getTimestamp("CheckOutTime"));
                    list.add(d);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public int countClientBookings(int userID, String search, String status, String dateFilter) {
        int total = 0;
        String sql = "SELECT COUNT(DISTINCT b.BookingID) FROM Bookings b "
                + "JOIN Cats c ON b.CatID = c.CatID "
                + "JOIN Owners o ON c.OwnerID = o.OwnerID "
                + "LEFT JOIN Appointment_Service ad ON b.BookingID = ad.BookingID "
                + "LEFT JOIN Services s ON ad.ServiceID = s.ServiceID "
                + "WHERE o.UserID = ? ";




        if (search != null && !search.trim().isEmpty()) {
            sql += " AND (c.Name LIKE ? OR s.NameService LIKE ?) ";
        }


        if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("ALL")) {
            sql += " AND b.Status = ? ";
        }


        if (dateFilter != null && !dateFilter.trim().isEmpty()) {
            sql += " AND b.AppointmentDate = ? ";
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
            if (dateFilter != null && !dateFilter.trim().isEmpty()) {
                ps.setString(index++, dateFilter);
            }


            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    public boolean isNotCheckedOut(int bookingId) {
        String sql = "SELECT CheckOutTime FROM BoardingRecords WHERE BookingID = ?";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getTimestamp("CheckOutTime") == null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
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
    public BookingHistoryDTO getBookingDetailByID2(int bookingID) {
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
    public List<CancelBookingDTO> getAllCancelBookings() {
        List<CancelBookingDTO> list = new ArrayList<>();


        String sql = "SELECT "
                + "    b.BookingID, "
                + "    s.NameService, "
                + "    b.Status, "
                + "    b.Note, "
                + "    b.RefundDate, " // Added this
                + "    a.PriceAtBooking "
                + "FROM Bookings b "
                + "INNER JOIN Appointment_Service a "
                + "    ON b.BookingID = a.BookingID "
                + "INNER JOIN Services s "
                + "    ON a.ServiceID = s.ServiceID "
                + "WHERE b.Status IN ('PendingCancel', 'CancelRefund', 'RejectedCancelRefund') "
                + "ORDER BY "
                + "    CASE "
                + "        WHEN b.Status = 'PendingCancel' THEN 1 "
                + "        WHEN b.Status = 'CancelRefund' THEN 2 "
                + "        WHEN b.Status = 'RejectedCancelRefund' THEN 3 "
                + "    END, "
                + "    b.BookingID DESC;";


        try (PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {


            while (rs.next()) {
                // Passing all 6 parameters to match your DTO constructor
                CancelBookingDTO dto = new CancelBookingDTO(
                        rs.getInt("BookingID"),
                        rs.getString("NameService"),
                        rs.getString("Status"),
                        rs.getString("Note"),
                        rs.getDate("RefundDate"), // Mapped to DTO refundDate
                        rs.getDouble("PriceAtBooking")
                );
                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public CancelBookingDTO getPendingCancelBookingById(int bookingID) {
        String sql = "SELECT "
                + "    b.BookingID, "
                + "    s.NameService, "
                + "    b.Status, "
                + "    b.Note, "
                + "    b.RefundDate, " // Added this
                + "    a.PriceAtBooking "
                + "FROM Bookings b "
                + "INNER JOIN Appointment_Service a "
                + "    ON b.BookingID = a.BookingID "
                + "INNER JOIN Services s "
                + "    ON a.ServiceID = s.ServiceID "
                + "WHERE b.Status = 'PendingCancel' AND b.BookingID = ?";


        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, bookingID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CancelBookingDTO(
                            rs.getInt("BookingID"),
                            rs.getString("NameService"),
                            rs.getString("Status"),
                            rs.getString("Note"),
                            rs.getDate("RefundDate"),
                            rs.getDouble("PriceAtBooking")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void rejectRefund(int bookingID) {
        String sql = "UPDATE Bookings SET Status = 'RejectedCancelRefund', "
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
        // Thêm RefundDate = GETDATE() vào câu lệnh UPDATE
        String sql = "UPDATE Bookings SET Status = 'CancelRefund', "
                + "Note = ?, "
                + "RefundDate = GETDATE() " // Cập nhật ngày giờ hiện tại
                + "WHERE BookingID = ?";


        try {
            // Sử dụng kết nối 'c' từ DBContext
            if (c != null) {
                PreparedStatement ps = c.prepareStatement(sql);
                ps.setString(1, imageUrl);
                ps.setInt(2, bookingID);

                ps.executeUpdate();
                ps.close();
            }
        } catch (SQLException e) {
            System.out.println("Lỗi tại approveRefund: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public double getTotalRefundedThisMonth() {
        double total = 0;
        String sql = "SELECT SUM(p.PriceAtBooking) AS TotalMonth "
                + "FROM Bookings B "
                + "JOIN Appointment_Service p ON B.BookingID = p.BookingID "
                + "WHERE B.Status = 'CancelRefund' "
                + "  AND MONTH(B.RefundDate) = MONTH(GETDATE()) "
                + "  AND YEAR(B.RefundDate) = YEAR(GETDATE())";
        try {
            Connection conn = getConnection();
            if (conn != null) {
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    // Bây giờ rs mới tìm thấy cột có tên "TotalMonth"
                    total = rs.getDouble("TotalMonth");
                }
                rs.close();
                ps.close();
            }
        } catch (Exception e) {
            System.out.println("Lỗi tại getTotalRefundedThisMonth: " + e.getMessage());
            e.printStackTrace();
        }
        return total;
    }
}
