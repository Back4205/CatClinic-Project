
package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                "SELECT mr.MedicalRecordID , b.AppointmentDate, u.FullName AS DoctorName, " +
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
                        rs.getInt("MedicalRecordID"),
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

        String sql = "  SELECT s.ServiceID, s.NameService, s.Price, s.Description,   s.TimeService, s.IsActive, s.CategoryID, s.ImgURL  FROM Services s INNER JOIN Appointment_Service bd ON s.ServiceID = bd.ServiceID WHERE bd.BookingID = ? ORDER BY s.NameService ASC  ";

        try (
                PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, bookingID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Service service = new Service(
                            rs.getInt("ServiceID"),
                            rs.getString("NameService"),
                            rs.getDouble("Price"),
                            rs.getString("Description"),
                            rs.getInt("TimeService"),
                            rs.getBoolean("IsActive"),
                            rs.getInt("CategoryID"),
                            rs.getString("ImgURL")
                    );
                    list.add(service);
                }
            }

        } catch (SQLException e) {
            // Production: dùng logger thay vì printStackTrace
            e.printStackTrace();
            // Optional: throw new RuntimeException("Lỗi lấy dịch vụ cho booking " + bookingID, e);
        }

        return list;
    }


    public List<PrescriptionDrugDTO> getPrescriptionDrugByBookingID(int bookingID) {

        List<PrescriptionDrugDTO> list = new ArrayList<>();

        String sql =
                "SELECT d.Price, d.Name, d.Unit, pd.Quantity, pd.Instruction  " +
                        "FROM Prescription_Drug  pd " +
                        "JOIN Drugs d ON pd.DrugID = d.DrugID " +
                        "JOIN Prescriptions p ON pd.PrescriptionID = p.PrescriptionID " +
                        "JOIN MedicalRecords  mr ON p.MedicalRecordID = mr.MedicalRecordID " +
                        "WHERE mr.BookingID = ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, bookingID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new PrescriptionDrugDTO(

                        rs.getString("Name"),
                        rs.getString("Unit"),
                        rs.getInt("Quantity"),
                        rs.getString("Instruction"),
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
                "SELECT us.FullName AS StaffName, " +
                        "       st.Position AS StaffRole, " +
                        "       t.TestName, " +
                        "       t.Result , t.ResultName " +
                        "FROM TestOrders t " +
                        "JOIN Staffs st ON t.StaffID = st.StaffID " +
                        "JOIN Users us ON st.UserID = us.UserID " +
                        "JOIN MedicalRecords mr ON t.MedicalRecordID = mr.MedicalRecordID " +
                        "WHERE mr.BookingID = ?";

        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, bookingID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new MedicalStaffWorkDTO(
                        rs.getString("StaffName"),
                        rs.getString("StaffRole"),
                        rs.getString("TestName"),
                        rs.getString("Result"),
                        rs.getString("ResultName")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public int countMedicalHistoryWithFilter(int catId, String searchDate, String searchDoctor) {

        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) " +
                        "FROM Bookings b " +
                        "JOIN MedicalRecords mr ON b.BookingID = mr.BookingID " +
                        "JOIN Veterinarians v ON b.VetID = v.VetID " +
                        "JOIN Users u ON v.UserID = u.UserID " +
                        "WHERE b.CatID = ? "
        );

        if (searchDate != null && !searchDate.isEmpty()) {
            sql.append("AND CAST(b.AppointmentDate AS DATE) = ? ");
        }

        if (searchDoctor != null && !searchDoctor.isEmpty()) {
            sql.append("AND u.FullName LIKE ? ");
        }

        try {
            PreparedStatement ps = c.prepareStatement(sql.toString());

            int index = 1;
            ps.setInt(index++, catId);

            if (searchDate != null && !searchDate.isEmpty()) {
                ps.setDate(index++, java.sql.Date.valueOf(searchDate));
            }

            if (searchDoctor != null && !searchDoctor.isEmpty()) {
                ps.setString(index++, "%" + searchDoctor.trim() + "%");
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
    public List<MedicalHistoryDTO> getMedicalHistoryWithFilterPaging(int catId, String searchDate, String searchDoctor, int page, int pageSize) {

        List<MedicalHistoryDTO> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT b.BookingID, b.AppointmentDate, u.FullName AS DoctorName, mr.Diagnosis " +
                        "FROM Bookings b " +
                        "JOIN MedicalRecords mr ON b.BookingID = mr.BookingID " +
                        "JOIN Veterinarians v ON b.VetID = v.VetID " +
                        "JOIN Users u ON v.UserID = u.UserID " +
                        "WHERE b.CatID = ? "
        );

        if (searchDate != null && !searchDate.isEmpty()) {
            sql.append("AND CAST(b.AppointmentDate AS DATE) = ? ");
        }

        if (searchDoctor != null && !searchDoctor.isEmpty()) {
            sql.append("AND u.FullName LIKE ? ");
        }

        sql.append("ORDER BY b.AppointmentDate DESC ");
        sql.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try {
            PreparedStatement ps = c.prepareStatement(sql.toString());

            int index = 1;
            ps.setInt(index++, catId);

            if (searchDate != null && !searchDate.isEmpty()) {
                ps.setDate(index++, java.sql.Date.valueOf(searchDate));
            }

            if (searchDoctor != null && !searchDoctor.isEmpty()) {
                ps.setString(index++, "%" + searchDoctor.trim() + "%");
            }

            ps.setInt(index++, (page - 1) * pageSize);
            ps.setInt(index, pageSize);

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
    public boolean updateStatusToWaiting(int medicalRecordID) {
    String sql = "UPDATE MedicalRecords SET Status = 'Waiting result' WHERE MedicalRecordID = ?";

    try (PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setInt(1, medicalRecordID);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    } catch (Exception e) {
        e.printStackTrace();
    }

    return false;
}
    
    public boolean autoUpdateAndCheckChanges() {
    boolean changed = false;
    
   
    String sqlUpdate = "UPDATE MedicalRecords " +
                       "SET Status = 'In Treatment' " +
                       "WHERE Status = 'Waiting result' " +
                       "AND MedicalRecordID NOT IN (" +
                       "    SELECT DISTINCT MedicalRecordID FROM TestOrders WHERE Status != 'Completed'" +
                       ")";

    try (PreparedStatement ps = c.prepareStatement(sqlUpdate)) {
        int rows = ps.executeUpdate();
        if (rows > 0) {
            changed = true; 
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    return changed;
}
    public int getMedicalRecordIdByBookingId(int bookingId) {
        String sql = "SELECT MedicalRecordID FROM MedicalRecords WHERE BookingID = ?";
        
        try (PreparedStatement stm = c.prepareStatement(sql)) {
            stm.setInt(1, bookingId);
            
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("MedicalRecordID");
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi tại getMedicalRecordIdByBookingId: " + e.getMessage());
        }
        
        return -1;
    }
    public int getGeneralCheckBookingId(int vetId) {
    String sql = "SELECT TOP 1 b.BookingID " + // Chỉ lấy đúng cột ID
                 "FROM Bookings b " +
                 "INNER JOIN Appointment_Service asv ON b.BookingID = asv.BookingID " +
                 "INNER JOIN Services s ON asv.ServiceID = s.ServiceID " +
                 "LEFT JOIN MedicalRecords m ON b.BookingID = m.BookingID " +
                 "WHERE b.VetID = ? " +
                 "AND b.Status = 'Completed' " +
                 "AND s.NameService = ? " +
                 "AND m.BookingID IS NULL " +
                 "ORDER BY b.BookingID ASC";

    try (PreparedStatement stm = c.prepareStatement(sql)) {
        stm.setInt(1, vetId);
        stm.setString(2, "General Check");
        
        try (ResultSet rs = stm.executeQuery()) {
            if (rs.next()) {
                // Trả về ID trực tiếp
                return rs.getInt("BookingID");
            }
        }
    } catch (SQLException e) {
        System.out.println("Lỗi: " + e.getMessage());
    }
    
    // Trả về -1 nếu không tìm thấy booking nào thỏa mãn
    return -1;
}



}