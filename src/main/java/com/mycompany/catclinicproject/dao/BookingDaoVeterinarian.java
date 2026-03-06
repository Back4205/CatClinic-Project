/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.TestOrderDTO;
import com.mycompany.catclinicproject.dao.DBContext;
import com.mycompany.catclinicproject.model.AssignCaseDTO;
import com.mycompany.catclinicproject.model.AssignCaseDTO2;

import com.mycompany.catclinicproject.model.DetailBookingDTO;
import com.mycompany.catclinicproject.model.EMRDTO;
import com.mycompany.catclinicproject.model.TestOrderViewDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Son
 */
public class BookingDaoVeterinarian extends DBContext {

    

    public int getVetIDByUserID(int userID) {

        String sql = "SELECT VetID FROM Veterinarians WHERE UserID = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

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

   public int countAssignCases(
        int vetID,
        String dateFrom,
        String dateTo,
        String keyword) {

    int total = 0;

    if (dateFrom == null || dateFrom.trim().isEmpty()) {
        dateFrom = null;
    }

    if (dateTo == null || dateTo.trim().isEmpty()) {
        dateTo = null;
    }

    if (keyword == null || keyword.trim().isEmpty()) {
        keyword = null;
    }

    StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) "
            + "FROM Bookings b "
            + "JOIN Cats c ON b.CatID = c.CatID "
            + "JOIN Owners o ON c.OwnerID = o.OwnerID "
            + "JOIN Users u ON o.UserID = u.UserID "
            + "WHERE b.VetID = ? "
            + "AND b.Status IN ('Confirmed','In Treatment','Completed') "
    );

    // Date filter
    if (dateFrom != null && dateTo != null) {
        sql.append("AND b.AppointmentDate BETWEEN ? AND ? ");
    } else {
        sql.append("AND b.AppointmentDate BETWEEN ")
           .append("DATEADD(MONTH, -1, CAST(GETDATE() AS DATE)) ")
           .append("AND CAST(GETDATE() AS DATE) ");
    }

    // Keyword filter
    if (keyword != null) {
        sql.append("AND (")
           .append("CAST(b.BookingID AS VARCHAR) LIKE ? ")
           .append("OR c.Name LIKE ? ")
           .append("OR u.FullName LIKE ?) ");
    }

    try (PreparedStatement ps = c.prepareStatement(sql.toString())) {

        int index = 1;

        // 1. vetID
        ps.setInt(index++, vetID);

        // 2. Date
        if (dateFrom != null && dateTo != null) {
            ps.setString(index++, dateFrom);
            ps.setString(index++, dateTo);
        }

        // 3. Keyword
        if (keyword != null) {
            String search = "%" + keyword + "%";
            ps.setString(index++, search);
            ps.setString(index++, search);
            ps.setString(index++, search);
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

    public List<AssignCaseDTO> getAssignCasesPaging(
            int vetID,
            String dateFrom,
            String dateTo,
            String keyword,
            int page,
            int pageSize) {

        List<AssignCaseDTO> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;

        if (dateFrom == null || dateFrom.trim().isEmpty()) {
            dateFrom = null;
        }

        if (dateTo == null || dateTo.trim().isEmpty()) {
            dateTo = null;
        }

        if (keyword == null || keyword.trim().isEmpty()) {
            keyword = null;
        }

        StringBuilder sql = new StringBuilder(
                "SELECT b.BookingID, c.Name AS CatName, u.FullName, b.Status,b.SlotID "
                + "FROM Bookings b "
                + "JOIN Cats c ON b.CatID = c.CatID "
                + "JOIN Owners o ON c.OwnerID = o.OwnerID "
                + "JOIN Users u ON o.UserID = u.UserID "
                + "WHERE b.VetID = ? "
                + "AND b.Status IN ('Confirmed','In Treatment','Completed') "
        );

        // Date filter
        if (dateFrom != null && dateTo != null) {
            sql.append("AND b.AppointmentDate BETWEEN ? AND ? ");
        } else {
            sql.append("AND b.AppointmentDate BETWEEN ")
                    .append("DATEADD(MONTH, -1, CAST(GETDATE() AS DATE)) ")
                    .append("AND CAST(GETDATE() AS DATE) ");
        }

        // Keyword search
        if (keyword != null) {
            sql.append("AND (")
                    .append("CAST(b.BookingID AS VARCHAR) LIKE ? ")
                    .append("OR c.Name LIKE ? ")
                    .append("OR u.FullName LIKE ?) ");
        }

        sql.append("ORDER BY b.AppointmentDate DESC ");
        sql.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement ps = c.prepareStatement(sql.toString())) {

            int index = 1;

            // 1. vetID
            ps.setInt(index++, vetID);

            // 2. Date
            if (dateFrom != null && dateTo != null) {
                ps.setString(index++, dateFrom);
                ps.setString(index++, dateTo);
            }

            // 3. Keyword
            if (keyword != null) {
                String search = "%" + keyword + "%";
                ps.setString(index++, search);
                ps.setString(index++, search);
                ps.setString(index++, search);
            }

            // 4. Pagination
            ps.setInt(index++, offset);
            ps.setInt(index++, pageSize);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new AssignCaseDTO(
                        rs.getInt("BookingID"),
                        rs.getString("CatName"),
                        rs.getString("FullName"),
                        rs.getString("Status"),
                        rs.getInt("SlotID")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int countByStatusWithDate(
            int vetID,
            String status,
            String dateFrom,
            String dateTo) {

        if (dateFrom == null || dateFrom.isEmpty()) {
            dateFrom = null;
        }
        if (dateTo == null || dateTo.isEmpty()) {
            dateTo = null;
        }

        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) "
                + "FROM Bookings b "
                + "WHERE b.VetID = ? "
                + "AND b.Status = ? "
        );

        if (dateFrom != null && dateTo != null) {
            sql.append("AND b.AppointmentDate BETWEEN ? AND ? ");
        } else {
            sql.append("AND b.AppointmentDate BETWEEN "
                    + "DATEADD(MONTH, -1, CAST(GETDATE() AS DATE)) "
                    + "AND CAST(GETDATE() AS DATE) ");
        }

        try (PreparedStatement ps = c.prepareStatement(sql.toString())) {

            int index = 1;
            ps.setInt(index++, vetID);
            ps.setString(index++, status);

            if (dateFrom != null && dateTo != null) {
                ps.setString(index++, dateFrom);
                ps.setString(index++, dateTo);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<AssignCaseDTO2> getAssignedCasesByVetID(
            int vetID,
            String keyword,
            String status,
            int page,
            int pageSize) {

        List<AssignCaseDTO2> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;

        if (keyword == null || keyword.trim().isEmpty()) {
            keyword = null;
        }
        if (status == null || status.trim().isEmpty() || status.equals("ALL")) {
            status = null;
        }

        StringBuilder sql = new StringBuilder(
                "SELECT "
                + "mr.MedicalRecordID, "
                + "c.Name AS CatName, "
                + "c.Breed, "
                + "u.FullName, "
                + "u.Phone, "
                + "b.Note, "
                + "b.AppointmentDate, "
                + "mr.Status, "
                + "mr.Diagnosis "
                + "FROM MedicalRecords mr "
                + "JOIN Bookings b ON mr.BookingID = b.BookingID "
                + "JOIN Cats c ON b.CatID = c.CatID "
                + "JOIN Owners o ON c.OwnerID = o.OwnerID "
                + "JOIN Users u ON o.UserID = u.UserID "
                + "WHERE b.VetID = ? "
        );

        // ===== STATUS FILTER =====
        if (status != null) {
            sql.append("AND mr.Status = ? ");
        }

        // ===== SEARCH =====
        if (keyword != null) {
            sql.append("AND (")
                    .append("c.Name LIKE ? ")
                    .append("OR u.FullName LIKE ? ")
                    .append("OR u.Phone LIKE ? ")
                    .append("OR CAST(mr.MedicalRecordID AS VARCHAR) LIKE ? ")
                    .append(") ");
        }

        sql.append("ORDER BY mr.MedicalRecordID DESC ");
        sql.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement ps = c.prepareStatement(sql.toString())) {

            int index = 1;

            ps.setInt(index++, vetID);

            // status
            if (status != null) {
                ps.setString(index++, status);
            }

            // search
            if (keyword != null) {
                String search = "%" + keyword + "%";
                ps.setString(index++, search);
                ps.setString(index++, search);
                ps.setString(index++, search);
                ps.setString(index++, search);
            }

            // paging
            ps.setInt(index++, offset);
            ps.setInt(index++, pageSize);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                AssignCaseDTO2 dto = new AssignCaseDTO2();

                dto.setMedicalRecordID(rs.getInt("MedicalRecordID"));
                dto.setCatName(rs.getString("CatName"));
                dto.setBreed(rs.getString("Breed"));
                dto.setOwnerName(rs.getString("FullName"));
                dto.setPhone(rs.getString("Phone"));
                dto.setNote(rs.getString("Note"));
                dto.setAppointmentDate(rs.getString("AppointmentDate"));
                dto.setStatus(rs.getString("Status"));
                dto.setDiagnosis(rs.getString("Diagnosis"));

                list.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public DetailBookingDTO getBookingDetail(int bookingID) {

        String sql
                = "SELECT b.BookingID, u.FullName, u.Phone, "
                + "c.Name AS CatName, c.Breed, "
                + "b.Note, b.AppointmentDate, b.Status "
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
                DetailBookingDTO dto = new DetailBookingDTO();

                dto.setBookingID(rs.getInt("BookingID"));
                dto.setOwnerName(rs.getString("FullName"));
                dto.setPhone(rs.getString("Phone"));
                dto.setCatName(rs.getString("CatName"));
                dto.setBreed(rs.getString("Breed"));
                dto.setNote(rs.getString("Note"));
                dto.setAppointmentDate(rs.getString("AppointmentDate"));
                dto.setStatus(rs.getString("Status"));

                return dto;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateStatusToInTreatment(int medicalRecordID) {

        String sql = "UPDATE MedicalRecords SET Status = ? WHERE MedicalRecordID = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, "In Treatment");
            ps.setInt(2, medicalRecordID);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean createMedicalRecord(int bookingID) {

        // 1️⃣ Check đã tồn tại chưa
        String checkSql = "SELECT 1 FROM MedicalRecords WHERE BookingID = ?";

        try (PreparedStatement checkPs = c.prepareStatement(checkSql)) {

            checkPs.setInt(1, bookingID);
            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                return false; // Đã tồn tại → không tạo nữa
            }

            // 2️⃣ Nếu chưa tồn tại → tạo mới
            String insertSql = "INSERT INTO MedicalRecords "
                    + "(BookingID, Diagnosis, Symptoms, TreatmentPlan, CreatedAt) "
                    + "VALUES (?, NULL, NULL, NULL, GETDATE())";

            try (PreparedStatement insertPs = c.prepareStatement(insertSql)) {

                insertPs.setInt(1, bookingID);
                return insertPs.executeUpdate() > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

  public int countAssignedCasesByVetID(
        int vetID,
        String keyword,
        String status) {

    int total = 0;

    if (keyword == null || keyword.trim().isEmpty()) {
        keyword = null;
    }
    if (status == null || status.trim().isEmpty() || status.equals("ALL")) {
        status = null;
    }

    StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) "
            + "FROM MedicalRecords mr "
            + "JOIN Bookings b ON mr.BookingID = b.BookingID "
            + "JOIN Cats c ON b.CatID = c.CatID "
            + "JOIN Owners o ON c.OwnerID = o.OwnerID "
            + "JOIN Users u ON o.UserID = u.UserID "
            + "WHERE b.VetID = ? "
    );

    // ===== STATUS FILTER =====
    if (status != null) {
        sql.append("AND mr.Status = ? ");
    }

    // ===== SEARCH =====
    if (keyword != null) {
        sql.append("AND (")
                .append("c.Name LIKE ? ")
                .append("OR u.FullName LIKE ? ")
                .append("OR u.Phone LIKE ? ")
                .append("OR CAST(mr.MedicalRecordID AS VARCHAR) LIKE ? ")
                .append(") ");
    }

    try (PreparedStatement ps = c.prepareStatement(sql.toString())) {

        int index = 1;

        ps.setInt(index++, vetID);

        // status
        if (status != null) {
            ps.setString(index++, status);
        }

        // search
        if (keyword != null) {
            String search = "%" + keyword + "%";
            ps.setString(index++, search);
            ps.setString(index++, search);
            ps.setString(index++, search);
            ps.setString(index++, search);
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

    public EMRDTO getEMRDetail(int medicalRecordID) {

        String sql = "SELECT mr.MedicalRecordID, "
                + "mr.Diagnosis, "
                + "mr.Symptoms, "
                + "mr.TreatmentPlan, "
                + "mr.CreatedAt, "
                + "b.BookingID, "
                + "b.AppointmentDate, "
                + "mr.Status, "
                + "b.Note, "
                + "c.Name AS CatName, "
                + "c.Breed, "
                + "c.Age, "
                + "u.FullName AS OwnerName, "
                + "u.Phone "
                + "FROM MedicalRecords mr "
                + "JOIN Bookings b ON mr.BookingID = b.BookingID "
                + "JOIN Cats c ON b.CatID = c.CatID "
                + "JOIN Owners o ON c.OwnerID = o.OwnerID "
                + "JOIN Users u ON o.UserID = u.UserID "
                + "WHERE mr.MedicalRecordID = ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, medicalRecordID);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                EMRDTO dto = new EMRDTO();

                dto.setMedicalRecordID(rs.getInt("MedicalRecordID"));
                dto.setDiagnosis(rs.getString("Diagnosis"));
                dto.setSymptoms(rs.getString("Symptoms"));
                dto.setTreatmentPlan(rs.getString("TreatmentPlan"));
                dto.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
                
                dto.setBookingID(rs.getInt("BookingID"));
                dto.setAppointmentDate(rs.getTimestamp("AppointmentDate").toLocalDateTime());
                dto.setStatus(rs.getString("Status"));
                dto.setNote(rs.getString("Note"));

                dto.setCatName(rs.getString("CatName"));
                dto.setBreed(rs.getString("Breed"));
                dto.setAge(rs.getInt("Age"));

                dto.setOwnerName(rs.getString("OwnerName"));
                dto.setPhone(rs.getString("Phone"));

                return dto;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateMedicalRecord(int medicalRecordID,
            String diagnosis,
            String symptoms,
            String treatmentPlan) {

        String sql = "UPDATE MedicalRecords "
                + "SET Diagnosis = ?, "
                + "    Symptoms = ?, "
                + "    TreatmentPlan = ? "
                + "WHERE MedicalRecordID = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, diagnosis);
            ps.setString(2, symptoms);
            ps.setString(3, treatmentPlan);
            ps.setInt(4, medicalRecordID);

            int rows = ps.executeUpdate();

            return rows > 0; // update thành công

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public int getMedicalRecordIDByBookingID(int bookingID) {

        String sql = "SELECT MedicalRecordID FROM MedicalRecords WHERE BookingID = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, bookingID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("MedicalRecordID");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1; // Không tồn tại
    }

    public boolean checkTestOrderExists(int medicalRecordID) {
        String sql = "SELECT 1 FROM TestOrders WHERE MedicalRecordID = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, medicalRecordID);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true nếu tồn tại
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<TestOrderDTO> getTestOrdersByMedicalRecordID(int medicalRecordID) {

        List<TestOrderDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM TestOrders WHERE MedicalRecordID = ? AND TestName = 'X-Ray'";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, medicalRecordID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TestOrderDTO t = new TestOrderDTO();
                t.setTestOrderID(rs.getInt("TestOrderID"));
                t.setMedicalRecordID(rs.getInt("MedicalRecordID"));
                t.setTestName(rs.getString("TestName"));
                t.setResultName(rs.getString("ResultName"));
                t.setResult(rs.getString("Result"));
                t.setStatus(rs.getString("Status"));
                t.setStaffID(rs.getInt("StaffID"));

                list.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<TestOrderDTO> getTestOrdersByMedicalRecordIDBloodTest(int medicalRecordID) {

        List<TestOrderDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM TestOrders WHERE MedicalRecordID = ? AND TestName = 'Blood Test'";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, medicalRecordID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TestOrderDTO t = new TestOrderDTO();
                t.setTestOrderID(rs.getInt("TestOrderID"));
                t.setMedicalRecordID(rs.getInt("MedicalRecordID"));
                t.setTestName(rs.getString("TestName"));
                t.setResultName(rs.getString("ResultName"));
                t.setResult(rs.getString("Result"));
                t.setStatus(rs.getString("Status"));
                t.setStaffID(rs.getInt("StaffID"));

                list.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean insertTestOrder(int medicalRecordID) {

        String sql = "INSERT INTO TestOrders "
                + "(MedicalRecordID, TestName, ResultName, Result, Status, StaffID) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setInt(1, medicalRecordID);
            ps.setString(2, "X-Ray");
            ps.setString(3, "Chest");
            ps.setString(4, "");
            ps.setString(5, "Pending");
            ps.setInt(6, 1);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }

    public boolean insertTestOrderBloodTest(int medicalRecordID) {

        String sql = "INSERT INTO TestOrders "
                + "(MedicalRecordID, TestName, ResultName, Result, Status, StaffID) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setInt(1, medicalRecordID);
            ps.setString(2, "Blood Test");
            ps.setString(3, "Hemoglobin");
            ps.setString(4, "");
            ps.setString(5, "Pending");
            ps.setInt(6, 1);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<TestOrderViewDTO> getTestOrdersByVetID(
        int vetID,
        String keyword,
        String status,
        int pageIndex,
        int pageSize) {

    List<TestOrderViewDTO> list = new ArrayList<>();
    StringBuilder sql = new StringBuilder();

    sql.append("SELECT t.TestOrderID, mr.MedicalRecordID, ");
    sql.append("u.FullName, c.Name AS CatName, ");
    sql.append("t.TestName, t.Status ");
    sql.append("FROM TestOrders t ");
    sql.append("JOIN MedicalRecords mr ON t.MedicalRecordID = mr.MedicalRecordID ");
    sql.append("JOIN Bookings b ON mr.BookingID = b.BookingID ");
    sql.append("JOIN Cats c ON b.CatID = c.CatID ");
    sql.append("JOIN Owners o ON c.OwnerID = o.OwnerID ");
    sql.append("JOIN Users u ON o.UserID = u.UserID ");
    sql.append("WHERE b.VetID = ? ");

    if (keyword != null && !keyword.trim().isEmpty()) {
        sql.append("AND (u.FullName LIKE ? OR c.Name LIKE ?) ");
    }

    if (status != null && !status.trim().isEmpty()) {
        sql.append("AND t.Status = ? ");
    }

    sql.append("ORDER BY t.TestOrderID ");
    sql.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

    try {
        PreparedStatement ps = c.prepareStatement(sql.toString());
        int index = 1;

        ps.setInt(index++, vetID);

        if (keyword != null && !keyword.trim().isEmpty()) {
            ps.setString(index++, "%" + keyword + "%");
            ps.setString(index++, "%" + keyword + "%");
        }

        if (status != null && !status.trim().isEmpty()) {
            ps.setString(index++, status);
        }

        ps.setInt(index++, (pageIndex - 1) * pageSize);
        ps.setInt(index++, pageSize);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            TestOrderViewDTO t = new TestOrderViewDTO();
            t.setTestOrderID(rs.getInt("TestOrderID"));
            t.setMedicalRecordID(rs.getInt("MedicalRecordID"));
            t.setFullName(rs.getString("FullName"));
            t.setCatName(rs.getString("CatName"));
            t.setTestName(rs.getString("TestName"));
            t.setStatus(rs.getString("Status"));
            list.add(t);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
    public int countTestOrdersByVetID(int vetID, String keyword, String status) {

    StringBuilder sql = new StringBuilder();
    sql.append("SELECT COUNT(*) ");
    sql.append("FROM TestOrders t ");
    sql.append("JOIN MedicalRecords mr ON t.MedicalRecordID = mr.MedicalRecordID ");
    sql.append("JOIN Bookings b ON mr.BookingID = b.BookingID ");
    sql.append("JOIN Cats c ON b.CatID = c.CatID ");
    sql.append("JOIN Owners o ON c.OwnerID = o.OwnerID ");
    sql.append("JOIN Users u ON o.UserID = u.UserID ");
    sql.append("WHERE b.VetID = ? ");

    if (keyword != null && !keyword.trim().isEmpty()) {
        sql.append("AND (u.FullName LIKE ? OR c.Name LIKE ?) ");
    }

    if (status != null && !status.trim().isEmpty()) {
        sql.append("AND t.Status = ? ");
    }

    try {
        PreparedStatement ps = c.prepareStatement(sql.toString());
        int index = 1;

        ps.setInt(index++, vetID);

        if (keyword != null && !keyword.trim().isEmpty()) {
            ps.setString(index++, "%" + keyword + "%");
            ps.setString(index++, "%" + keyword + "%");
        }

        if (status != null && !status.trim().isEmpty()) {
            ps.setString(index++, status);
        }

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}

    public int countTestOrdersByVetID(int vetID) {

        String sql = "SELECT COUNT(*) "
                + "FROM TestOrders t "
                + "JOIN MedicalRecords mr ON t.MedicalRecordID = mr.MedicalRecordID "
                + "JOIN Bookings b ON mr.BookingID = b.BookingID "
                + "WHERE b.VetID = ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, vetID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    public boolean completeMedicalRecord(int medicalRecordID) {

    String sql = "UPDATE MedicalRecords "
               + "SET Status = ? "
               + "WHERE MedicalRecordID = ?";

    try {
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, "Completed");
        ps.setInt(2, medicalRecordID);

        int rows = ps.executeUpdate();

        return rows > 0;

    } catch (Exception e) {
        e.printStackTrace();
    }
    
    

    return false;
}
   public String getStatusByMedicalRecordID(int medicalRecordID) {

    String sql = "SELECT Status FROM MedicalRecords "
               + "WHERE MedicalRecordID = ?";

    try {
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, medicalRecordID);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getString("Status");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}
   public int getCatIDByMedicalRecordID(int medicalRecordID) {

    String sql = "SELECT b.CatID "
               + "FROM MedicalRecords m "
               + "JOIN Bookings b ON m.BookingID = b.BookingID "
               + "WHERE m.MedicalRecordID = ?";

    try {
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, medicalRecordID);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("CatID");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return -1; 
} 

}
