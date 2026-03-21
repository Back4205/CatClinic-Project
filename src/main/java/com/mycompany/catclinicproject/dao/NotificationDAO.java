package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.NotificationDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO extends DBContext {

   public int createNotification(int vetID, String message, int refID, String type) {
    String sql = "INSERT INTO Notifications(VetID, Message, RefID, Type, IsRead) VALUES(?,?,?,?,0)";

    try (PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

        ps.setInt(1, vetID);
        ps.setString(2, message);
        ps.setInt(3, refID);
        ps.setString(4, type);

        int affectedRows = ps.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating notification failed, no rows affected.");
        }

        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1); // trả về notificationID vừa tạo
            } else {
                throw new SQLException("Creating notification failed, no ID obtained.");
            }
        }

    } catch (SQLException e) {
        System.out.println("Create Notification Error: " + e.getMessage());
    }

    return -1; // nếu lỗi thì trả về -1
}

    public List<NotificationDTO> getNotificationByVet(int vetID) {

        List<NotificationDTO> list = new ArrayList<>();

        String sql = "SELECT * FROM Notifications WHERE VetID = ? ORDER BY CreatedAt DESC";

        try {

            PreparedStatement ps = c.prepareStatement(sql);

            ps.setInt(1, vetID);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                NotificationDTO n = new NotificationDTO();

                n.setNotificationID(rs.getInt("NotificationID"));
                n.setVetID(rs.getInt("VetID"));
                n.setMessage(rs.getString("Message"));
                n.setRefID(rs.getInt("RefID"));
                n.setType(rs.getString("Type"));
                n.setIsRead(rs.getBoolean("IsRead"));
                n.setCreatedAt(rs.getString("CreatedAt"));

                list.add(n);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<NotificationDTO> NotificationByVet(int vetID) {

        List<NotificationDTO> list = new ArrayList<>();

        String sql = "SELECT  * FROM Notifications WHERE VetID = ? ORDER BY CreatedAt DESC";

        try {

            PreparedStatement ps = c.prepareStatement(sql);

            ps.setInt(1, vetID);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                NotificationDTO n = new NotificationDTO();

                n.setNotificationID(rs.getInt("NotificationID"));
                n.setVetID(rs.getInt("VetID"));
                n.setMessage(rs.getString("Message"));
                n.setRefID(rs.getInt("RefID"));
                n.setType(rs.getString("Type"));
                n.setIsRead(rs.getBoolean("IsRead"));
                n.setCreatedAt(rs.getString("CreatedAt"));

                list.add(n);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean markAsRead(int notificationID) {

        String sql = "UPDATE Notifications SET IsRead = 1 WHERE NotificationID = ?";

        try {

            PreparedStatement ps = c.prepareStatement(sql);

            ps.setInt(1, notificationID);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public int countUnreadByVet(int vetID) {

        String sql = "SELECT COUNT(*) FROM Notifications WHERE VetID = ? AND IsRead = 0";

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

    public int getVetIDByUserID(int userID) {

        int vetID = -1;

        String sql = "SELECT VetID FROM Veterinarians WHERE UserID = ?";

        try {

            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, userID);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                vetID = rs.getInt("VetID");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return vetID;
    }

    public NotificationDTO getNotificationByID(int notificationID) {

        String sql = "SELECT * FROM Notifications WHERE NotificationID = ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, notificationID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                NotificationDTO n = new NotificationDTO();

                n.setNotificationID(rs.getInt("NotificationID"));
                n.setVetID(rs.getInt("VetID"));
                n.setMessage(rs.getString("Message"));
                n.setRefID(rs.getInt("RefID"));
                n.setType(rs.getString("Type"));
                n.setIsRead(rs.getBoolean("IsRead"));
                n.setCreatedAt(rs.getString("CreatedAt"));

                return n;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
