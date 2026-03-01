package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.TimeSlot;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class TimeSlotDAO extends DBContext {

    //  Lấy slot trống của bác sĩ trong 7 ngày tới (từ hôm nay)
    public List<TimeSlot> getSlotsNext7Days(int vetID) {

        List<TimeSlot> list = new ArrayList<>();

        String sql =
                "SELECT * FROM TimeSlots " +
                        "WHERE VetID = ? " +
                        "AND Date BETWEEN CAST(GETDATE() AS DATE) " +
                        "AND DATEADD(DAY, 6, CAST(GETDATE() AS DATE)) " +
                        "AND ( " +
                        "       Date > CAST(GETDATE() AS DATE) " +
                        "    OR (Date = CAST(GETDATE() AS DATE) " +
                        "        AND StartTime > CAST(GETDATE() AS TIME)) " +
                        "    ) " +
                        "AND Status = N'Available' " +
                        "ORDER BY Date, StartTime";

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

}