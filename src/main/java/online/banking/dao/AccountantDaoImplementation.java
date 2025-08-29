package online.banking.dao;

import online.banking.databaseConnection.DatabaseConnection;
import online.banking.entity.Accountant;
import online.banking.exceptions.AccountantException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountantDaoImplementation implements AccountantDao{

    @Override
    public Accountant loginAccountant(String userName, String userPass) throws AccountantException {
        Accountant acc = null;
        try(Connection conn = DatabaseConnection.provideConnection()) {
            PreparedStatement ps = conn.prepareStatement("select * from accountant where userName = ?");
            ps.setString(1,userName);
//            ps.setString(2,password);

            ResultSet rs = ps.executeQuery();
            System.out.println(rs.toString());
            if(rs.next()) {

                String name = rs.getString("userName");
                String mail = rs.getString("email");
                String pass = rs.getString("password");

                System.out.println(name+" "+mail+" "+pass);
                if(!pass.equals(userPass)) {
                    throw new AccountantException("Invalid username or password");
                }
                acc = new Accountant(name, mail, pass);
            }
        } catch (SQLException e) {
            throw new AccountantException("Invalid username or password");
        }
        return  acc;
    }
}
