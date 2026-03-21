package com.mycompany.catclinicproject.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CheckInBookingDAO extends DBContext{


        public void checkInBooking(int bookingId) {
            String sql = " SELECT b.BookingID, b.CatID, b.StaffID, s.Position\n" +
                    "            FROM Bookings b\n" +
                    "            JOIN Staffs s ON b.StaffID = s.StaffID\n" +
                    "            WHERE b.BookingID = ?\n" +
                    "              AND b.StaffID IS NOT NULL";

            try (
                 PreparedStatement ps = c.prepareStatement(sql)) {

                ps.setInt(1, bookingId);
                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {
                    System.out.println("❌ Booking không tồn tại hoặc chưa có Staff");
                    return;
                }

                int catId = rs.getInt("CatID");
                int staffId = rs.getInt("StaffID");
                String position = rs.getString("Position");

                c.setAutoCommit(false);

                if ("Technician".equalsIgnoreCase(position)) {
                    handleTechnician( bookingId, staffId);
                } else if ("Care".equalsIgnoreCase(position)) {
                    handleCare( bookingId, catId, staffId);
                } else {
                    System.out.println("⚠ Không xử lý với position: " + position);
                    c.rollback();
                    return;
                }

                c.commit();
                System.out.println("✔ Check-in thành công");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    private void handleTechnician(int bookingId, int staffId) {

        try {
            c.setAutoCommit(false);

            String insertMedical = " INSERT INTO MedicalRecords (BookingID, CreatedAt)\n" +
                    "            VALUES (?, GETDATE())" ;

            PreparedStatement psMedical = c.prepareStatement(insertMedical, Statement.RETURN_GENERATED_KEYS);
            psMedical.setInt(1, bookingId);
            psMedical.executeUpdate();

            ResultSet rs = psMedical.getGeneratedKeys();
            int medicalRecordId = 0;

            if (rs.next()) {
                medicalRecordId = rs.getInt(1);
            } else {
                throw new SQLException("Không lấy được MedicalRecordID");
            }

            String insertTest = " INSERT INTO TestOrders (MedicalRecordID, TestName, ResultName, Result, Status, StaffID)\n" +
                    "            VALUES (?, N'Pending Test', N'Pending', N'Waiting result', 'Pending', ?)";

            PreparedStatement psTest = c.prepareStatement(insertTest);
            psTest.setInt(1, medicalRecordId);
            psTest.setInt(2, staffId);
            psTest.executeUpdate();

            c.commit();
            System.out.println("✔ Technician check-in OK");

        } catch (Exception e) {
            try {
                c.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private void handleCare(int bookingId, int catId, int staffId) {

        try {
            c.setAutoCommit(false);

            String insertCare = " INSERT INTO CareJourneys (CatID, BookingID, RecordTime, StaffID, Note , Status)\n" +
                    "            VALUES (?, ?, GETDATE(), ?, N'Check-in Care' , 'Pending')";

            PreparedStatement psCare = c.prepareStatement(insertCare);
            psCare.setInt(1, catId);
            psCare.setInt(2, bookingId);
            psCare.setInt(3, staffId);
            psCare.executeUpdate();

            c.commit();
            System.out.println("✔ Care check-in OK");

        } catch (Exception e) {
            try {
                c.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
