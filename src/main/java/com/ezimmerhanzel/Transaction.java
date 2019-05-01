package com.ezimmerhanzel;

import java.util.ArrayList;
import java.util.Arrays;

public class Transaction {
    private static void createTransaction(int value, String type, int account1, int account2) {
        if (Math.abs(value) <= DatabaseManager.getBalance(account1) | type.equals("deposit")) {
            DatabaseManager.addTransaction(value, type, account1, account2);
        } else {
            System.out.println("Insufficient Funds");
        }
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

    public static void create(String value, String type, String account1, String account2) {
        ArrayList<String> types = new ArrayList<>(Arrays.asList("deposit", "withdraw", "transfer"));
        try {
            int valueNum = Integer.parseInt(value);
            int account1Num = Integer.parseInt(account1);
            int account2Num = Integer.parseInt(account2);
            if ((valueNum > 0) & types.contains(type) & DatabaseManager.checkAccount(account1Num)
                    & (DatabaseManager.checkAccount(account2Num) | account2Num == 0)) {
                switch (type) {
                    case "deposit":
                        deposit(valueNum, account1Num);
                        break;
                    case "withdraw":
                        withdraw(valueNum, account1Num);
                        break;
                    case "transfer":
                        transfer(valueNum, account1Num, account2Num);
                }
            } else if (valueNum <= 0) {
                System.out.println("Invalid Transaction Value");
            } else if (!types.contains(type)) {
                System.out.println("Invalid Transaction Type");
            } else if (!DatabaseManager.checkAccount(account1Num)) {
                System.out.println("Account Does Not Exist");
            } else if (!(DatabaseManager.checkAccount(account2Num) | account2Num == 0)) {
                System.out.println("Receiving Account Does Not Exist");
            }
        } catch (NumberFormatException e) {
            System.out.println("A numerical value must be entered for Amount, Account Number, and target Account Number.");
        }
    }
}
