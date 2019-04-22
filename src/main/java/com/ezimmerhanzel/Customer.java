package com.ezimmerhanzel;

//come back to this when you are finished with person

public class Customer{

    public static void createTransaction(int value, String type, int account1, int account2, String username) {
        if (value >= 0 & DatabaseManager.checkAccount(account1) & (DatabaseManager.checkAccount(account2) | (account2 == 0))) {
            Transaction.create(value, type, account1, account2);
        } else if (value < 0){
            System.out.println("Cannot Process Negative Amounts");
        } else if (!DatabaseManager.hasAccount(account1, username)) {
            System.out.println("Invalid account Number");
        } else if (DatabaseManager.checkAccount(account2)) {
            System.out.println("Invalid Target Account");
        }
    }

    public static void requestJointAccount(String username, int accountNumber) {
        if(DatabaseManager.checkAccount(accountNumber) & DatabaseManager.jointUserCheck(accountNumber, username)) {
            DatabaseManager.addJointUser(username, accountNumber);
        }else{
            System.out.println("An error has occurred with the Account Number that you have entered.");
        }
    }

    public static void requestAccount(String username) {
        DatabaseManager.addAccountRequest(username);
    }

    public static void listAccounts(String username) {
        DatabaseManager.listCustomerInformation(username);
    }

    public static void listOptions() {
        System.out.println("Customer options:\n   Deposit\n   Withdraw\n   Transfer Funds\n" +
                "   Create new Account\n   Request access to Account\n   List Accounts");
    }
}
