package online.banking.exceptions;

public class CustomerException extends Exception {
    public CustomerException(String FailedtoAddCustomer)  {
        super(FailedtoAddCustomer);
    }
}
