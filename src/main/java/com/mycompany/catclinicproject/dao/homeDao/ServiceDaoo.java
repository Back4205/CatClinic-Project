package com.mycompany.catclinicproject.dao.homeDao;

import com.mycompany.catclinicproject.dao.DBContext;
import com.mycompany.catclinicproject.model.ServiceDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ServiceDaoo extends DBContext {

    // ================= INSERT SERVICE =================
    public int insertService(String name, double price, String description,
            int timeService, int categoryID) {

        String sql = "INSERT INTO Services "
                + "(NameService, Price, Description, TimeService, IsActive, CategoryID) "
                + "VALUES (?, ?, ?, ?, 1, ?)";

        try (PreparedStatement ps
                = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setString(3, description);
            ps.setInt(4, timeService);
            ps.setInt(5, categoryID);

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

    // ================= GET ALL ACTIVE SERVICES =================
    public List<ServiceDTO> getAllServices() {

        List<ServiceDTO> list = new ArrayList<>();

        String sql = "SELECT * FROM Services WHERE IsActive = 1";

        try (PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                ServiceDTO service = new ServiceDTO(
                        rs.getInt("ServiceID"),
                        rs.getString("NameService"),
                        rs.getDouble("Price"),
                        rs.getString("Description"),
                        rs.getInt("TimeService"),
                        rs.getBoolean("IsActive"),
                        rs.getInt("CategoryID"),
                        rs.getString("ImgURL")
                );

                list.add(service);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= GET SERVICE BY ID =================
    public ServiceDTO getServiceById(int id) {

        String sql = "SELECT * FROM Services WHERE ServiceID = ? AND IsActive = 1";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new ServiceDTO(
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    
    

    // ================= PAGINATION =================
    public List<ServiceDTO> getServicesByPage(int page, int pageSize) {

        List<ServiceDTO> list = new ArrayList<>();

        String sql = "SELECT * FROM Services "
                + "WHERE IsActive = 1 "
                + "ORDER BY ServiceID "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, (page - 1) * pageSize);
            ps.setInt(2, pageSize);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    ServiceDTO service = new ServiceDTO(
                            rs.getInt("ServiceID"),
                            rs.getString("NameService"),
                            rs.getDouble("Price"),
                            rs.getString("Description"),
                            rs.getInt("TimeService"),
                            rs.getBoolean("IsActive"),
                            rs.getInt("CategoryID"),
                            rs.getString("ImgURL")
                    );
                    list.add(service);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    // ================= COUNT ACTIVE SERVICES =================
    public int getTotalService() {

        String sql = "SELECT COUNT(*) FROM Services WHERE IsActive = 1";

        try (PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    public List<ServiceDTO> getServiceByCategory(int categoryId, int page, int pageSize) {

    List<ServiceDTO> list = new ArrayList<>();

    String sql = "SELECT * FROM Services " +
                 "WHERE CategoryID = ? AND IsActive = 1 " +
                 "ORDER BY ServiceID " +
                 "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    try (PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setInt(1, categoryId);
        ps.setInt(2, (page - 1) * pageSize);
        ps.setInt(3, pageSize);

        try (ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                ServiceDTO service = new ServiceDTO(
                        rs.getInt("ServiceID"),
                        rs.getString("NameService"),
                        rs.getDouble("Price"),
                        rs.getString("Description"),
                        rs.getInt("TimeService"),
                        rs.getBoolean("IsActive"),
                        rs.getInt("CategoryID"),
                        rs.getString("ImgURL")
                );

                list.add(service);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
    public int countServiceByCategory(int categoryId) {

    String sql = "SELECT COUNT(*) FROM Services WHERE CategoryID = ? AND IsActive = 1";

    try (PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setInt(1, categoryId);

        try (ResultSet rs = ps.executeQuery()) {

            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}
}
