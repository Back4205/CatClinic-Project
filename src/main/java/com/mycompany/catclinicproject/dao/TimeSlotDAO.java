package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.TimeSlot;
import com.mycompany.catclinicproject.model.TimeSlotDTO;
import com.mycompany.catclinicproject.model.TimeSlotDetailDTO;
import com.mycompany.catclinicproject.model.VetScheduleDTO;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TimeSlotDAO extends DBContext {

    public List<TimeSlot> getSlotsNext7Days(int vetID) {

        List<TimeSlot> list = new ArrayList<>();

        String sql
                = "SELECT * FROM TimeSlots "
                + "WHERE VetID = ? "
                + "AND Date BETWEEN CAST(GETDATE() AS DATE) "
                + "AND DATEADD(DAY, 6, CAST(GETDATE() AS DATE)) "
                + "AND ( "
                + "       Date > CAST(GETDATE() AS DATE) "
                + "    OR (Date = CAST(GETDATE() AS DATE) "
                + "        AND StartTime > CAST(GETDATE() AS TIME)) "
                + "    ) "
                + "AND Status = N'Available' "
                + "ORDER BY Date, StartTime";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, vetID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TimeSlot slot = new TimeSlot();

                slot.setSlotID(rs.getInt("SlotID"));
                slot.setVetID(rs.getInt("VetID"));
                slot.setSlotDate(rs.getDate("Date"));
                slot.setStartTime(rs.getTime("StartTime"));
                slot.setEndTime(rs.getTime("EndTime"));
                slot.setStatus(rs.getString("Status"));

                list.add(slot);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Lấy slot theo ID
    public TimeSlot getSlotByID(int slotID) {

        String sql = "SELECT * FROM TimeSlots WHERE SlotID = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, slotID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                TimeSlot slot = new TimeSlot();

                slot.setSlotID(rs.getInt("SlotID"));
                slot.setVetID(rs.getInt("VetID"));
                slot.setSlotDate(rs.getDate("Date"));
                slot.setStartTime(rs.getTime("StartTime"));
                slot.setEndTime(rs.getTime("EndTime"));
                slot.setStatus(rs.getString("Status"));

                return slot;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<TimeSlot> getSlotsNext7Days1(int vetID, Date fromDate) {

        List<TimeSlot> list = new ArrayList<>();

        boolean isToday = fromDate.toLocalDate()
                .equals(java.time.LocalDate.now());

        String sql;

        if (isToday) {
            sql
                    = "SELECT * FROM TimeSlots "
                    + "WHERE VetID = ? "
                    + "AND Date BETWEEN ? AND DATEADD(DAY, 6, ?) "
                    + "AND Status = N'Available' "
                    + "AND (Date > CAST(GETDATE() AS DATE) "
                    + "     OR (Date = CAST(GETDATE() AS DATE) "
                    + "         AND StartTime > CAST(GETDATE() AS TIME))) "
                    + "ORDER BY Date, StartTime";
        } else {
            sql
                    = "SELECT * FROM TimeSlots "
                    + "WHERE VetID = ? "
                    + "AND Date BETWEEN ? AND DATEADD(DAY, 6, ?) "
                    + "AND Status = N'Available' "
                    + "ORDER BY Date, StartTime";
        }

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, vetID);
            ps.setDate(2, fromDate);
            ps.setDate(3, fromDate);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TimeSlot slot = new TimeSlot();

                slot.setSlotID(rs.getInt("SlotID"));
                slot.setVetID(rs.getInt("VetID"));
                slot.setSlotDate(rs.getDate("Date"));
                slot.setStartTime(rs.getTime("StartTime"));
                slot.setEndTime(rs.getTime("EndTime"));
                slot.setStatus(rs.getString("Status"));

                list.add(slot);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<TimeSlotDTO> getAllTimeSlots() {

        List<TimeSlotDTO> list = new ArrayList<>();

        String sql = "SELECT SlotID, StartTime, EndTime, IsActive FROM TimeSlots WHERE IsActive = 1";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                TimeSlotDTO slot = new TimeSlotDTO(
                        rs.getInt("SlotID"),
                        rs.getTime("StartTime"),
                        rs.getTime("EndTime"),
                        rs.getBoolean("IsActive")
                );

                list.add(slot);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<VetScheduleDTO> getVetSchedule(int vetID, LocalDate startDate, LocalDate endDate) {

        List<VetScheduleDTO> list = new ArrayList<>();

        String sql = "SELECT ts.SlotID, ts.StartTime, ts.EndTime, tsv.Date, "
                + "b.BookingID, u.FullName, c.Name AS CatName, "
                + "CASE "
                + "WHEN tsv.Status = 'Absent' THEN 'Absent' "
                + "WHEN b.BookingID IS NOT NULL THEN 'Booked' "
                + "ELSE 'Available' "
                + "END AS Status "
                + "FROM TimeSlot_Vet tsv "
                + "LEFT JOIN TimeSlots ts ON tsv.SlotID = ts.SlotID "
                + "LEFT JOIN Bookings b ON b.VetID = tsv.VetID "
                + "AND b.SlotID = tsv.SlotID "
                + "AND b.AppointmentDate = tsv.Date "
                + "LEFT JOIN Cats c ON b.CatID = c.CatID "
                + "LEFT JOIN Owners o ON c.OwnerID = o.OwnerID "
                + "LEFT JOIN Users u ON o.UserID = u.UserID "
                + "WHERE tsv.VetID = ? "
                + "AND tsv.Date BETWEEN ? AND ? "
                + "ORDER BY tsv.Date, ts.StartTime";;

        try {

            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, vetID);
            ps.setDate(2, java.sql.Date.valueOf(startDate));
            ps.setDate(3, java.sql.Date.valueOf(endDate));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                VetScheduleDTO slot = new VetScheduleDTO();
                slot.setSlotID(rs.getInt("SlotID"));
                slot.setStartTime(rs.getTime("StartTime").toString());
                slot.setEndTime(rs.getTime("EndTime").toString());
                slot.setDate(rs.getDate("Date"));
                slot.setBookingID(rs.getObject("BookingID") != null ? rs.getInt("BookingID") : null);
                slot.setFullName(rs.getString("FullName"));
                slot.setCatName(rs.getString("CatName"));
                slot.setStatus(rs.getString("Status"));
                list.add(slot);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int getVetIDByUserID(int userID) {

        String sql = "SELECT VetID FROM Veterinarians WHERE UserID = ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, userID);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("VetID");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public List<Integer> getBookedSlots(int vetID, Date date) {

        List<Integer> list = new ArrayList<>();

        String sql = "SELECT SlotID FROM TimeSlot_Vet "
                + "WHERE VetID = ? AND Date = ? AND Status = 'Booked'";

        try {
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setInt(1, vetID);
            ps.setDate(2, date);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getInt("SlotID"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void insertAbsent(int vetID, int slotID, Date date) {
        String sql = "INSERT INTO TimeSlot_Vet (VetID, SlotID, Date, Status) "
                + "VALUES (?, ?, ?, 'Absent')";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, vetID);
            ps.setInt(2, slotID);
            ps.setDate(3, date);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getVetForBooking(int slotID, Date date) {
        int vetID = -1;

        String sql = "SELECT TOP 1 v.VetID "
                + "FROM Veterinarians v "
                + "LEFT JOIN TimeSlot_Vet t "
                + "ON v.VetID = t.VetID "
                + "AND t.SlotID = ? "
                + "AND t.Date = ? "
                + "WHERE (t.Status = 'Available' OR t.VetID IS NULL) "
                + "AND NOT EXISTS ( "
                + "SELECT 1 FROM TimeSlot_Vet x "
                + "WHERE x.VetID = v.VetID "
                + "AND x.SlotID = ? "
                + "AND x.Date = ? "
                + "AND x.Status = 'Absent' "
                + ") "
                + "ORDER BY ( "
                + "SELECT COUNT(*) "
                + "FROM TimeSlot_Vet t2 "
                + "WHERE t2.VetID = v.VetID "
                + "AND t2.Date = ? "
                + "AND t2.Status = 'Booked' "
                + ")";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, slotID);
            ps.setDate(2, date);
            ps.setInt(3, slotID);

            ps.setDate(4, date);
            ps.setDate(5, date);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                vetID = rs.getInt("VetID");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return vetID;
    }

    public void upsertBooked(int vetID, int slotID, Date date) {

        String checkSql = "SELECT Status FROM TimeSlot_Vet "
                + "WHERE VetID = ? AND SlotID = ? AND Date = ?";

        try {
            PreparedStatement ps = c.prepareStatement(checkSql);
            ps.setInt(1, vetID);
            ps.setInt(2, slotID);
            ps.setDate(3, date);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                // chưa có record → INSERT Booked
                String insertSql = "INSERT INTO TimeSlot_Vet (VetID, SlotID, Date, Status) "
                        + "VALUES (?, ?, ?, 'Booked')";

                PreparedStatement psInsert = c.prepareStatement(insertSql);
                psInsert.setInt(1, vetID);
                psInsert.setInt(2, slotID);
                psInsert.setDate(3, date);
                psInsert.executeUpdate();

            } else {
                String status = rs.getString("Status");

                if ("Available".equals(status)) {
                    // nếu Available → UPDATE thành Booked
                    String updateSql = "UPDATE TimeSlot_Vet "
                            + "SET Status = 'Booked' "
                            + "WHERE VetID = ? AND SlotID = ? AND Date = ?";

                    PreparedStatement psUpdate = c.prepareStatement(updateSql);
                    psUpdate.setInt(1, vetID);
                    psUpdate.setInt(2, slotID);
                    psUpdate.setDate(3, date);
                    psUpdate.executeUpdate();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getBookingID(int vetID, int slotID, Date date) {
        int bookingID = -1;

        String sql = "SELECT b.BookingID "
                + "FROM Bookings b "
                + "JOIN TimeSlot_Vet tsv "
                + "ON b.VetID = tsv.VetID "
                + "AND b.SlotID = tsv.SlotID "
                + "AND b.AppointmentDate = tsv.Date "
                + "WHERE tsv.VetID = ? "
                + "AND tsv.SlotID = ? "
                + "AND tsv.Date = ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, vetID);
            ps.setInt(2, slotID);
            ps.setDate(3, date);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                bookingID = rs.getInt("BookingID");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookingID;
    }

    public boolean updateVetByBookingID(int bookingID, int newVetID) {
        String sql = "UPDATE Bookings SET VetID = ? WHERE BookingID = ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, newVetID);
            ps.setInt(2, bookingID);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public int getTotalSlots() {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM TimeSlots";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    public List<Integer> getBookedSlotsInLeave(int vetID, Date date, String[] slots) {

        List<Integer> list = new ArrayList<>();

        if (slots == null || slots.length == 0) {
            return list;
        }

        StringBuilder sql = new StringBuilder(
                "SELECT SlotID FROM TimeSlot_Vet "
                + "WHERE VetID = ? AND Date = ? "
                + "AND Status = 'Booked' AND SlotID IN ("
        );

        for (int i = 0; i < slots.length; i++) {
            sql.append("?");
            if (i < slots.length - 1) {
                sql.append(",");
            }
        }

        sql.append(")");

        try {
            PreparedStatement ps = c.prepareStatement(sql.toString());

            ps.setInt(1, vetID);
            ps.setDate(2, date);

            for (int i = 0; i < slots.length; i++) {
                ps.setInt(i + 3, Integer.parseInt(slots[i]));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getInt("SlotID"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean updateAbsent(int vetID, int slotID, Date date) {

        String sql = "UPDATE TimeSlot_Vet "
                + "SET Status = 'Absent' "
                + "WHERE VetID = ? AND SlotID = ? AND Date = ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setInt(1, vetID);
            ps.setInt(2, slotID);
            ps.setDate(3, date);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getEmailByBookingID(int bookingID) {
        String sql = "SELECT u.Email "
                + "FROM Bookings b "
                + "JOIN Cats c ON b.CatID = c.CatID "
                + "JOIN Owners o ON c.OwnerID = o.OwnerID "
                + "JOIN Users u ON o.UserID = u.UserID "
                + "WHERE b.BookingID = ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, bookingID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("Email");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<TimeSlotDetailDTO> getActiveAbsentSlots(int vetID, Date date) {
        List<TimeSlotDetailDTO> list = new ArrayList<>();
        String sql = "SELECT ts.SlotID, ts.StartTime, ts.EndTime, tsv.Date, tsv.Status, ts.IsActive "
                + "FROM TimeSlots ts "
                + "INNER JOIN TimeSlot_Vet tsv ON ts.SlotID = tsv.SlotID "
                + "WHERE tsv.VetID = ? "
                + "AND tsv.Status = 'Absent' "
                + "AND tsv.Date = ? "
                + "AND ts.IsActive = 1";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, vetID);
            ps.setDate(2, date);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TimeSlotDetailDTO dto = new TimeSlotDetailDTO(
                        rs.getInt("SlotID"),
                        rs.getTime("StartTime"),
                        rs.getTime("EndTime"),
                        rs.getDate("Date"),
                        rs.getString("Status"),
                        rs.getBoolean("IsActive")
                );
                list.add(dto);
            }
        } catch (Exception e) {
            System.out.println("Lỗi DAO: " + e.getMessage());
        }
        return list;
    }
}
