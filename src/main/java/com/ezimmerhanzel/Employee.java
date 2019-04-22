package com.ezimmerhanzel;

public class Employee {

    public static void listAccountRequests() {
        DatabaseManager.listAccountRequests();
    }

    public static void approveAccount(int accountNumber) {
        DatabaseManager.addAccount(accountNumber);
    }

    public static void denyAccount(int accountNumber) {
        DatabaseManager.removeAccountRequest(accountNumber);
    }

    public static void listCustomers() {
        DatabaseManager.listCustomers();
    }

    public static void listInformation(String customer) {
        DatabaseManager.listCustomerInformation(customer);
    }

    public static void listOptions() {
        System.out.println("Employee options:\n  1 List Account Requests\n  2 Approve Account Request\n" +
                "  3 Deny Account Request\n  4 List Customers\n  5 List Customer Information");
    }
}
