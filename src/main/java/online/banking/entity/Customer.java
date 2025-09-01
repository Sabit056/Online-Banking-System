package online.banking.entity;

import java.util.Objects;

public class Customer {
    private int customerAccountNo;
    private String customerName;
    private String customerEmail;
    private String customerAddress;
    private String customerContact;
    private String customerPassword;
//    private int customerBalance;

    public Customer(int customerAccountNo, String customerName, String customerEmail, String customerAddress, String customerContact, String customerPassword) {
        this.customerAccountNo = customerAccountNo;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerAddress = customerAddress;
        this.customerContact = customerContact;
        this.customerPassword = customerPassword;
//        this.customerBalance = customerBalance;
    }
    public Customer() {
        super();
    }

//    public int getCustomerBalance() {
//        return customerBalance;
//    }
//
//    public void setCustomerBalance(int customerBalance) {
//        this.customerBalance = customerBalance;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return customerAccountNo == customer.customerAccountNo;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(customerPassword);
    }

    public String getCustomerPassword() {
        return customerPassword;
    }

    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }
    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }
    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }
    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public int getCustomerAccountNo() {
        return customerAccountNo;
    }

    public void setCustomerAccountNo(int customerAccountNo) {
        this.customerAccountNo = customerAccountNo;
    }

    @Override
    public String toString() {
        return "-----Customer Info: -----\n" +
                "Account NO : " + customerAccountNo +'\n'+
                "Name : " + customerName.toUpperCase() + '\n' +
                "Email : " + customerEmail + '\n' +
                "Address: " + customerAddress.toUpperCase() + '\n' +
                "Contact: " + customerContact + '\n' +
                "-----------X------------";
    }


}
