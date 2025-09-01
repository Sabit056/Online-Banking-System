package online.banking.dao;

import at.favre.lib.crypto.bcrypt.BCrypt;
import online.banking.databaseConnection.DatabaseConnection;
import online.banking.entity.Accountant;
import online.banking.entity.Customer;
import online.banking.exceptions.AccountantException;
import online.banking.exceptions.CustomerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountantDaoImplementation implements AccountantDao{
    String loginAccountant = "SELECT * FROM accountant WHERE userName = ?";
    String addCustomer = "insert into customer (customerName, customerEmail, customerAddress, customerContact, customerPassword) values (?,?,?,?,?)";
    String addAccount = "insert into account (customerAccountNo ,customerBalance, cid) values (?,?,?)";
    String updateCustomer = "UPDATE customer SET customerAddress = ? WHERE cid = ?";
    String deleteAccount =  "DELETE i FROM customer i INNER JOIN account a ON i.cid = a.cid WHERE a.cid = ?";
    String customerInfo = "select * from customer i inner join account a on a.cid = i.cid where a.cid = ?";
    String allCustomerInfo = "select * from customer i inner join account a on a.cid = i.cid";
    String findCustomerID = "SELECT cid FROM customer WHERE customerName = ? AND customerEmail = ?";

    //Accountant Login
    @Override
    public Accountant loginAccountant(String userName, String userPass) throws AccountantException {
        Accountant acc = null;

        try(Connection conn = DatabaseConnection.provideConnection();
            PreparedStatement ps = conn.prepareStatement(loginAccountant)) {
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

    //Customer Creation
    @Override
    public int addCustomer(String customerName, String customerEmail, String customerAddress, String customerContact, String customerPassword, int customerBalance) throws CustomerException {
        int cid = -1;
        try(Connection conn = DatabaseConnection.provideConnection()){
            cid = findCustomerId(customerName, customerEmail);
            if(cid == -1) {
                PreparedStatement ps = conn.prepareStatement(addCustomer, PreparedStatement.RETURN_GENERATED_KEYS);

                ps.setString(1, customerName);
                ps.setString(2, customerEmail);
                ps.setString(3, customerAddress);
                ps.setString(4, customerContact);
                String hashPass = BCrypt.withDefaults().hashToString(12, customerPassword.toCharArray());
                ps.setString(5, hashPass);

                int x = ps.executeUpdate();
                if (x > 0) {

                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        cid = rs.getInt(1);
                        long acNO = addAccount(customerBalance, cid);
                        System.out.println("Customer Account has been created successfully with AC No: " + acNO);
                    }
                } else {
                    throw new CustomerException("Failed to insert." +
                            " Please check the details and try again.");
                }
            }else{
                long newAC = addAccount(customerBalance, cid);
                System.out.println("Customer already exists. New account created for existing customer. Account No: " +newAC );
            }
        } catch (SQLException e) {
            throw new CustomerException("Error Occured in Creating Customer: " + e.getMessage());
        }
        return cid;
    }

    //Account Creation
    @Override
    public long addAccount(int customerBalance, int cid) throws CustomerException {
        try(Connection conn = DatabaseConnection.provideConnection()){
            //generate 4 digit random number for account number
            int randomAC = 1000 + new java.util.Random().nextInt(9000);
            long accountNo = Long.parseLong("2025"+randomAC);

            PreparedStatement ps = conn.prepareStatement(addAccount);

            ps.setLong(1,accountNo);
            ps.setInt(2,customerBalance);
            ps.setInt(3,cid);

            int x = ps.executeUpdate();
            if (x > 0) {
                return  accountNo;
            } else {
                throw new CustomerException("Failed to create account.");
            }
        } catch (SQLException e) {
            throw new CustomerException("Error Occured in Creating Account: " + e.getMessage());
        }
    }

    //Update Customer Address
    @Override
    public String updateCustomer(int cid, String customerAddress) throws CustomerException {
        try (Connection conn = DatabaseConnection.provideConnection()) {
            PreparedStatement ps = conn.prepareStatement(updateCustomer);

            ps.setString(1, customerAddress);
            ps.setInt(2, cid);

            int x = ps.executeUpdate();

            if (x > 0) {
                return "Customer address updated successfully.";
            } else {
                throw new CustomerException("Customer with Account No " + cid + " does not exist.");
            }
        } catch (SQLException e) {
            throw new CustomerException("SQL Error while updating address: " + e.getMessage());
        }
    }


    //Delete Account
    @Override
    public String deleteCustomer(int cid) throws CustomerException {
        try (Connection conn = DatabaseConnection.provideConnection()) {
            PreparedStatement ps = conn.prepareStatement(deleteAccount);
            ps.setInt(1, cid);

            int x = ps.executeUpdate();

            if (x > 0) {
                return "Account deleted successfully.";
            } else {
                throw new CustomerException("Account with ID No " + cid + " does not exist.");
            }
        } catch (SQLException e) {
            throw new CustomerException("SQL Error while deleting account: " + e.getMessage());
        }
    }

    //Customer Info by ID
    @Override
    public Customer customerInfo(int cid) throws CustomerException {
        Customer cs = null;
        try(Connection conn = DatabaseConnection.provideConnection()){
            PreparedStatement ps = conn.prepareStatement(customerInfo);
            ps.setInt(1,cid);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    int acNo = rs.getInt("customerAccountNo");
                    String name = rs.getString("customerName");
                    String email = rs.getString("customerEmail");
                    String address = rs.getString("customerAddress");
                    String contact = rs.getString("customerContact");
//                    int balance = rs.getInt("customerBalance");

                    cs = new Customer(acNo,name,email,address,contact,null);
                }else{
                    throw new CustomerException("Customer with Account No " + cid + " does not exist.");
                }
            }
        } catch (SQLException e) {
            throw new CustomerException("SQL Error Occured in Fetching Customer Info: " + e.getMessage());
        }
        return cs;
    }

    //All Customer Info
    @Override
    public Customer allCustomerInfo() throws CustomerException {
        Customer ci = null;
        try(Connection conn = DatabaseConnection.provideConnection()) {
            PreparedStatement ps = conn.prepareStatement(allCustomerInfo);

            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    int acNo = rs.getInt("customerAccountNo");
                    String name = rs.getString("customerName");
                    String email = rs.getString("customerEmail");
                    String address = rs.getString("customerAddress");
                    String contact = rs.getString("customerContact");

                    ci = new Customer(acNo,name,email,address,contact,null);
                    if(ci != null){
                        System.out.println(ci);
                    }else {
                        throw new CustomerException("No customer records found.");
                    }
                }
            }
        } catch (SQLException e) {
            throw new CustomerException("SQL Error Occured in Fetching All Customer Info: " + e.getMessage());
        }
        return ci;
    }

    private int findCustomerId(String customerName, String customerEmail) throws SQLException {
        int cid = -1;
        try (Connection conn = DatabaseConnection.provideConnection()) {
            PreparedStatement ps = conn.prepareStatement(findCustomerID);
            ps.setString(1, customerName);
            ps.setString(2, customerEmail);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cid = rs.getInt("cid");
                }
            }
        }
        return cid;
    }


}
