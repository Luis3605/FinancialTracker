package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction {
    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;
    private double amount;

    public Transaction(String date, String time, String description, String vendor, double amount) {
        this.date = LocalDate.parse(date);
        this.time = LocalTime.parse(time);
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public Transaction(LocalDate date, LocalTime time, String vendor, double amount) {
    }

    public Transaction() {

    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    public static class Deposit extends Transaction {
        public Deposit(LocalDate date, LocalTime time, String vendor, double amount) {
            super(date, time, vendor, amount);
        }
    }

    public class Payment extends Transaction {
        public Payment(LocalDate date, LocalTime time, String vendor, double amount) {
            super(date, time, vendor, amount);
        }
    }
}
