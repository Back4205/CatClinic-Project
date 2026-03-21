package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.BillingBookingDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
    public List<BillingBookingDTO> getBillingBookings(
            String search,
            String status,
            String filterDate,
            int page,
            int pageSize
    ) {
        if (search != null) {
            search = search.trim().toLowerCase();
        }
        List<BillingBookingDTO> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT\n" +
                "            b.BookingID,\n" +
                "            c.Name AS CatName,\n" +
                "            c.Image,\n" +
                "            u.FullName AS OwnerName,\n" +
                "            u.Phone, \n" +
                "            br.CheckInTime,\n" +
                "            br.CheckOutTime,\n" +
                "            ISNULL(i.PaymentStatus,'Unpaid') AS InvoiceStatus\n" +
                "        FROM Bookings b\n" +
                "        JOIN BoardingRecords br ON b.BookingID = br.BookingID\n" +
                "        JOIN Cats c ON b.CatID = c.CatID\n" +
                "        JOIN Owners o ON c.OwnerID = o.OwnerID\n" +
                "        JOIN Users u ON o.UserID = u.UserID\n" +
                "        LEFT JOIN Invoices i ON b.BookingID = i.BookingID\n" +
                "        WHERE br.CheckOutTime IS NOT NULL " );

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (\n" +
                    "                c.Name LIKE ?\n" +
                    "                OR u.FullName LIKE ?\n" +
                    "                OR u.Phone LIKE ?\n" +
                    "            )");
        }

        if (status != null && !status.equals("ALL")) {
            sql.append(" AND ISNULL(i.PaymentStatus,'Unpaid') = ? ");
        }

        if (filterDate != null && !filterDate.isEmpty()) {
            sql.append(" AND CAST(br.CheckOutTime AS DATE) = ? ");
        }

        sql.append(" ORDER BY br.CheckInTime DESC\n" +
                "        OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement ps = c.prepareStatement(sql.toString())) {

            int index = 1;

            if (search != null && !search.trim().isEmpty()) {
                ps.setString(index++, "%" + search + "%");
                ps.setString(index++, "%" + search + "%");
                ps.setString(index++, "%" + search + "%");
            }

            if (status != null && !status.equals("ALL")) {
                ps.setString(index++, status);
            }

            if (filterDate != null && !filterDate.isEmpty()) {
                ps.setDate(index++, java.sql.Date.valueOf(filterDate));
            }

            int offset = (page - 1) * pageSize;

            ps.setInt(index++, offset);
            ps.setInt(index, pageSize);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                BillingBookingDTO b = new BillingBookingDTO();

                b.setBookingID(rs.getInt("BookingID"));
                b.setCatName(rs.getString("CatName"));
                b.setOwnerName(rs.getString("OwnerName"));
                b.setCheckInTime(rs.getTimestamp("CheckInTime"));
                b.setCheckOutTime(rs.getTimestamp("CheckOutTime"));
                b.setInvoiceStatus(rs.getString("InvoiceStatus"));
                b.setImage(rs.getString("Image"));
                b.setPhone(rs.getString("Phone"));


                list.add(b);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    public int countBillingBookings(String search, String status, String filterDate) {

        int total = 0;

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT COUNT(*)\n" +
                "        FROM Bookings b\n" +
                "        JOIN BoardingRecords br ON b.BookingID = br.BookingID\n" +
                "        JOIN Cats c ON b.CatID = c.CatID\n" +
                "        JOIN Owners o ON c.OwnerID = o.OwnerID\n" +
                "        JOIN Users u ON o.UserID = u.UserID\n" +
                "        LEFT JOIN Invoices i ON b.BookingID = i.BookingID\n" +
                "        WHERE br.CheckOutTime IS NOT NULL ");

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (\n" +
                    "                c.Name LIKE ?\n" +
                    "                OR u.FullName LIKE ?\n" +
                    "                OR u.Phone LIKE ?\n" +
                    "            )");
        }

        if (status != null && !status.equals("ALL")) {
            sql.append(" AND ISNULL(i.PaymentStatus,'PendingPayment') = ? ");
        }

        if (filterDate != null && !filterDate.isEmpty()) {
            sql.append(" AND CAST(br.CheckInTime AS DATE) = ? ");
        }

        try (PreparedStatement ps = c.prepareStatement(sql.toString())) {

            int index = 1;

            if (search != null && !search.trim().isEmpty()) {
                ps.setString(index++, "%" + search + "%");
                ps.setString(index++, "%" + search + "%");
                ps.setString(index++, "%" + search + "%");
            }

            if (status != null && !status.equals("ALL")) {
                ps.setString(index++, status);
            }

            if (filterDate != null && !filterDate.isEmpty()) {
                ps.setDate(index++, java.sql.Date.valueOf(filterDate));
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

    public double getPaidAmount(int bookingID) {

        double paid = 0;

        String sql = "SELECT ISNULL(SUM(p.TotalAmount),0) AS PaidAmount\n" +
                "FROM Payments p\n" +
                "JOIN Invoices i ON p.InvoiceID = i.InvoiceID\n" +
                "WHERE i.BookingID = ?";

        try {

            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, bookingID);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                paid = rs.getDouble("PaidAmount");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return paid;
    }
    public boolean insertPayment1(int invoiceID, double amount, String method, String transactionCode) {
        String sql = "INSERT INTO Payments (InvoiceID, TotalAmount, PaymentMethod, TransactionCode, PaymentDate) " +
                "VALUES (?, ?, ?, ?, GETDATE())";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, invoiceID);
            ps.setDouble(2, amount);
            ps.setString(3, method);
            ps.setString(4, transactionCode);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu insert thành công

        } catch (Exception e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi
        }
    }
    public double getDepositByInvoice(int BookingID) {

        String sql = "SELECT TOP 1 p.TotalAmount\n" +
                "        FROM Payments p \n" +
                "        join Invoices i on p.InvoiceID = i.InvoiceID\n" +
                "\t\twhere BookingID = ? \n" +
                "        ORDER BY PaymentDate ASC";


        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, BookingID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getLong("TotalAmount");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}