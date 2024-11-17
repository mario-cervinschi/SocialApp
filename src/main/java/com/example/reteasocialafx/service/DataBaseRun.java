package com.example.reteasocialafx.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseRun {
    public static Connection connect() throws SQLException {

        try{
            var jdbcURL = "jdbc:postgresql://localhost:6000/postgres";
            var jdbcUser = "postgres";
            var jdbcPass = "1234";

            return DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass);
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
            return null;
        }
    }

}
