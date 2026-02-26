package com.mycompany.catclinicproject.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PaymentDAO extends DBContext{
    public void insertPayment(int invoiceID, double amount, String method, String transactionCode) {

        String sql = "INSERT INTO Payments (InvoiceID, AmountPaid, PaymentMethod, TransactionCode) VALUES (?, ?, ?, ?)";

        try (
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, invoiceID);
            ps.setDouble(2, amount);
            ps.setString(3, method);
            ps.setString(4, transactionCode);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public double getTotalPaidAmount(int invoiceID) {

        String sql = "SELECT ISNULL(SUM(AmountPaid), 0) FROM Payments WHERE InvoiceID = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, invoiceID);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
