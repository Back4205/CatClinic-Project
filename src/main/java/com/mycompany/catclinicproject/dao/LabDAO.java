package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.dao.DBContext;
import com.mycompany.catclinicproject.model.Category;
import com.mycompany.catclinicproject.model.LabTestDTO;
import com.mycompany.catclinicproject.model.TestOrders;
import java.sql.*;
import java.util.*;

public class LabDAO extends DBContext {

    public List<LabTestDTO> getLabQueue(String status) {
        List<LabTestDTO> list = new ArrayList<>();

        String sql = "SELECT t.TestOrderID, c.Name AS CatName, t.TestName, t.ResultName, t.Result, u.FullName AS DoctorName, t.Status " +
                "FROM TestOrders t " +
                "JOIN MedicalRecords m ON t.MedicalRecordID = m.MedicalRecordID " +
                "JOIN Bookings b ON m.BookingID = b.BookingID " +
                "JOIN Cats c ON b.CatID = c.CatID " +
                " LEFT JOIN Veterinarians v ON b.VetID = v.VetID " +
                "LEFT JOIN Users u ON v.UserID = u.UserID ";

        if (status != null && !status.equals("All")) {
            sql += " WHERE t.Status = ? ";
        }

        sql += " ORDER BY t.TestOrderID DESC";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            if (status != null && !status.equals("All")) {
                ps.setString(1, status);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LabTestDTO d = new LabTestDTO();
                d.setTestOrderID(rs.getInt("TestOrderID"));
                d.setCatName(rs.getString("CatName"));
                d.setTestName(rs.getString("TestName"));
                d.setResultName(rs.getString("ResultName"));
                d.setResult(rs.getString("Result"));
                d.setDoctorName(rs.getString("DoctorName"));
                d.setStatus(rs.getString("Status"));
                list.add(d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public Map<String, Integer> getLabStats() {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT " +
                "COUNT(*) as Total, " +
                "SUM(CASE WHEN Status = 'Pending' THEN 1 ELSE 0 END) as Pending, " +
                "SUM(CASE WHEN Status = 'In-progress' THEN 1 ELSE 0 END) as InProgress, " +
                "SUM(CASE WHEN Status = 'Completed' THEN 1 ELSE 0 END) as Completed " +
                "FROM TestOrders";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                stats.put("Total", rs.getInt("Total"));
                stats.put("Pending", rs.getInt("Pending"));
                stats.put("InProgress", rs.getInt("InProgress")); // Sẽ hiện số 1 cho Tom
                stats.put("Completed", rs.getInt("Completed"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stats;
    }
    public void updateTestStatus(int testID, String status) {
        String sql = "UPDATE TestOrders SET Status = ? WHERE TestOrderID = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, testID);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public LabTestDTO getTestDetailByID(int id) {
        String sql = "SELECT t.TestOrderID, c.Name AS CatName, t.TestName, u.FullName AS DoctorName, t.Status " +
                "FROM TestOrders t " +
                "JOIN MedicalRecords m ON t.MedicalRecordID = m.MedicalRecordID " +
                "JOIN Bookings b ON m.BookingID = b.BookingID " +
                "JOIN Cats c ON b.CatID = c.CatID " +
                "JOIN Veterinarians v ON b.VetID = v.VetID " +
                "JOIN Users u ON v.UserID = u.UserID " +
                "WHERE t.TestOrderID = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                LabTestDTO d = new LabTestDTO();
                d.setTestOrderID(rs.getInt("TestOrderID"));
                d.setCatName(rs.getString("CatName"));
                d.setTestName(rs.getString("TestName"));
                d.setDoctorName(rs.getString("DoctorName"));
                d.setStatus(rs.getString("Status"));
                return d;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
    public TestOrders getTestOrderById(int testOrderID){
        String sql = "select * from TestOrders where TestOrderID = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, testOrderID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                TestOrders d = new TestOrders();
                d.setTestOrderID(rs.getInt("TestOrderID"));
                d.setMedicalRecordID(rs.getInt("MedicalRecordID"));
                d.setTestName(rs.getString("TestName"));
                d.setResultName(rs.getString("ResultName"));
                d.setResult(rs.getString("Result"));
                d.setStatus(rs.getString("Status"));
                d.setStaffID(rs.getInt("StaffID"));
                return d;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public void saveDraftFull(int testOrderID, String resultName, String resultPath) {
    String sql = "UPDATE TestOrders SET ResultName = ?, Result = ?  WHERE TestOrderID = ?";
    try (PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setString(1, resultName);
        ps.setString(2, resultPath);
        ps.setInt(3, testOrderID);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    public void submitFull(int testOrderID, String resultName, String resultPath) {
    String sql = "UPDATE TestOrders SET ResultName = ?, Result = ?, Status = 'Completed' WHERE TestOrderID = ?";
    try (PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setString(1, resultName);
        ps.setString(2, resultPath);
        ps.setInt(3, testOrderID);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    public void submitTextOnly(int testOrderID, String resultName) {
    String sql = "UPDATE TestOrders SET ResultName = ?, Status = 'Completed' WHERE TestOrderID = ?";
    try (PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setString(1, resultName);
        ps.setInt(2, testOrderID);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    public void saveDraftTextOnly(int testOrderID, String resultName) {
    String sql = "UPDATE TestOrders SET ResultName = ? WHERE TestOrderID = ?";
    try (PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setString(1, resultName);
        ps.setInt(2, testOrderID);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}