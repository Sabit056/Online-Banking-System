package com.online.banking.system;

import online.banking.dao.AccountantDao;
import online.banking.dao.AccountantDaoImplementation;
import online.banking.dao.CustomerDao;
import online.banking.dao.CustomerDaoImplementation;
import online.banking.entity.Accountant;
import online.banking.entity.Customer;
import online.banking.exceptions.CustomerException;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);

        boolean flag = true;

        while (flag) {

            System.out.println("-----Welcome to Online Banking System-----");
            System.out.println("-------------------------------------------");
            System.out.println("1. ADMIN Login " + "\r\n" +
                                "2. CUSTOMER Login " + "\r\n"+
                                "0. Sign Out "+
                                "..................");
            System.out.print("Select your option : ");

            int option = cin.nextInt();
            cin.nextLine();

            switch (option) {
                case 1:
                    System.out.println("ADMIN Login Credentials \r\n"+"-------------------");
                    System.out.print("Enter Username : ");
                    String adminUserName = cin.nextLine();
                    System.out.print("Enter Password : ");
                    String adminPassword = cin.next();

                    AccountantDao ad = new AccountantDaoImplementation();

                    try {
                        Accountant admin = ad.loginAccountant(adminUserName, adminPassword);
                        if (admin == null) {
                            System.out.println("Found Null  ");
                            break;
                        }

                        System.out.println("Login Successful...! Welcome " + admin.getUserName().toUpperCase() + "!\n");

                        boolean y = true;

                        while (y) {
                            System.out.println("-----------------Choose Option-----------------\r\n"
                                    + "1. Add New Customer Account \r\n" +
                                    "2. Update Customer Address \r\n" +
                                    "3. Delete Customer Account \r\n" +
                                    "4. Customer Details \r\n" +
                                    "5. All Customer Details \r\n" +
                                    "0. Logout");

                            System.out.print("Select your option : ");
                            int x = cin.nextInt();
                            cin.nextLine();

                            if (x == 1) {
                                System.out.println("------New Account Creation------");
                                System.out.print("Enter Customer Name : ");
                                String customerName = cin.next();
                                System.out.print("Enter Customer Email : ");
                                String customerEmail = cin.next();
                                System.out.print("Enter Customer Address : ");
                                String customerAddress = cin.next();
                                System.out.print("Enter Customer Contact : ");
                                String customerContact = cin.next();
                                System.out.print("Enter Customer Password : ");
                                String customerPassword = cin.next();
                                System.out.print("Enter Initial Balance : ");
                                int customerBalance = cin.nextInt();

                                int s1 = -1;
                                try {
                                    s1 = ad.addCustomer(customerName, customerEmail, customerAddress, customerContact, customerPassword, customerBalance);
                                } catch (CustomerException e) {
                                    System.out.println(e.getMessage());
                                }
                            }

                            if (x == 2) {
                                System.out.println("------Update Customer Address------");
                                System.out.print("Enter Customer ID No : ");
                                int cid = cin.nextInt();
                                cin.nextLine();
                                System.out.print("Enter New Address : ");
                                String customerAddress1 = cin.nextLine();
                                try {
                                    String up = ad.updateCustomer(cid, customerAddress1);
                                    System.out.println(up);
                                } catch (CustomerException e) {
                                    System.out.println("Error Occured in Updating Customer Address: " + e.getMessage());
                                }
                            }

                            if (x == 3) {
                                System.out.println("------Delete Customer ------");
                                System.out.print("Enter Customer ID No : ");
                                int idNo = cin.nextInt();
                                try {
                                    String del = ad.deleteCustomer(idNo);
                                    System.out.println(del);
                                } catch (CustomerException e) {
                                    System.out.println("Error Occured in Deleting Customer : " + e.getMessage());
                                }
                            }

                            if (x == 4) {
                                System.out.println("------Customer Details------");
                                System.out.print("Enter Customer ID No : ");
                                int idNo = cin.nextInt();
                                try {
                                    Customer c = ad.customerInfo(idNo);
                                    System.out.println(c);
                                } catch (Exception e) {
                                    System.out.println("Error Occured in Fetching Customer Details: " + e.getMessage());
                                }
                            }

                            if (x == 5) {
                                System.out.println("------All Customer Details------");
                                try {
                                    Customer c = ad.allCustomerInfo();
                                } catch (Exception e) {
                                    System.out.println("Error Occured in Fetching All Customer Details: " + e.getMessage());
                                }
                            }

                            if (x == 0) {
                                System.out.println("Logged Out Successfully");
                                y = false;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    System.out.println("--------Customer Login------");
                    System.out.print("Enter Account No : ");
                    int accountNo = cin.nextInt();
                    cin.nextLine();
                    System.out.print("Enter Password : ");
                    String password = cin.next();

                    CustomerDao cd = new CustomerDaoImplementation();
                    try {
                        cd.customerLogin(accountNo, password);
                        System.out.println("Login Successful....! " + "Welcome '" + cd.customerLogin(accountNo, password).getCustomerName().toUpperCase() + "'! \n");

                        boolean z = true;
                        while (z) {
                            System.out.println("-----------------Choose Option-----------------\r\n" +
                                    "1. View Balance \r\n" +
                                    "2. Deposit Money \r\n" +
                                    "3. Withdraw Money \r\n" +
                                    "4. Transfer Money \r\n" +
                                    "0. Logout");

                            System.out.print("Select your option : ");
                            int opt = cin.nextInt();
                            cin.nextLine();

                            if (opt == 1) {
                                System.out.println("------View Balance------");
                                try {
                                    int balance = cd.viewBalance(accountNo);
                                    System.out.println("Your Current Balance is : " + balance);
                                } catch (CustomerException e) {
                                    System.out.println("Error Occured in Viewing Balance: " + e.getMessage());
                                }

                            }

                            if(opt == 2){
                                System.out.println("------Deposit Money------");
                                System.out.print("Enter Amount to Deposit : ");
                                int amount = cin.nextInt();
                                try {
                                    int balance = cd.depositMoney(accountNo, amount);
                                    System.out.println("Money Deposited Successfully...! Your New Balance is : " + balance);
                                } catch (CustomerException e) {
                                    System.out.println("Error Occured in Depositing Money: " + e.getMessage());
                                }
                            }

                            if(opt==3){
                                System.out.println("------Withdraw Money------");
                                System.out.print("Enter Amount to Withdraw : ");
                                int amount = cin.nextInt();
                                try {
                                    int balance = cd.withdrawMoney(accountNo, amount);
                                    System.out.println("Money Withdrawn Successfully...! Your New Balance is : " + balance);
                                } catch (CustomerException e) {
                                    System.out.println("Error Occured in Withdrawing Money: " + e.getMessage());
                                }
                            }

                            if(opt ==4) {
                                System.out.println("------Transfer Money------");
                                System.out.print("Enter Recipient Account No : ");
                                int toAccountNo = cin.nextInt();
                                System.out.print("Enter Amount to Transfer : ");
                                int amount = cin.nextInt();
                                try{
                                    int check = cd.transferMoney(accountNo,toAccountNo,amount);
                                    if (check>0) {
                                        System.out.println("Money Transferred Successfully...! Your New Balance is : " + cd.viewBalance(accountNo));
                                    }else {
                                        System.out.println("Failed to Transfer Money. Please check Recipient Account Number and Try Again.");
                                    }
                                } catch (Exception e) {
                                    System.out.println("Error Occured in Transferring Money: " + e.getMessage());
                                }
                            }

                            if(opt == 0){
                                System.out.println("Logged Out Successfully");
                                z = false;
                            }
                        }
                        break;
                    } catch (CustomerException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 0:
                    System.out.println("Thank You for Using Online Banking System. Goodbye!");
                    flag = false;
                    break;
                default:
                    System.out.println("Invalid Option. Please Try Again.");
            }
        }
    }
}
