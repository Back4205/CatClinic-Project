package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.Cat;
import com.mycompany.catclinicproject.model.Owner;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CatDAO extends DBContext {


    public List<Cat> getCatList() {
        List<Cat> catList = new ArrayList<Cat>();
        String sql = "SELECT * FROM Cats where  IsActive = 1";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Cat cat = new Cat();
                cat.setCatID(rs.getInt("catID"));
                cat.setOwnerID(rs.getInt("ownerID"));
                cat.setName(rs.getString("name"));
                cat.setGender(rs.getInt("gender"));
                cat.setBreed(rs.getString("breed"));
                cat.setAge(rs.getInt("age"));
                cat.setImg(rs.getString("Image"));
                cat.setIsActive(rs.getInt("IsActive"));
                catList.add(cat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return catList;

    }

    public List<Cat> getCatsByOwnerID(int ownerID) {
        List<Cat> catList = new ArrayList<Cat>();
        String sql = "SELECT * FROM Cats WHERE ownerID = ? AND IsActive = 1";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, ownerID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Cat cat = new Cat();
                cat.setCatID(rs.getInt("catID"));
                cat.setOwnerID(rs.getInt("ownerID"));
                cat.setName(rs.getString("name"));
                cat.setGender(rs.getInt("gender"));
                cat.setBreed(rs.getString("breed"));
                cat.setAge(rs.getInt("age"));
                cat.setImg(rs.getString("Image"));
                cat.setIsActive(rs.getInt("IsActive"));
                catList.add(cat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return catList;
    }

    public boolean addCat(Cat cat) {
        String sql = "INSERT INTO Cats (ownerID, name, gender, breed, age,Image,IsActive ) VALUES (?, ?, ?, ?, ?,?,1)";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, cat.getOwnerID());
            ps.setString(2, cat.getName());
            ps.setInt(3, cat.getGender());
            ps.setString(4, cat.getBreed());
            ps.setInt(5, cat.getAge());
            ps.setString(6, cat.getImg());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCat(Cat cat) {
        String sql = "UPDATE Cats SET  name = ?,  age = ? , Image = ? WHERE catID = ?";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, cat.getName());
            ps.setInt(2, cat.getAge());
            ps.setString(3, cat.getImg());
            ps.setInt(4, cat.getCatID());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean hasBooking(int catID) {
        String sql = "SELECT 1 FROM Bookings WHERE CatID = ? " ;

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, catID);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteOrDeactivateCat(int catID) {


        if (hasBooking(catID)) {
            String sql = "UPDATE Cats SET isActive = 0 WHERE catID = ?";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, catID);
                return ps.executeUpdate() > 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else {
            String sql = "DELETE FROM Cats WHERE catID = ?";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, catID);
                return ps.executeUpdate() > 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }


    public List<Cat> filterAndPagingCats(int ownerID, String name, String gender, String breed, int age, int numberItemPerPage , int indexPage) {
        List<Cat> list = new ArrayList<>();

        int offset = (indexPage - 1) * numberItemPerPage;

        StringBuilder sql = new StringBuilder("SELECT * FROM Cats WHERE IsActive =1");
        List<Object> params = new ArrayList<>();


        if (ownerID > 0) {
            sql.append(" AND ownerID = ?");
            params.add(ownerID);
        }


        if (name != null && !name.isEmpty()) {
            sql.append(" AND name LIKE ?");
            params.add("%" + name + "%");
        }


        if (gender != null && !gender.isEmpty()) {
            sql.append(" AND gender = ?");
            params.add(Integer.parseInt(gender));
        }


        if (breed != null && !breed.isEmpty()) {
            sql.append(" AND breed LIKE ?");
            params.add("%" + breed + "%");
        }


        if (age != -1) {
            if (age == 3) {
                sql.append(" AND age >= 3");
            } else {
                sql.append(" AND age = ?");
                params.add(age);
            }
        }


        sql.append(" ORDER BY catID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(offset);
        params.add(numberItemPerPage);

        try (PreparedStatement ps = c.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Cat cat = new Cat();
                cat.setCatID(rs.getInt("catID"));
                cat.setOwnerID(rs.getInt("ownerID"));
                cat.setName(rs.getString("name"));
                cat.setGender(rs.getInt("gender"));
                cat.setBreed(rs.getString("breed"));
                cat.setAge(rs.getInt("age"));
                    cat.setImg(rs.getString("Image"));
                cat.setIsActive(rs.getInt("IsActive"));
                list.add(cat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }



    public Cat getCatByID(int catID) {
        String sql = "SELECT * FROM Cats WHERE catID = ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, catID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Cat cat = new Cat();
                cat.setCatID(rs.getInt("catID"));
                cat.setOwnerID(rs.getInt("ownerID"));
                cat.setName(rs.getString("name"));
                cat.setGender(rs.getInt("gender"));
                cat.setBreed(rs.getString("breed"));
                cat.setAge(rs.getInt("age"));
                cat.setImg(rs.getString("Image"));
                cat.setIsActive(rs.getInt("IsActive"));
                return cat;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public int countCatsWithFilter(int ownerID, String name, String gender, String breed, int age) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) AS total FROM Cats WHERE IsActive =1"
        );

        List<Object> params = new ArrayList<>();


        if (ownerID > 0) {
            sql.append(" AND ownerID = ?");
            params.add(ownerID);
        }


        if (name != null && !name.trim().isEmpty()) {
            sql.append(" AND name LIKE ?");
            params.add("%" + name.trim() + "%");
        }

        if (gender != null && !gender.isEmpty()) {
            sql.append(" AND gender = ?");
            params.add(Integer.parseInt(gender));
        }

        if (breed != null && !breed.trim().isEmpty()) {
            sql.append(" AND breed LIKE ?");
            params.add("%" + breed.trim() + "%");
        }

        if (age != -1) {
            if (age == 3) {
                sql.append(" AND age >= 3");
            } else {
                sql.append(" AND age = ?");
                params.add(age);
            }
        }

        try (PreparedStatement ps = c.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public boolean checkCatNameExistbyOwnerID(int ownerId , String name) {
        String sql = "SELECT 1 FROM Cats WHERE ownerID = ? AND name = ?";
        try{
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, ownerId);
            ps.setString(2, name);
            ResultSet rs = ps.executeQuery();
            return   rs.next();

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean checkCatNameExistUpdate(int ownerId , String name , int catId) {
        String sql = "SELECT 1 FROM Cats WHERE ownerID = ? AND name = ? and catID <> ?";
        try{
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, ownerId);
            ps.setString(2, name);
            ps.setInt(3, catId);
            ResultSet rs = ps.executeQuery();
            return   rs.next();

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public Owner getOwnerByUserId(int userID) {
        String sql = "SELECT * FROM Owners WHERE UserID = ?";
        try  {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Owner(
                        rs.getInt("OwnerID"),
                        rs.getInt("UserID"),
                        rs.getString("Address")
                );
            }
        }catch (Exception e){
            
        }
        return null;
    }


    public static void main(String[] args) {
        CatDAO catDAO = new CatDAO();
        List<Cat> cats = catDAO.getCatList();
        int count = 0;
        for (Cat cat : cats) {
          count ++;
        }
        System.out.println(count);
    }

}





