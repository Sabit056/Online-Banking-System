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

    final String customerLogin = "SELECT c.customerName, c.customerEmail, c.customerAddress,c.customerContact, c.customerPassword FROM account a INNER JOIN customer c ON a.cid = c.cid WHERE a.customerAccountNo = ?";
    final String viewBalance = "SELECT customerBalance FROM account WHERE customerAccountNo = ?";
    final String depositMoney = "UPDATE account SET customerBalance = customerBalance + ? WHERE customerAccountNo = ?";
    final String withdrawMoney = "UPDATE account SET customerBalance = customerBalance - ? WHERE customerAccountNo = ?";

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
        try (Connection conn = DatabaseConnection.provideConnection()) {
            return viewBalance(accountNo, conn);
        } catch (SQLException e) {
            throw new CustomerException("SQL Error in Viewing Balance: " + e.getMessage());
        }
    }

    @Override
    public int depositMoney(int accountNo, int amount) throws CustomerException {
        try (Connection conn = DatabaseConnection.provideConnection()) {
            return depositMoney(accountNo, amount, conn);
        } catch (SQLException e) {
            throw new CustomerException("SQL Error in Depositing Money: " + e.getMessage());
        }
    }

    @Override
    public int withdrawMoney(int accountNo, int amount) throws CustomerException {
        try (Connection conn = DatabaseConnection.provideConnection()) {
            return withdrawMoney(accountNo, amount, conn);
        } catch (SQLException e) {
            throw new CustomerException("SQL Error in Withdrawing Money: " + e.getMessage());
        }
    }

    @Override
    public int transferMoney(int fromAccountNo, int toAccountNo, int amount) throws CustomerException {
        try (Connection conn = DatabaseConnection.provideConnection()) {
            conn.setAutoCommit(false);

            if (viewBalance(fromAccountNo, conn) < amount) {
                throw new CustomerException("Insufficient Balance");
            }

            int wb = withdrawMoney(fromAccountNo, amount, conn);
            int db = depositMoney(toAccountNo, amount, conn);

            if (wb > 0 && db > 0) {
                conn.commit();
                return 1;
            } else {
                conn.rollback();
                throw new CustomerException("Transaction Failed");
            }
        } catch (SQLException e) {
            throw new CustomerException("SQL Error in Transferring Money: " + e.getMessage());
        }
    }

    // -------------------------
    // Helper Methods
    // -------------------------

    private int viewBalance(int accountNo, Connection conn) throws SQLException, CustomerException {
        try (PreparedStatement ps = conn.prepareStatement(viewBalance)) {
            ps.setInt(1, accountNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("customerBalance");
                } else {
                    throw new CustomerException("Account Not Found with account number: " + accountNo);
                }
            }
        }
    }

    private int withdrawMoney(int accountNo, int amount, Connection conn) throws SQLException, CustomerException {
        if (viewBalance(accountNo, conn) < amount) {
            throw new CustomerException("Insufficient Balance");
        }
        try (PreparedStatement ps = conn.prepareStatement(withdrawMoney)) {
            ps.setInt(1, amount);
            ps.setInt(2, accountNo);
            int x = ps.executeUpdate();
            if (x > 0) {
                return viewBalance(accountNo, conn);
            } else {
                throw new CustomerException("Account Not Found with account number: " + accountNo);
            }
        }
    }

    private int depositMoney(int accountNo, int amount, Connection conn) throws SQLException, CustomerException {
        try (PreparedStatement ps = conn.prepareStatement(depositMoney)) {
            ps.setInt(1, amount);
            ps.setInt(2, accountNo);
            int x = ps.executeUpdate();
            if (x > 0) {
                return viewBalance(accountNo, conn);
            } else {
                throw new CustomerException("Account Not Found with account number: " + accountNo);
            }
        }
    }
}
