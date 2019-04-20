package com.ezimmerhanzel;

//come back to this when you are finished with person

public class Customer implements Listable {

    public static void createTransaction(int value, String type, int account1, int account2) {
        if (value >= 0) {
            Transaction.create(value, type, account1, account2);
        } else {
            System.out.println("Cannot Process Negative Amounts");
        }
    }

    public static void requestJointAccount(String username, int accountNumber) {
        DatabaseManager.addJointUser(username, accountNumber);
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
