//package com.mycompany.catclinicproject.dao;
//
//import com.mycompany.catclinicproject.model.*;
//
//import java.sql.PreparedStatement;
//import java.util.List;
//
//public class MedicalRecordDAO extends DBContext {
//
//    public List<MedicalHistoryView> getMedicalHistoryByCatID(int catID) {
//        String sql =
//                " SELECT  b.BookingID, b.AppointmentDate, u.FullName AS DoctorName, mr.ClinicalNote" +
//                        " FROM Booking b" +
//                        " JOIN MedicalRecords mr ON b.BookingID = mr.BookingID" +
//                        "  JOIN Veterinarians v ON b.VeterinarianID = v.VetID" +
//                        " JOIN Users u ON v.UserID = u.UserID" +
//                        " WHERE b.CatID = ?" +
//                        " ORDER BY b.AppointmentDate DESC";
//
//
//        List<MedicalHistoryView> medicalHistoryList = new java.util.ArrayList<>();
//       try {
//            java.sql.PreparedStatement ps = c.prepareStatement(sql);
//            ps.setInt(1, catID);
//            java.sql.ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                MedicalHistoryView mhv = new MedicalHistoryView();
//                mhv.setBookingID(rs.getInt("BookingID"));
//                mhv.setVisitDate(rs.getDate("AppointmentDate").toLocalDate());
//                mhv.setDoctorName(rs.getString("DoctorName"));
//                mhv.setClinicalNote(rs.getString("ClinicalNote"));
//                medicalHistoryList.add(mhv);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//    }
//        return medicalHistoryList;
//    }
//
//
//
//    public Booking getBookingByID(int bookingID) {
//        String sql = "SELECT * FROM Booking WHERE BookingID = ?";
//        Booking booking = null;
//        try {
//            java.sql.PreparedStatement ps = c.prepareStatement(sql);
//            ps.setInt(1, bookingID);
//            java.sql.ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                booking = new Booking();
//                booking.setBookingID(rs.getInt("BookingID"));
//                booking.setCatID(rs.getInt("CatID"));
//                booking.setVeterinarianID(rs.getInt("VeterinarianID"));
//                booking.setAppointmentDate(rs.getTimestamp("AppointmentDate").toLocalDateTime());
//
//                booking.setStatus(rs.getString("Status"));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return booking;
//    }
//
//    public MedicalRecordDetailView getMedicalRecordDetailViewByBookingID(int bookingID) {
//        String sql = "SELECT \n" +
//                "    mr.MedicalRecordID,\n" +
//                "    b.AppointmentDate,\n" +
//                "    u.FullName AS DoctorName,\n" +
//                "    mr.Diagnosis,\n" +
//                "    mr.ClinicalNote\n" +
//                "FROM MedicalRecords mr\n" +
//                "JOIN Booking b ON mr.BookingID = b.BookingID\n" +
//                "JOIN Veterinarians v ON b.VeterinarianID = v.VetID\n" +
//                "JOIN Users u ON v.UserID = u.UserID\n" +
//                "WHERE mr.BookingID = ?";
//        MedicalRecords medicalRecords = null;
//        try {
//            PreparedStatement ps = c.prepareStatement(sql);
//            ps.setInt(1, bookingID);
//            java.sql.ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                MedicalRecordDetailView mrdv = new MedicalRecordDetailView();
//                mrdv.setMedicalRecordID(rs.getInt("MedicalRecordID"));
//                mrdv.setVisitDate(rs.getDate("AppointmentDate"));
//                mrdv.setDoctorName(rs.getString("DoctorName"));
//                mrdv.setDiagnosis(rs.getString("Diagnosis"));
//                mrdv.setClinicalNote(rs.getString("ClinicalNote"));
//                return mrdv;
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//    public Prescription getPrescriptionByMedicalRecordID(int medicalRecordID){
//        String sql = "SELECT * FROM Prescription WHERE MedicalRecordID = ?";
//        Prescription prescription = null;
//        try {
//            PreparedStatement ps = c.prepareStatement(sql);
//            ps.setInt(1, medicalRecordID);
//            java.sql.ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                prescription = new Prescription();
//                prescription.setPrescriptionID(rs.getInt("PrescriptionID"));
//                prescription.setMedicalRecordID(rs.getInt("MedicalRecordID"));
//                prescription.setNote(rs.getString("Note"));
//                return prescription;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public List<PrescriptionDrug> getPrescriptionDrugs(int prescriptionID){
//        String sql = "SELECT * FROM Prescription_Drug WHERE PrescriptionID = ?";
//        List<PrescriptionDrug> prescriptionDrugList = new java.util.ArrayList<>();
//        try {
//            PreparedStatement ps = c.prepareStatement(sql);
//            ps.setInt(1, prescriptionID);
//            java.sql.ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                PrescriptionDrug pd = new PrescriptionDrug();
//                pd.setPrescriptionID(rs.getInt("PrescriptionID"));
//
//                pd.setDrugID(rs.getInt("DrugID"));
//                pd.setQuantity(rs.getInt("Quantity"));
//                pd.setInstruction(rs.getString("Instruction"));
//
//                prescriptionDrugList.add(pd);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return prescriptionDrugList;
//    }
//
//    public Drug getDrugByID(int drugID){
//        String sql = "SELECT * FROM Drug WHERE DrugID = ?";
//        Drug drug = null;
//        try {
//            PreparedStatement ps = c.prepareStatement(sql);
//            ps.setInt(1, drugID);
//            java.sql.ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                drug = new Drug();
//                drug.setDrugID(rs.getInt("DrugID"));
//                drug.setName(rs.getString("Name"));
//                drug.setDescription(rs.getString("Description"));
//                drug.setPrice(rs.getDouble("Price"));
//                return drug;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//    public List<Service> getServiceByBookingID(int bookingID){
//        String sql = "SELECT s.ServiceID, s.NameService, s.Description, s.Price " +
//                "FROM Appointment_Service bs " +
//                "JOIN Service s ON bs.ServiceID = s.ServiceID " +
//                "WHERE bs.BookingID = ?";
//        List<Service> serviceList = new java.util.ArrayList<>();
//        try {
//            PreparedStatement ps = c.prepareStatement(sql);
//            ps.setInt(1, bookingID);
//            java.sql.ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                Service service = new Service();
//                service.setServiceID(rs.getInt("ServiceID"));
//                service.setNameService(rs.getString("NameService"));
//                service.setDescription(rs.getString("Description"));
//                service.setPrice(rs.getDouble("Price"));
//                serviceList.add(service);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return serviceList;
//    }
//
//    public List<Drug> getDrugByBookingID(int bookingID){
//        String sql = "SELECT d.DrugID, d.Name, d.Description, d.Price ,d.Unit " +
//                "FROM Prescription_Drug pd " +
//                "JOIN Prescription p ON pd.PrescriptionID = p.PrescriptionID " +
//                "JOIN MedicalRecords mr ON p.MedicalRecordID = mr.MedicalRecordID " +
//                "JOIN Drug d ON pd.DrugID = d.DrugID " +
//                "WHERE mr.BookingID = ?";
//        List<Drug> drugList = new java.util.ArrayList<>();
//        try {
//            PreparedStatement ps = c.prepareStatement(sql);
//            ps.setInt(1, bookingID);
//            java.sql.ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                Drug drug = new Drug();
//                drug.setDrugID(rs.getInt("DrugID"));
//                drug.setName(rs.getString("Name"));
//                drug.setDescription(rs.getString("Description"));
//                drug.setPrice(rs.getDouble("Price"));
//                drug.setUnit(rs.getString("Unit"));
//                drugList.add(drug);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return drugList;
//    }
//
//
//
//
//
//    public static void main(String[] args) {
//        MedicalRecordDAO dao = new MedicalRecordDAO();
//        MedicalRecordDetailView mrdv = dao.getMedicalRecordDetailViewByBookingID(1);
//        System.out.println(mrdv.getDoctorName() + " | " + mrdv.getDiagnosis() + " | " + mrdv.getClinicalNote()+" | " + mrdv.getVisitDate() +" | " + mrdv.getMedicalRecordID());
//    }
//}
//
//



package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordDAO extends DBContext {



    public List<MedicalHistoryView> getMedicalHistoryByCatID(int catId) {
        List<MedicalHistoryView> list = new ArrayList<>();
        // Query: Join Booking -> MedicalRecords -> Veterinarians -> Users
        // Mục đích: Lấy ngày khám, tên bác sĩ, ghi chú lâm sàng của một con mèo cụ thể
        String sql = "SELECT b.BookingID, b.AppointmentDate, u.FullName AS DoctorName, mr.ClinicalNote " +
                "FROM Booking b " +
                "JOIN MedicalRecords mr ON b.BookingID = mr.BookingID " +
                "JOIN Veterinarians v ON b.VeterinarianID = v.VetID " +
                "JOIN Users u ON v.UserID = u.UserID " +
                "WHERE b.CatID = ? " +
                "ORDER BY b.AppointmentDate DESC"; // Sắp xếp ngày gần nhất lên đầu

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, catId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new MedicalHistoryView(
                        rs.getInt("BookingID"),
                        rs.getDate("AppointmentDate"),
                        rs.getString("DoctorName"),
                        rs.getString("ClinicalNote")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }



    // Lấy thông tin chi tiết bệnh án
    public MedicalRecordDetailView getMedicalRecordDetailViewByBookingID(int bookingID) {
        String sql = "SELECT mr.MedicalRecordID, b.AppointmentDate, u.FullName AS DoctorName, mr.Diagnosis, mr.ClinicalNote " +
                "FROM MedicalRecords mr " +
                "JOIN Booking b ON mr.BookingID = b.BookingID " +
                "JOIN Veterinarians v ON b.VeterinarianID = v.VetID " +
                "JOIN Users u ON v.UserID = u.UserID " +
                "WHERE mr.BookingID = ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, bookingID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new MedicalRecordDetailView(
                       // rs.getInt("BookingID"),
                        rs.getInt("MedicalRecordID"),
                        rs.getDate("AppointmentDate"),
                        rs.getString("DoctorName"),
                        rs.getString("Diagnosis"),
                        rs.getString("ClinicalNote")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy danh sách dịch vụ theo BookingID
    public List<Service> getServiceByBookingID(int bookingID) {
        List<Service> list = new ArrayList<>();
        String sql = "SELECT s.* " +
                "FROM Service s " +
                "JOIN Appointment_Service asv ON s.ServiceID = asv.ServiceID " +
                "WHERE asv.BookingID = ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, bookingID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Service(
                        rs.getInt("ServiceID"),
                        rs.getString("NameService"),
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

    // Lấy danh sách thuốc theo BookingID
    public List<Drug> getDrugByBookingID(int bookingID) {
        List<Drug> list = new ArrayList<>();
        String sql = "SELECT d.* " +
                "FROM Drug d " +
                "JOIN Prescription_Drug pd ON d.DrugID = pd.DrugID " +
                "JOIN Prescription p ON pd.PrescriptionID = p.PrescriptionID " +
                "JOIN MedicalRecords mr ON p.MedicalRecordID = mr.MedicalRecordID " +
                "WHERE mr.BookingID = ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, bookingID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Drug(
                        rs.getInt("DrugID"),
                        rs.getString("Name"),
                        rs.getString("Unit"),
                        rs.getDouble("Price"),
                        rs.getString("Description"),
                        rs.getBoolean("IsActive")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<PrescriptionDrugView> getPrescriptionDrugByBookingID(int bookingID) {

        List<PrescriptionDrugView> list = new ArrayList<>();

        String sql =
                "SELECT d.Name, d.Unit, pd.Quantity, pd.Instruction " +
                        "FROM Prescription_Drug pd " +
                        "JOIN Drug d ON pd.DrugID = d.DrugID " +
                        "JOIN Prescription p ON pd.PrescriptionID = p.PrescriptionID " +
                        "JOIN MedicalRecords mr ON p.MedicalRecordID = mr.MedicalRecordID " +
                        "WHERE mr.BookingID = ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, bookingID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new PrescriptionDrugView(
                        rs.getString("Name"),
                        rs.getString("Unit"),
                        rs.getInt("Quantity"),
                        rs.getString("Instruction")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {
        MedicalRecordDAO dao = new MedicalRecordDAO();

        MedicalRecordDetailView d = dao.getMedicalRecordDetailViewByBookingID(1);
        List<Service> serviceList = dao.getServiceByBookingID(1);
        List<PrescriptionDrugView> drugList = dao.getPrescriptionDrugByBookingID(1);
        System.out.println(d.getDoctorName() + " | " + d.getDiagnosis() + " | " + d.getClinicalNote() + " | " + d.getVisitDate() + " | " + d.getMedicalRecordID());
        for (Service s : serviceList) {
            System.out.println(s.getNameService() + " - " + s.getPrice() +" - " + s.getTimeService()) ;
    }
        for (PrescriptionDrugView pdv : drugList) {
            System.out.println(pdv.getDrugName() + " - " + pdv.getQuantity() + " " + pdv.getUnit() + " - " + pdv.getInstruction());
        }

        System.out.println("-----------------------------------");
        List<MedicalHistoryView> medicalHistoryList = dao.getMedicalHistoryByCatID(1);
        for (MedicalHistoryView mhv : medicalHistoryList) {
            if (mhv.getBookingID() == 1) {
                MedicalRecordDetailView d1 = dao.getMedicalRecordDetailViewByBookingID(mhv.getBookingID());
                List<Service> serviceList1 = dao.getServiceByBookingID(mhv.getBookingID());
                List<PrescriptionDrugView> drugList1 = dao.getPrescriptionDrugByBookingID(mhv.getBookingID());
                System.out.println(d1.getDoctorName() + " | " + d1.getDiagnosis() + " | " + d1.getClinicalNote() + " | " + d1.getVisitDate() + " | " + d1.getMedicalRecordID());
                for (Service s : serviceList1) {
                    System.out.println(s.getNameService() + " - " + s.getPrice() +" - " + s.getTimeService()) ;
                }
                for (PrescriptionDrugView pdv : drugList1) {
                    System.out.println(pdv.getDrugName() + " - " + pdv.getQuantity() + " " + pdv.getUnit() + " - " + pdv.getInstruction());
                }
            }
        }
    }
}


