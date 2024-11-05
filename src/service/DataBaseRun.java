package service;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseRun {
    public static Connection connect() throws SQLException {

        try{
            var jdbcURL = DatabaseConfig.getDbUrl();
            var jdbcUser = DatabaseConfig.getDbUsername();
            var jdbcPass = DatabaseConfig.getDbPassword();

            return DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass);
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
            return null;
        }
    }

}
