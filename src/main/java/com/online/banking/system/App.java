package com.online.banking.system;

import online.banking.dao.AccountantDao;
import online.banking.dao.AccountantDaoImplementation;
import online.banking.entity.Accountant;

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
                    String adminUserName = cin.nextLine().trim();
                    System.out.print("Enter Password : ");
                    String adminPassword = cin.next();
                    AccountantDao ad = new AccountantDaoImplementation();
                    try{
                        Accountant user = ad.loginAccountant(adminUserName,adminPassword);
                        if(user == null){
                            System.out.println("Invalid  ");
                            break;
                        }
                        System.out.println("Login Successful");
                        System.out.println("Welcome "+user.getUserName());
                        // Admin functionalities can be added here
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
            }
        }
    }
}
