package com.ezimmerhanzel;

public class Admin extends Employee implements Listable {
    public static void editUserName(String currentName, String newName) {
        DatabaseManager.changeName(currentName, newName);
    }

    public static void editUserPassword(String username, String newPassword) {
        DatabaseManager.changePassword(username, newPassword);
    }

    public static void createTransaction(String value, String type, String account1, String account2) {
        if (account2.equals("")) {
            account2 = "0";
        }
        Transaction.create(Integer.parseInt(value), type, Integer.parseInt(account1),
                Integer.parseInt(account2.replace(" ", "")));
    }

    public static void deleteAccount(int accountNumber) {
        DatabaseManager.removeAccount(accountNumber);
    }

    public static void listOptions() {
        System.out.println("Admin options:\n   List Account Requests\n   Approve Account Request\n" +
                "   Deny Account Request\n   List Customers\n   List Customer Information\n   Edit User's Name\n" +
                "   Edit Users Password\n   Create Transaction\n   Delete Account");
    }
}
