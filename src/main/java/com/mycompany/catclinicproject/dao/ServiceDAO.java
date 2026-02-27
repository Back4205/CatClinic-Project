package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.Service;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO extends DBContext {
    public List<Service> getAllServices() {
        List<Service> list = new ArrayList<>();

        String sql = "SELECT ServiceID, NameService, Price, TimeService, IsActive FROM Services WHERE IsActive = 1";

        try (PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Service s = new Service();
                s.setServiceID(rs.getInt("ServiceID"));
                s.setNameService(rs.getString("NameService"));
                s.setPrice(rs.getDouble("Price"));
                s.setTimeService(rs.getInt("TimeService"));
                s.setIsActive(rs.getBoolean("IsActive")); 

                list.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
public void insertService(Service s) {
    String sql = "INSERT INTO Services (NameService, Price, Description, TimeService, isActive) "
               + "VALUES (?, ?, ?, ?, ?)";

    try {
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, s.getNameService());
        ps.setDouble(2, s.getPrice());
        ps.setString(3, s.getDescription());
        ps.setInt(4, s.getTimeService());
        ps.setBoolean(5, s.isIsActive());

        ps.executeUpdate();
        System.out.println("INSERT SERVICE SUCCESS");

    } catch (Exception e) {
        System.out.println("INSERT SERVICE FAILED");
        e.printStackTrace();
    } finally {
        closeConnection();
    }
}

    public Service getServiceById(int id) {
        String sql = " SELECT ServiceID, NameService, Price, Description, TimeService,  IsActive, CategoryID, ImgURL  FROM Services  WHERE ServiceID = ? ";

        try (
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Service(
                            rs.getInt("ServiceID"),
                            rs.getString("NameService"),
                            rs.getDouble("Price"),          // chú ý: cột tên là Price (viết hoa P)
                            rs.getString("Description"),
                            rs.getInt("TimeService"),
                            rs.getBoolean("IsActive"),
                            rs.getInt("CategoryID"),
                            rs.getString("ImgURL")
                    );
                }
            }
        } catch (SQLException e) {
            // Nên log thay vì chỉ printStackTrace
            e.printStackTrace(); // tạm thời, production → dùng Logger
            // Hoặc: throw new RuntimeException("Lỗi lấy dịch vụ ID " + id, e);
        }

        return null; // không tìm thấy hoặc lỗi → trả null
    }
    public void updateService(Service s) {
    String sql = "UPDATE Services "
               + "SET NameService = ?, price = ?, description = ?, timeService = ? "
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

        String sql = "SELECT * FROM Services WHERE NameService LIKE ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, "%" + nameService + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Service s = new Service();
                s.setServiceID(rs.getInt("ServiceID"));
                s.setNameService(rs.getString("NameService"));
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
        sql += " AND NameService LIKE ? ";
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
            s.setNameService(rs.getString("NameService"));
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
        sql += " AND NameService LIKE ? ";
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
    public List<Service> getServicesByCategoryID(int categoryID) {
        List<Service> list = new ArrayList<>();  // hoặc new ArrayList<Service>()

        String sql = " SELECT ServiceID, NameService, Price, Description, TimeService, IsActive, CategoryID, ImgURL FROM Services WHERE CategoryID = ? AND IsActive = 1 ORDER BY NameService ASC ";

        try (
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, categoryID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Service s = new Service();
                    s.setServiceID(rs.getInt("ServiceID"));
                    s.setNameService(rs.getString("NameService"));
                    s.setPrice(rs.getDouble("Price"));
                    s.setDescription(rs.getString("Description"));
                    s.setTimeService(rs.getInt("TimeService"));
                    s.setIsActive(rs.getBoolean("IsActive"));
                    s.setCategoryID(rs.getInt("CategoryID"));
                    s.setImgURL(rs.getString("ImgURL"));

                    list.add(s);
                }
            }
        } catch (SQLException e) {
            // Nên log thay vì printStackTrace
            // Logger.getLogger(getClass().getName()).severe("Lỗi lấy dịch vụ: " + e.getMessage());
            e.printStackTrace(); // tạm thời giữ nếu chưa có logger
        }

        return list;
    }

}
