import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class MySqlConnection {

    public void mysqlcon() {
        Properties properties = new Properties();

        try (FileInputStream input = new FileInputStream("config.txt")) {
            // Load properties file
            properties.load(input);

            // Get properties
            String jdbcUrl = properties.getProperty("jdbcUrl");
            String dbusername = properties.getProperty("dbusername");
            String dbpassword = properties.getProperty("dbpassword");
            String dbdatabaseName = properties.getProperty("dbdatabase");

            // Establish a connection
            try (Connection connection = DriverManager.getConnection(jdbcUrl, dbusername, dbpassword)) {
                System.out.println("Connected to the database!");

                // Create the database if it does not exist
                createDatabaseIfNotExists(connection, dbdatabaseName);

                // Switch to the specified database
                connection.setCatalog(dbdatabaseName);

                // Create the table if it does not exist
                createTableIfNotExists(connection);

                // Now you can perform operations on the database and table using the 'connection' object
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createDatabaseIfNotExists(Connection connection, String databaseName) {
        try (Statement statement = connection.createStatement()) {
            // SQL query to check if the database exists
            String checkDatabaseQuery = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '" + databaseName + "'";

            // Execute the query
            if (!statement.executeQuery(checkDatabaseQuery).next()) {
                // If the result set is empty, the database does not exist, so create it
                String createDatabaseQuery = "CREATE DATABASE " + databaseName;
                statement.executeUpdate(createDatabaseQuery);
                System.out.println("Database created: " + databaseName);
            } else {
                System.out.println("Database already exists: " + databaseName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTableIfNotExists(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            // SQL query to check if the table exists
            String checkTableQuery = "SHOW TABLES LIKE 'users'";

            // Execute the query
            if (!statement.executeQuery(checkTableQuery).next()) {
                // If the result set is empty, the table does not exist, so create it
                String createTableQuery = "CREATE TABLE users (" +
                        "id INT PRIMARY KEY AUTO_INCREMENT," +
                        "username VARCHAR(255) NOT NULL," +
                        "password VARCHAR(255) NOT NULL," +
                        "email VARCHAR(255) NOT NULL)";
                statement.executeUpdate(createTableQuery);
                System.out.println("Table 'users' created");
            } else {
                System.out.println("Table 'users' already exists");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
