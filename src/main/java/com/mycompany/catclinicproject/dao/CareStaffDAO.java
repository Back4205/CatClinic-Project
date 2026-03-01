package com.mycompany.catclinicproject.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CareStaffDAO extends DBContext {

    public int getStaffIdByUserId(int userId) {
        String sql = "SELECT StaffID FROM Staffs WHERE UserID = ?";
        try {
            if (c == null || c.isClosed()) c = new DBContext().c;
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("StaffID");
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    // Lấy danh sách mèo nội trú (Cột trái)
    public List<Map<String, Object>> getInpatientCats() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT b.BookingID, b.CatID, c.Name AS CatName, c.Breed, c.Age, o.OwnerID, u.FullName AS OwnerName " +
                "FROM Bookings b " +
                "JOIN Cats c ON b.CatID = c.CatID " +
                "JOIN Owners o ON c.OwnerID = o.OwnerID " +
                "JOIN Users u ON o.UserID = u.UserID " +
                "WHERE b.Status IN ('Intreatment', 'Assigned', 'Waiting') " +
                "ORDER BY b.AppointmentDate DESC";
        try {
            if (c == null || c.isClosed()) c = new DBContext().c;
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("BookingID", rs.getInt("BookingID"));
                map.put("CatID", rs.getInt("CatID"));
                map.put("CatName", rs.getString("CatName"));
                map.put("Breed", rs.getString("Breed"));
                map.put("Age", rs.getInt("Age"));
                map.put("OwnerName", rs.getString("OwnerName"));
                list.add(map);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // UC40 & UC42: Lấy danh sách Task và trạng thái Pending/Completed trong ngày HÔM NAY của 1 bé mèo
    public List<Map<String, Object>> getDailyTasksStatus(int catId) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT t.CareTaskID, t.TaskName, " +
                "(SELECT TOP 1 CareJID FROM CareJourneys j " +
                " WHERE j.CareTaskID = t.CareTaskID AND j.CatID = ? " +
                " AND CAST(j.RecordTime AS DATE) = CAST(GETDATE() AS DATE)) AS CompletedJID " +
                "FROM CareTasks t";
        try {
            if (c == null || c.isClosed()) c = new DBContext().c;
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, catId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("CareTaskID", rs.getInt("CareTaskID"));
                map.put("TaskName", rs.getString("TaskName"));

                // Nếu có log trong hôm nay -> Completed, Nếu không -> Pending
                boolean isCompleted = rs.getInt("CompletedJID") > 0;
                map.put("Status", isCompleted ? "Completed" : "Pending");
                list.add(map);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // UC41: Lấy lịch sử Ghi chú (Observations Log) của 1 bé mèo
    public List<Map<String, Object>> getObservations(int catId) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT t.TaskName, j.RecordTime, j.Note, u.FullName AS StaffName " +
                "FROM CareJourneys j " +
                "JOIN CareTasks t ON j.CareTaskID = t.CareTaskID " +
                "JOIN Staffs s ON j.StaffID = s.StaffID " +
                "JOIN Users u ON s.UserID = u.UserID " +
                "WHERE j.CatID = ? ORDER BY j.RecordTime DESC";
        try {
            if (c == null || c.isClosed()) c = new DBContext().c;
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, catId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("TaskName", rs.getString("TaskName"));
                String time = rs.getString("RecordTime");
                if(time != null && time.length() > 16) time = time.substring(0, 16);
                map.put("RecordTime", time);
                map.put("Note", rs.getString("Note"));
                map.put("StaffName", rs.getString("StaffName"));
                list.add(map);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Dùng chung cho UC41 (Log Entry) và UC42 (Mark Completed)
    public boolean addCareJourney(int catId, int bookingId, int staffId, int taskId, String note) {
        String sql = "INSERT INTO CareJourneys (CatID, BookingID, RecordTime, StaffID, CareTaskID, Note) VALUES (?, ?, GETDATE(), ?, ?, ?)";
        try {
            if (c == null || c.isClosed()) c = new DBContext().c;
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, catId);
            ps.setInt(2, bookingId);
            ps.setInt(3, staffId);
            ps.setInt(4, taskId);
            ps.setString(5, note);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // Lấy list CareTasks (dành cho popup Log)
    public List<Map<String, Object>> getAllCareTasks() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT CareTaskID, TaskName FROM CareTasks";
        try {
            if (c == null || c.isClosed()) c = new DBContext().c;
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("CareTaskID", rs.getInt("CareTaskID"));
                map.put("TaskName", rs.getString("TaskName"));
                list.add(map);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}