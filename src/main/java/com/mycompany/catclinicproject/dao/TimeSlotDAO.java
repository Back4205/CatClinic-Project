package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.TimeSlot;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class TimeSlotDAO extends DBContext {

    // 1️⃣ Lấy slot trống của bác sĩ trong 7 ngày tới (từ hôm nay)
    public List<TimeSlot> getSlotsNext7Days(int vetID) {

        List<TimeSlot> list = new ArrayList<>();

        String sql =
                "SELECT * FROM TimeSlots " +
                        "WHERE VetID = ? " +
                        "AND SlotDate BETWEEN CAST(GETDATE() AS DATE) " +
                        "AND DATEADD(DAY, 6, CAST(GETDATE() AS DATE)) " +
                        "AND ( " +
                        "       SlotDate > CAST(GETDATE() AS DATE) " +
                        "    OR (SlotDate = CAST(GETDATE() AS DATE) " +
                        "        AND StartTime > CAST(GETDATE() AS TIME)) " +
                        "    ) " +
                        "AND Status = N'Available' " +
                        "ORDER BY SlotDate, StartTime";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, vetID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TimeSlot slot = new TimeSlot();

                slot.setSlotID(rs.getInt("SlotID"));
                slot.setVetID(rs.getInt("VetID"));
                slot.setSlotDate(rs.getDate("SlotDate"));
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

    // 2️⃣ Lấy slot trống theo ngày cụ thể
    public List<TimeSlot> getSlotsByDoctorAndDate(int vetID, Date date) {

        List<TimeSlot> list = new ArrayList<>();

        String sql =
                "SELECT * FROM TimeSlots " +
                        "WHERE VetID = ? " +
                        "AND SlotDate = ? " +
                        "AND ( " +
                        "       SlotDate > CAST(GETDATE() AS DATE) " +
                        "    OR (SlotDate = CAST(GETDATE() AS DATE) " +
                        "        AND StartTime > CAST(GETDATE() AS TIME)) " +
                        "    ) " +
                        "AND Status = N'Available' " +
                        "ORDER BY StartTime";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, vetID);
            ps.setDate(2, date);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TimeSlot slot = new TimeSlot();

                slot.setSlotID(rs.getInt("SlotID"));
                slot.setVetID(rs.getInt("VetID"));
                slot.setSlotDate(rs.getDate("SlotDate"));
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

    // 3️⃣ Lấy slot theo ID
    public TimeSlot getSlotByID(int slotID) {

        String sql = "SELECT * FROM TimeSlots WHERE SlotID = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, slotID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                TimeSlot slot = new TimeSlot();

                slot.setSlotID(rs.getInt("SlotID"));
                slot.setVetID(rs.getInt("VetID"));
                slot.setSlotDate(rs.getDate("SlotDate"));
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

    // 4️⃣ Tạo slot mới
    public boolean createSlot(int vetID, Date date,
                              Time startTime, Time endTime) {

        String sql =
                "INSERT INTO TimeSlots (VetID, SlotDate, StartTime, EndTime, Status) " +
                        "VALUES (?, ?, ?, ?, N'Available')";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, vetID);
            ps.setDate(2, date);
            ps.setTime(3, startTime);
            ps.setTime(4, endTime);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // 5️⃣ Update trạng thái slot
    public boolean updateSlotStatus(int slotID, String status) {

        String sql = "UPDATE TimeSlots SET Status = ? WHERE SlotID = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, slotID);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public List<TimeSlot> getSlotsNext7DaysFromDate(int assigneeID, String startDate) {
        List<TimeSlot> list = new ArrayList<>();
        // Sử dụng tham số linh hoạt cho ID người phụ trách và ngày bắt đầu
        String sql = "SELECT * FROM TimeSlots " +
                "WHERE (VetID = ? OR StaffID = ?) " +
                "AND SlotDate BETWEEN ? AND DATEADD(DAY, 6, ?) " +
                "AND ( " +
                "       SlotDate > CAST(GETDATE() AS DATE) " +
                "    OR (SlotDate = CAST(GETDATE() AS DATE) " +
                "        AND StartTime > CAST(GETDATE() AS TIME)) " +
                "    ) " +
                "AND Status = N'Available' " +
                "ORDER BY SlotDate, StartTime";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, assigneeID);
            ps.setInt(2, assigneeID);
            ps.setString(3, startDate);
            ps.setString(4, startDate);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TimeSlot slot = new TimeSlot();
                slot.setSlotID(rs.getInt("SlotID"));
                slot.setVetID(rs.getInt("VetID"));
                // slot.setStaffID(rs.getInt("StaffID")); // Thêm nếu model có StaffID
                slot.setSlotDate(rs.getDate("SlotDate"));
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
}