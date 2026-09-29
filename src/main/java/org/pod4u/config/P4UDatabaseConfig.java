package org.pod4u.config;

/** Reads database configuration from environment variables
 *
 * This avoids hard-coding usernames, passwords and connection strings
 * directly in the Java source code.
 */

public final class P4UDatabaseConfig {
    private P4UDatabaseConfig() {
        // Utility class: prevents object creation
    }

    public static String getUrl() {
        return getEnvironmentVariable("P4UDB_URL");
    }

    public static String getUser() {
        return getEnvironmentVariable("P4UDB_USER");
    }

    public static String getPassword() {
        return getEnvironmentVariable("P4UDB_PASSWORD");
    }

    private static String getEnvironmentVariable(String variable) {
        String value = System.getenv(variable);
        if (value==null || value.isBlank())
            throw new IllegalStateException("Missing required environment variable: " + variable);
        return value;
    }
}
