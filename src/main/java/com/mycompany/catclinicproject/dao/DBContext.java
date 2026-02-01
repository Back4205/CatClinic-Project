package com.mycompany.catclinicproject.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {
    protected Connection c;

    public DBContext() {
        try {
            // Mật khẩu của bạn là abc@123
            String url = "jdbc:sqlserver://localhost:1433;databaseName=Cat_Clinic_Final;encrypt=true;trustServerCertificate=true";
            String username = "sa";
            String pass = "abc@123"; 
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            c = DriverManager.getConnection(url, username, pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- ĐÂY LÀ HÀM QUAN TRỌNG MÀ SERVER ĐANG BÁO THIẾU ---
    public Connection getConnection() {
        return c;
    }

    public void closeConnection() {
        try {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}