/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.model;


import java.util.Map;

public class DashboardDTO {
    private int activeServicesCount;      
    private int pendingCancelCount;      
    private double monthlyRevenue;       
    private int activeAccounts;          
    private int inactiveAccounts;
    private Map<String, Integer> accountStats;

    public DashboardDTO() {
    }

    public DashboardDTO(int activeServicesCount, int pendingCancelCount, double monthlyRevenue, int activeAccounts, int inactiveAccounts, Map<String, Integer> accountStats) {
        this.activeServicesCount = activeServicesCount;
        this.pendingCancelCount = pendingCancelCount;
        this.monthlyRevenue = monthlyRevenue;
        this.activeAccounts = activeAccounts;
        this.inactiveAccounts = inactiveAccounts;
        this.accountStats = accountStats;
    }

    public int getActiveServicesCount() {
        return activeServicesCount;
    }

    public void setActiveServicesCount(int activeServicesCount) {
        this.activeServicesCount = activeServicesCount;
    }

    public int getPendingCancelCount() {
        return pendingCancelCount;
    }

    public void setPendingCancelCount(int pendingCancelCount) {
        this.pendingCancelCount = pendingCancelCount;
    }

    public double getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public void setMonthlyRevenue(double monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    public int getActiveAccounts() {
        return activeAccounts;
    }

    public void setActiveAccounts(int activeAccounts) {
        this.activeAccounts = activeAccounts;
    }

    public int getInactiveAccounts() {
        return inactiveAccounts;
    }

    public void setInactiveAccounts(int inactiveAccounts) {
        this.inactiveAccounts = inactiveAccounts;
    }

    public Map<String, Integer> getAccountStats() {
        return accountStats;
    }

    public void setAccountStats(Map<String, Integer> accountStats) {
        this.accountStats = accountStats;
    }

    
    
}
