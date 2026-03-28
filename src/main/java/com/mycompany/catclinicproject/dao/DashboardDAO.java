package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.DashboardDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;


public class DashboardDAO extends DBContext {

    public DashboardDTO getDashboardStats() {
        DashboardDTO dto = new DashboardDTO();
        String sql = "SELECT \n"
                + "    (SELECT COUNT(*) FROM Services WHERE IsActive = 1) AS ActiveServices,\n"
                + "    (SELECT COUNT(*) FROM Bookings WHERE Status = 'PendingCancel') AS PendingCancel,\n"
                + "    (SELECT ISNULL(SUM(TotalAmount), 0) FROM Invoices WHERE PaymentStatus = 'Paid' \n"
                + "     AND MONTH(CreatedDate) = MONTH(GETDATE()) AND YEAR(CreatedDate) = YEAR(GETDATE())) AS MonthlyRevenue,\n"
                + "    (SELECT COUNT(*) FROM Users WHERE IsActive = 1) AS ActiveAccounts,\n"
                + "    (SELECT COUNT(*) FROM Users WHERE IsActive = 0) AS InactiveAccounts";

        try {
            if (c != null) {
                PreparedStatement ps = c.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    dto.setActiveServicesCount(rs.getInt("ActiveServices"));
                    dto.setPendingCancelCount(rs.getInt("PendingCancel"));
                    dto.setMonthlyRevenue(rs.getDouble("MonthlyRevenue"));
                    dto.setActiveAccounts(rs.getInt("ActiveAccounts"));
                    dto.setInactiveAccounts(rs.getInt("InactiveAccounts"));
                }
                rs.close();
                ps.close();
                dto.setAccountStats(getAccountStatsByRole(c));
            }
        } catch (Exception e) {
            System.out.println("Error at DashboardDAO: " + e.getMessage());
            e.printStackTrace();
        }
        return dto;
    }
    private Map<String, Integer> getAccountStatsByRole(Connection conn) throws Exception {
        Map<String, Integer> stats = new LinkedHashMap<>();
        String sql = "SELECT r.RoleName, COUNT(u.UserID) AS Total "
                + "FROM Roles r "
                + "LEFT JOIN Users u ON r.RoleID = u.RoleID "
                + "GROUP BY r.RoleName, r.RoleID "
                + "ORDER BY r.RoleID ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                stats.put(rs.getString("RoleName"), rs.getInt("Total"));
            }
        }
        return stats;
    }
}