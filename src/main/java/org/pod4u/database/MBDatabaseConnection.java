package org.pod4u.database;

import org.pod4u.config.MBDatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.postgresql.Driver;

public class MBDatabaseConnection {
    private MBDatabaseConnection() {

    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(MBDatabaseConfig.getUrl(), MBDatabaseConfig.getUser(), MBDatabaseConfig.getPassword());
    }
}
