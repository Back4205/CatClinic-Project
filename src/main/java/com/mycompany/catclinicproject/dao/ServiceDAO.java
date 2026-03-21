package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.AddServiceDTO;
import com.mycompany.catclinicproject.model.Service;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO extends DBContext {

    public List<Service> getAllServices() {
        List<Service> list = new ArrayList<>();

        String sql = "SELECT ServiceID, NameService, Price, TimeService, IsActive,CategoryID FROM Services";

        try (PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Service s = new Service();
                s.setServiceID(rs.getInt("ServiceID"));
                s.setNameService(rs.getString("NameService"));
                s.setPrice(rs.getDouble("Price"));
                s.setTimeService(rs.getInt("TimeService"));
                s.setIsActive(rs.getBoolean("IsActive"));
                s.setCategoryID(rs.getInt("CategoryID"));

                list.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void insertServices(AddServiceDTO s) {
        String sqlService = "INSERT INTO Services (ServiceName, Price, Description, TimeService, IsActive, CategoryID) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        String sqlImg = "INSERT INTO ImgServices (ImgURL, IsActive, ServiceID) "
                + "VALUES (?, ?, ?)";

        try {
            c.setAutoCommit(false);

            PreparedStatement ps = c.prepareStatement(sqlService, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, s.getNameService());
            ps.setDouble(2, s.getPrice());
            ps.setString(3, s.getDescription());
            ps.setInt(4, s.getTimeService());
            ps.setBoolean(5, s.isIsActive());
            ps.setInt(6, s.getCategoryID());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (!rs.next()) {
                throw new Exception("Cannot get ServiceID");
            }
            int serviceID = rs.getInt(1);

            PreparedStatement psImg = c.prepareStatement(sqlImg);
            psImg.setString(1, s.getImgURL());
            psImg.setBoolean(2, true);
            psImg.setInt(3, serviceID);
            psImg.executeUpdate();

            c.commit();
            System.out.println("INSERT SERVICE + IMAGE SUCCESS");

        } catch (Exception e) {
            try {
                c.rollback();
            } catch (Exception ex) {
            }
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void insertService(AddServiceDTO s) {

        String sqlService
                = "INSERT INTO Services "
                + "(ServiceName, Price, Description, TimeService, IsActive, CategoryID) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        String sqlImg
                = "INSERT INTO ImgServices "
                + "(ImgURL, IsActive, ServiceID) "
                + "VALUES (?, ?, ?)";

        try {
            // ===== START TRANSACTION =====
            c.setAutoCommit(false);

            // ===== INSERT SERVICE =====
            PreparedStatement psService
                    = c.prepareStatement(sqlService, Statement.RETURN_GENERATED_KEYS);

            psService.setString(1, s.getNameService());
            psService.setDouble(2, s.getPrice());          // PRICE = double
            psService.setString(3, s.getDescription());
            psService.setInt(4, s.getTimeService());
            psService.setBoolean(5, s.isIsActive());
            psService.setInt(6, s.getCategoryID());

            psService.executeUpdate();

            // ===== GET SERVICE ID =====
            ResultSet rs = psService.getGeneratedKeys();
            if (!rs.next()) {
                throw new SQLException("Cannot get generated ServiceID");
            }
            int serviceID = rs.getInt(1);

            // ===== INSERT IMAGE (NẾU CÓ) =====
            if (s.getImgURL() != null && !s.getImgURL().isEmpty()) {

                PreparedStatement psImg = c.prepareStatement(sqlImg);
                psImg.setString(1, s.getImgURL());
                psImg.setBoolean(2, true);
                psImg.setInt(3, serviceID);

                psImg.executeUpdate();
            }

            // ===== COMMIT =====
            c.commit();
            System.out.println("INSERT SERVICE + IMAGE SUCCESS");

        } catch (Exception e) {
            // ===== ROLLBACK =====
            try {
                if (c != null) {
                    c.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();

        } finally {
            closeConnection();
        }
    }

    public List<Service> getServicesByCategoryID(int CategoryID) {
        List<Service> list = new ArrayList<>();
        Service s = null;
        String sql = "select * from Services Where CategoryID = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, CategoryID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                s = new Service(
                        rs.getInt("ServiceID"),
                        rs.getString("NameService"),
                        rs.getDouble("Price"),
                        rs.getString("Description"),
                        rs.getInt("TimeService"),
                        rs.getBoolean("IsActive"),
                        rs.getInt("CategoryID"),
                        rs.getString("ImgURL"));
                list.add(s);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /* =======================
       INSERT SERVICE
       ======================= */
    public void insertService(Service s) {
        String sql = "INSERT INTO Services\n"
                + "            (NameService, Price, Description, TimeService, IsActive, CategoryID,ImgURL)\n"
                + "            VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, s.getNameService());
            ps.setDouble(2, s.getPrice());
            ps.setString(3, s.getDescription());
            ps.setInt(4, s.getTimeService());
            ps.setBoolean(5, s.isIsActive());
            ps.setInt(6, s.getCategoryID());
            ps.setString(7, s.getImgUrl());
            ps.executeUpdate();
            System.out.println("INSERT SERVICE SUCCESS");

        } catch (Exception e) {
            System.out.println("INSERT SERVICE FAILED");
            e.printStackTrace();
        }
    }

    /* =======================
       GET SERVICE BY ID
       ======================= */
    public Service getServiceById(int id) {
        String sql = "SELECT * FROM Services WHERE ServiceID = ?";
        Service s = null;

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                s = new Service(
                        rs.getInt("ServiceID"),
                        rs.getString("NameService"),
                        rs.getDouble("Price"),
                        rs.getString("Description"),
                        rs.getInt("TimeService"),
                        rs.getBoolean("IsActive"),
                        rs.getInt("CategoryID"),
                        rs.getString("ImgURL")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return s;
    }

    /* =======================
       UPDATE SERVICE
       ======================= */
    public void updateService(Service s) {
        String sql = "UPDATE Services\n"
                + "            SET NameService = ?,\n"
                + "                Price = ?,\n"
                + "                Description = ?,\n"
                + "                TimeService = ?,\n"
                + "                CategoryID = ?,\n"
                + "                ImgURL = ?\n"
                + "            WHERE ServiceID = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, s.getNameService());
            ps.setDouble(2, s.getPrice());
            ps.setString(3, s.getDescription());
            ps.setInt(4, s.getTimeService());
            ps.setInt(5, s.getCategoryID());
            ps.setString(6, s.getImgUrl());
            ps.setInt(7, s.getServiceID());

            ps.executeUpdate();
            System.out.println("UPDATE SERVICE SUCCESS");

        } catch (Exception e) {
            System.out.println("UPDATE SERVICE FAILED");
            e.printStackTrace();
        }
    }

    /* =======================
       UPDATE SERVICE STATUS
       ======================= */
    public void updateServiceStatus(int serviceID, boolean isActive) {
        String sql = "UPDATE Services SET IsActive = ? WHERE ServiceID = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setBoolean(1, isActive);
            ps.setInt(2, serviceID);
            ps.executeUpdate();
            System.out.println("UPDATE SERVICE STATUS SUCCESS");
        } catch (Exception e) {
            System.out.println("UPDATE SERVICE STATUS FAILED");
            e.printStackTrace();
        }
    }

    public void InsertImgService(String ImgName, String ImgURL, int ServiceID) {
        String sql = "INSERT INTO ImgServices\n"
                + "            (ImgName,ImgURL,ServiceID)\n"
                + "            VALUES (?,?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, ImgName);
            ps.setString(2, ImgURL);
            ps.setInt(3, ServiceID);
            ps.executeUpdate();
            System.out.println("INSERT SERVICE SUCCESS");
        } catch (Exception e) {
            System.out.println("INSERT SERVICE FAILED");
            e.printStackTrace();
        }
    }

    public boolean isServiceNameExists(String name) {
        String sql = "SELECT 1 FROM Services WHERE NameService = ?";
        try {
            PreparedStatement ps = this.c.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void addServiceToBooking(int bookingID, int serviceID) {
        String sql = "INSERT INTO Appointment_Service (BookingID, ServiceID, PriceAtBooking) "
                + "VALUES (?, ?, ?)";
        try {
            String priceSql = "SELECT Price FROM Services WHERE ServiceID = ?";
            PreparedStatement ps1 = c.prepareStatement(priceSql);
            ps1.setInt(1, serviceID);
            ResultSet rs = ps1.executeQuery();
            if (rs.next()) {
                double price = rs.getDouble("Price");
                PreparedStatement ps2 = c.prepareStatement(sql);
                ps2.setInt(1, bookingID);
                ps2.setInt(2, serviceID);
                ps2.setDouble(3, price);
                ps2.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int getBookingIDByMedicalRecordID(int medicalRecordID) {
    int bookingID = -1;
    String sql = "SELECT BookingID FROM MedicalRecords WHERE MedicalRecordID = ?";

    try {
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, medicalRecordID);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            bookingID = rs.getInt("BookingID");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return bookingID;
}

}
