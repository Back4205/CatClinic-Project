/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.dao.DBContext;
import com.mycompany.catclinicproject.model.DrugVeteCompletedDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Son
 */
public class MedicalRecordVeterinarianDao  extends DBContext{
    
  public int countUncompletedTestOrders(int medicalRecordID) {

    int total = 0;
    String sql = "SELECT COUNT(*) "
               + "FROM TestOrders "
               + "WHERE MedicalRecordID = ? "
               + "AND Status <> 'Complete'";

    try (PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setInt(1, medicalRecordID);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            total = rs.getInt(1);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return total;
}
  public List<DrugVeteCompletedDTO> getDrugsByMedicalRecordID(int medicalRecordID) {

    List<DrugVeteCompletedDTO> list = new ArrayList<>();

    String sql = "SELECT pd.DrugID, pd.Quantity "
               + "FROM Prescription_Drug pd "
               + "JOIN Prescriptions p "
               + "ON pd.PrescriptionID = p.PrescriptionID "
               + "WHERE p.MedicalRecordID = ?";

    try (PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setInt(1, medicalRecordID);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            int drugID = rs.getInt("DrugID");
            int quantity = rs.getInt("Quantity");

            list.add(new DrugVeteCompletedDTO(drugID, quantity));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
}
