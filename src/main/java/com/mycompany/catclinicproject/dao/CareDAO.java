package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.CareTaskDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CareDAO extends DBContext {

    // 1. Lấy danh sách nhiệm vụ từ CareTasks (Feeding, Cleaning, Monitoring, Medication)
    public Map<Integer, String> getMasterCareTasks() {
        Map<Integer, String> tasks = new HashMap<>();
        try {
            PreparedStatement ps = c.prepareStatement("SELECT CareTaskID, TaskName FROM CareTasks");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) tasks.put(rs.getInt("CareTaskID"), rs.getString("TaskName"));
        } catch (Exception e) { e.printStackTrace(); }
        return tasks;
    }

    // 2. Lấy dữ liệu CareJourneys trong ngày
    public List<CareTaskDTO> getDailyTasks(int staffId) {
        List<CareTaskDTO> list = new ArrayList<>();
        String sql = "SELECT cj.CareJID, cj.Status, cj.BookingID, b.EndDate, b.Status as BookingStatus, " +
                "c.CatID, c.Name as CatName, c.Age, c.Breed, c.Gender, c.Image, " +
                "u.FullName as OwnerName, u.Phone as OwnerPhone, o.Address " +
                "FROM CareJourneys cj " +
                "JOIN Bookings b ON cj.BookingID = b.BookingID " +
                "JOIN Cats c ON cj.CatID = c.CatID " +
                "JOIN Owners o ON c.OwnerID = o.OwnerID " +
                "JOIN Users u ON o.UserID = u.UserID " +
                "WHERE cj.StaffID = ? AND CAST(cj.RecordTime AS DATE) = CAST(GETDATE() AS DATE)";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, staffId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CareTaskDTO dto = new CareTaskDTO();
                dto.setCareJID(rs.getInt("CareJID"));
                dto.setStatus(rs.getString("Status"));
                dto.setBookingID(rs.getInt("BookingID"));
                dto.setEndDate(rs.getDate("EndDate"));
                dto.setBookingStatus(rs.getString("BookingStatus"));

                dto.setCatID(rs.getInt("CatID"));
                dto.setCatName(rs.getString("CatName"));
                dto.setCatAge(rs.getInt("Age"));
                dto.setBreed(rs.getString("Breed"));
                // Xử lý Gender kiểu BIT
                boolean isMale = rs.getBoolean("Gender");
                dto.setGender(isMale ? "Male" : "Female");
                dto.setCatImage(rs.getString("Image"));

                dto.setOwnerName(rs.getString("OwnerName"));
                dto.setOwnerPhone(rs.getString("OwnerPhone"));
                dto.setAddress(rs.getString("Address"));

                // Lấy các task đã hoàn thành (tích xanh)
                dto.setCompletedTaskIds(getCompletedTaskIds(dto.getCareJID()));
                list.add(dto);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    private List<Integer> getCompletedTaskIds(int careJID) {
        List<Integer> list = new ArrayList<>();
        try {
            PreparedStatement ps = c.prepareStatement("SELECT CareTaskID FROM CareJourney_Task_Detail WHERE CareJID = ?");
            ps.setInt(1, careJID);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) list.add(rs.getInt("CareTaskID"));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 3. Đánh dấu 1 task là hoàn thành và cập nhật Status tự động (Pending -> In Progress -> Completed)
    public void markTaskAsDone(int careJID, int taskId) {
        try {
            // 1. Lưu task chi tiết
            PreparedStatement ps = c.prepareStatement(
                    "IF NOT EXISTS (SELECT 1 FROM CareJourney_Task_Detail WHERE CareJID=? AND CareTaskID=?) " +
                            "INSERT INTO CareJourney_Task_Detail (CareJID, CareTaskID) VALUES (?, ?)");
            ps.setInt(1, careJID); ps.setInt(2, taskId);
            ps.setInt(3, careJID); ps.setInt(4, taskId);
            ps.executeUpdate();

            // 2. Cập nhật Status của Journey (Chỉ có 3 mốc: Pending -> In Progress -> Completed)
            List<Integer> doneTasks = getCompletedTaskIds(careJID);
            String newStatus = "In Progress";
            if (doneTasks.size() >= 4) {
                newStatus = "Completed"; // Khi đủ 4 ô thì chốt là Completed
            }

            PreparedStatement up = c.prepareStatement("UPDATE CareJourneys SET Status = ? WHERE CareJID = ?");
            up.setString(1, newStatus);
            up.setInt(2, careJID);
            up.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // 4. Chuyển hồ sơ cho Lễ Tân (Checkout)
    public void setReadyForCheckout(int bookingID) {
        try {
            PreparedStatement ps = c.prepareStatement("UPDATE Bookings SET Status = 'Ready for Checkout' WHERE BookingID = ?");
            ps.setInt(1, bookingID);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}