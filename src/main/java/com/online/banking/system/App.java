package com.online.banking.system;

import online.banking.dao.AccountantDao;
import online.banking.dao.AccountantDaoImplementation;
import online.banking.entity.Accountant;
import online.banking.entity.Customer;
import online.banking.exceptions.CustomerException;

import java.util.Scanner;

public class App
{
    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);
        boolean flag = true;
        while(flag){
            System.out.println("-----Welcome to Online Banking System-----");
            System.out.println("---------------------------------------------------");
            System.out.println("1. ADMIN Login "+"\n"+"2. Accountant Login"+"\n");
            System.out.print("Select your option : ");
            int option = cin.nextInt();
            cin.nextLine();
            switch (option){
                case 1 :
                    System.out.println("Admin Login Credentials ");
                    System.out.print("Enter Username : ");
                    String adminUserName = cin.nextLine();
                    System.out.print("Enter Password : ");
                    String adminPassword = cin.next();

                    AccountantDao ad = new AccountantDaoImplementation();

                    try{
                        Accountant admin = ad.loginAccountant(adminUserName,adminPassword);
                        if(admin == null){
                            System.out.println("Found Null  ");
                            break;
                        }

                        System.out.println("Login Successful");
                        System.out.println("Welcome "+admin.getUserName()+"\n");

                    boolean y = true;

                    while (y){
                        System.out.println("-----------------Choose Option-----------------\r\n"
                        +"1. Add New Customer Account \r\n");

                        System.out.print("Select your option : ");
                        int x = cin.nextInt();
                        cin.nextLine();

                        if(x==1){
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
                            try{
                                s1 = ad.addCustomer(customerName,customerEmail,customerAddress,customerContact,customerPassword,customerBalance);
                            } catch (CustomerException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
            }
        }
    }
}
