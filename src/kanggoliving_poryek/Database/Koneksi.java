package kanggoliving_poryek.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {
    // Port default MySQL XAMPP biasanya 3306
    private static final String URL = "jdbc:mysql://localhost:3306/kanggoliving_db";
    private static final String USER = "root";// Username default XAMPP
    private static final String PASSWORD = "";// Password default XAMPP (kosong)

    public static Connection getConnection() throws SQLException {
        try {
            // Load driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL tidak ditemukan!", e);
        }
    }
}