package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Page4Controller {

    @FXML
    private VBox workersList;

    public void initialize() {
        loadWorkersData();
    }

    private void loadWorkersData() {
        Path filePath = Paths.get("workers.txt");
        if (Files.exists(filePath)) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String workerName = line.trim();
                    displayWorker(workerName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("workers.txt not found.");
        }
    }

    private void displayWorker(String workerName) {
        // Create a worker section for each worker
        WorkerSection workerSection = new WorkerSection(workerName);
        workersList.getChildren().add(workerSection);
    }

    private static class WorkerSection extends VBox {

        private final String workerName;
        private int totalDiameterCount = 0; // To store the count of diameters

        WorkerSection(String workerName) {
            this.workerName = workerName;
            this.setSpacing(15);

            Label workerLabel = new Label("Operator: " + workerName);
            workerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;"); // Make font bold
            this.getChildren().add(workerLabel);
            loadOperationsData(workerName);

            // Display the total quantity of diameters
            Label totalQuantityLabel = new Label("Total Quantity of Diameters: " + totalDiameterCount);
            totalQuantityLabel.setStyle("-fx-padding: 0 0 0 20px;"); // Indent the total quantity label
            this.getChildren().add(totalQuantityLabel);

            // Add a TextArea for remarks
            Label remarksLabel = new Label("Remarks:");
            remarksLabel.setStyle("-fx-padding: 0 0 0 20px;");
            TextArea remarksArea = new TextArea();
            remarksArea.setPrefWidth(400);
            remarksArea.setPrefHeight(100);
            remarksArea.setPromptText("Enter remarks");
            this.getChildren().addAll(remarksLabel, remarksArea);

            // Save remarks when text changes
            remarksArea.textProperty().addListener((observable, oldValue, newValue) -> {
                saveRemarksToFile(newValue);
            });
        }

        private void loadOperationsData(String workerName) {
            String fileName = System.getProperty("user.home") + "/Desktop/operations_" + workerName + ".txt";
            Path filePath = Paths.get(fileName);
            if (Files.exists(filePath)) {
                Map<String, Map<Double, Integer>> operationDiameters = readOperationsFile(fileName);

                for (Map.Entry<String, Map<Double, Integer>> entry : operationDiameters.entrySet()) {
                    String operationName = entry.getKey();
                    Map<Double, Integer> diameters = entry.getValue();

                    // Increment the total diameter count
                    totalDiameterCount += diameters.values().stream().mapToInt(Integer::intValue).sum();

                    // Build the diameters string
                    StringBuilder diametersStr = new StringBuilder();
                    diametersStr.append(operationName).append(": ");
                    diameters.forEach((diameter, count) -> diametersStr.append("[").append(diameter).append(" : ").append(count).append("] "));

                    // Display the list of diameters
                    Label operationLabel = new Label(diametersStr.toString());
                    operationLabel.setStyle("-fx-padding: 0 0 0 20px;"); // Indent the operation label
                    this.getChildren().add(operationLabel);
                }
            } else {
                System.err.println("File not found: " + fileName);
            }
        }

        private Map<String, Map<Double, Integer>> readOperationsFile(String fileName) {
            Map<String, Map<Double, Integer>> operationDiameters = new HashMap<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Worker:")) {
                        // Read worker details, skip "Worker:" line
                        continue;
                    } else if (line.startsWith("Presences")) {
                        // Read presences, skip "Presences" line
                        continue;
                    } else if (line.startsWith("Remarks: ")) {
                        // Load remarks
                        TextArea remarksArea = findRemarksTextArea(this);
                        if (remarksArea != null) {
                            remarksArea.setText(line.substring(9).trim());
                        }
                        continue;
                    } else if (line.contains("Φ:")) {
                        // Read operation and diameters
                        String[] parts = line.split("Φ:");
                        if (parts.length == 2) {
                            String operationName = parts[0].trim();
                            double diameter = Double.parseDouble(parts[1].trim());
                            // Update operationDiameters map
                            Map<Double, Integer> diameters = operationDiameters.computeIfAbsent(operationName, k -> new HashMap<>());
                            diameters.put(diameter, diameters.getOrDefault(diameter, 0) + 1);
                        }
                    }
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
            return operationDiameters;
        }

        private TextArea findRemarksTextArea(VBox parent) {
            for (int i = 0; i < parent.getChildren().size(); i++) {
                if (parent.getChildren().get(i) instanceof TextArea) {
                    return (TextArea) parent.getChildren().get(i);
                }
            }
            return null;
        }

        private void saveRemarksToFile(String remarks) {
            String filePath = System.getProperty("user.home") + "/Desktop/operations_" + workerName + ".txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                writer.write("Remarks: " + remarks);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    @FXML
    private void handleExportListButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("page5.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Export List");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


