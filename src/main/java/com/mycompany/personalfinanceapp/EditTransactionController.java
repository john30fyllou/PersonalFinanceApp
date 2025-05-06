package com.mycompany.personalfinanceapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class EditTransactionController {

    @FXML private TextField descriptionField;
    @FXML private TextField amountField;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> transactionTypeComboBox;
    @FXML private ComboBox<String> categoryComboBox;

    private Transaction transaction;
    private PrimaryController mainController;

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
        loadTransactionData();
    }

    public void setPrimaryController(PrimaryController controller) {
        this.mainController = controller;
    }

    @FXML
    public void initialize() {
        ObservableList<String> types = FXCollections.observableArrayList("Έσοδο", "Έξοδο");
        transactionTypeComboBox.setItems(types);

        ObservableList<String> categories = FXCollections.observableArrayList(
                "Φαγητό", "Καύσιμα", "Μισθός", "Ψυχαγωγία", "Άλλο"
        );
        categoryComboBox.setItems(categories);
    }

    private void loadTransactionData() {
        if (transaction != null) {
            descriptionField.setText(transaction.getDescription());
            amountField.setText(String.valueOf(transaction.getAmount()));
            datePicker.setValue(transaction.getDate());
            transactionTypeComboBox.setValue(transaction.getType());
            categoryComboBox.setValue(transaction.getCategory());
        }
    }

    @FXML
    public void handleSave(ActionEvent event) {
        String description = descriptionField.getText();
        String amountText = amountField.getText();
        LocalDate date = datePicker.getValue();
        String type = transactionTypeComboBox.getValue();
        String category = categoryComboBox.getValue();

        if (description.isEmpty() || amountText.isEmpty() || date == null || type == null || category == null) {
            showAlert("Παρακαλώ συμπληρώστε όλα τα πεδία.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            showAlert("Μη έγκυρο ποσό.");
            return;
        }

        transaction.setDescription(description);
        transaction.setAmount(amount);
        transaction.setDate(date);
        transaction.setType(type);
        transaction.setCategory(category);

        if (mainController != null) {
            mainController.updateTransaction(transaction);
        }

        closeWindow();
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) descriptionField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Σφάλμα");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
