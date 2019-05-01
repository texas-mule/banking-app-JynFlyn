package com.ezimmerhanzel;

import java.util.ArrayList;
import java.util.Arrays;

public class Transaction {
    private static void createTransaction(int value, String type, int account1, int account2) {
        DatabaseManager.addTransaction(value, type, account1, account2);
    }

    private static void deposit(int value, int account) {
        createTransaction(value, "deposit", account, 0);
    }

    private static void withdraw(int value, int account) {
        value = -value;
        createTransaction(value, "withdrawal", account, 0);
    }

    private static void transfer(int value, int account1, int account2) {
        value = -value;
        createTransaction(value, "transfer", account1, account2);
    }

    public static void create(int value, String type, int account1, int account2) {
        ArrayList<String> types = new ArrayList<>(Arrays.asList("deposit", "withdraw", "transfer"));
        if ((value > 0) & types.contains(type) & DatabaseManager.checkAccount(account1)
                & (DatabaseManager.checkAccount(account2) | account2 == 0)) {
            switch (type) {
                case "deposit":
                    deposit(value, account1);
                    break;
                case "withdraw":
                    withdraw(value, account1);
                    break;
                case "transfer":
                    transfer(value, account1, account2);
            }
        } else if (value <= 0) {
            System.out.println("Invalid Transaction Value");
        } else if (!types.contains(type)) {
            System.out.println("Invalid Transaction Type");
        } else if (!DatabaseManager.checkAccount(account1)) {
            System.out.println("Account Does Not Exist");
        } else if (!(DatabaseManager.checkAccount(account2) | account2 == 0)) {
            System.out.println("Receiving Account Does Not Exist");
        }
    }
}
