package com.mycompany.catclinicproject.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PaymentDAO extends DBContext{
    public void insertPayment(int invoiceID, double amount, String method, String transactionCode) {
        // Thêm cột PaymentDate và giá trị GETDATE()
        String sql = "INSERT INTO Payments (InvoiceID, TotalAmount, PaymentMethod, TransactionCode, PaymentDate) " +
                "VALUES (?, ?, ?, ?, GETDATE())";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, invoiceID);
            ps.setDouble(2, amount);
            ps.setString(3, method);
            ps.setString(4, transactionCode);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isTransactionExists(String transactionNo) {

        String sql = "SELECT 1 FROM Payments WHERE TransactionCode = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, transactionNo);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public long getTotalPaidByInvoice(int invoiceID) {
        String sql = "SELECT ISNULL(SUM(TotalAmount),0) FROM Payments WHERE InvoiceID = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, invoiceID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}