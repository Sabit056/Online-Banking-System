package online.banking.exceptions;

public class AccountantException extends Exception{
    public AccountantException(String invalidUsernameOrPassword) {
        super(invalidUsernameOrPassword);
    }
}
