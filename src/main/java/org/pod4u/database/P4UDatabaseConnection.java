package org.pod4u.database;

import org.pod4u.config.P4UDatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Creates JDBC database connections.
 *
 * Each method call returns a new Connection object.
 * The caller is responsible for closing the connection.
 */

public final class P4UDatabaseConnection {
    private P4UDatabaseConnection() {
        // Utility class: prevents object creation
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(P4UDatabaseConfig.getUrl(), P4UDatabaseConfig.getUser(), P4UDatabaseConfig.getPassword());
    }
}