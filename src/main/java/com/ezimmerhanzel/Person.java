package com.ezimmerhanzel;

public class Person {
    public static boolean login(String username, String password) {
        if (DatabaseManager.correctPassword(username, password)) {
            System.out.println("Welcome to your account, " + username + ".");
            return true;
        }
        System.out.println("Incorrect Password");
        return false;

    }
}
