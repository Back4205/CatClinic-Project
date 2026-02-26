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

        try (PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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
        try { c.rollback(); } catch (Exception ex) {}
        e.printStackTrace();
    } finally {
        closeConnection();
    }
}


public void insertService(AddServiceDTO s) {

        String sqlService =
                "INSERT INTO Services "
              + "(ServiceName, Price, Description, TimeService, IsActive, CategoryID) "
              + "VALUES (?, ?, ?, ?, ?, ?)";

        String sqlImg =
                "INSERT INTO ImgServices "
              + "(ImgURL, IsActive, ServiceID) "
              + "VALUES (?, ?, ?)";

        try {
            // ===== START TRANSACTION =====
            c.setAutoCommit(false);

            // ===== INSERT SERVICE =====
            PreparedStatement psService =
                    c.prepareStatement(sqlService, Statement.RETURN_GENERATED_KEYS);

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
                if (c != null) c.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();

        } finally {
            closeConnection();
        }
}


    public Service getServiceById(int id) {
    String sql = "SELECT * FROM Services WHERE ServiceID = ?";
    Service s = null;

    try {
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            s = new Service(
                    rs.getInt("ServiceID"),
                    rs.getString("ServiceName"),
                    rs.getDouble("price"),
                    rs.getString("description"),
                    rs.getInt("timeService"),
                    rs.getBoolean("isActive"),
                    rs.getInt("CategoryID")
            );
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        closeConnection();
    }
    return s;
}
    public void updateService(Service s) {
    String sql = "UPDATE Services "
               + "SET ServiceName = ?, price = ?, description = ?, timeService = ? "
               + "WHERE serviceID = ?";
    
    try {
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, s.getNameService());
        ps.setDouble(2, s.getPrice());
        ps.setString(3, s.getDescription());
        ps.setInt(4, s.getTimeService());
        ps.setInt(5, s.getServiceID());

        ps.executeUpdate();
        System.out.println("UPDATE SERVICE SUCCESS");

    } catch (Exception e) {
        System.out.println("UPDATE SERVICE FAILED");
        e.printStackTrace();
    } finally {
        closeConnection();
    }
}

    public void updateServiceStatus(int serviceID, boolean isActive) {
    String sql = "UPDATE Services SET isActive = ? WHERE serviceID = ?";

    try {
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setBoolean(1, isActive);
        ps.setInt(2, serviceID);

        ps.executeUpdate();
        System.out.println("UPDATE SERVICE STATUS SUCCESS");

    } catch (Exception e) {
        System.out.println("UPDATE SERVICE STATUS FAILED");
        e.printStackTrace();
    } finally {
        closeConnection();
    }
}
     public ArrayList<Service> searchByName(String nameService) {
        ArrayList<Service> list = new ArrayList<>();

        String sql = "SELECT * FROM Services WHERE ServiceName LIKE ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, "%" + nameService + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Service s = new Service();
                s.setServiceID(rs.getInt("ServiceID"));
                s.setNameService(rs.getString("ServiceName"));
                s.setPrice(rs.getDouble("Price"));
                s.setDescription(rs.getString("Description"));
                s.setTimeService(rs.getInt("TimeService"));
                s.setIsActive(rs.getBoolean("IsActive"));
                list.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(); 
        }

        return list;
    }
    public ArrayList<Service> search(
        String name,
        Boolean status,
        String sortPrice,
        int page,
        int pageSize) {

    ArrayList<Service> list = new ArrayList<>();

    String sql = "SELECT * FROM Services WHERE 1=1 ";

    if (name != null && !name.isEmpty()) {
        sql += " AND ServiceName LIKE ? ";
    }

    if (status != null) {
        sql += " AND isActive = ? ";
    }

    if ("asc".equals(sortPrice)) {
        sql += " ORDER BY price ASC ";
    } else if ("desc".equals(sortPrice)) {
        sql += " ORDER BY price DESC ";
    } else {
        sql += " ORDER BY serviceID ";
    }

    sql += " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";

    try {
        PreparedStatement ps = c.prepareStatement(sql);
        int index = 1;

        if (name != null && !name.isEmpty()) {
            ps.setString(index++, "%" + name + "%");
        }

        if (status != null) {
            ps.setBoolean(index++, status);
        }

        ps.setInt(index++, (page - 1) * pageSize);
        ps.setInt(index++, pageSize);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Service s = new Service();
            s.setServiceID(rs.getInt("serviceID"));
            s.setNameService(rs.getString("ServiceName"));
            s.setPrice(rs.getDouble("price"));
            s.setTimeService(rs.getInt("timeService"));
            s.setIsActive(rs.getBoolean("isActive"));
            list.add(s);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}

    public int count(String name, Boolean status) {
    String sql = "SELECT COUNT(*) FROM Services WHERE 1=1 ";

    if (name != null && !name.isEmpty()) {
        sql += " AND ServiceName LIKE ? ";
    }

    if (status != null) {
        sql += " AND isActive = ? ";
    }

    try {
        PreparedStatement ps = c.prepareStatement(sql);
        int index = 1;

        if (name != null && !name.isEmpty()) {
            ps.setString(index++, "%" + name + "%");
        }

        if (status != null) {
            ps.setBoolean(index++, status);
        }

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}

     
}
