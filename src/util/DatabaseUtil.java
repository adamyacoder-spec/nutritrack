package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseUtil - Manages JDBC connection to MySQL database.
 * 
 * Syllabus: JDBC (Unit V) - JDBC Drivers, Connection
 *           Exception Handling (Unit III) - try-catch, throws
 * 
 * HOW JDBC WORKS:
 * 1. Load the MySQL JDBC Driver (com.mysql.cj.jdbc.Driver)
 * 2. Create a Connection using DriverManager.getConnection()
 * 3. Use the Connection to create Statements and execute SQL
 * 4. Close the Connection when done
 */
public class DatabaseUtil {

    // Database connection details
    // Change these values to match your MySQL setup
    private static final String URL = "jdbc:mysql://localhost:3306/nutritrack";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";  // <-- Change this to YOUR MySQL root password

    /**
     * Get a connection to the MySQL database.
     * Uses DriverManager which is the core JDBC class for establishing connections.
     * 
     * @return Connection object to interact with the database
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Step 1: Load the MySQL JDBC driver
            // This tells Java which driver to use for MySQL connections
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: MySQL JDBC Driver not found!");
            System.out.println("Make sure mysql-connector-j JAR is in the classpath.");
            e.printStackTrace();
        }

        // Step 2: Establish and return the connection
        // DriverManager.getConnection() uses the URL, username, and password
        // URL format: jdbc:mysql://hostname:port/databaseName
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    /**
     * Safely close a database connection.
     * Always close connections to avoid memory leaks!
     * 
     * @param connection the Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Test the database connection.
     * Call this to verify your MySQL setup is working.
     */
    public static boolean testConnection() {
        Connection conn = null;
        try {
            conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("=== Database Connection Successful! ===");
                System.out.println("Connected to: " + URL);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("=== Database Connection FAILED! ===");
            System.out.println("Error: " + e.getMessage());
            System.out.println("\nTroubleshooting:");
            System.out.println("1. Is MySQL server running?");
            System.out.println("2. Is the password correct in DatabaseUtil.java?");
            System.out.println("3. Does the 'nutritrack' database exist? Run schema.sql first.");
        } finally {
            closeConnection(conn);
        }
        return false;
    }
}
