package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
public class FinancialTracker {
    // ANSI color codes
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BG_COLOR_22 = "\u001B[48;5;22m";
    private static final String ANSI_TEXT_GOLD = "\u001B[38;5;214m";

    private static final String ANSI_BLUE = "\u001B[34m";


    private static final ArrayList<Transaction> transactions = new ArrayList<>();

    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println(ANSI_BG_COLOR_22 + ANSI_TEXT_GOLD + "Welcome to TransactionApp" + ANSI_RESET);
            System.out.println("Choose an option:");
            System.out.println(ANSI_GREEN + "D) Add Deposit" + ANSI_RESET);
            System.out.println(ANSI_GREEN + "P) Make Payment (Debit)" + ANSI_RESET);
            System.out.println(ANSI_GREEN + "L) Ledger" + ANSI_RESET);
            System.out.println(ANSI_RED + "X) Exit" + ANSI_RESET);

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println(ANSI_RED + "Invalid option" + ANSI_RESET);
                    break;
            }
        }

        scanner.close();
    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println(ANSI_BG_COLOR_22 + ANSI_TEXT_GOLD + "Ledger" + ANSI_RESET);
            System.out.println(ANSI_BG_COLOR_22 + ANSI_TEXT_GOLD + "Choose an option:" + ANSI_RESET);
            System.out.println(ANSI_GREEN + "A) All" + ANSI_RESET);
            System.out.println(ANSI_GREEN + "D) Deposits" + ANSI_RESET);
            System.out.println(ANSI_GREEN + "P) Payments" + ANSI_RESET);
            System.out.println(ANSI_YELLOW + "R) Reports" + ANSI_RESET);
            System.out.println(ANSI_RED + "H) Home" + ANSI_RESET);

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                    break;
                default:
                    System.out.println(ANSI_RED + "Invalid option" + ANSI_RESET);
                    break;
            }
        }
    }

    private static void addDeposit(Scanner scanner) {
        System.out.println("Enter the date (yyyy-MM-dd HH:mm:ss): ");
        String dateTimeStr = scanner.nextLine();
        LocalDate date;
        LocalTime time;
        try {
            String[] dateTimeParts = dateTimeStr.split(" ");
            date = LocalDate.parse(dateTimeParts[0], DATE_FORMATTER);
            time = LocalTime.parse(dateTimeParts[1], TIME_FORMATTER);
        } catch (Exception e) {
            System.err.println(ANSI_RED + "Invalid date/time format. Use yyyy-MM-dd HH:mm:ss." + ANSI_RESET);
            return;
        }

        System.out.println("Enter the vendor: ");
        String vendor = scanner.nextLine();

        double amount;
        while (true) {
            System.out.println("Enter the deposit amount: ");
            try {
                amount = Double.parseDouble(scanner.nextLine());
                if (amount <= 0) {
                    System.err.println("Amount must be a positive number.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.err.println(ANSI_RED + "Invalid amount format. Please enter a positive number." + ANSI_RESET);
            }
        }

        Transaction deposit = new Transaction.Deposit(date, time, vendor, amount);
        transactions.add(deposit);
        System.out.println(ANSI_GREEN + "Deposit added successfully." + ANSI_RESET);
    }

    private static void addPayment(Scanner scanner) {
        System.out.println("Enter the date (yyyy-MM-dd HH:mm:ss): ");
        String dateTimeStr = scanner.nextLine();
        try {
            String[] dateTimeParts = dateTimeStr.split(" ");
        } catch (Exception e) {
            System.err.println(ANSI_RED + "Invalid date/time format. Use yyyy-MM-dd HH:mm:ss." + ANSI_RESET);
            return; // Exit the method
        }

        System.out.println("Enter the vendor: ");
        String vendor = scanner.nextLine();

        double amount;
        while (true) {
            System.out.println("Enter the payment amount: ");
            try {
                amount = Double.parseDouble(scanner.nextLine());
                if (amount <= 0) {
                    System.err.println("Amount must be a positive number.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.err.println(ANSI_RED + "Invalid input." + ANSI_RESET);
            }
        }

        System.out.println(ANSI_GREEN + "Payment added successfully." + ANSI_RESET);
    }



    private static void displayLedger() {
        System.out.println(ANSI_BG_COLOR_22 + ANSI_TEXT_GOLD + "Ledger");
        System.out.println(ANSI_BG_COLOR_22 + ANSI_TEXT_GOLD + "Date       Time     Vendor             Description         Amount");

        for (Transaction transaction : transactions)
            System.out.printf("%-10s %-8s %-20s %-20s %.2f%n",
                    transaction.getDate(),
                    transaction.getTime(),
                    transaction.getVendor(),
                    transaction.getDescription(),
                    transaction.getAmount());
    }


    private static void displayDeposits() {
        System.out.println("Deposits");
        System.out.println("Date       Time     Vendor             Amount");

        for (Transaction transaction : transactions) {
            if (transaction instanceof Transaction.Deposit) {
                Transaction.Deposit deposit = (Transaction.Deposit) transaction; // Cast to Deposit
                System.out.printf("%-10s %-8s %-20s %.2f%n",
                        deposit.getDate(),
                        deposit.getTime(),
                        deposit.getVendor(),
                        deposit.getAmount());
            }
        }
    }

    private static void displayPayments() {
        System.out.println(ANSI_BG_COLOR_22 + ANSI_TEXT_GOLD + "Payments");
        System.out.println("Date       Time     Vendor             Amount");

        for (Transaction transaction : transactions) {
            if (transaction instanceof Transaction.Payment) {
                Transaction.Payment payment = (Transaction.Payment) transaction; // Cast to Payment
                System.out.printf("%-10s %-8s %-20s %.2f%n",
                        payment.getDate(),
                        payment.getTime(),
                        payment.getVendor(),
                        payment.getAmount());
            }
        }
    }


    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println(ANSI_BG_COLOR_22 + ANSI_TEXT_GOLD + "Reports" + ANSI_RESET);
            System.out.println(ANSI_BG_COLOR_22 + ANSI_TEXT_GOLD + "Choose an option:" + ANSI_RESET);
            System.out.println(ANSI_GREEN + "1) Month To Date");
            System.out.println(ANSI_GREEN + "2) Previous Month");
            System.out.println(ANSI_GREEN + "3) Year To Date");
            System.out.println(ANSI_YELLOW + "4) Previous Year");
            System.out.println(ANSI_YELLOW + "5) Search by Vendor");
            System.out.println(ANSI_RED + "0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    LocalDate today = LocalDate.now();
                    LocalDate startOfMonth = today.withDayOfMonth(1);
                    filterTransactionsByDate(startOfMonth, today);
                    break;

                case "2":
                    today = LocalDate.now();
                    LocalDate firstDayOfCurrentMonth = today.withDayOfMonth(1);
                    LocalDate lastDayOfLastMonth = firstDayOfCurrentMonth.minusDays(1);
                    LocalDate firstDayOfLastMonth = lastDayOfLastMonth.withDayOfMonth(1);
                    filterTransactionsByDate(firstDayOfLastMonth, lastDayOfLastMonth);
                    break;

                case "3":
                    today = LocalDate.now();
                    LocalDate startOfYear = today.withDayOfYear(1);
                    filterTransactionsByDate(startOfYear, today);
                    break;

                case "4":
                    today = LocalDate.now();
                    startOfYear = today.withDayOfYear(1);
                    LocalDate endOfLastYear = startOfYear.minusDays(1);
                    LocalDate startOfLastYear = endOfLastYear.withDayOfYear(1);
                    filterTransactionsByDate(startOfLastYear, endOfLastYear);
                    break;

                case "5":
                    System.out.print("Enter the vendor name: ");
                    String vendorName = scanner.nextLine();
                    filterTransactionsByVendor(vendorName);
                    break;

                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        System.out.println("Transactions between " + startDate + " and " + endDate);
        System.out.println("Date       Time     Vendor             Description         Amount");

        for (Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();

            if (transactionDate.isAfter(startDate) && transactionDate.isBefore(endDate)) {
                System.out.printf("%-10s %-8s %-20s %-20s %.2f%n",
                        transaction.getDate(),
                        transaction.getTime(),
                        transaction.getVendor(),
                        transaction.getDescription(),
                        transaction.getAmount());
            }
        }
    }


    private static void filterTransactionsByVendor(String vendor) {
        System.out.println("Transactions with Vendor: " + vendor);
        System.out.println("Date       Time     Description         Amount");

        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equals(vendor)) {
                System.out.printf("%-10s %-8s %-20s %.2f%n",
                        transaction.getDate(),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getAmount());
            }
        }
    }


    public static void loadTransactions(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    String date = parts[0];
                    String time = parts[1];
                    String vendor = parts[2];
                    String description = parts[3];
                    double amount;

                    try {
                        amount = Double.parseDouble(parts[4]);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid amount format on line: " + line);
                        continue; // Skip this line and move to the next one
                    }

                    Transaction transaction = new Transaction(date, time, vendor, description, amount);
                    transactions.add(transaction);
                } else {
                    System.err.println("Invalid transaction format: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading transactions: " + e.getMessage());
        }
    }

}
