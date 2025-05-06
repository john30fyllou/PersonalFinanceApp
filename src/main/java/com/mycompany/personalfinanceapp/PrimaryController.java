package com.mycompany.personalfinanceapp;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;
import java.io.IOException;
import java.time.LocalDate;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class PrimaryController {

    @FXML private TableView<Transaction> table;
    @FXML private TableColumn<Transaction, String> descCol, catCol, typeCol;
    @FXML private TableColumn<Transaction, Double> amountCol;
    @FXML private TableColumn<Transaction, LocalDate> dateCol;
    @FXML private Label incomeLabel, expenseLabel, balanceLabel;
    @FXML private TableColumn<Transaction, Void> actionColumn;
    
private final ObservableList<Transaction> transactions = TransactionData.getTransactions();

    @FXML
    public void initialize() {
        addActionButtonsToTable();
        table.setItems(transactions);

        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        catCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        updateSummary();
    }

    @FXML
    public void openAddDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("secondary.fxml"));
            Parent root = loader.load();

            // Πάρε το controller και πέρασέ του την αναφορά στο this
            SecondaryController controller = loader.getController();
            controller.setPrimaryController(this);

            Stage stage = new Stage();
            stage.setTitle("Προσθήκη Συναλλαγής");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
        }
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
        updateSummary();
    }

    private void updateSummary() {
        double income = transactions.stream()
                .filter(t -> t.getType().equals("Έσοδο"))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double expense = transactions.stream()
                .filter(t -> t.getType().equals("Έξοδο"))
                .mapToDouble(Transaction::getAmount)
                .sum();

        incomeLabel.setText("Έσοδα: " + income + "€");
        expenseLabel.setText("Έξοδα: " + expense + "€");
        balanceLabel.setText("Υπόλοιπο: " + (income - expense) + "€");
    }

    private void addActionButtonsToTable() {
    Callback<TableColumn<Transaction, Void>, TableCell<Transaction, Void>> cellFactory = (final TableColumn<Transaction, Void> param) -> new TableCell<>() {

        private final Button editButton = new Button("\u270E");   // ✎
        private final Button deleteButton = new Button("\u2716"); // ✖
        private final HBox actionBox = new HBox(5, editButton, deleteButton);

        {
            // Στυλ κουμπιών
            editButton.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black; -fx-font-weight: bold;");
            deleteButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-weight: bold;");

            editButton.setPrefWidth(30); // Ίδιο πλάτος
            deleteButton.setPrefWidth(30);

            actionBox.setAlignment(Pos.CENTER);
            actionBox.setStyle("-fx-alignment: center;");

            // Επεξεργασία
            editButton.setOnAction(e -> {
                Transaction transaction = getTableView().getItems().get(getIndex());
                openEditDialog(transaction);
            });

            // Διαγραφή
            deleteButton.setOnAction(e -> {
                Transaction transaction = getTableView().getItems().get(getIndex());
                transactions.remove(transaction);
                updateSummaryAfterDeletion(transaction);
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(actionBox);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setAlignment(Pos.CENTER); // Κεντράρισμα του κελιού
            }
        }
    };

    actionColumn.setCellFactory(cellFactory);
}

   public void openEditDialog(Transaction transaction) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EditTransactionDialog.fxml"));
        Parent root = loader.load();

        EditTransactionController controller = loader.getController();
        controller.setTransaction(transaction);
        controller.setPrimaryController(this);
        Stage stage = new Stage();
        stage.setTitle("Επεξεργασία Συναλλαγής");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

    } catch (IOException e) {
        
    }
}


    public void updateTransaction(Transaction updatedTransaction) {
        int index = transactions.indexOf(updatedTransaction);
        if (index != -1) {
            transactions.set(index, updatedTransaction);
            updateSummary();  // Αν θέλεις να ανανεώσεις το υπόλοιπο κλπ.
        }
    }

    public void updateSummaryAfterDeletion(Transaction transaction) {
        double income = transactions.stream()
                .filter(t -> t.getType().equals("Έσοδο"))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double expense = transactions.stream()
                .filter(t -> t.getType().equals("Έξοδο"))
                .mapToDouble(Transaction::getAmount)
                .sum();

        // Ενημέρωση των ετικετών με τα νέα σύνολα
        incomeLabel.setText("Έσοδα: " + income + "€");
        expenseLabel.setText("Έξοδα: " + expense + "€");
        balanceLabel.setText("Υπόλοιπο: " + (income - expense) + "€");
    }

    public void refreshTransactionTable() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
