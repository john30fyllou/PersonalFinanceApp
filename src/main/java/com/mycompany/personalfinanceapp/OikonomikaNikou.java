package com.mycompany.personalfinanceapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class OikonomikaNikou extends Application {

    private static Scene scene;

    @Override
public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("MainLayout.fxml"));
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.setTitle("Personal Finance App");
    stage.show();
}


    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        // Φορτώνει το Dashboard.fxml
        FXMLLoader fxmlLoader = new FXMLLoader(OikonomikaNikou.class.getResource("/com/mycompany/personalfinanceapp/Dashboard.fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
