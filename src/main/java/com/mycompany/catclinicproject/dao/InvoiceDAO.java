package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.Invoice;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class InvoiceDAO extends DBContext{
    public int createInvoice(int bookingID, double total) {
        String sql = "INSERT INTO Invoices (BookingID, TotalAmount, PaymentStatus, CreatedDate)\n" +
                "VALUES (?, ?, 'Unpaid', GETDATE())";

        try {
            PreparedStatement ps = c.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
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

    public Invoice getInvoiceByBookingID(int bookingID) {

        String sql = "SELECT * FROM Invoices WHERE BookingID = ?";

        try (
                PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, bookingID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Invoice(
                            rs.getInt("InvoiceID"),
                            rs.getInt("BookingID"),
                            rs.getDouble("TotalAmount"),
                            rs.getTimestamp("CreatedDate"),
                            rs.getString("PaymentStatus")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public void updatePaymentStatus(int invoiceID, String status) {

        String sql = "UPDATE Invoices SET PaymentStatus = ? WHERE InvoiceID = ?";

        try (
                PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, invoiceID);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Invoice getInvoiceByID(int invoiceID) {
        String sql = "SELECT * FROM Invoices WHERE InvoiceID = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, invoiceID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Invoice(
                        rs.getInt("InvoiceID"),
                        rs.getInt("BookingID"),
                        rs.getDouble("TotalAmount"),
                        rs.getTimestamp("CreatedDate"),
                        rs.getString("PaymentStatus")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}