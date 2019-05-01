package com.ezimmerhanzel;

public class Admin extends Employee {
    public static void editUserName(String currentName, String newName) {
        DatabaseManager.changeName(currentName, newName);
    }

    public static void editUserPassword(String username, String newPassword) {
        DatabaseManager.changePassword(username, newPassword);
    }

    public static void createTransaction(String value, String type, String account1, String account2) {
        Transaction.create(Integer.parseInt(value), type, Integer.parseInt(account1),
                Integer.parseInt(account2));
    }

    public static void deleteAccount(int accountNumber) {
        DatabaseManager.removeAccount(accountNumber);
    }

    public static void listTransaction() {
        System.out.println("Value  Type  Account");
        DatabaseManager.listTransactions();
    }

    public static void listOptions() {
        System.out.println("Admin options:\n  1 List Account Requests\n  2 Approve Account Request\n" +
                "  3 Deny Account Request\n  4 List Customers\n  5 List Customer Information\n  6 Edit User's Name\n" +
                "  7 Edit Users Password\n  8 Create Transaction\n  9 Delete Account\n  10 List Transactions");
    }
}
