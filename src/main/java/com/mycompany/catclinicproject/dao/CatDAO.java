package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.Cat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CatDAO extends DBContext {


    public List<Cat> getCatList() {
        List<Cat> catList = new ArrayList<Cat>();
        String sql = "SELECT * FROM Cats";
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
                catList.add(cat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return catList;

    }

    public List<Cat> getCatsByOwnerID(int ownerID) {
        List<Cat> catList = new ArrayList<Cat>();
        String sql = "SELECT * FROM Cats WHERE ownerID = ?";
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
                catList.add(cat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return catList;
    }

    public boolean addCat(Cat cat) {
        String sql = "INSERT INTO Cats (ownerID, name, gender, breed, age) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, cat.getOwnerID());
            ps.setString(2, cat.getName());
            ps.setInt(3, cat.getGender());
            ps.setString(4, cat.getBreed());
            ps.setInt(5, cat.getAge());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCat(Cat cat) {
        String sql = "UPDATE Cats SET  name = ?,  age = ? WHERE catID = ?";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, cat.getName());
            ps.setInt(2, cat.getAge());
            ps.setInt(3, cat.getCatID());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCat(int catID) {
        String sql = "DELETE FROM Cats WHERE catID = ?";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, catID);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Cat> filterAndPagingCats(int ownerID, String name, String gender, String breed, int age, int numberItemPerPage , int indexPage) {
        List<Cat> list = new ArrayList<>();

        int offset = (indexPage - 1) * numberItemPerPage;

        StringBuilder sql = new StringBuilder("SELECT * FROM Cats WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // Owner filter (Admin / Staff: ownerID = 0 → bỏ qua)
        if (ownerID > 0) {
            sql.append(" AND ownerID = ?");
            params.add(ownerID);
        }

        // Name
        if (name != null && !name.isEmpty()) {
            sql.append(" AND name LIKE ?");
            params.add("%" + name + "%");
        }

        // Gender
        if (gender != null && !gender.isEmpty()) {
            sql.append(" AND gender = ?");
            params.add(Integer.parseInt(gender));
        }

        // Breed
        if (breed != null && !breed.isEmpty()) {
            sql.append(" AND breed LIKE ?");
            params.add("%" + breed + "%");
        }

        // Age
        if (age != -1) {
            if (age == 3) {
                sql.append(" AND age >= 3");
            } else {
                sql.append(" AND age = ?");
                params.add(age);
            }
        }

        // Paging (SQL Server)
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
                list.add(cat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean hasBooking(int catID) {
        String sql = "SELECT 1 FROM Booking WHERE CatID = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, catID);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // có dòng => có booking
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
                return cat;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public int countCatsWithFilter(int ownerID, String name, String gender, String breed, int age) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) AS total FROM Cats WHERE 1=1"
        );

        List<Object> params = new ArrayList<>();

        // Owner (Admin/Staff: ownerID <= 0 thì bỏ)
        if (ownerID > 0) {
            sql.append(" AND ownerID = ?");
            params.add(ownerID);
        }

        // Name
        if (name != null && !name.trim().isEmpty()) {
            sql.append(" AND name LIKE ?");
            params.add("%" + name.trim() + "%");
        }

        // Gender
        if (gender != null && !gender.isEmpty()) {
            sql.append(" AND gender = ?");
            params.add(Integer.parseInt(gender));
        }

        // Breed
        if (breed != null && !breed.trim().isEmpty()) {
            sql.append(" AND breed LIKE ?");
            params.add("%" + breed.trim() + "%");
        }

        // Age
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


//    public List<Cat> pagdingCats(int index , int ownerID) {
//        List<Cat> catList = new ArrayList<Cat>();
//        String sql = "SELECT * FROM Cats where OwnerID = ? ORDER BY catID OFFSET ? ROWS FETCH NEXT 5 ROWS ONLY";
//        try {
//            PreparedStatement ps = c.prepareStatement(sql);
//            ps.setInt(1, ownerID);
//            ps.setInt(2, (index - 1) * 5);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                Cat cat = new Cat();
//                cat.setCatID(rs.getInt("catID"));
//                cat.setOwnerID(rs.getInt("ownerID"));
//                cat.setName(rs.getString("name"));
//                cat.setGender(rs.getInt("gender"));
//                cat.setBreed(rs.getString("breed"));
//                cat.setAge(rs.getInt("age"));
//                catList.add(cat);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return catList;
//    }


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





