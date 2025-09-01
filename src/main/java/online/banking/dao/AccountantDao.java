package online.banking.dao;

import online.banking.entity.Accountant;
import online.banking.entity.Customer;
import online.banking.exceptions.AccountantException;
import online.banking.exceptions.CustomerException;

public interface AccountantDao {
    Accountant loginAccountant (String userName,String password) throws AccountantException;

    int addCustomer(String customerName, String customerEmail, String customerAddress, String customerContact, String customerPassword, int customerBalance) throws CustomerException;

    long addAccount(int customerBalance,int cid) throws CustomerException;

    String updateCustomer(int cid, String customerAddress) throws CustomerException;

    String deleteCustomer(int cid) throws CustomerException;

    Customer customerInfo(int cid) throws CustomerException;

    Customer allCustomerInfo() throws  CustomerException;

}
