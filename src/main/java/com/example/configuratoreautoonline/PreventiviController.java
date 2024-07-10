package com.example.configuratoreautoonline;

import Classi.Configurazione;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
import java.util.Optional;

public class PreventiviController {
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
    private TableColumn<Configurazione, String> dataArrivoColumn;
    @FXML
    private TableColumn<Configurazione, String> concessionarioColumn;
    @FXML
    private TableColumn<Configurazione, Void> actionColumn;

    @FXML
    private Button homeButton;

    private ObservableList<Configurazione> preventivi;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idConfigurazione"));
        marcaColumn.setCellValueFactory(new PropertyValueFactory<>("marcaAutomobile"));
        modelloColumn.setCellValueFactory(new PropertyValueFactory<>("modelloAutomobile"));
        coloreColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getColore().split(" ")[0]));
        motorizzazioneColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMotorizzazione().getAlimentazione()));
        prezzoColumn.setCellValueFactory(new PropertyValueFactory<>("prezzo"));
        dataArrivoColumn.setCellValueFactory(new PropertyValueFactory<>("dataArrivo"));
        concessionarioColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLuogoConcessionario().getNome()));

        loadPreventivi();
        addButtonToTable();
    }

    private void loadPreventivi() {
        UserSession session = UserSession.getInstance();
        String userEmail = session.getEmail();
        preventivi = FXCollections.observableArrayList();
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("public/res/data/preventivi.json");

        if (file.exists() && file.length() != 0) {
            try {
                JsonNode root = objectMapper.readTree(file);
                if (root.isArray()) {
                    for (JsonNode node : root) {
                        JsonNode emailNode = node.get("emailCliente");
                        if (emailNode != null && emailNode.asText().equals(userEmail)) {
                            Configurazione configurazione = objectMapper.treeToValue(node, Configurazione.class);
                            preventivi.add(configurazione);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        tableView.setItems(preventivi);
    }

    private void addButtonToTable() {
        TableColumn<Configurazione, Void> colBtn = new TableColumn("Azione");

        javafx.util.Callback<TableColumn<Configurazione, Void>, TableCell<Configurazione, Void>> cellFactory = new javafx.util.Callback<>() {
            @Override
            public TableCell<Configurazione, Void> call(final TableColumn<Configurazione, Void> param) {
                final TableCell<Configurazione, Void> cell = new TableCell<>() {

                    private final Button btn = new Button("Paga Acconto");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Configurazione data = getTableView().getItems().get(getIndex());
                            showConfirmDialog(data);
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

    private void showConfirmDialog(Configurazione preventivo) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Preventivo");
        alert.setHeaderText(null);
        alert.setContentText("Sei sicuro di voler confermare questo preventivo?");

        ButtonType buttonTypeYes = new ButtonType("Sì");
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeYes) {
            confermaPreventivo(preventivo);
        }
    }

    private void confermaPreventivo(Configurazione preventivo) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        File ordiniFile = new File("public/res/data/ordini.json");
        File preventiviFile = new File("public/res/data/preventivi.json");

        try {
            // Aggiungi il preventivo agli ordini
            ObservableList<Configurazione> ordini = FXCollections.observableArrayList();
            if (ordiniFile.exists() && ordiniFile.length() != 0) {
                ordini.addAll(objectMapper.readValue(ordiniFile, Configurazione[].class));
            }
            ordini.add(preventivo);
            objectMapper.writeValue(ordiniFile, ordini);

            // Rimuovi il preventivo dai preventivi
            preventivi.remove(preventivo);
            objectMapper.writeValue(preventiviFile, preventivi);

            // Aggiorna la tabella
            tableView.setItems(preventivi);

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
