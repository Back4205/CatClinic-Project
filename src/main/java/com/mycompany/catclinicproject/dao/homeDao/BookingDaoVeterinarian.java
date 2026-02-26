/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.dao.homeDao;

import com.mycompany.catclinicproject.dao.DBContext;
import com.mycompany.catclinicproject.model.AssignCaseDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Son
 */
public class BookingDaoVeterinarian extends DBContext{
    public List<AssignCaseDTO> getAssignCasesByVet(int vetID) {

    List<AssignCaseDTO> list = new ArrayList<>();

    String sql = "SELECT b.BookingID, c.Name AS CatName, u.FullName, b.Status "
            + "FROM Bookings b "
            + "JOIN Cats c ON b.CatID = c.CatID "
            + "JOIN Owners o ON c.OwnerID = o.OwnerID "
            + "JOIN Users u ON o.UserID = u.UserID "
            + "WHERE b.Status IN ('Confirmed', 'InTreatment', 'Completed') "
            + "AND b.VetID = ? "
            + "ORDER BY b.BookingID DESC";

    try (PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setInt(1, vetID);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            AssignCaseDTO dto = new AssignCaseDTO(
                    rs.getInt("BookingID"),
                    rs.getString("CatName"),
                    rs.getString("FullName"),
                    rs.getString("Status")
            );

            list.add(dto);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
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
        String dateTo) {

    if (dateFrom == null || dateFrom.isEmpty()) {
        dateFrom = null;
    }
    if (dateTo == null || dateTo.isEmpty()) {
        dateTo = null;
    }

    StringBuilder sql = new StringBuilder(
        "SELECT COUNT(*) " +
        "FROM Bookings b " +
        "WHERE b.VetID = ? " +
        "AND b.Status IN ('Confirmed','InTreatment','Completed') "
    );

    if (dateFrom != null && dateTo != null) {
        sql.append("AND b.AppointmentDate BETWEEN ? AND ? ");
    } else {
        sql.append("AND b.AppointmentDate BETWEEN " +
                   "DATEADD(MONTH, -1, CAST(GETDATE() AS DATE)) " +
                   "AND CAST(GETDATE() AS DATE) ");
    }

    try (PreparedStatement ps = c.prepareStatement(sql.toString())) {

        int index = 1;
        ps.setInt(index++, vetID);

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
    public List<AssignCaseDTO> getAssignCasesPaging(
        int vetID,
        String dateFrom,
        String dateTo,
        int page,
        int pageSize) {

    List<AssignCaseDTO> list = new ArrayList<>();
    int offset = (page - 1) * pageSize;

    if (dateFrom == null || dateFrom.isEmpty()) {
        dateFrom = null;
    }
    if (dateTo == null || dateTo.isEmpty()) {
        dateTo = null;
    }

    StringBuilder sql = new StringBuilder(
        "SELECT b.BookingID, c.Name AS CatName, u.FullName, b.Status " +
        "FROM Bookings b " +
        "JOIN Cats c ON b.CatID = c.CatID " +
        "JOIN Owners o ON c.OwnerID = o.OwnerID " +
        "JOIN Users u ON o.UserID = u.UserID " +
        "WHERE b.VetID = ? " +
        "AND b.Status IN ('Confirmed','Intreatment','Completed') "
    );

    if (dateFrom != null && dateTo != null) {
        sql.append("AND b.AppointmentDate BETWEEN ? AND ? ");
    } else {
        sql.append("AND b.AppointmentDate BETWEEN " +
                   "DATEADD(MONTH, -1, CAST(GETDATE() AS DATE)) " +
                   "AND CAST(GETDATE() AS DATE) ");
    }

    sql.append("ORDER BY b.AppointmentDate DESC ");
    sql.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

    try (PreparedStatement ps = c.prepareStatement(sql.toString())) {

        int index = 1;
        ps.setInt(index++, vetID);

        if (dateFrom != null && dateTo != null) {
            ps.setString(index++, dateFrom);
            ps.setString(index++, dateTo);
        }

        ps.setInt(index++, offset);
        ps.setInt(index++, pageSize);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(new AssignCaseDTO(
                    rs.getInt("BookingID"),
                    rs.getString("CatName"),
                    rs.getString("FullName"),
                    rs.getString("Status")
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
        "SELECT COUNT(*) " +
        "FROM Bookings b " +
        "WHERE b.VetID = ? " +
        "AND b.Status = ? "
    );

    if (dateFrom != null && dateTo != null) {
        sql.append("AND b.AppointmentDate BETWEEN ? AND ? ");
    } else {
        sql.append("AND b.AppointmentDate BETWEEN " +
                   "DATEADD(MONTH, -1, CAST(GETDATE() AS DATE)) " +
                   "AND CAST(GETDATE() AS DATE) ");
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
}
