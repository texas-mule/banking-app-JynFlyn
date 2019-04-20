package com.ezimmerhanzel;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        DatabaseManager.startDatabase();
        if(!DatabaseManager.userExists("admin")) {
            DatabaseManager.addUser("admin", "admin", "Admin");
            DatabaseManager.addUser("emp", "emp", "Employee");
        }

        Scanner scanner = new Scanner(System.in);
        String username;
        String password;
        Bank bank;
        boolean on = true;
        while (on) {
            System.out.print("\nLogin or Create Account\nusername: ");
            username = scanner.nextLine();
            if (username.toLowerCase().equals("quit")) {
                on = false;
                break;
            }
            if (DatabaseManager.userExists(username)) {
                System.out.print("password: ");
                password = scanner.nextLine();
                if (Person.login(username, password)) {
                    bank = new Bank(username);
                    bank.run();
                }

            } else {
                System.out.println("Would you like to Register?");
                if (scanner.nextLine().toLowerCase().equals("yes")) {
                    System.out.print("Enter your password: ");
                    DatabaseManager.addUser(username, scanner.nextLine(), "Customer");
                    System.out.println("New account created. Welcome " + username + ". What would you like to do?");
                    bank = new Bank(username);
                    bank.run();
                }
            }
        }
    }
}
