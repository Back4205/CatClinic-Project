/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.dao;

import com.mycompany.catclinicproject.model.DashboardDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public class DashboardDAO extends DBContext {

    public DashboardDTO getDashboardData() {
        DashboardDTO dto = new DashboardDTO();
        try {
            // 1. Lấy tổng dịch vụ và booking chờ hủy
            String sql1 = "SELECT (SELECT COUNT(*) FROM Services WHERE IsActive = 1) as TotalS, " +
                          "(SELECT COUNT(*) FROM Bookings WHERE Status = 'PendingCancelRefund') as TotalP";
            PreparedStatement ps = c.prepareStatement(sql1);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                dto.setTotalServices(rs.getInt("TotalS"));
                dto.setPendingCancelCount(rs.getInt("TotalP"));
            }

            // 2. Thống kê Account theo Role
            String sql2 = "SELECT r.RoleName, COUNT(u.UserID) as Total FROM Roles r " +
                          "LEFT JOIN Users u ON r.RoleID = u.RoleID GROUP BY r.RoleName";
            ps = c.prepareStatement(sql2);
            rs = ps.executeQuery();
            Map<String, Integer> roleMap = new LinkedHashMap<>();
            while (rs.next()) {
                roleMap.put(rs.getString("RoleName"), rs.getInt("Total"));
            }
            dto.setAccountStats(roleMap);

            // 3. Tỷ lệ dịch vụ (Tính % cho Pie Chart CSS)
            String sql3 = "SELECT s.NameService, COUNT(aps.ServiceID) as UsageCount FROM Services s " +
                          "LEFT JOIN Appointment_Service aps ON s.ServiceID = aps.ServiceID GROUP BY s.NameService";
            ps = c.prepareStatement(sql3);
            rs = ps.executeQuery();
            List<DashboardDTO.ServiceUsage> list = new ArrayList<>();
            int total = 0;
            while (rs.next()) {
                int count = rs.getInt("UsageCount");
                list.add(new DashboardDTO.ServiceUsage(rs.getString("NameService"), count));
                total += count;
            }
            if (total > 0) {
                for (DashboardDTO.ServiceUsage su : list) {
                    su.setPercent((double) su.getCount() / total * 100);
                }
            }
            dto.setServiceUsages(list);
        } catch (SQLException e) { e.printStackTrace(); }
        return dto;
    }
}    
