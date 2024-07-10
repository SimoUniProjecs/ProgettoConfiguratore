package com.example.configuratoreautoonline;

import Classi.Configurazione;
import Classi.Motorizzazione;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MieiOrdiniController {
    @FXML
    private TableView<Configurazione> tableView;
    @FXML
    private TableColumn<Configurazione, Integer> idColumn;
    @FXML
    private TableColumn<Configurazione, String> marcaColumn;
    @FXML
    private TableColumn<Configurazione, String> modelloColumn;
    @FXML
    private TableColumn<Configurazione, String> coloreColumn;
    @FXML
    private TableColumn<Configurazione, String> motorizzazioneColumn;
    @FXML
    private TableColumn<Configurazione, Integer> prezzoColumn;
    @FXML
    private TableColumn<Configurazione, Void> actionColumn;
    @FXML
    private Button homeButton;

    private ObservableList<Configurazione> ordini;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idConfigurazione"));
        marcaColumn.setCellValueFactory(new PropertyValueFactory<>("marcaAutomobile"));
        modelloColumn.setCellValueFactory(new PropertyValueFactory<>("modelloAutomobile"));
        coloreColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getColore().split(" ")[0]));
        motorizzazioneColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMotorizzazione().getAlimentazione()));
        prezzoColumn.setCellValueFactory(new PropertyValueFactory<>("prezzo"));

        loadOrdini();
        addButtonToTable();
    }

    private void loadOrdini() {
        UserSession session = UserSession.getInstance();
        String userEmail = session.getEmail();
        ordini = FXCollections.observableArrayList();
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("public/res/data/configurazioni.json");

        if (file.exists() && file.length() != 0) {
            try {
                JsonNode root = objectMapper.readTree(file);
                if (root.isArray()) {
                    for (JsonNode node : root) {
                        JsonNode emailNode = node.get("emailCliente");
                        if (emailNode != null && emailNode.asText().equals(userEmail)) {
                            Configurazione configurazione = objectMapper.treeToValue(node, Configurazione.class);
                            ordini.add(configurazione);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        tableView.setItems(ordini);
    }

    private void addButtonToTable() {
        TableColumn<Configurazione, Void> colBtn = new TableColumn("Action");

        javafx.util.Callback<TableColumn<Configurazione, Void>, TableCell<Configurazione, Void>> cellFactory = new javafx.util.Callback<>() {
            @Override
            public TableCell<Configurazione, Void> call(final TableColumn<Configurazione, Void> param) {
                final TableCell<Configurazione, Void> cell = new TableCell<>() {

                    private final Button btn = new Button("Delete");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Configurazione data = getTableView().getItems().get(getIndex());
                            deleteConfiguration(data);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);
        tableView.getColumns().add(colBtn);
    }

    private void deleteConfiguration(Configurazione configurazione) {
        ordini.remove(configurazione);
        saveConfigurations();
    }

    private void saveConfigurations() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("public/res/data/configurazioni.json"), ordini);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHomeButtonAction(ActionEvent event) {
        try {
            Parent homeView = FXMLLoader.load(getClass().getResource("/com/example/configuratoreautoonline/Home-view.fxml"));
            Scene homeScene = new Scene(homeView);
            Stage stage = (Stage) homeButton.getScene().getWindow();
            stage.setScene(homeScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load home view.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
