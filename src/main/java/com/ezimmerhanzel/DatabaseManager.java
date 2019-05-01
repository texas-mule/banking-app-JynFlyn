package com.ezimmerhanzel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

public class DatabaseManager {
    public static void startDatabase() {
        executeCommand("CREATE TABLE IF NOT EXISTS users" +
                "(username TEXT, password TEXT, status TEXT)");
        executeCommand("CREATE TABLE IF NOT EXISTS accounts" +
                "(accountNumber INTEGER, balance INTEGER)");
        executeCommand("CREATE TABLE IF NOT EXISTS transactions" +
                "(value INTEGER, type TEXT, accountNumbers TEXT)");
        executeCommand("CREATE TABLE IF NOT EXISTS account_requests" +
                "(username TEXT, accountNumber INTEGER, type TEXT)");
        executeCommand("CREATE TABLE IF NOT EXISTS linking_table" +
                "(username TEXT, accountNumber INTEGER)");
        if (!DatabaseManager.userExists("admin")) {
            DatabaseManager.addUser("admin", "admin", "admin");
        }
        if (!DatabaseManager.userExists("emp")) {
            DatabaseManager.addUser("emp", "emp", "employee");
        }
    }

    private static void executeCommand(String command) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/Eric", "postgres",
                "password");
             Statement statement = connection.createStatement()) {
            statement.execute(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static ArrayList<String> getResult(String command, String column) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/Eric", "postgres",
                "password");
             Statement statement = connection.createStatement()) {
            ArrayList<String> results = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(command);
            while (resultSet.next()) {
                results.add(resultSet.getString(column));
            }
            return results;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    public static void addUser(String username, String password, String status) {
        executeCommand("INSERT INTO users (username, password, status) " +
                "VALUES('" + username + "', '" + password + "', '" + status + "')");
    }

    public static void addAccountRequest(String username) {
        int accountNumber = new Random().nextInt(99999) + 1;
        if (checkAccounts(accountNumber)) {
            executeCommand("INSERT INTO account_requests (username, accountNumber, type) " +
                    "VALUES('" + username + "', " + Integer.toString(accountNumber) + ", 'New')");
            return;
        }
        addAccountRequest(username);
    }

    public static void addAccountRequest(String username, int accountNumber) {
        executeCommand("INSERT INTO account_requests (username, accountNumber, type) " +
                "VALUES('" + username + "', " + Integer.toString(accountNumber) + ", 'Joint')");
    }

    public static void addAccount(int accountNumber) {
        try {
            executeCommand("INSERT INTO linking_table (username, accountNumber) " + "VALUES('" +
                    getResult("SELECT * FROM account_requests WHERE accountNumber = "
                            + accountNumber, "username").get(0) + "', " + Integer.toString(accountNumber) + ")");
            executeCommand("INSERT INTO accounts (accountNumber, balance) " + "VALUES(" +
                    Integer.toString(accountNumber) + ", 0)");
            removeAccountRequest(accountNumber);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("The Account Number you provided could not be found.");
        }
    }

    public static void addTransaction(int value, String type, int accountNumber1, int accountNumber2) {
        String accountNumbers;
        if (accountNumber2 != 0) {
            accountNumbers = Integer.toString(accountNumber1) + " to " + Integer.toString(accountNumber2);
        } else {
            accountNumbers = Integer.toString(accountNumber1);
        }
        executeCommand("INSERT INTO transactions (value, type, accountNumbers) " +
                "VALUES(" + Integer.toString(value) + ", '" + type + "', "
                + accountNumbers + ")");
        changeBalance(value, accountNumber1);
        if (type.equals("Transfer")) {
            changeBalance(Math.abs(value), accountNumber2);
        }
    }

    public static void removeAccountRequest(int accountNumber) {
        if (checkAccount(accountNumber)) {
            executeCommand("DELETE FROM account_requests WHERE accountNumber = " + accountNumber);
        } else {
            System.out.println("The Account Number you provided could not be found.");
        }
    }

    public static void removeAccount(int accountNumber) {
        if (checkAccount(accountNumber)) {
            executeCommand("DELETE FROM accounts WHERE accountNumber=" + accountNumber);
            executeCommand("DELETE FROM linking_table WHERE accountNumber=" +accountNumber);
        } else {
            System.out.println("Account doesn't exist");
        }
    }

    public static boolean userExists(String username) {
        return !getResult("SELECT * FROM users WHERE username = '" + username + "'", "username").isEmpty();
    }

    public static boolean correctPassword(String username, String password) {
        String correctPassword = getResult("SELECT password FROM users WHERE username = '" + username + "'",
                "password").get(0);
        return password.equals(correctPassword);
    }

    private static void changeBalance(int value, int accountNumber) {
        int balance = getBalance(accountNumber);
        executeCommand("UPDATE accounts SET balance = " + (balance + value) + " WHERE accountNumber = " + accountNumber);
    }

    private static int getBalance(int accountNumber) {
        return Integer.parseInt(getResult("SELECT balance FROM accounts WHERE accountNumber = +" + accountNumber,
                "balance").get(0));
    }

    public static String getUserStatus(String username) {
        return getResult("SELECT status FROM users WHERE username = '" + username + "'",
                "status").get(0);
    }

    public static void listAccountRequests() {
        ArrayList<String> usernames = getResult("SELECT * FROM account_requests", "username");
        ArrayList<String> accountNumbers = getResult("SELECT * FROM account_requests", "accountNumber");
        ArrayList<String> types = getResult("SELECT * FROM account_requests", "type");
        for (int i = 0; i < usernames.size(); i++) {
            System.out.println(types.get(i) + " account request from " + usernames.get(i) + ": " + accountNumbers.get(i));
        }
    }

    private static boolean checkAccounts(int accountNumber) {
        return Stream.concat(getResult("SELECT * FROM accounts", "accountNumber").stream(),
                getResult("SELECT * FROM account_requests", "accountNumber").stream())
                .noneMatch(e -> e.equals(Integer.toString(accountNumber)));
    }

    public static boolean checkAccount(int accountNumber) {
        return getResult("SELECT * FROM accounts", "accountNumber").stream()
                .anyMatch(e -> e.equals(Integer.toString(accountNumber)));
    }

    public static void listCustomers() {
        ArrayList<String> customers = getResult("SELECT username FROM users WHERE status = 'Customer'", "username");
        ArrayList<String> passwords = getResult("SELECT password FROM users WHERE status = 'Customer'", "password");
        for (int i = 0; i < customers.size(); i++) {
            System.out.println("Username: " + customers.get(i) + " Password: " + passwords.get(i));
        }
    }

    public static boolean hasAccount(int accountNumber, String username) {
        String command = "SELECT * FROM linking_table WHERE accountNumber = ";
        return getResult(command + accountNumber, "username").contains(username);
    }

    public static void listCustomerInformation(String username) {
        if (userExists(username)) {
            ArrayList<String> accountNumbers = getResult("SELECT * FROM linking_table WHERE username = '" + username + "'",
                    "accountNumber");
            for (String accountNumber : accountNumbers) {
                System.out.println("Account: " + accountNumber + "   Balance: " +
                        getResult("SELECT * FROM accounts WHERE accountNumber = " + accountNumber, "balance").get(0));
            }
        } else {
            System.out.println("User not found.");
        }
    }

    public static void changeName(String currentName, String newName) {
        if (userExists(currentName)) {
            executeCommand("UPDATE users SET username = '" + newName + "' WHERE username = '" + currentName + "'");
            System.out.println("username changed");
        } else {
            System.out.println("user doesn't exist");
        }
    }

    public static void changePassword(String username, String newPassword) {
        if (userExists(username)) {
            executeCommand("UPDATE users SET password = '" + newPassword + "' WHERE username = '" + username + "'");
            System.out.println("password changed");
        } else {
            System.out.println("user doesn't exist");
        }
    }

    public static void listTransactions() {
        ArrayList<String> values = getResult("SELECT value FROM transactions", "value");
        ArrayList<String> types = getResult("SELECT type FROM transactions", "type");
        ArrayList<String> accountNumber1 = getResult("SELECT accountNumbers FROM transactions", "accountNumbers");
        for (int i = 0; i < values.size(); i++) {
            System.out.println(values.get(i) + " " + types.get(i) + " " + accountNumber1.get(i));
        }
    }
}
