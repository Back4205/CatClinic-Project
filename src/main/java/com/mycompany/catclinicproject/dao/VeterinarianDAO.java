/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.VeteNameID;
import com.mycompany.catclinicproject.model.VeterinarianDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class VeterinarianDAO extends DBContext{
    public List<VeterinarianDTO> getActiveVeterinarians() {
        List<VeterinarianDTO> list = new ArrayList<>();

        String sql = "SELECT "
                + "u.UserID, "
                + "u.UserName, "
                + "u.PassWord, "
                + "u.FullName, "
                + "u.Male, "
                + "u.Email, "
                + "u.RoleID, "
                + "u.IsActive, "
                + "u.Phone, "
                + "u.GoogleID, "
                + "v.VetID, "
                + "v.Degree, "
                + "v.ExperienceYear, "
                + "v.Bio, "
                + "v.Image "
                + "FROM Users u "
                + "INNER JOIN Veterinarians v ON u.UserID = v.UserID "
                + "WHERE u.IsActive = 1";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                VeterinarianDTO vet = new VeterinarianDTO();

                // ===== Users =====
                vet.setUserID(rs.getInt("UserID"));
                vet.setUserName(rs.getString("UserName"));
                vet.setPassword(rs.getString("PassWord"));
                vet.setFullName(rs.getString("FullName"));
                vet.setMale(rs.getBoolean("Male"));
                vet.setEmail(rs.getString("Email"));
                vet.setRoleID(rs.getInt("RoleID"));
                vet.setIsActive(rs.getBoolean("IsActive"));
                vet.setPhone(rs.getString("Phone"));
                vet.setGoogleID(rs.getString("GoogleID"));

                // ===== Veterinarian =====
                vet.setVetID(rs.getInt("VetID"));
                vet.setDegree(rs.getString("Degree"));
                vet.setExperienceYear(rs.getInt("ExperienceYear"));
                vet.setBio(rs.getString("Bio"));
                vet.setImage(rs.getString("Image"));

                list.add(vet);
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public VeterinarianDTO getVetProfileByVetID(int vetID) {

        VeterinarianDTO vet = null;

        String sql = "SELECT "
                + "u.UserID, "
                + "u.UserName, "
                + "u.Password, "
                + "u.FullName, "
                + "u.Male, "
                + "u.Email, "
                + "u.RoleID, "
                + "u.IsActive, "
                + "u.Phone, "
                + "u.GoogleID, "
                + "v.VetID, "
                + "v.Degree, "
                + "v.ExperienceYear, "
                + "v.Bio, "
                + "v.Image "
                + "FROM Users u "
                + "INNER JOIN Veterinarians v ON u.UserID = v.UserID "
                + "WHERE v.VetID = ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, vetID);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                vet = new VeterinarianDTO();

                vet.setUserID(rs.getInt("UserID"));
                vet.setUserName(rs.getString("UserName"));
                vet.setPassword(rs.getString("Password"));
                vet.setFullName(rs.getString("FullName"));
                vet.setMale(rs.getBoolean("Male"));
                vet.setEmail(rs.getString("Email"));
                vet.setRoleID(rs.getInt("RoleID"));
                vet.setIsActive(rs.getBoolean("IsActive"));
                vet.setPhone(rs.getString("Phone"));
                vet.setGoogleID(rs.getString("GoogleID"));

                vet.setVetID(rs.getInt("VetID"));
                vet.setDegree(rs.getString("Degree"));
                vet.setExperienceYear(rs.getInt("ExperienceYear"));
                vet.setBio(rs.getString("Bio"));
                vet.setImage(rs.getString("Image"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return vet;
    }
    public List<VeteNameID> getAllVets() {
    List<VeteNameID> list = new ArrayList<>();

    String sql = "SELECT v.VetID, u.FullName "
               + "FROM Veterinarians v "
               + "JOIN Users u ON v.UserID = u.UserID";

    try {
        PreparedStatement ps = c.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            VeteNameID v = new VeteNameID();
            v.setVetID(rs.getInt("VetID"));
            v.setFullName(rs.getString("FullName"));
            list.add(v);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
}
