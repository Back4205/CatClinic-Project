
package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordDAO extends DBContext {



    public List<MedicalHistoryDTO> getMedicalHistoryByCatID(int catId) {
        List<MedicalHistoryDTO> list = new ArrayList<>();

        String sql =
                "SELECT b.BookingID, b.AppointmentDate, u.FullName AS DoctorName, mr.Diagnosis " +
                        "FROM Bookings b " +
                        "JOIN MedicalRecords mr ON b.BookingID = mr.BookingID " +
                        "JOIN Veterinarians v ON b.VetID = v.VetID " +
                        "JOIN Users u ON v.UserID = u.UserID " +
                        "WHERE b.CatID = ? " +
                        "ORDER BY b.AppointmentDate DESC";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, catId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new MedicalHistoryDTO(
                        rs.getInt("BookingID"),
                        rs.getDate("AppointmentDate"),
                        rs.getString("DoctorName"),
                        rs.getString("Diagnosis")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }




    public MedicalRecordDetailDTO getMedicalRecordDetailViewByBookingID(int bookingID) {

        String sql =
                "SELECT mr.RecordID, b.AppointmentDate, u.FullName AS DoctorName, " +
                        "       mr.Diagnosis, mr.TreatmentPlan " +
                        "FROM MedicalRecords mr " +
                        "JOIN Bookings b ON mr.BookingID = b.BookingID " +
                        "JOIN Veterinarians v ON b.VetID = v.VetID " +
                        "JOIN Users u ON v.UserID = u.UserID " +
                        "WHERE mr.BookingID = ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, bookingID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new MedicalRecordDetailDTO(
                        rs.getInt("RecordID"),
                        rs.getDate("AppointmentDate"),
                        rs.getString("DoctorName"),
                        rs.getString("Diagnosis"),
                        rs.getString("TreatmentPlan")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public List<Service> getServiceByBookingID(int bookingID) {

        List<Service> list = new ArrayList<>();

        String sql =
                "SELECT s.* " +
                        "FROM Services s " +
                        "JOIN BookingDetails bd ON s.ServiceID = bd.ServiceID " +
                        "WHERE bd.BookingID = ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, bookingID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Service(
                        rs.getInt("ServiceID"),
                        rs.getString("ServiceName"),
                        rs.getDouble("Price"),
                        rs.getString("Description"),
                        rs.getInt("TimeService"),
                        rs.getBoolean("IsActive")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public List<PrescriptionDrugDTO> getPrescriptionDrugByBookingID(int bookingID) {

        List<PrescriptionDrugDTO> list = new ArrayList<>();

        String sql =
                "SELECT d.Price, d.DrugName, d.Unit, pd.Quantity, pd.UsageInstruction " +
                        "FROM PrescriptionDetails pd " +
                        "JOIN Drugs d ON pd.DrugID = d.DrugID " +
                        "JOIN Prescriptions p ON pd.PrescriptionID = p.PrescriptionID " +
                        "JOIN MedicalRecords mr ON p.RecordID = mr.RecordID " +
                        "WHERE mr.BookingID = ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, bookingID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new PrescriptionDrugDTO(

                        rs.getString("DrugName"),
                        rs.getString("Unit"),
                        rs.getInt("Quantity"),
                        rs.getString("UsageInstruction"),
                        rs.getDouble("Price")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<MedicalStaffWorkDTO> getStaffWorkByBookingID(int bookingID) {

        List<MedicalStaffWorkDTO> list = new ArrayList<>();

        String sql =
                "SELECT " +
                        "   us.FullName AS StaffName, " +
                        "   st.Position AS StaffRole, " +
                        "   to2.TestName, " +
                        "   to2.ResultImage " +
                        "FROM Bookings b " +
                        "LEFT JOIN Staffs st ON b.StaffID = st.StaffID " +
                        "LEFT JOIN Users us ON st.UserID = us.UserID " +
                        "LEFT JOIN MedicalRecords mr ON mr.BookingID = b.BookingID " +
                        "LEFT JOIN TestOrders to2 " +
                        "       ON to2.RecordID = mr.RecordID " +
                        "       AND to2.StaffID = b.StaffID " +
                        "WHERE b.BookingID = ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, bookingID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new MedicalStaffWorkDTO(
                        rs.getString("StaffName"),
                        rs.getString("StaffRole"),
                        rs.getString("TestName"),
                        rs.getString("ResultImage")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
        public static void main( String[] args){
        MedicalRecordDAO dao = new MedicalRecordDAO();
        List<MedicalStaffWorkDTO> list = dao.getStaffWorkByBookingID(1);
        for(MedicalStaffWorkDTO mw:list){
            System.out.println(mw.getResultImage()+" , " + mw.getStaffName() +" , " + mw.getTestName() + " , " + mw.getStaffRole());
        }

        }



}


