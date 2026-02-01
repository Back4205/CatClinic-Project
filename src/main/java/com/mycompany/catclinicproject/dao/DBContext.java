package com.mycompany.catclinicproject.dao;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {
    protected Connection c;

    public DBContext() {
        try {
            String url = "jdbc:sqlserver://localhost:1433;databaseName=Cat_Clinic_Final;encrypt=true;trustServerCertificate=true";
            String username = "sa";
            String pass = "abc@123";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            c = DriverManager.getConnection(url, username, pass);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (c != null && !c.isClosed()) {
                c.close();
                System.out.println("Connection closed successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }


}

