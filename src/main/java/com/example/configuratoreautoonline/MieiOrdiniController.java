package com.example.configuratoreautoonline;

import Classi.Configurazione;
import Enums.Concessionari;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

// controller della pagina del cliente relativa ai suoi ordini
public class MieiOrdiniController {
    // utilizziamo una tabella per visualizzare la lista degli ordini con i relativi dettagli
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

        loadOrdini();
        addButtonToTable();
    }

    // carica gli ordini dal file json
    private void loadOrdini() {
        UserSession session = UserSession.getInstance();
        String userEmail = session.getEmail();
        ordini = FXCollections.observableArrayList();
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("public/res/data/ordini.json");

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

    // funzione per modificare l'ordine ( modificare la sede di riferimento )
    private void showEditDialog(Configurazione data) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modifica Ordine");
        dialog.setHeaderText(null);

        // Create the form fields
        ComboBox<Concessionari> concessionarioComboBox = new ComboBox<>();
        concessionarioComboBox.setItems(FXCollections.observableArrayList(Concessionari.values()));
        concessionarioComboBox.setValue(data.getLuogoConcessionario());

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        gridPane.add(new Label("Luogo del Concessionario:"), 0, 0);
        gridPane.add(concessionarioComboBox, 1, 0);

        dialog.getDialogPane().setContent(gridPane);

        // Add the buttons
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            data.setLuogoConcessionario(concessionarioComboBox.getValue());
            saveConfigurations();
        }
    }

    // funzione per aggiungere i bottoni alla tabella, per saldare il conto dell'ordine oppure modificarlo o annullarlo
    private void addButtonToTable() {
        TableColumn<Configurazione, Void> colBtnAnnulla = new TableColumn<>("Azione");
        TableColumn<Configurazione, Void> colBtnModifica = new TableColumn<>("Modifica");
        TableColumn<Configurazione, Void> colBtnSalda = new TableColumn<>("Salda");

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

        // Creo pulsante SALDA in base a se è stato già pagato o meno
        javafx.util.Callback<TableColumn<Configurazione, Void>, TableCell<Configurazione, Void>> cellFactorySalda = new javafx.util.Callback<>() {
            @Override
            public TableCell<Configurazione, Void> call(final TableColumn<Configurazione, Void> param) {
                final TableCell<Configurazione, Void> cell = new TableCell<>() {

                    private final Button btn = new Button("Salda");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Configurazione data = getTableView().getItems().get(getIndex());
                            showConfirmPayDialog(data);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Configurazione data = getTableView().getItems().get(getIndex());
                            if (data.getPagato()) {
                                btn.setDisable(true); // Disabilita il pulsante se la configurazione è già saldata
                            } else {
                                btn.setDisable(false); // Abilita il pulsante se la configurazione non è saldata
                            }
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtnAnnulla.setCellFactory(cellFactoryAnnulla);
        colBtnModifica.setCellFactory(cellFactoryModifica);
        colBtnSalda.setCellFactory(cellFactorySalda);

        tableView.getColumns().addAll(colBtnAnnulla, colBtnModifica, colBtnSalda);
    }

    // dialogo per la conferma del saldo
    private void showConfirmPayDialog(Configurazione data) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Pagamento");
        alert.setHeaderText(null);
        alert.setContentText("Sei sicuro di voler saldare questo ordine?");

        ButtonType buttonTypeYes = new ButtonType("Sì");
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeYes) {
            markAsPaid(data);
        }
    }

    // funzione per segnare che l'ordine è stato saldato
    private void markAsPaid(Configurazione data) {
        data.setPagato(true); // Esempio: assuming there's a setPaid method
        saveConfigurations();
        tableView.refresh();
    }

    // conferma per l'annullamento dell'ordine
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

    // funzione per mostrare il pop-up di modifica dell'ordine
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

            // Aggiorna la tabella dopo la chiusura del popup
            tableView.refresh();
            saveConfigurations();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // funzione per eliminazione dell'ordine
    private void deleteConfiguration(Configurazione configurazione) {
        ordini.remove(configurazione);
        saveConfigurations();
    }


    private void saveConfigurations() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writeValue(new File("public/res/data/ordini.json"), ordini);
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
            stage.setFullScreen(true);
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
