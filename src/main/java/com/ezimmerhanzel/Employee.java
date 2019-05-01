package com.ezimmerhanzel;

public class Employee {

    public static void listAccountRequests() {
        DatabaseManager.listAccountRequests();
    }

    public static void approveAccount(String accountNumber) {
        try {
            DatabaseManager.addAccount(Integer.parseInt(accountNumber));
        } catch (NumberFormatException e) {
            System.out.println("A numerical value must be entered for Account Number.");
        }
    }

    public static void denyAccount(String accountNumber) {
        try {
            DatabaseManager.removeAccountRequest(Integer.parseInt(accountNumber));
        } catch (NumberFormatException e) {
            System.out.println("A numerical value must be entered for Account Number.");
        }
    }

    public static void listCustomers() {
        DatabaseManager.listCustomers();
    }

    public static void listInformation(String customer) {
        DatabaseManager.listCustomerInformation(customer);
    }

    public static void listOptions() {
        System.out.println("\n\nEmployee options:\n  1 List Account Requests\n  2 Approve Account Request\n" +
                "  3 Deny Account Request\n  4 List Customers\n  5 List Customer Information");
    }
}
