package org.pod4u.config;

public final class MBDatabaseConfig {
    private MBDatabaseConfig() {

    }

    public static String getUrl() {
        return getEnvironmentVariable("MBDB_URL");
    }

    public static String getUser() {
        return getEnvironmentVariable("MBDB_USER");
    }

    public static String getPassword() {
        return getEnvironmentVariable("MBDB_PASSWORD");
    }

    private static String getEnvironmentVariable(String variable) {
        String value = System.getenv(variable);
        if (value==null || value.isBlank())
            throw new IllegalStateException("Missing required environment variable: " + variable);
        return value;
    }
}
