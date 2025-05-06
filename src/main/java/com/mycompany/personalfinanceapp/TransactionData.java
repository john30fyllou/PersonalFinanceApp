package com.mycompany.personalfinanceapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TransactionData {
    private static final ObservableList<Transaction> transactions = FXCollections.observableArrayList();

    public static ObservableList<Transaction> getTransactions() {
        return transactions;
    }

    public static void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public static void clearTransactions() {
        transactions.clear();
    }
}
