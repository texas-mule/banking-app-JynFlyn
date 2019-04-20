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
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS users" +
                    "(username TEXT, password TEXT, status TEXT)");
            statement.execute("CREATE TABLE IF NOT EXISTS accounts" +
                    "(accountNumber INTEGER, balance INTEGER, owner1 TEXT, owner2 TEXT)");
            statement.execute("CREATE TABLE IF NOT EXISTS transactions" +
                    "(value INTEGER, type TEXT, accountNumber1 INTEGER, accountNumber2 INTEGER)");
            statement.execute("CREATE TABLE IF NOT EXISTS account_requests" +
                    "(username TEXT, accountNumber INTEGER)");
            statement.execute("INSERT INTO users (username, password, status) VALUES('', '', '')");
        } catch (Exception e) {
            System.out.println("error" + e.getMessage());
        }

    }

    private static void executeCommand(String command) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
             Statement statement = connection.createStatement()) {
            statement.execute(command);
        } catch (Exception e) {
            System.out.println("error" + e.getMessage());
        }
    }


    private static ArrayList<String> getResult(String command, String column) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
             Statement statement = connection.createStatement()) {
            ArrayList<String> results = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(command);
            while (resultSet.next()) {
                results.add(resultSet.getString(column));
            }
            return results;
        } catch (Exception e) {
            e.printStackTrace();
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
            executeCommand("INSERT INTO account_requests (username, accountNumber) " +
                    "VALUES('" + username + "', " + Integer.toString(accountNumber) + ")");
            return;
        }
        addAccountRequest(username);
    }

    public static void addAccount(int accountNumber) {
        executeCommand("INSERT INTO accounts (accountNumber, balance, owner1, owner2) " + "VALUES(" +
                Integer.toString(accountNumber) + ", 0, '" + getResult("SELECT * FROM account_requests WHERE accountNumber = "
                + accountNumber, "username").get(0) + "', 'null')");
        removeAccountRequest(accountNumber);
    }

    public static void addTransaction(int value, String type, int accountNumber1, int accountNumber2) {
        executeCommand("INSERT INTO transactions (value, type, accountNumber1, accountNumber2) " +
                "VALUES(" + Integer.toString(value) + ", '" + type + "', "
                + Integer.toString(accountNumber1) + ", " + Integer.toString(accountNumber2) + ")");
        changeBalance(value, accountNumber1);
        if (type.equals("Transfer")) {
            changeBalance(Math.abs(value), accountNumber2);
        }
    }

    public static void addJointUser(String username, int accountNumber) {
        executeCommand("UPDATE accounts SET owner2 = '" + username + "' WHERE accountNumber = " + accountNumber);
        removeAccountRequest(accountNumber);
    }

    public static void removeAccountRequest(int accountNumber) {
        executeCommand("DELETE FROM account_requests WHERE accountNumber = " + accountNumber);
    }

    public static void removeAccount(int accountNumber) {
        executeCommand("DELETE FROM accounts WHERE accountNumber=" + accountNumber);
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
        ArrayList<String> userNames = getResult("SELECT * FROM account_requests", "username");
        ArrayList<String> passwords = getResult("SELECT * FROM account_requests", "accountNumber");
        for (int i = 0; i < userNames.size(); i++) {
            System.out.println(userNames.get(i) + "   " + passwords.get(i));
        }
    }

    private static boolean checkAccounts(int accountNumber) {
        return Stream.concat(getResult("SELECT * FROM account_requests", "accountNumber").stream(),
                getResult("SELECT * FROM account_requests", "accountNumber").stream())
                .noneMatch(e -> e.equals(Integer.toString(accountNumber)));
    }

    public static void listCustomers() {
        ArrayList<String> customers = getResult("SELECT username FROM users WHERE status = 'Customer'", "username");
        for (String customer : customers) {
            System.out.println(customer);
        }
    }

    public static void listCustomerInformation(String username) {
        ArrayList<String> accountNumbers = getResult("SELECT * FROM accounts WHERE owner1 = '" + username + "'",
                "accountNumber");
        ArrayList<String> balances = getResult("SELECT * FROM accounts WHERE owner1 = '" + username + "'",
                "balance");
        for (int i = 0; i < balances.size(); i++) {
            System.out.println(accountNumbers.get(i) + "   " + balances.get(i));
        }
    }

    public static void changeName(String currentName, String newName) {
        executeCommand("UPDATE users SET username = '" + newName + "' WHERE username = '" + currentName + "'");
    }

    public static void changePassword(String username, String newPassword) {
        executeCommand("UPDATE users SET password = '" + newPassword + "' WHERE username = '" + username + "'");
    }
}
