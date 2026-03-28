package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.CareTaskDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Date ;

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
                "u.FullName as OwnerName, u.Phone as OwnerPhone, o.Address, cj.Note , " + // Thêm cj.Note
                "br.CheckOutTime " +
                "FROM CareJourneys cj " +
                "JOIN Bookings b ON cj.BookingID = b.BookingID " +
                "JOIN Cats c ON cj.CatID = c.CatID " +
                "JOIN Owners o ON c.OwnerID = o.OwnerID " +
                "JOIN Users u ON o.UserID = u.UserID " +
                "JOIN BoardingRecords br ON b.BookingID = br.BookingID " +
                "WHERE cj.StaffID = ? AND CAST(cj.RecordTime AS DATE) = CAST(GETDATE() AS DATE)";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, staffId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CareTaskDTO dto = new CareTaskDTO();
                dto.setCareJID(rs.getInt("CareJID"));
                dto.setStatus(rs.getString("Status"));
                dto.setCatName(rs.getString("CatName"));
                dto.setCatAge(rs.getInt("Age"));
                dto.setBreed(rs.getString("Breed"));
                boolean isMale = rs.getBoolean("Gender"); // Tùy theo CSDL lưu kiểu BIT hay Chuỗi
                dto.setGender(isMale ? "Male" : "Female");
                dto.setOwnerName(rs.getString("OwnerName"));
                dto.setOwnerPhone(rs.getString("OwnerPhone"));
                dto.setAddress(rs.getString("Address"));
                dto.setCatImage(rs.getString("Image"));
                dto.setNote(rs.getString("Note")); // THÊM DÒNG NÀY ĐỂ MAPPING NOTE
                dto.setCheckOutTime(rs.getTimestamp("CheckOutTime"));
                dto.setBookingID(rs.getInt("BookingID"));
                dto.setBookingStatus(rs.getString("BookingStatus"));
                dto.setEndDate(rs.getDate("EndDate"));

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
    // 3. Đánh dấu 1 task là hoàn thành
    public void markTaskAsDone(int careJID, int taskId) {
        try {
            // 1. Lưu task chi tiết
            PreparedStatement ps = c.prepareStatement(
                    "IF NOT EXISTS (SELECT 1 FROM CareJourney_Task_Detail WHERE CareJID=? AND CareTaskID=?) " +
                            "INSERT INTO CareJourney_Task_Detail (CareJID, CareTaskID) VALUES (?, ?)");
            ps.setInt(1, careJID); ps.setInt(2, taskId);
            ps.setInt(3, careJID); ps.setInt(4, taskId);
            ps.executeUpdate();

            // 2. GIỮ NGUYÊN IN PROGRESS ĐỂ LỄ TÂN KHÔNG THẤY
            String newStatus = "In Progress";

            PreparedStatement up = c.prepareStatement("UPDATE CareJourneys SET Status = ? WHERE CareJID = ?");
            up.setString(1, newStatus);
            up.setInt(2, careJID);
            up.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // 4. Chuyển hồ sơ cho Lễ Tân (Chỉ khi bạn bấm nút Checkout)
    public void setReadyForCheckout(int bookingID) {
        try {
            // Việc 1: Chốt đơn hàng tổng thành Completed (Dành cho chức năng thống kê doanh thu)
            PreparedStatement ps = c.prepareStatement("UPDATE Bookings SET Status = 'Completed' WHERE BookingID = ?");
            ps.setInt(1, bookingID);
            ps.executeUpdate();

            // Việc 2: LÚC NÀY MỚI CHỐT SỔ THÀNH COMPLETED (Để đánh thức code của Lễ tân hút mèo sang)
            PreparedStatement ps2 = c.prepareStatement("UPDATE CareJourneys SET Status = 'Completed' WHERE BookingID = ? AND CAST(RecordTime AS DATE) = CAST(GETDATE() AS DATE)");
            ps2.setInt(1, bookingID);
            ps2.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Lưu Record Care Diary (Cập nhật Note và Status - Hỗ trợ CỘNG DỒN)
    public boolean updateCareDiary(int careJID, String note, String status) {
        // SQL thông minh: Nếu Note cũ trống thì lưu mới, nếu đã có thì nối thêm xuống dòng rồi cộng dồn
        String sql = "UPDATE CareJourneys "
                + "SET Note = CASE WHEN Note IS NULL OR DATALENGTH(Note) = 0 THEN ? "
                + "ELSE Note + CHAR(13) + CHAR(10) + CHAR(13) + CHAR(10) + ? END, "
                + "Status = ? WHERE CareJID = ?";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, note); // Tham số 1: Dành cho trường hợp sổ mới tinh
            ps.setString(2, note); // Tham số 2: Dành cho trường hợp cộng dồn
            ps.setString(3, status);
            ps.setInt(4, careJID);

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 5. Tự động sinh sổ CareJourneys cho ngày mới nếu mèo chưa Check-out
    public void generateDailyJourneysIfMissing(int staffID) {
        String sql = "INSERT INTO CareJourneys (CatID, BookingID, RecordTime, StaffID, Status) " +
                "SELECT b.CatID, b.BookingID, GETDATE(), ?, 'Pending' " +
                "FROM Bookings b " +
                "JOIN BoardingRecords br ON b.BookingID = br.BookingID " +
                "WHERE br.CheckInTime IS NOT NULL AND br.CheckOutTime IS NULL " +
                "AND NOT EXISTS (" +
                "    SELECT 1 FROM CareJourneys cj " +
                "    WHERE cj.BookingID = b.BookingID AND CAST(cj.RecordTime AS DATE) = CAST(GETDATE() AS DATE)" +
                ")";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, staffID);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void extendBookingEndDate(int bookingID, String newEndDate) {
        try {
            c.setAutoCommit(false); // Bật chế độ Transaction để đảm bảo an toàn dữ liệu

            // Việc 1: Cập nhật EndDate mới vào bảng Bookings
            PreparedStatement ps1 = c.prepareStatement("UPDATE Bookings SET EndDate = ? WHERE BookingID = ?");
            ps1.setString(1, newEndDate);
            ps1.setInt(2, bookingID);
            ps1.executeUpdate();

            // Việc 2: Tính lại tiền lưu trú và ghi đè vào bảng Appointment_Service
            // Công thức: Đơn giá 1 ngày (s.Price) * (Số ngày từ lúc nhập viện đến ngày xuất viện mới + 1)
            // (Chỉ áp dụng cho dịch vụ Boarding có CategoryID = 4)
            String sqlUpdatePrice =
                    "UPDATE aps " +
                            "SET aps.PriceAtBooking = s.Price * (DATEDIFF(DAY, b.AppointmentDate, ?) + 1) " +
                            "FROM Appointment_Service aps " +
                            "JOIN Bookings b ON aps.BookingID = b.BookingID " +
                            "JOIN Services s ON aps.ServiceID = s.ServiceID " +
                            "WHERE aps.BookingID = ? AND s.CategoryID = 4";

            PreparedStatement ps2 = c.prepareStatement(sqlUpdatePrice);
            ps2.setString(1, newEndDate); // Truyền ngày xuất viện mới vào hàm DATEDIFF
            ps2.setInt(2, bookingID);
            ps2.executeUpdate();

            c.commit(); // Chốt lưu toàn bộ thay đổi xuống DB
        } catch (Exception e) {
            try { c.rollback(); } catch (Exception ex) {}
            e.printStackTrace();
        } finally {
            try { c.setAutoCommit(true); } catch (Exception e) {}
        }
    }

    // Lấy lịch sử ghi chú bệnh án của các ngày trước
    public List<String> getCareHistoryLogs(int bookingID) {
        List<String> history = new ArrayList<>();
        // Sắp xếp giảm dần để ngày mới nhất lên đầu
        String sql = "SELECT CAST(RecordTime AS DATE) as RecDate, Note " +
                "FROM CareJourneys " +
                "WHERE BookingID = ? AND Note IS NOT NULL " +
                "ORDER BY RecordTime DESC";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, bookingID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String log = "Ngày " + rs.getString("RecDate") + "|||" + rs.getString("Note");
                history.add(log);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return history;
    }
}