package com.example.configuratoreautoonline;

import Classi.Configurazione;
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
import java.util.Optional;

public class SegretariaController {
    @FXML
    private TableView<Configurazione> tableView;
    @FXML
    private TextField filtroTxt;
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
    private ComboBox<String> filtra;
    @FXML
    private TableColumn<Configurazione, Integer> prezzoColumn;
    @FXML
    private TableColumn<Configurazione, String> dataArrivoColumn;
    @FXML
    private TableColumn<Configurazione, Void> actionColumn;
    @FXML
    private TableColumn<Configurazione, String> concessionarioColumn;
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
        dataArrivoColumn.setCellValueFactory(new PropertyValueFactory<>("dataArrivo"));
        concessionarioColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLuogoConcessionario().getNome()));

        filtra.setItems(FXCollections.observableArrayList("email", "marca", "sede"));
        filtra.setOnAction(event -> {
            filtroTxt.setDisable(false); // Abilita il campo di testo quando viene selezionato un filtro
            loadOrdini(filtroTxt.getText());
        });

        filtroTxt.setDisable(true);
        filtroTxt.textProperty().addListener((observable, oldValue, newValue) -> loadOrdini(newValue));

        loadOrdini(null); // Carica inizialmente tutti gli ordini
        addButtonToTable();
    }

    private void loadOrdini(String filtro) {
        UserSession session = UserSession.getInstance();
        String userEmail = session.getEmail();
        ordini = FXCollections.observableArrayList();
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
                            if (filtro == null || filtro.isEmpty() || matchesFilter(configurazione, filtro)) {
                                ordini.add(configurazione);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        tableView.setItems(ordini);
    }

    private boolean matchesFilter(Configurazione configurazione, String filtro) {
        String selectedFilter = filtra.getValue();
        if (selectedFilter == null || filtro == null || filtro.isEmpty()) {
            return true;
        }

        switch (selectedFilter) {
            case "email":
                return configurazione.getEmailCliente().contains(filtro);
            case "marca":
                return configurazione.getMarcaAutomobile().equalsIgnoreCase(filtro);
            case "sede":
                return configurazione.getLuogoConcessionario().getNome().equalsIgnoreCase(filtro);
            default:
                return true;
        }
    }

    private void addButtonToTable() {
        TableColumn<Configurazione, Void> colBtnAnnulla = new TableColumn<>("Azione");
        TableColumn<Configurazione, Void> colBtnModifica = new TableColumn<>("Modifica");

        javafx.util.Callback<TableColumn<Configurazione, Void>, TableCell<Configurazione, Void>> cellFactoryAnnulla = new javafx.util.Callback<>() {
            @Override
            public TableCell<Configurazione, Void> call(final TableColumn<Configurazione, Void> param) {
                final TableCell<Configurazione, Void> cell = new TableCell<>() {

                    private final Button btn = new Button("Annulla ordine");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Configurazione data = getTableView().getItems().get(getIndex());
                            showConfirmDeleteDialog(data);
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

        javafx.util.Callback<TableColumn<Configurazione, Void>, TableCell<Configurazione, Void>> cellFactoryModifica = new javafx.util.Callback<>() {
            @Override
            public TableCell<Configurazione, Void> call(final TableColumn<Configurazione, Void> param) {
                final TableCell<Configurazione, Void> cell = new TableCell<>() {

                    private final Button btn = new Button("Modifica");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Configurazione data = getTableView().getItems().get(getIndex());
                            showEditOrderDialog(data);
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

        colBtnAnnulla.setCellFactory(cellFactoryAnnulla);
        colBtnModifica.setCellFactory(cellFactoryModifica);

        tableView.getColumns().addAll(colBtnAnnulla, colBtnModifica);
    }

    // pannello per confermare l'annullamento dell'ordine
    private void showConfirmDeleteDialog(Configurazione data) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Cancellazione");
        alert.setHeaderText(null);
        alert.setContentText("Sei sicuro di voler annullare l'ordine?");

        ButtonType buttonTypeYes = new ButtonType("Sì");
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeYes) {
            deleteConfiguration(data);
        }
    }

    // pannello per modificare ordine
    private void showEditOrderDialog(Configurazione data) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/configuratoreautoonline/modifica-configurazione.fxml"));
            Parent root = loader.load();

            ModificaOrdineController controller = loader.getController();
            controller.setConfigurazione(data);

            Stage stage = new Stage();
            stage.setTitle("Modifica Ordine");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            tableView.refresh();
            saveConfigurations();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Eliminare la configurazione
    private void deleteConfiguration(Configurazione configurazione) {
        ordini.remove(configurazione);
        saveConfigurations();
    }

    private void saveConfigurations() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writeValue(new File("public/res/data/preventivi.json"), ordini);
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

    private void changeScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage currentStage = (Stage) tableView.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.show();
        } catch (Exception e) {
            showAlert("Error loading scene", "Cannot load scene from file: " + fxmlFile + "\n" + e.getMessage());
            e.printStackTrace();
        }
    }
}