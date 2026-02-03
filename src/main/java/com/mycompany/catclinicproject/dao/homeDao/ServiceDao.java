package com.mycompany.catclinicproject.dao.homeDao;

import com.mycompany.catclinicproject.dao.DBContext;
import com.mycompany.catclinicproject.model.Service;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ServiceDao extends DBContext {

    // Lấy tất cả service
    public List<Service> getAllService() {
        List<Service> list = new ArrayList<>();
        String sql = "SELECT * FROM Services Where isActive = 1";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
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
        }

        return list;
    }

    public Service getServiceById(int id) {
        String sql = "SELECT * FROM Service WHERE ServiceID = ?";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Service(
                        rs.getInt("ServiceID"),
                        rs.getString("NameService"),
                        rs.getDouble("Price"),
                        rs.getString("Description"),
                        rs.getInt("TimeService"),
                        rs.getBoolean("IsActive")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insert(Service s) {
        String sql = "INSERT INTO Service(NameService, Price, Description, TimeService, IsActive) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, s.getNameService());
            ps.setDouble(2, s.getPrice());
            ps.setString(3, s.getDescription());
            ps.setInt(4, s.getTimeService());
            ps.setBoolean(5, s.isIsActive());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Service s) {
        String sql = "UPDATE Service SET NameService=?, Price=?, Description=?, "
                   + "TimeService=?, IsActive=? WHERE ServiceID=?";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, s.getNameService());
            ps.setDouble(2, s.getPrice());
            ps.setString(3, s.getDescription());
            ps.setInt(4, s.getTimeService());
            ps.setBoolean(5, s.isIsActive());
            ps.setInt(6, s.getServiceID());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Xóa service
    public void delete(int id) {
        String sql = "DELETE FROM Service WHERE ServiceID=?";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
