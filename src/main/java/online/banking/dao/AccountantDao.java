package online.banking.dao;

import online.banking.entity.Accountant;
import online.banking.exceptions.AccountantException;

public interface AccountantDao {
    Accountant loginAccountant (String userName,String password) throws AccountantException;
}
