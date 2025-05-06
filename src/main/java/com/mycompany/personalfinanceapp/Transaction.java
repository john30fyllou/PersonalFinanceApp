package com.mycompany.personalfinanceapp;

import java.time.LocalDate;

public class Transaction {
    private String description;
    private double amount;
    private String category;
    private String type;
    private LocalDate date;

    public Transaction(String description, String category, String type, double amount, LocalDate date) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.type = type;
        this.date = date;
    }

    // Getters and Setters

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
