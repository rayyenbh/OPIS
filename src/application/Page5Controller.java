package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class Page5Controller {

    @FXML
    private TextField diameterField;

    @FXML
    private TextField quantityField;

    @FXML
    private Button saveButton;

    private static final String FILE_PATH = System.getProperty("user.home") + "/Desktop/exported_data.txt";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    @FXML
    public void initialize() {
        saveButton.setOnAction(event -> saveData());
    }

    private void saveData() {
        String diameter = diameterField.getText();
        String quantity = quantityField.getText();

        if (isValidInput(diameter, quantity)) {
            String currentDate = DATE_FORMAT.format(new Date());
            saveDataToFile(currentDate, diameter, quantity);
            showAlert("Success", "Data saved successfully!");
        } else {
            showAlert("Invalid Input", "Please enter valid numeric values for diameter and quantity.");
        }
    }

    private boolean isValidInput(String diameter, String quantity) {
        try {
            Double.parseDouble(diameter);
            Integer.parseInt(quantity);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void saveDataToFile(String date, String diameter, String quantity) {
        try {
            // Read existing data
            Map<String, List<String>> dataMap = readExistingData();

            // Add new data
            dataMap.computeIfAbsent(date, k -> new ArrayList<>()).add(diameter + " : " + quantity);

            // Write data back to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
                for (Map.Entry<String, List<String>> entry : dataMap.entrySet()) {
                    writer.write(entry.getKey() + " :");
                    writer.newLine();
                    for (String entryData : entry.getValue()) {
                        writer.write(entryData);
                        writer.newLine();
                    }
                }
            }

        } catch (IOException e) {
            showAlert("Error", "Failed to save data: " + e.getMessage());
        }
    }

    private Map<String, List<String>> readExistingData() throws IOException {
        Map<String, List<String>> dataMap = new LinkedHashMap<>();
        Path filePath = Paths.get(FILE_PATH);
        if (Files.exists(filePath)) {
            List<String> lines = Files.readAllLines(filePath);
            String currentDate = null;

            for (String line : lines) {
                if (line.matches("\\d{2}/\\d{2}/\\d{4} :")) {
                    currentDate = line.replace(" :", "");
                    dataMap.put(currentDate, new ArrayList<>());
                } else if (currentDate != null) {
                    dataMap.get(currentDate).add(line);
                }
            }
        }
        return dataMap;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
