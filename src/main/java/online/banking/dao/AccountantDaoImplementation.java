package online.banking.dao;

import at.favre.lib.crypto.bcrypt.BCrypt;
import online.banking.databaseConnection.DatabaseConnection;
import online.banking.entity.Accountant;
import online.banking.exceptions.AccountantException;
import online.banking.exceptions.CustomerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountantDaoImplementation implements AccountantDao{

    @Override
    public Accountant loginAccountant(String userName, String userPass) throws AccountantException {
        Accountant acc = null;
        String sql = "SELECT * FROM accountant WHERE userName = ?";
        try(Connection conn = DatabaseConnection.provideConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,userName);

            try(ResultSet rs = ps.executeQuery()){
                if(!rs.next()) {
                    throw new AccountantException("Error: Invalid username or password");
                }
                String name = rs.getString("userName");
                String mail = rs.getString("email");
                String hashedPass = rs.getString("password");

                BCrypt.Result result = BCrypt.verifyer().verify(userPass.toCharArray(),hashedPass);
                if(!result.verified){
                    throw new AccountantException("Invalid username or password");
                }
                acc = new Accountant(name, mail, null);
            }
        } catch (SQLException e) {
            throw new AccountantException("Database error: " + e.getMessage());
        }
        return  acc;
    }

    @Override
    public int addCustomer(String customerName, String customerEmail, String customerAddress, String customerContact, String customerPassword, int customerBalance) throws CustomerException {
        int cid = -1;
        try(Connection conn = DatabaseConnection.provideConnection()){
            PreparedStatement ps = conn.prepareStatement("insert into customer (customerName, customerEmail, customerAddress, customerContact, customerPassword,customerBalance) values (?,?,?,?,?,?)");
            ps.setString(1,customerName);
            ps.setString(2,customerEmail);
            ps.setString(3,customerAddress);
            ps.setString(4,customerContact);

            String hashPass = BCrypt.withDefaults().hashToString(12, customerPassword.toCharArray());
            ps.setString(5,hashPass);
            ps.setInt(6,customerBalance);

            int x = ps.executeUpdate();
            if (x > 0) {
                System.out.println("Customer added successfully.");
            } else {
                throw new CustomerException("Failed to add customer.");
            }

        } catch (Exception e) {
            System.out.println("Error Occured in Creating Customer: " + e.getMessage());
        }
        return 0;
    }
}
