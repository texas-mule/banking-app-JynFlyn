package com.ezimmerhanzel;

public class Employee implements Listable {

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
        System.out.println("Employee options:\n   List Account Requests\n   Approve Account Request\n" +
                "   Deny Account Request\n   List Customers\n   List Customer Information");
    }
}
