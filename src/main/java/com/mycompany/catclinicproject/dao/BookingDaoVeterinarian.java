/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.dao;
import com.mycompany.catclinicproject.model.VeterinarianDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
/**
 *
 * @author ADMIN
 */
public class BookingDaoVeterinarian extends DBContext{
    public VeterinarianDTO getVetProfile(int userID) {
        VeterinarianDTO v = null;

        String sql = "SELECT u.UserID, u.FullName, u.Male, u.Email, u.Phone, " +
                     "v.VetID, v.Degree, v.ExperienceYear, v.Bio, v.Image " +
                     "FROM Users u JOIN Veterinarians v ON u.UserID = v.UserID " +
                     "WHERE u.UserID = ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                v = new VeterinarianDTO();

                v.setUserID(rs.getInt("UserID"));
                v.setVetID(rs.getInt("VetID"));
                v.setFullName(rs.getString("FullName"));
                v.setMale(rs.getBoolean("Male"));
                v.setEmail(rs.getString("Email"));
                v.setPhone(rs.getString("Phone"));

                v.setDegree(rs.getString("Degree"));
                v.setExperienceYear(rs.getInt("ExperienceYear"));
                v.setBio(rs.getString("Bio"));
                v.setImage(rs.getString("Image"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return v;
    }
    public void updateVetProfile(VeterinarianDTO v) {

        String sqlUser = "UPDATE Users SET FullName=?, Male=?, Email=?, Phone=? WHERE UserID=?";
        String sqlVet = "UPDATE Veterinarians SET Degree=?, ExperienceYear=?, Bio=?, Image=? WHERE UserID=?";

        try {
            c.setAutoCommit(false);

            // update Users
            PreparedStatement ps1 = c.prepareStatement(sqlUser);
            ps1.setString(1, v.getFullName());
            ps1.setBoolean(2, v.getMale());
            ps1.setString(3, v.getEmail());
            ps1.setString(4, v.getPhone());
            ps1.setInt(5, v.getUserID());
            ps1.executeUpdate();

            // update Veterinarians
            PreparedStatement ps2 = c.prepareStatement(sqlVet);
            ps2.setString(1, v.getDegree());
            ps2.setInt(2, v.getExperienceYear());
            ps2.setString(3, v.getBio());
            ps2.setString(4, v.getImage());
            ps2.setInt(5, v.getUserID());
            ps2.executeUpdate();

            c.commit();

        } catch (Exception e) {
            try {
                c.rollback();
            } catch (Exception ex) {}
            e.printStackTrace();
        } finally {
            try {
                c.setAutoCommit(true);
            } catch (Exception e) {}
        }
    }
}
