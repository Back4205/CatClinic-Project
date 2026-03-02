package com.mycompany.catclinicproject.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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

    // Danh sách mèo đang nội trú
    public List<Map<String, Object>> getInpatientCats() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT b.BookingID, b.CatID, c.Name AS CatName, c.Breed, c.Age, u.FullName AS OwnerName " +
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

    // UC: View Pet Details (Lấy toàn bộ thông tin chi tiết mèo và chủ)
    public Map<String, Object> getPetDetail(int catId) {
        Map<String, Object> map = new HashMap<>();
        String sql = "SELECT c.Name, c.Breed, c.Age, c.Gender, c.Image, u.FullName, u.Phone, u.Email, o.Address " +
                "FROM Cats c " +
                "JOIN Owners o ON c.OwnerID = o.OwnerID " +
                "JOIN Users u ON o.UserID = u.UserID " +
                "WHERE c.CatID = ?";
        try {
            if (c == null || c.isClosed()) c = new DBContext().c;
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, catId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                map.put("CatName", rs.getString("Name"));
                map.put("Breed", rs.getString("Breed"));
                map.put("Age", rs.getInt("Age"));
                // Xử lý Gender kiểu BIT
                map.put("Gender", rs.getBoolean("Gender") ? "Male" : "Female");
                map.put("OwnerName", rs.getString("FullName"));
                map.put("Phone", rs.getString("Phone"));
                map.put("Email", rs.getString("Email"));
                map.put("Address", rs.getString("Address"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return map;
    }

    // UC: View Care Task & Task Tracking (Lấy qua bảng trung gian)
    public List<Map<String, Object>> getDailyTasksStatus(int catId) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT t.CareTaskID, t.TaskName, " +
                "(SELECT TOP 1 jd.CareJID FROM CareJourneys j " +
                " JOIN CareJourney_Task_Detail jd ON j.CareJID = jd.CareJID " +
                " WHERE jd.CareTaskID = t.CareTaskID AND j.CatID = ? " +
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
                boolean isCompleted = rs.getInt("CompletedJID") > 0;
                map.put("Status", isCompleted ? "Completed" : "Pending");
                list.add(map);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // UC: Record Care Diary (Thêm nhật ký & gán Task bằng Transaction 2 bảng)
    public boolean addCareJourney(int catId, int bookingId, int staffId, int taskId, String note) {
        String sqlJourney = "INSERT INTO CareJourneys (CatID, BookingID, RecordTime, StaffID, Note) VALUES (?, ?, GETDATE(), ?, ?)";
        String sqlDetail = "INSERT INTO CareJourney_Task_Detail (CareJID, CareTaskID) VALUES (?, ?)";

        try {
            if (c == null || c.isClosed()) c = new DBContext().c;
            c.setAutoCommit(false); // Bắt đầu Transaction

            // 1. Insert vào CareJourneys
            PreparedStatement ps1 = c.prepareStatement(sqlJourney, Statement.RETURN_GENERATED_KEYS);
            ps1.setInt(1, catId);
            ps1.setInt(2, bookingId);
            ps1.setInt(3, staffId);
            ps1.setString(4, note);
            ps1.executeUpdate();

            // 2. Lấy ID vừa tạo
            ResultSet rs = ps1.getGeneratedKeys();
            int careJID = 0;
            if (rs.next()) careJID = rs.getInt(1);

            // 3. Insert vào bảng trung gian
            PreparedStatement ps2 = c.prepareStatement(sqlDetail);
            ps2.setInt(1, careJID);
            ps2.setInt(2, taskId);
            ps2.executeUpdate();

            c.commit(); // Lưu thay đổi
            c.setAutoCommit(true);
            return true;
        } catch (Exception e) {
            try { if (c != null) c.rollback(); } catch (Exception ex) {}
            e.printStackTrace();
        }
        return false;
    }

    // Lấy lịch sử Record Care Diary
    public List<Map<String, Object>> getObservations(int catId) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT t.TaskName, j.RecordTime, j.Note, u.FullName AS StaffName " +
                "FROM CareJourneys j " +
                "JOIN CareJourney_Task_Detail jd ON j.CareJID = jd.CareJID " +
                "JOIN CareTasks t ON jd.CareTaskID = t.CareTaskID " +
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