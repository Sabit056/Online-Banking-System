package online.banking.dao;

import online.banking.entity.Customer;
import online.banking.exceptions.CustomerException;

public interface CustomerDao {
    public Customer customerLogin(int accountNo, String password) throws CustomerException;

    int viewBalance(int accountNo) throws CustomerException;

    int depositMoney(int accountNo, int amount) throws CustomerException;

    int withdrawMoney(int customerAccountNo, int amount) throws CustomerException;

}
