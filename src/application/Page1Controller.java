package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

public class Page1Controller {

    @FXML
    private void handleChefDEquipeButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("page4.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.setTitle("Chef d'équipe Data");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOperateurButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("page2.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.setTitle("Workers List");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
