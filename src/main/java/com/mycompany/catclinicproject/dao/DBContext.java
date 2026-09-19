package com.mycompany.catclinicproject.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {
    protected Connection c;

    public DBContext() {
        try {
            String envUrl = System.getenv("DB_URL");
            String envUser = System.getenv("DB_USER");
            String envPass = System.getenv("DB_PASS");

            String url = (envUrl != null && !envUrl.trim().isEmpty()) 
                    ? envUrl 
                    : "jdbc:sqlserver://cat_clinic.mssql.somee.com:1433;databaseName=cat_clinic;encrypt=false;trustServerCertificate=true";
            String username = (envUser != null && !envUser.trim().isEmpty()) ? envUser : "klgames0510_SQLLogin_1";
            String pass = (envPass != null) ? envPass : "Dinhkhanh2005";

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            c = DriverManager.getConnection(url, username, pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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