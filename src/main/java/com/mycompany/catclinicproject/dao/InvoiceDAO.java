package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.Invoice;
import com.mycompany.catclinicproject.model.InvoiceDetail;
import com.mycompany.catclinicproject.model.InvoiceItem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO extends DBContext{

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


    public InvoiceDetail getInvoiceInfo(int bookingID) {

        String sql = "\n" +
                " SELECT \n" +
                "            i.InvoiceID,\n" +
                "            i.PaymentStatus,\n" +
                "            i.CreatedDate,\n" +
                "            u.FullName,\n" +
                "            u.Phone,\n" +
                "            c.Name AS CatName,\n" +
                "            b.BookingID\n" +
                "        FROM Invoices i\n" +
                "        JOIN Bookings b ON i.BookingID = b.BookingID\n" +
                "        JOIN Cats c ON b.CatID = c.CatID\n" +
                "        JOIN Owners o ON c.OwnerID = o.OwnerID\n" +
                "        JOIN Users u ON o.UserID = u.UserID\n" +
                "        WHERE b.BookingID = ?" ;

        try {

            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, bookingID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                InvoiceDetail invoice = new InvoiceDetail();

                invoice.setInvoiceCode(rs.getString("InvoiceID"));
                invoice.setInvoiceStatus(rs.getString("PaymentStatus"));
                invoice.setCreatedDate(rs.getDate("CreatedDate"));

                invoice.setCustomerName(rs.getString("FullName"));
                invoice.setPhone(rs.getString("Phone"));

                invoice.setPetName(rs.getString("CatName"));
                invoice.setBookingCode(rs.getString("BookingID"));

                return invoice;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<InvoiceItem> getServicesByBookingID(int bookingID) {

        List<InvoiceItem> list = new ArrayList<>();

        String sql = " SELECT \n" +
                "            s.NameService,\n" +
                "            s.Price\n" +
                "        FROM Appointment_Service aps\n" +
                "        JOIN Services s ON aps.ServiceID = s.ServiceID\n" +
                "        WHERE aps.BookingID = ?";

        try {

            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, bookingID);

            ResultSet rs = ps.executeQuery();
            int id = 1 ;

            while (rs.next()) {

                String name = rs.getString("NameService");
                double price = rs.getDouble("Price");

                int quantity = 1; // auto

                InvoiceItem item = new InvoiceItem(id++ , name, "Service" , 1, price, quantity*price);

                list.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public double getTotalServiceAmount(int bookingID) {

        double total = 0;

        String sql = "SELECT ISNULL(SUM(PriceAtBooking),0) AS TotalAmount\n" +
                "        FROM Appointment_Service\n" +
                "        WHERE BookingID = ?" ;

        try {

            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, bookingID);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("TotalAmount");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }


    public void updateTotalAmount(int invoiceID, double totalAmount) {

        String sql = "UPDATE Invoices\n" +
                "        SET TotalAmount = ?\n" +
                "        WHERE InvoiceID = ?" ;

        try {
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setDouble(1, totalAmount);
            ps.setInt(2, invoiceID);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}