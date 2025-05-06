package com.mycompany.personalfinanceapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;

import java.io.IOException;

public class MainController {

    @FXML
    private StackPane contentPane;

    @FXML
    public void initialize() {
        loadDashboard(); // Φόρτωσε την αρχική σελίδα στην εκκίνηση
    }

    @FXML
    public void loadDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
            Node node = loader.load();

            // Πάρε τον controller και κάλεσε την update μέθοδο
            DashboardController controller = loader.getController();
            controller.refresh();

            contentPane.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadPrimary() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("primary.fxml"));
            Node node = loader.load();
            contentPane.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
