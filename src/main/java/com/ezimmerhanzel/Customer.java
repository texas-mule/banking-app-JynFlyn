package com.ezimmerhanzel;

//come back to this when you are finished with person

public class Customer {

    public static void createTransaction(String value, String type, String account1, String account2, String username) {
        try {
            if (DatabaseManager.hasAccount(Integer.parseInt(account1), username)) {
                Transaction.create(value, type, account1, account2);
            } else {
                System.out.println("You do not have access to an account with the number you specified.");
            }
        } catch (NumberFormatException e) {
            System.out.println("A numerical value must be entered for Account Number.");
        }
    }

    public static void requestJointAccount(String username, String accountNumber) {
        try {
            int accountNum = Integer.parseInt(accountNumber);
            if (DatabaseManager.checkAccount(accountNum) & !DatabaseManager.hasAccount(accountNum, username)) {
                DatabaseManager.addAccountRequest(username, accountNum);
            } else if (!DatabaseManager.checkAccount(accountNum)) {
                System.out.println("The account number you provided could not be found.");
            } else if (DatabaseManager.hasAccount(accountNum, username)) {
                System.out.println("You already have access to this account.");
            }
        } catch (NumberFormatException e) {
            System.out.println("A numerical value must be entered for Account Number.");
        }
    }

    public static void requestAccount(String username) {
        DatabaseManager.addAccountRequest(username);
    }

    public static void listAccounts(String username) {
        DatabaseManager.listCustomerInformation(username);
    }

    public static void listOptions() {
        System.out.println("\n\nCustomer options:\n  1 Deposit\n  2 Withdraw\n  3 Transfer Funds\n" +
                "  4 Create new Account\n  5 Request access to Account\n  6 List Accounts");
    }
}
