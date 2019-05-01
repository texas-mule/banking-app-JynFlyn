package com.ezimmerhanzel;

//come back to this when you are done with customer employee and admin

import java.util.Scanner;

public class Bank {
    private String currentUser;
    private String userStatus;
    private boolean loggedIn;

    Bank(String userName) {
        loggedIn = true;
        this.currentUser = userName;
        userStatus = DatabaseManager.getUserStatus(userName);
    }

    public void run() {
        while (loggedIn) {
            switch (userStatus) {
                case "admin":
                    admin();
                    break;
                case "employee":
                    employee();
                    break;
                default:
                    customer();
            }
        }
    }

    private void admin() {
        Admin.listOptions();
        Scanner scanner = new Scanner(System.in);
        switch (scanner.nextLine().toLowerCase()) {
            case "1":
            case "list account requests":
                Admin.listAccountRequests();
                break;
            case "2":
            case "approve account request":
                System.out.print("Account to Approve: ");
                Admin.approveAccount(Integer.parseInt(scanner.nextLine()));
                break;
            case "3":
            case "deny account request":
                System.out.print("Account to Deny: ");
                Admin.denyAccount(Integer.parseInt(scanner.nextLine()));
                break;
            case "4":
            case "list customers":
                Admin.listCustomers();
                break;
            case "5":
            case "list customer information":
                System.out.print("Customer: ");
                Admin.listInformation(scanner.nextLine());
                break;
            case "6":
            case "edit user's name":
                System.out.print("Current Name: ");
                String currentName = scanner.nextLine();
                System.out.print("New Name: ");
                String newName = scanner.nextLine();
                Admin.editUserName(currentName, newName);
                break;
            case "7":
            case "edit users password":
                System.out.print("Username: ");
                String username = scanner.nextLine();
                System.out.print("New Password: ");
                String password = scanner.nextLine();
                Admin.editUserPassword(username, password);
                break;
            case "8":
            case "create transaction":
                System.out.print("Transaction Type: ");
                String type = scanner.nextLine().toLowerCase();
                System.out.print("Amount: ");
                String amount = scanner.nextLine().toLowerCase();
                System.out.print("Account Number: ");
                String accountNumber = scanner.nextLine().toLowerCase();
                String targetAccount = "0";
                if (type.equals("transfer")) {
                    System.out.print("Receiving Account: ");
                    targetAccount = scanner.nextLine().toLowerCase();
                }
                Admin.createTransaction(amount, type, accountNumber, targetAccount);
                break;
            case "9":
            case "delete account":
                System.out.print("Account Number: ");
                Admin.deleteAccount(Integer.parseInt(scanner.nextLine()));
                break;
            case "10":
            case "list transactions":
                Admin.listTransaction();
                break;
            case "logout":
                this.loggedIn = false;
                break;
            default:
                System.out.println("Invalid Option");
        }
    }

    private void employee() {
        Employee.listOptions();
        Scanner scanner = new Scanner(System.in);
        switch (scanner.nextLine().toLowerCase()) {
            case "1":
            case "list account requests":
                Employee.listAccountRequests();
                break;
            case "2":
            case "approve account request":
                System.out.print("Account to Approve: ");
                Employee.approveAccount(Integer.parseInt(scanner.nextLine()));
                break;
            case "3":
            case "deny account request":
                System.out.print("Account to Deny: ");
                Employee.denyAccount(Integer.parseInt(scanner.nextLine()));
                break;
            case "4":
            case "list customers":
                Employee.listCustomers();
                break;
            case "5":
            case "list customer information":
                System.out.print("Customer: ");
                Employee.listInformation(scanner.nextLine());
                break;
            case "logout":
                this.loggedIn = false;
                break;
            default:
                System.out.println("Invalid Option");
        }
    }

    private void customer() {
        Customer.listOptions();
        Scanner scanner = new Scanner(System.in);
        switch (scanner.nextLine().toLowerCase()) {
            case "1":
            case "deposit":
                System.out.print("Amount to Deposit: ");
                String value = scanner.nextLine();
                System.out.println("Account Number: ");
                String accountNumber = scanner.nextLine();
                Customer.createTransaction(Integer.parseInt(value), "deposit", Integer.parseInt(accountNumber),
                        0, currentUser);
                break;
            case "2":
            case "withdraw":
                System.out.print("Amount to Withdraw: ");
                String value2 = scanner.nextLine();
                System.out.println("Account Number: ");
                String accountNumber2 = scanner.nextLine();
                Customer.createTransaction(Integer.parseInt(value2), "withdrawal", Integer.parseInt(accountNumber2),
                        0, currentUser);
                break;
            case "3":
            case "transfer funds":
                System.out.print("Amount to Transfer:, Account Number, Target Account: ");
                String value3 = scanner.nextLine();
                System.out.println("Account Number: ");
                String accountNumber3 = scanner.nextLine();
                System.out.println("Receiving Account: ");
                String targetAccount = scanner.nextLine();
                Customer.createTransaction(Integer.parseInt(value3), "transfer", Integer.parseInt(accountNumber3),
                        Integer.parseInt(targetAccount), currentUser);
                break;
            case "4":
            case "create new account":
                Customer.requestAccount(currentUser);
                break;
            case "5":
            case "request access to account":
                System.out.print("Account Number: ");
                Customer.requestJointAccount(currentUser, scanner.nextInt());
                break;
            case "6":
            case "list accounts":
                Customer.listAccounts(currentUser);
                break;
            case "logout":
                this.loggedIn = false;
                break;
            default:
                System.out.println("Invalid Option");
        }
    }
}
