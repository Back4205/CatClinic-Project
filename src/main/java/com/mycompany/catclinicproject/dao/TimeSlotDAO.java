package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.TimeSlot;
import com.mycompany.catclinicproject.model.TimeSlotDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class TimeSlotDAO extends DBContext {

    public TimeSlot getSlotByID(int slotID) {
        String sql = "SELECT SlotID, StartTime, EndTime, IsActive FROM TimeSlots WHERE SlotID = ?";

        try (
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, slotID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                TimeSlot slot = new TimeSlot();
                slot.setSlotID(rs.getInt("SlotID"));
                slot.setStartTime(rs.getTime("StartTime"));
                slot.setEndTime(rs.getTime("EndTime"));
                slot.setActive(rs.getBoolean("IsActive"));

                return slot;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
//    public boolean isTimeSlotBooked(int vetID, int slotID, java.sql.Date date) {
//        String sql = "SELECT 1 FROM TimeSlot_Vet WHERE VetID = ? AND SlotID = ? AND Date = ? AND Status = 'Available";
//
//        try (
//             PreparedStatement ps = c.prepareStatement(sql)) {
//
//            ps.setInt(1, vetID);
//            ps.setInt(2, slotID);
//            ps.setDate(3, date);
//
//            ResultSet rs = ps.executeQuery();
//            return rs.next();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }


    public List<TimeSlotDTO> getAvailableSlotsNext7Days(int vetID, java.sql.Date fromDate) {

        List<TimeSlotDTO> allSlots = new ArrayList<>();

        // Lấy tất cả slot gốc
        List<TimeSlot> masterSlots = getAllTimeSlots();

        // Map chứa slot đã bị book
        Map<String, Boolean> bookedMap = getBookedSlotsMap(vetID, fromDate);

        LocalDate start = fromDate.toLocalDate();
        LocalDate today = LocalDate.now();
        java.time.LocalTime nowTime = java.time.LocalTime.now();

        for (int i = 0; i < 7; i++) {

            LocalDate current = start.plusDays(i);
            java.sql.Date sqlCurrent = java.sql.Date.valueOf(current);

            for (TimeSlot master : masterSlots) {

                //  Ẩn slot quá khứ nếu là ngày hôm nay
                if (current.equals(today)) {
                    java.time.LocalTime slotTime = master.getStartTime().toLocalTime();

                    if (slotTime.isBefore(nowTime)) {
                        continue;
                    }
                }

                String key = master.getSlotID() + "_" + sqlCurrent.toString();

                //  Ẩn slot đã bị book
                if (bookedMap.containsKey(key)) {
                    continue;
                }


                TimeSlotDTO dto = new TimeSlotDTO();
                dto.setSlotID(master.getSlotID());
                dto.setStartTime(master.getStartTime());
                dto.setEndTime(master.getEndTime());
                dto.setSlotDate(sqlCurrent);
                dto.setVetID(vetID);

                allSlots.add(dto);
            }
        }

        return allSlots;
    }

    private Map<String, Boolean> getBookedSlotsMap(int vetID, java.sql.Date fromDate) {
        Map<String, Boolean> map = new HashMap<>();
        String sql = "SELECT SlotID, Date FROM TimeSlot_Vet " +
                "WHERE VetID = ? AND Date >= ? AND Date < DATEADD(DAY, 7, ?) AND Status = 'Booked'";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, vetID);
            ps.setDate(2, fromDate);
            ps.setDate(3, fromDate);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Tạo key kết hợp SlotID và Ngày để check nhanh
                String key = rs.getInt("SlotID") + "_" + rs.getDate("Date").toString();
                map.put(key, true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    public List<TimeSlot> getAllTimeSlots() {
        String sql = "SELECT SlotID, StartTime, EndTime, IsActive FROM TimeSlots WHERE IsActive = 1 ORDER BY StartTime";
        List<TimeSlot> list = new ArrayList<>();

        try (
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                TimeSlot slot = new TimeSlot();
                slot.setSlotID(rs.getInt("SlotID"));
                slot.setStartTime(rs.getTime("StartTime"));
                slot.setEndTime(rs.getTime("EndTime"));
                slot.setActive(rs.getBoolean("IsActive"));

                list.add(slot);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


}