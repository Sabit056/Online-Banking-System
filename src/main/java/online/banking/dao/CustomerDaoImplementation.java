package online.banking.dao;

import at.favre.lib.crypto.bcrypt.BCrypt;
import online.banking.databaseConnection.DatabaseConnection;
import online.banking.entity.Customer;
import online.banking.exceptions.CustomerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDaoImplementation implements CustomerDao{

    String customerLogin = "SELECT c.customerName, c.customerEmail, c.customerAddress,c.customerContact, c.customerPassword FROM account a INNER JOIN customer c ON a.cid = c.cid WHERE a.customerAccountNo = ?";
    String viewBalance = "SELECT customerBalance FROM account WHERE customerAccountNo = ?";
    String depositMoney = "UPDATE account SET customerBalance = customerBalance + ? WHERE customerAccountNo = ?";
    String withdrawMoney = "UPDATE account SET customerBalance = customerBalance - ? WHERE customerAccountNo = ?";

    @Override
    public Customer customerLogin(int accountNo, String password) throws CustomerException {
        Customer customer = null;


        try(Connection conn = DatabaseConnection.provideConnection()){
            PreparedStatement ps = conn.prepareStatement(customerLogin);
            ps.setInt(1, accountNo);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    String hashedPass = rs.getString("customerPassword");
                    BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(),hashedPass);
                    if(result.verified){
                        String name = rs.getString("customerName");
                        String email = rs.getString("customerEmail");
                        String address = rs.getString("customerAddress");
                        String contact = rs.getString("customerContact");
                        String pass = rs.getString("customerPassword");
                        customer = new Customer(accountNo,name,email,address,contact,pass);
                    }else{
                        throw new CustomerException("Invalid account number or password.");
                    }
                }else {
                    throw new CustomerException("Account Not Found with account number: " + accountNo);
                }
            }
        }
        catch (SQLException e) {
            throw new CustomerException("SQL Error Occured in Customer Login: " + e.getMessage());
        }

        return customer;
    }

    @Override
    public int viewBalance(int accountNo) throws CustomerException {
        int balance = -1;
        try(Connection conn = DatabaseConnection.provideConnection()){
            PreparedStatement ps = conn.prepareStatement(viewBalance);
            ps.setInt(1,accountNo);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    balance = rs.getInt("customerBalance");
                }else {
                    throw new CustomerException("Account Not Found with account number: " + accountNo);
                }
            }
        } catch (SQLException e) {
            throw new CustomerException("SQL Error Occured in Viewing Balance: " + e.getMessage());
        }
        return balance;
    }

    @Override
    public int depositMoney(int accountNo, int amount) throws CustomerException {
        int balance = -1;
        try(Connection conn = DatabaseConnection.provideConnection()){
            PreparedStatement ps = conn.prepareStatement(depositMoney);
            ps.setInt(1,amount);
            ps.setInt(2, accountNo);
            int x = ps.executeUpdate();
            if(x > 0){
                balance = viewBalance(accountNo);
            }else {
                throw new CustomerException("Account Not Found with account number: " + accountNo);
            }
        } catch (SQLException e) {
            throw new CustomerException("SQL Error Occured in Depositing Money: " + e.getMessage());
        }

        return balance;
    }

    @Override
    public int withdrawMoney(int customerAccountNo, int amount) throws CustomerException {
        int balance = -1;
        if(viewBalance(customerAccountNo) < amount){
            throw new CustomerException("Insufficient Balance");
        }
        try(Connection conn = DatabaseConnection.provideConnection()){
            PreparedStatement ps = conn.prepareStatement(withdrawMoney);
            ps.setInt(1,amount);
            ps.setInt(2, customerAccountNo);
            int x = ps.executeUpdate();
            if(x > 0){
                balance = viewBalance(customerAccountNo);
            }else {
                throw new CustomerException("Account Not Found with account number: " + customerAccountNo);
            }
        } catch (SQLException e) {
            throw new CustomerException("SQL Error Occured in Withdrawing Money: " + e.getMessage());
        }
        return balance;
    }

    @Override
    public int transferMoney(int fromAccountNo, int toAccountNo, int amount) throws CustomerException {
        if(viewBalance(fromAccountNo) < amount){
            throw new CustomerException("Insufficient Balance");
        }
        if(withdrawMoney(fromAccountNo,amount) > 0 && depositMoney(toAccountNo,amount) > 0){
            return 1;
        }
        return -1;
    }
}
