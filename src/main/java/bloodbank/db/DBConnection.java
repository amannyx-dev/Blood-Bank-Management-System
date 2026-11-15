package bloodbank.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DBConnection {
    private static final Properties SETTINGS = new Properties();

    static {
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) throw new IllegalStateException("db.properties is missing from src/main/resources.");
            SETTINGS.load(input);
        } catch (Exception exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }

    private DBConnection() { }

    public static Connection getConnection() throws SQLException {
        String url = SETTINGS.getProperty("db.url");
        String username = SETTINGS.getProperty("db.username");
        String password = SETTINGS.getProperty("db.password");
        if (url == null || username == null || password == null) throw new SQLException("Set db.url, db.username and db.password in db.properties.");
        return DriverManager.getConnection(url, username, password);
    }
}
