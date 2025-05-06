package com.mycompany.personalfinanceapp;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Control;
import java.time.LocalDate;

public class SecondaryController {

    @FXML private TextField descField;
    @FXML private TextField amountField;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private ComboBox<String> typeCombo;
    @FXML private DatePicker datePicker;

    private PrimaryController primaryController;

    public void setPrimaryController(PrimaryController controller) {
        this.primaryController = controller;
    }

    @FXML
public void initialize() {
    typeCombo.setItems(FXCollections.observableArrayList("Έσοδο", "Έξοδο"));
    categoryCombo.setItems(FXCollections.observableArrayList("Φαγητό", "Καύσιμα", "Μισθός", "Ψυχαγωγία", "Άλλο"));
    datePicker.setValue(LocalDate.now());

    amountField.setTextFormatter(new TextFormatter<>(change -> {
        String newText = change.getControlNewText();
        if (newText.matches("\\d*(\\.\\d{0,2})?")) {
            return change;
        } else {
            return null;
        }
    }));

    addResetOnEdit(descField);
    addResetOnEdit(amountField);

    typeCombo.valueProperty().addListener((obs, oldVal, newVal) -> resetFieldStyle(typeCombo));
    datePicker.valueProperty().addListener((obs, oldVal, newVal) -> resetFieldStyle(datePicker));

    // Προσθήκη ελέγχου για κατηγορία "Μισθός"
    categoryCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
        resetFieldStyle(categoryCombo);

        if ("Μισθός".equals(newVal)) {
            typeCombo.setValue("Έσοδο");
            typeCombo.setDisable(true);
        } else {
            typeCombo.setDisable(false);
        }
    });
}

private void handleCategorySelection() {
    String selectedCategory = categoryCombo.getValue();
    if ("Μισθός".equals(selectedCategory)) {
        categoryCombo.setValue("Έσοδο");
        typeCombo.setDisable(true);  // Δεν επιτρέπεται να το αλλάξει
    } else {
        typeCombo.setDisable(false); // Ενεργοποιείται ξανά
    }
}
    private void highlightField(Control field) {
        field.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
    }

    private void resetFieldStyle(Control field) {
        field.setStyle("");
    }

    private void addResetOnEdit(TextField field) {
        field.textProperty().addListener((obs, oldText, newText) -> resetFieldStyle(field));
    }

    @FXML
    public void handleSave() {
        boolean valid = true;

        resetFieldStyle(descField);
        resetFieldStyle(amountField);
        resetFieldStyle(categoryCombo);
        resetFieldStyle(typeCombo);
        resetFieldStyle(datePicker);

        String description = descField.getText();
        String amountText = amountField.getText();
        String category = categoryCombo.getValue();
        String type = typeCombo.getValue();
        LocalDate date = datePicker.getValue();

        if (description == null || description.trim().isEmpty()) {
            highlightField(descField);
            valid = false;
        }

        if (amountText == null || amountText.trim().isEmpty()) {
            highlightField(amountField);
            valid = false;
        } else {
            try {
                Double.valueOf(amountText);
            } catch (NumberFormatException e) {
                highlightField(amountField);
                System.out.println("Μη έγκυρος αριθμός: " + amountText);
                return;
            }
        }

        if (category == null) {
            highlightField(categoryCombo);
            valid = false;
        }

        if (type == null) {
            highlightField(typeCombo);
            valid = false;
        }

        if (date == null) {
            highlightField(datePicker);
            valid = false;
        }

        if (!valid) {
            System.out.println("Υπάρχουν κενά ή μη έγκυρα πεδία.");
            return;
        }

        double amount = Double.parseDouble(amountText);
        Transaction transaction = new Transaction(description, category, type, amount, date);

        if (primaryController != null) {
            primaryController.addTransaction(transaction);
        }

        ((Stage) descField.getScene().getWindow()).close();
    }

    @FXML
    public void handleCancel() {
        ((Stage) descField.getScene().getWindow()).close();
    }
}
