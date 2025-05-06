package com.mycompany.personalfinanceapp;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class DashboardController implements Initializable {

    @FXML private PieChart pieChart;
    @FXML private Label incomeLabel;
    @FXML private Label expenseLabel;
    @FXML private Label balanceLabel;
    @FXML private ComboBox<String> filterComboBox;

    private final ObservableList<Transaction> allTransactions = TransactionData.getTransactions();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filterComboBox.getItems().addAll("Ημερήσιο", "Μηνιαίο", "Ετήσιο");
        filterComboBox.setValue("Μηνιαίο");

        updateDashboard();  // Εμφανίζει αρχικές τιμές

        filterComboBox.setOnAction(e -> updateDashboard());
    }

    private void updateDashboard() {
        LocalDate now = LocalDate.now();
        String filter = filterComboBox.getValue();

        List<Transaction> filtered = allTransactions.stream()
                .filter(t -> {
                    LocalDate date = t.getDate();
                    switch (filter) {
                        case "Ημερήσιο":
                            return date.equals(now);
                        case "Μηνιαίο":
                            return date.getMonth().equals(now.getMonth()) && date.getYear() == now.getYear();
                        case "Ετήσιο":
                            return date.getYear() == now.getYear();
                        default:
                            return true;
                    }
                })
                .collect(Collectors.toList());

        double income = filtered.stream()
                .filter(t -> "Έσοδο".equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double expense = filtered.stream()
                .filter(t -> "Έξοδο".equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        incomeLabel.setText(String.format("Έσοδα: %.2f €", income));
        expenseLabel.setText(String.format("Έξοδα: %.2f €", expense));
        balanceLabel.setText(String.format("Υπόλοιπο: %.2f €", income - expense));

        Map<String, Double> categoryTotals = new HashMap<>();

        for (Transaction t : filtered) {
            if ("Έξοδο".equals(t.getType())) {
                categoryTotals.merge(t.getCategory(), t.getAmount(), Double::sum);
            }
        }

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        categoryTotals.forEach((category, total) -> {
            pieData.add(new PieChart.Data(category, total));
        });

        pieChart.setData(pieData);
    }

    @FXML
    public void goToMainApp() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("primary.fxml"));
            Stage stage = (Stage) incomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refresh() {
        updateDashboard();
    }
}
