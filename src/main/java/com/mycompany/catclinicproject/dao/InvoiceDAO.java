package com.mycompany.catclinicproject.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class InvoiceDAO extends DBContext{
    public int createInvoice(int bookingID, double total) {
        String sql = "INSERT INTO Invoices (BookingID, TotalAmount, PaymentStatus) "
                + "VALUES (?, ?, 'Unpaid')";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, bookingID);
            ps.setDouble(2, total);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void updateInvoiceStatus(int invoiceID, String status) {
        String sql = "UPDATE Invoices SET PaymentStatus = ? WHERE InvoiceID = ?";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, invoiceID);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int getBookingIDByInvoice(int invoiceID) {

        String sql = "SELECT BookingID FROM Invoices WHERE InvoiceID = ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, invoiceID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("BookingID");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

}

