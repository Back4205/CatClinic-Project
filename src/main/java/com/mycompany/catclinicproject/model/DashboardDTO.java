/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.model;

/**
 *
 * @author ADMIN
 */
import java.util.List;
import java.util.Map;

public class DashboardDTO {
    private int totalServices;
    private int pendingCancelCount;
    private Map<String, Integer> accountStats; // Thống kê User theo Role
    private List<ServiceUsage> serviceUsages; // Dữ liệu cho biểu đồ

    // Class phụ để chứa dữ liệu từng loại dịch vụ
    public static class ServiceUsage {
        private String name;
        private int count;
        private double percent;

        public ServiceUsage(String name, int count) {
            this.name = name;
            this.count = count;
        }
        // Getter/Setter cho name, count, percent...
        public String getName() { return name; }
        public int getCount() { return count; }
        public double getPercent() { return percent; }
        public void setPercent(double percent) { this.percent = percent; }
    }

    // Getter/Setter cho DashboardDTO...
    public int getTotalServices() { return totalServices; }
    public void setTotalServices(int totalServices) { this.totalServices = totalServices; }
    public int getPendingCancelCount() { return pendingCancelCount; }
    public void setPendingCancelCount(int count) { this.pendingCancelCount = count; }
    public Map<String, Integer> getAccountStats() { return accountStats; }
    public void setAccountStats(Map<String, Integer> stats) { this.accountStats = stats; }
    public List<ServiceUsage> getServiceUsages() { return serviceUsages; }
    public void setServiceUsages(List<ServiceUsage> list) { this.serviceUsages = list; }
}
