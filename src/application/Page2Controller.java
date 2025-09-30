package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Page2Controller {

    @FXML
    private HBox workersList;

    @FXML
    private ImageView addWorkerImage;

    private static final String WORKER_IMAGE_PATH = "file:/C:/Users/Admin/Downloads/images__1_-removebg-preview.png";
    private static final String ADD_WORKER_IMAGE_PATH = "file:/C:/Users/Admin/Downloads/feature.png";
    private static final String WORKERS_FILE = "workers.txt";

    @FXML
    public void initialize() {
        loadWorkers();
        addWorkerImage.setImage(new Image(ADD_WORKER_IMAGE_PATH));
    }

    @FXML
    private void handleAddWorker() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Worker");
        dialog.setHeaderText("Enter the name of the worker:");
        dialog.setContentText("Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(workerName -> {
            addWorkerButton(workerName);
            saveWorkersToFile();
        });
    }

    private void addWorkerButton(String workerName) {
        ImageView workerImageView = new ImageView(new Image(WORKER_IMAGE_PATH));
        workerImageView.setFitHeight(200);
        workerImageView.setFitWidth(200);
        workerImageView.setPreserveRatio(true);

        Button workerButton = new Button(workerName);
        workerButton.setOnAction(event -> handleWorkerButton(workerName));

        VBox workerBox = new VBox(workerImageView, workerButton);
        workerBox.setAlignment(javafx.geometry.Pos.CENTER); // Center the button under the image
        workersList.getChildren().add(workerBox);
    }

    private void handleWorkerButton(String workerName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("page3.fxml"));
            Parent root = loader.load();

            Page3Controller controller = loader.getController();
            controller.setWorkerName(workerName);

            Stage stage = new Stage();
            stage.setTitle(workerName + " Operations");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveWorkersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(WORKERS_FILE))) {
            for (javafx.scene.Node node : workersList.getChildren()) {
                if (node instanceof VBox) {
                    VBox workerBox = (VBox) node;
                    if (workerBox.getChildren().get(1) instanceof Button) {
                        Button workerButton = (Button) workerBox.getChildren().get(1);
                        writer.write(workerButton.getText());
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadWorkers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(WORKERS_FILE))) {
            String workerName;
            while ((workerName = reader.readLine()) != null) {
                addWorkerButton(workerName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("page1.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) workersList.getScene().getWindow(); // Get the current stage
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
