package com.mycompany.catclinicproject.dao;



import com.mycompany.catclinicproject.model.Category;
import com.mycompany.catclinicproject.model.Service;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO extends DBContext {
    public List<Service> getServicesByCategoryID(int CategoryID) {
        List<Service> list = new ArrayList<>();
        Service s = null;
        String sql = "select * from Services Where CategoryID = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)){
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
        String sql = "INSERT INTO Services\n" +
"            (NameService, Price, Description, TimeService, IsActive, CategoryID,ImgURL)\n" +
"            VALUES (?, ?, ?, ?, ?, ?, ?)"
            
        ;

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
        String sql = "UPDATE Services\n" +
"            SET NameService = ?,\n" +
"                Price = ?,\n" +
"                Description = ?,\n" +
"                TimeService = ?,\n" +
"                CategoryID = ?,\n" +
"                ImgURL = ?\n" +
"            WHERE ServiceID = ?"
            
        ;

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
    public void InsertImgService(String ImgName,String ImgURL,int ServiceID){
        String sql = "INSERT INTO ImgServices\n" +
"            (ImgName,ImgURL,ServiceID)\n" +
"            VALUES (?,?, ?)";
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
    
}

