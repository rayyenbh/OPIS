package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Page3Controller {

    private Map<String, List<Double>> operationDiameters = new HashMap<>();
    private int presences = 0;
    private LocalDate lastUpdate = null;
    private String workerName;

    @FXML
    private VBox operationsList;

    @FXML
    private Label presencesLabel;

    @FXML
    private Label workerNameLabel;

    @FXML
    private TextArea remarksField; // TextArea for remarks

    @FXML
    public void initialize() {
        // Set remarksField to be non-editable
        remarksField.setEditable(false);

        String[] operations = {"Formage :", "Glaçage :", "Pré-ajustage :", "Ajustage :", "Gachette :"};

        for (String operation : operations) {
            VBox operationBox = new VBox(5);

            Label operationLabel = new Label(operation);
            HBox diameterBox = new HBox(5);
            Label diameterLabel = new Label("Φ :");
            TextField diameterField = new TextField();
            diameterField.setPromptText("Enter Diameter");
            diameterField.setPrefWidth(100); // Set preferred width for the text field

            HBox quantityBox = new HBox(5);
            Label quantityLabel = new Label("q :");
            TextField quantityField = new TextField();
            quantityField.setEditable(false);
            quantityField.setText("0");
            quantityField.setPrefWidth(100); // Set preferred width for the text field

            diameterBox.setStyle("-fx-padding: 0 0 0 20;"); // Add padding to indent the diameter box
            quantityBox.setStyle("-fx-padding: 0 0 0 20;"); // Add padding to indent the quantity box

            // Event handler for diameter field input
            diameterField.setOnAction(event -> {
                String diameterText = diameterField.getText();
                if (isValidDiameter(diameterText)) {
                    List<Double> diameters = operationDiameters.computeIfAbsent(operation, k -> new ArrayList<>());
                    double diameterValue = Double.parseDouble(diameterText);
                    diameters.add(diameterValue);

                    int currentCount = diameters.size();
                    quantityField.setText(String.valueOf(currentCount));

                    LocalDate today = LocalDate.now();
                    if (lastUpdate == null || !lastUpdate.equals(today)) {
                        presences++;
                        lastUpdate = today;
                        presencesLabel.setText("Presences this month: " + presences);
                    }

                    saveOperationsToFile();
                } else {
                    showAlert("Invalid Input", "Please enter a valid diameter (numeric value).");
                }
            });

            diameterBox.getChildren().addAll(diameterLabel, diameterField);
            quantityBox.getChildren().addAll(quantityLabel, quantityField);
            operationBox.getChildren().addAll(operationLabel, diameterBox, quantityBox);
            operationsList.getChildren().add(operationBox);
        }
    }

    // Method to set the worker's name and update the label
    public void setWorkerName(String workerName) {
        this.workerName = workerName;
        workerNameLabel.setText(workerName);
        loadOperationsFromFile(); // Load existing data for this worker
    }

    // Validation method for diameter input
    private boolean isValidDiameter(String input) {
        try {
            double diameter = Double.parseDouble(input);
            // Add additional validation rules if needed
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Utility method to show an alert dialog
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void saveOperationsToFile() {
        String filePath = System.getProperty("user.home") + "/Desktop/operations_" + workerName + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Worker: " + workerName);
            writer.newLine();
            writer.write("Presences this month: " + presences);
            writer.newLine();
            writer.newLine();

            for (Map.Entry<String, List<Double>> entry : operationDiameters.entrySet()) {
                String operationName = entry.getKey();
                List<Double> diameters = entry.getValue();

                for (double diameter : diameters) {
                    writer.write(operationName + " Φ: " + diameter);
                    writer.newLine();
                }
            }

            // Save the remarks
            writer.write("Remarks: " + remarksField.getText());
            writer.newLine();

            System.out.println("Operations data saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadOperationsFromFile() {
        String filePath = System.getProperty("user.home") + "/Desktop/operations_" + workerName + ".txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Read and skip the first two lines (worker name and presences)
            reader.readLine();
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Remarks: ")) {
                    // Load remarks
                    remarksField.setText(line.substring(9).trim());
                    continue;
                }

                String[] parts = line.split(" Φ: ");
                if (parts.length < 2) {
                    System.err.println("Invalid format in file: " + filePath);
                    continue; // Skip processing this line
                }
                String operationName = parts[0];
                String diameter = parts[1];

                // Update the UI with loaded data
                for (javafx.scene.Node node : operationsList.getChildren()) {
                    if (node instanceof VBox) {
                        VBox operationBox = (VBox) node;
                        if (operationBox.getChildren().get(0) instanceof Label) {
                            Label operationLabel = (Label) operationBox.getChildren().get(0);
                            if (operationLabel.getText().equals(operationName)) {
                                HBox diameterBox = (HBox) operationBox.getChildren().get(1);
                                TextField diameterField = (TextField) diameterBox.getChildren().get(1);
                                diameterField.setText(diameter);

                                // Update operationDiameters map
                                List<Double> diameters = operationDiameters.computeIfAbsent(operationName, k -> new ArrayList<>());
                                diameters.add(Double.parseDouble(diameter));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



