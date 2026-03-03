/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.dao.homeDao;

import com.mycompany.catclinicproject.dao.DBContext;
import com.mycompany.catclinicproject.model.DrugDTO;
import com.mycompany.catclinicproject.model.PrescriptionView;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Son
 */
public class DrugDAO extends DBContext{
    
    public List<DrugDTO> getAllDrug() {

    List<DrugDTO> list = new ArrayList<>();

    String sql = "SELECT DrugID, Price, StockQuantity, Unit, Name, IsActive FROM Drugs";

    try {
        PreparedStatement ps = c.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            DrugDTO drug = new DrugDTO(
                rs.getInt("DrugID"),
                rs.getDouble("Price"),
                rs.getInt("StockQuantity"),
                rs.getString("Unit"),
                rs.getString("Name"),
                rs.getBoolean("IsActive")
            );

            list.add(drug);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
   public int createPrescription(int medicalRecordID, String note) {
    int prescriptionID = 0;
    String sql = "INSERT INTO Prescriptions (MedicalRecordID, Note) VALUES (?, ?)";
    try {
        PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setInt(1, medicalRecordID);
        ps.setString(2, note);

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            prescriptionID = rs.getInt(1);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return prescriptionID;
}
    public void insertPrescriptionDrug(int prescriptionID,
                                   int drugID,
                                   int quantity,
                                   String instruction) {

    String sql = "INSERT INTO Prescription_Drug "
               + "(DrugID,PrescriptionID, Quantity, Instruction) "
               + "VALUES (?, ?, ?, ?)";

    try {

        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, drugID);
        ps.setInt(2, prescriptionID);
        ps.setInt(3, quantity);
        ps.setString(4, instruction);

        ps.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    public void updateStockAfterPrescription(int drugID, int quantity) {

    String sql = "UPDATE Drugs "
               + "SET StockQuantity = StockQuantity - ? "
               + "WHERE DrugID = ?";

    try {

        PreparedStatement ps = c.prepareStatement(sql);

        ps.setInt(1, quantity);
        ps.setInt(2, drugID);

        ps.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    public boolean isMedicalRecordExistInPrescription(int medicalRecordID) {

    String sql = "SELECT 1 FROM Prescriptions WHERE MedicalRecordID = ?";

    try (PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setInt(1, medicalRecordID);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return true;   // đã tồn tại
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return false;  
}
    public List<PrescriptionView> getPrescriptionByMedicalRecordID(int medicalRecordID) {

        List<PrescriptionView> list = new ArrayList<>();

        String sql = "SELECT p.MedicalRecordID, p.PrescriptionID, p.Note, "
                   + "pd.DrugID, pd.Quantity, pd.Instruction "
                   + "FROM Prescriptions p "
                   + "JOIN Prescription_Drug pd "
                   + "ON p.PrescriptionID = pd.PrescriptionID "
                   + "WHERE p.MedicalRecordID = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, medicalRecordID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                PrescriptionView p = new PrescriptionView();

                p.setMedicalRecordID(rs.getInt("MedicalRecordID"));
                p.setPrescriptionID(rs.getInt("PrescriptionID"));
                p.setNote(rs.getString("Note"));
                p.setDrugID(rs.getInt("DrugID"));
                p.setQuantity(rs.getInt("Quantity"));
                p.setInstruction(rs.getString("Instruction"));

                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
   
    public int getPrescriptionIDByMedicalRecordID(int medicalRecordID) {

    String sql = "SELECT PrescriptionID FROM Prescriptions WHERE MedicalRecordID = ?";

    try (PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setInt(1, medicalRecordID);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("PrescriptionID");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return -1; 
}
    public void deletePrescriptionDrugByPrescriptionID(int prescriptionID) {

    String sql = "DELETE FROM Prescription_Drug WHERE PrescriptionID = ?";

    try (PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setInt(1, prescriptionID);
        ps.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    public void deletePrescriptionByMedicalRecordID(int medicalRecordID) {

    String sql = "DELETE FROM Prescriptions WHERE MedicalRecordID = ?";

    try (PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setInt(1, medicalRecordID);
        ps.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
}
   

