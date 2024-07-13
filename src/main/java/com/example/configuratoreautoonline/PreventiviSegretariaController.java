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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.Optional;

public class PreventiviSegretariaController {
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
    private TableColumn<Configurazione, String> dataPreventivoColumn;
    @FXML
    private TableColumn<Configurazione, String> concessionarioColumn;
    @FXML
    private TableColumn<Configurazione, String> giorniRimanentiColumn;

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
        dataPreventivoColumn.setCellValueFactory(new PropertyValueFactory<>("dataPreventivo"));
        concessionarioColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLuogoConcessionario().getNome()));
        giorniRimanentiColumn.setCellValueFactory(data -> {
            LocalDate dataPreventivo = LocalDate.parse(data.getValue().getDataPreventivo(), DateTimeFormatter.ISO_DATE);
            long giorniRimanenti = ChronoUnit.DAYS.between(LocalDate.now(), dataPreventivo.plusDays(20));
            return new SimpleStringProperty(giorniRimanenti > 0 ? String.valueOf(giorniRimanenti) : "Scaduto");
        });

        loadPreventivi();
        addButtonToTable();
    }

    private void loadPreventivi() {
        preventivi = FXCollections.observableArrayList();
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("public/res/data/preventivi.json");

        if (file.exists() && file.length() != 0) {
            try {
                JsonNode root = objectMapper.readTree(file);
                if (root.isArray()) {
                    Iterator<JsonNode> iterator = root.iterator();
                    while (iterator.hasNext()) {
                        JsonNode node = iterator.next();
                        JsonNode dataPreventivoNode = node.get("dataPreventivo");
                        if (dataPreventivoNode != null) {
                            LocalDate dataPreventivo = LocalDate.parse(dataPreventivoNode.asText(), DateTimeFormatter.ISO_DATE);
                            if (ChronoUnit.DAYS.between(dataPreventivo, LocalDate.now()) <= 20) {
                                Configurazione configurazione = objectMapper.treeToValue(node, Configurazione.class);
                                preventivi.add(configurazione);
                            } else {
                                iterator.remove();
                            }
                        }
                    }
                    objectMapper.writeValue(file, root);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        tableView.setItems(preventivi);
    }

    private void addButtonToTable() {
        TableColumn<Configurazione, Void> colBtn = new TableColumn<>("Azione");

        javafx.util.Callback<TableColumn<Configurazione, Void>, TableCell<Configurazione, Void>> cellFactory = new javafx.util.Callback<>() {
            @Override
            public TableCell<Configurazione, Void> call(final TableColumn<Configurazione, Void> param) {
                final TableCell<Configurazione, Void> cell = new TableCell<>() {

                    private final Button btn = new Button("Modifica Data");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Configurazione data = getTableView().getItems().get(getIndex());
                            showEditDateDialog(data);
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

    private void showEditDateDialog(Configurazione configurazione) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/configuratoreautoonline/edit-dialog.fxml"));
            Parent root = loader.load();

            EditDateController controller = loader.getController();
            controller.setConfigurazione(configurazione);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modifica Data Preventivo");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Save changes and refresh table
            saveConfigurations();
            tableView.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveConfigurations() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        File file = new File("public/res/data/preventivi.json");

        try {
            objectMapper.writeValue(file, preventivi);
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
