package com.ezimmerhanzel;

public class Transaction {
    private static void createTransaction(int value, String type, int account1, int account2) {
        DatabaseManager.addTransaction(value, type, account1, account2);
    }

    private static void deposit(int value, int account) {
        createTransaction(value, "Deposit", account, 0);
    }

    private static void withdraw(int value, int account) {
        value = -value;
        createTransaction(value, "Withdrawal", account, 0);
    }

    private static void transfer(int value, int account1, int account2) {
        value = -value;
        createTransaction(value, "Transfer", account1, account2);
    }

    public static void create(int value, String type, int account1, int account2) {
        switch (type) {
            case "Deposit":
                deposit(value, account1);
                break;
            case "Withdraw":
                withdraw(value, account1);
                break;
            case "Transfer":
                transfer(value, account1, account2);
        }
    }
}
