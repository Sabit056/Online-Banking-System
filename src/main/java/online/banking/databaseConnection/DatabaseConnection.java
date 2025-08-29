package online.banking.databaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public static Connection provideConnection() {
        Connection conn = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = "jdbc:mysql://localhost:3306/bankingsystem";
        try{
            conn = DriverManager.getConnection(url, "root", "admin");
        } catch (Exception e){
            System.out.println("Connection not established");
        }
        return conn;
    }
}
