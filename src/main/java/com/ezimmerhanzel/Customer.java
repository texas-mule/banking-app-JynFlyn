package com.ezimmerhanzel;

//come back to this when you are finished with person

public class Customer {

    public static void createTransaction(int value, String type, int account1, int account2, String username) {
        if (DatabaseManager.hasAccount(account1, username)) {
            Transaction.create(value, type, account1, account2);
        }
    }

    public static void requestJointAccount(String username, int accountNumber) {
        if (DatabaseManager.checkAccount(accountNumber)) {
            DatabaseManager.addAccountRequest(username, accountNumber);
        } else {
            System.out.println("The account number you provided could not be found.");
        }
    }

    public static void requestAccount(String username) {
        DatabaseManager.addAccountRequest(username);
    }

    public static void listAccounts(String username) {
        DatabaseManager.listCustomerInformation(username);
    }

    public static void listOptions() {
        System.out.println("Customer options:\n  1 Deposit\n  2 Withdraw\n  3 Transfer Funds\n" +
                "  4 Create new Account\n  5 Request access to Account\n  6 List Accounts");
    }
}
