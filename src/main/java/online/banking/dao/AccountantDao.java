package online.banking.dao;

import online.banking.entity.Accountant;
import online.banking.exceptions.AccountantException;
import online.banking.exceptions.CustomerException;

public interface AccountantDao {
    Accountant loginAccountant (String userName,String password) throws AccountantException;

    public int addCustomer(String customerName, String customerEmail, String customerAddress, String customerContact, String customerPassword, int customerBalance) throws CustomerException;
}
