package com.example.configuratoreautoonline;

import Classi.Configurazione;
import Classi.Motorizzazione;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class CarConfiguratorController {
    @FXML
    public Label scontoMensile;
    @FXML
    private ComboBox<String> motorizzazioneComboBox;
    @FXML
    private ComboBox<String> modelloComboBox;
    @FXML
    private ComboBox<String> coloreComboBox;
    @FXML
    private CheckBox vetriCheck;
    @FXML
    private CheckBox cerchiCheck;
    @FXML
    private CheckBox pinzeCheck;
    @FXML
    private CheckBox cerchiScuriCheck;
    @FXML
    private CheckBox internoCheck;
    @FXML
    private CheckBox impiantoAudioCheck;
    @FXML
    private CheckBox abbonamentoCheck;
    @FXML
    private ImageView carImageView;
    @FXML
    private Label prezzoLbl;
    @FXML
    private Button interniBtn;
    @FXML
    private Button downloadPdfButton;
    private Stage stage;
    private String selectedMarca;
    private JsonNode datiModelliAuto;
    private int prezzoBase = 0;
    private int sconto =0;
    private String path = "";

    private int prezzo = 0;
    private List<String> optionals = new ArrayList<>();
    private Motorizzazione selectedMotorizzazione;

    // Inizializza i dati per la marca dell'auto selezionata
    public void initData(String marca) {
        this.selectedMarca = marca;
        loadJsonData();
        initializeModelComboBox();
    }
    public void onDownloadPdfButtonClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            generatePdf(file.getAbsolutePath());
        }
    }

    public void generatePdf(String dest) {
        try {
            // Creare un nuovo PdfWriter
            PdfWriter writer = new PdfWriter(dest);

            // Creare un nuovo PdfDocument
            PdfDocument pdf = new PdfDocument(writer);

            // Creare un nuovo Document
            Document document = new Document(pdf);

            // Aggiungere il testo al PDF
            document.add(new Paragraph("PREVENTIVO AUTO"));
            document.add(new Paragraph("Marca: " + selectedMarca));
            document.add(new Paragraph("Modello: " + modelloComboBox.getValue()));
            document.add(new Paragraph("Colore: " + coloreComboBox.getValue()));
            document.add(new Paragraph("Prezzo: " + prezzoLbl.getText()));

            // aggiungere l'immagine al PDF
            document.add(new com.itextpdf.layout.element.Image(ImageDataFactory.create(path)));
            document.add(new com.itextpdf.layout.element.Image(ImageDataFactory.create(generaPathInterni(selectedMarca, modelloComboBox.getValue()))));

            // Chiudere il document
            document.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void initialize() {
        stage = new Stage();
        /*stage.setFullScreen(true);*/
        // Listener per aggiornare l'immagine quando cambia il modello
        modelloComboBox.setOnAction(event -> {
            onModelloSelected(event);
            updateImage();
        });

        // Listener per aggiornare l'immagine quando cambia il colore
        coloreComboBox.setOnAction(event -> updateImage());

        // Listener per aggiornare l'immagine quando cambia la motorizzazione
        motorizzazioneComboBox.setOnAction(event -> {
            onMotoSelected(event);
            updateImage();
        });

        // Listener per aggiornare l'immagine quando si selezionano/deselezionano i CheckBox
        vetriCheck.setOnAction(event -> updateImage());
        cerchiCheck.setOnAction(event -> updateImage());
        pinzeCheck.setOnAction(event -> updateImage());
        cerchiScuriCheck.setOnAction(event -> updateImage());
        internoCheck.setOnAction(this::onInternoCheckChanged);
        impiantoAudioCheck.setOnAction(event -> updateImage());
        abbonamentoCheck.setOnAction(event -> updateImage());

        // Inizializza il pulsante downloadPdfButton
        downloadPdfButton.setOnAction(this::onDownloadPdfButtonClicked);
    }

    @FXML
    private void onInternoCheckChanged(ActionEvent event) {
        if (interniBtn.getText().equals("Visualizza Esterni")) {
            // Se stiamo visualizzando gli interni, aggiorniamo l'immagine degli interni
            loadImage(generaPathInterni(selectedMarca, modelloComboBox.getValue()));
        } else {
            // Altrimenti, aggiorniamo l'immagine degli esterni
            interniBtn.setText("Visualizza Interni");
            updateImage();
        }
    }

    @FXML
    private void onModelloSelected(ActionEvent event) {
        String selectedModello = modelloComboBox.getValue();
        if (selectedModello != null) {
            List<String> colori = getColoriPerModello(selectedMarca, selectedModello);
            coloreComboBox.setItems(FXCollections.observableArrayList(colori));
            coloreComboBox.setDisable(false);
            coloreComboBox.getSelectionModel().clearSelection();

            updateMotorizzazioneComboBox(selectedModello);
        }
        updateImage();
    }

    private String generaPathInterni(String marca, String modello){
        String percorso = "";
        JsonNode marcaNode = datiModelliAuto.get(marca.toLowerCase());
        if (marcaNode != null) {
            Iterator<JsonNode> modelliIterator = marcaNode.elements();
            while (modelliIterator.hasNext()) {
                JsonNode modelloNode = modelliIterator.next().get("modelli").get(0);
                JsonNode optionalNode = modelloNode.get(modello);
                if (optionalNode != null && optionalNode.has("percorsoImg")) {
                    percorso = optionalNode.get("percorsoImg").asText();
                }
            }
        }

        if(internoCheck.isSelected() && getOptionalsForModello(selectedMarca, modelloComboBox.getValue()).toString().contains("interni pelle")){
            percorso += "internopelle.png";
        } else {
            percorso += "internobase.png";
        }
        return percorso.toLowerCase();
    }

    @FXML
    private void onInterniClicked(ActionEvent event){
        if(interniBtn.getText().equals("Visualizza Interni")){
            try {
                loadImage(generaPathInterni(selectedMarca, modelloComboBox.getValue()));
                interniBtn.setText("Visualizza Esterni");
            }catch (Exception e) {
                showAlert("Errore", "Impossibile caricare l'immagine degli interni.");
            }
        } else {
            interniBtn.setText("Visualizza Interni");
            updateImage();
        }
    }

    // Carica datiModelliAuto.json in datiModelliAuto
    private void loadJsonData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File("public/res/data/datiModelliAuto.json");
            JsonNode root = objectMapper.readTree(file);
            datiModelliAuto = root.get("datiModelliAuto").get(0);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Errore di caricamento", "Impossibile caricare i dati del file JSON.");
        }
    }

    // Inizializza il ComboBox per i modelli dell'auto [ con visibilità o meno ]
    private void initializeModelComboBox() {
        if (selectedMarca != null) {
            List<String> modelli = getModelliForMarca(selectedMarca);
            modelloComboBox.setItems(FXCollections.observableArrayList(modelli));
            modelloComboBox.setDisable(false);
            modelloComboBox.getSelectionModel().clearSelection();
            coloreComboBox.setDisable(true);
            coloreComboBox.getItems().clear();
            interniBtn.setDisable(true);
            motorizzazioneComboBox.setDisable(true);
            motorizzazioneComboBox.getItems().clear();
            carImageView.setImage(null);

            // Rimuovi il listener vecchio se presente
            modelloComboBox.setOnAction(null);

            // Aggiungi nuovo listener per il cambio dinamico dell'immagine
            modelloComboBox.setOnAction(event -> onModelloSelected(event));

            // Disabilita gli optional
            disableOptionalCheckboxes(true);
        }
    }

    @FXML
    private void disableOptionalCheckboxes(boolean disable) {
        vetriCheck.setDisable(disable);
        cerchiCheck.setDisable(disable);
        cerchiScuriCheck.setDisable(disable);
        pinzeCheck.setDisable(disable);
        internoCheck.setDisable(disable);
        impiantoAudioCheck.setDisable(disable);
        abbonamentoCheck.setDisable(disable);
    }

    // Una volta che è stato scelto il modello, si possono selezionare gli optional partendo dal colore

    // Richiama getMotorizzazioniForModello per ottenere le motorizzazioni del modello selezionato
    private void updateMotorizzazioneComboBox(String modello) {
        List<String> motorizzazioni = getMotorizzazioniForModello(selectedMarca, modello);
        motorizzazioneComboBox.setItems(FXCollections.observableArrayList(motorizzazioni));
        motorizzazioneComboBox.setDisable(false);

        // Rimuovi il listener vecchio se presente
        motorizzazioneComboBox.setOnAction(null);

        // Aggiungi nuovo listener per il cambio dinamico dell'immagine
        motorizzazioneComboBox.setOnAction(event -> onMotoSelected(event));
    }

    public void updateVisibility()  {
        int mese = LocalDate.now().getMonthValue();
        if(mese == 12 || mese == 7 || mese == 10) {
            scontoMensile.setVisible(true);
        } else {
            scontoMensile.setVisible(false);
        }
    }

    // Restituisci gli optional per il modello selezionato
    private List<String> getOptionalsForModello(String marca, String modello) {
        List<String> optionals = new ArrayList<>();
        JsonNode marcaNode = datiModelliAuto.get(marca.toLowerCase());
        if (marcaNode != null) {
            for (JsonNode modelloNode : marcaNode) {
                JsonNode modelliNode = modelloNode.get("modelli");
                if (modelliNode != null) {
                    for (JsonNode modNode : modelliNode) {
                        JsonNode modelloSpecificoNode = modNode.get(modello);
                        if (modelloSpecificoNode != null && modelloSpecificoNode.has("optionals")) {
                            for (JsonNode optionalNode : modelloSpecificoNode.get("optionals")) {
                                optionals.add(optionalNode.asText());
                            }
                            return optionals; // Trovati gli optional per il modello, interrompe il loop
                        }
                    }
                }
            }
        }
        return optionals;
    }


    // popola la choicebox con i colori disponibili per il modello selezionato
    private List<String> getColoriPerModello(String marca, String modello) {
        List<String> colori = new ArrayList<>();
        JsonNode marcaNode = datiModelliAuto.get(marca.toLowerCase());
        if (marcaNode != null) {
            Iterator<JsonNode> modelliIterator = marcaNode.elements();
            while (modelliIterator.hasNext()) {
                JsonNode modelloNode = modelliIterator.next().get("modelli").get(0);
                JsonNode optionalNode = modelloNode.get(modello);
                if (optionalNode != null && optionalNode.has("colori")) {
                    optionalNode.get("colori").forEach(optional -> colori.add(optional.asText()));
                }
            }
        }
        return colori;
    }

    // Ritorna il percorso alla cartella del modello:
    private String getPercorsoIMGPerModello(String marca, String modello) {
        String percorso = "";
        JsonNode marcaNode = datiModelliAuto.get(marca.toLowerCase());
        if (marcaNode != null) {
            Iterator<JsonNode> modelliIterator = marcaNode.elements();
            while (modelliIterator.hasNext()) {
                JsonNode modelloNode = modelliIterator.next().get("modelli").get(0);
                JsonNode optionalNode = modelloNode.get(modello);
                if (optionalNode != null && optionalNode.has("percorsoImg")) {
                    percorso = optionalNode.get("percorsoImg").asText();
                }
            }
        }
        return percorso;
    }

    // popola la choicebox con le motorizzazioni disponibili per il modello selezionato
    private List<String> getMotorizzazioniForModello(String marca, String modello) {
        List<String> motorizzazioni = new ArrayList<>();
        JsonNode marcaNode = datiModelliAuto.get(marca.toLowerCase());
        if (marcaNode != null) {
            Iterator<JsonNode> modelliIterator = marcaNode.elements();
            while (modelliIterator.hasNext()) {
                JsonNode modelloNode = modelliIterator.next().get("modelli").get(0);
                JsonNode modelloSpecificoNode = modelloNode.get(modello);
                if (modelloSpecificoNode != null && modelloSpecificoNode.has("motorizzazioni")) {
                    modelloSpecificoNode.get("motorizzazioni").forEach(motorizzazione -> {
                        String motorizzazioneStr = motorizzazione.get("alimentazione").asText() + " - " + motorizzazione.get("potenza").asText();
                        motorizzazioni.add(motorizzazioneStr);
                    });
                }
            }
        }
        return motorizzazioni;
    }

    //scelta motorizzazione
    public void onMotoSelected(ActionEvent event) {
        String selectedModello = modelloComboBox.getValue();
        if (selectedModello != null) {
            List<Motorizzazione> motori = getMotorizzazioniDetailsForModello(selectedMarca, selectedModello);
            String selectedMotorizzazioneStr = motorizzazioneComboBox.getValue();

            for (Motorizzazione motorizzazione : motori) {
                String motorizzazioneStr = motorizzazione.getAlimentazione() + " - " + motorizzazione.getPotenza();
                if (motorizzazioneStr.equals(selectedMotorizzazioneStr)) {
                    this.selectedMotorizzazione = motorizzazione;
                    this.prezzoBase = Integer.parseInt(motorizzazione.getPrezzo().replace(".", "").replace(" EUR", ""));
                    this.prezzo = prezzoBase;
                    break;
                }
            }

            List<String> optionals = getOptionalsForModello(selectedMarca, selectedModello);

            vetriCheck.setDisable(!optionals.toString().contains("vetri oscurati"));
            cerchiCheck.setDisable(!optionals.toString().contains("cerchi maggiorati"));
            cerchiScuriCheck.setDisable(!optionals.toString().contains("cerchi neri"));
            pinzeCheck.setDisable(!optionals.toString().contains("freni rossi €800"));
            internoCheck.setDisable(!optionals.toString().contains("interni pelle"));
            impiantoAudioCheck.setDisable(!optionals.toString().contains("impianto audio HarmanCardon"));
            abbonamentoCheck.setDisable(!(selectedMotorizzazioneStr.toString().contains("Elettrica") || selectedMotorizzazioneStr.toString().contains("Ibrida Plug-in")));

            updateImage();

            stampaPrezzo(prezzo);
            // Aggiorna il prezzo base con quel motore

            // Abilita il pulsante per vedere gli interni
            interniBtn.setDisable(false);
        }
    }
    private List<Motorizzazione> getMotorizzazioniDetailsForModello(String marca, String modello) {
        List<Motorizzazione> motorizzazioni = new ArrayList<>();
        JsonNode marcaNode = datiModelliAuto.get(marca.toLowerCase());
        if (marcaNode != null) {
            Iterator<JsonNode> modelliIterator = marcaNode.elements();
            while (modelliIterator.hasNext()) {
                JsonNode modelloNode = modelliIterator.next().get("modelli").get(0);
                JsonNode modelloSpecificoNode = modelloNode.get(modello);
                if (modelloSpecificoNode != null && modelloSpecificoNode.has("motorizzazioni")) {
                    modelloSpecificoNode.get("motorizzazioni").forEach(motorizzazione -> {
                        Motorizzazione motorizzazioneObj = new Motorizzazione(
                                motorizzazione.get("cilindrata").asText(),
                                motorizzazione.get("potenza").asText(),
                                motorizzazione.get("coppia").asText(),
                                motorizzazione.get("alimentazione").asText(),
                                motorizzazione.get("prezzo").asText()
                        );
                        motorizzazioni.add(motorizzazioneObj);
                    });
                }
            }
        }
        return motorizzazioni;
    }



    @FXML
    private void onConfiguraButtonClicked(ActionEvent event) {
        String selectedModello = modelloComboBox.getValue();
        String selectedColore = coloreComboBox.getValue();
        UserSession session = UserSession.getInstance();

        if (!session.isLoggato()) {
            showErrorAlert("Errore", "Devi essere loggato per configurare l'auto.");
            return;
        }

        if (selectedMarca != null && selectedModello != null && selectedColore != null && selectedMotorizzazione != null) {
            List<String> selectedOptionals = new ArrayList<>();
            if (vetriCheck.isSelected()) selectedOptionals.add("vetri oscurati €800");
            if (cerchiCheck.isSelected()) selectedOptionals.add("cerchi maggiorati €500");
            if (cerchiScuriCheck.isSelected()) selectedOptionals.add("cerchi neri €1000");
            if (pinzeCheck.isSelected()) selectedOptionals.add("freni rossi €800");
            if (internoCheck.isSelected()) selectedOptionals.add("interni pelle €2590");
            if (impiantoAudioCheck.isSelected()) selectedOptionals.add("impianto audio HarmanCardon €1500");
            if (abbonamentoCheck.isSelected()) selectedOptionals.add("abbonamento");

            int optionalPrice = selectedOptionals.stream()
                    .mapToInt(o -> {
                        String[] parts = o.split("€");
                        if (parts.length == 2) {
                            try {
                                return Integer.parseInt(parts[1].trim());
                            } catch (NumberFormatException e) {
                                return 0;
                            }
                        } else {
                            return 0;
                        }
                    })
                    .sum();

            int totalePrezzo;

            int mese = LocalDate.now().getMonthValue();

            if (mese == 12 || mese == 7 || mese == 10) {
                totalePrezzo = (this.prezzo) * 97 / 100;
                this.sconto = this.prezzo - totalePrezzo;
            } else {
                totalePrezzo = this.prezzo;
            }

            // Mostra il pop-up di conferma
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Conferma Ordine");
            confirmationAlert.setHeaderText("Conferma configurazione dell'auto");
            if (mese == 12 || mese == 7 || mese == 10) {
                confirmationAlert.setHeaderText("E' stato applicato uno sconto su questa vettura");
            }
            confirmationAlert.setContentText("Sei sicuro di voler configurare l'auto con le opzioni selezionate?\nPrezzo totale: " + totalePrezzo + " €");

            // Aggiungi i pulsanti al pop-up di conferma
            ButtonType confirmButton = new ButtonType("Conferma");
            ButtonType cancelButton = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);
            confirmationAlert.getButtonTypes().setAll(confirmButton, cancelButton);

            int finalTotalePrezzo = totalePrezzo;
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == confirmButton) {
                    int newId = 1; // ID iniziale
                    File file = new File("public/res/data/preventivi.json");
                    ObjectMapper objectMapper = new ObjectMapper();

                    try {
                        JsonNode root;
                        ArrayNode configurazioni;

                        if (file.exists() && file.length() != 0) {
                            root = objectMapper.readTree(file);
                            if (root.isArray()) {
                                configurazioni = (ArrayNode) root;
                            } else {
                                configurazioni = objectMapper.createArrayNode();
                                configurazioni.add(root);
                            }

                            for (JsonNode node : configurazioni) {
                                JsonNode idNode = node.get("idConfigurazione");
                                if (idNode != null) {
                                    int id = idNode.asInt();
                                    if (id >= newId) {
                                        newId = id + 1;
                                    }
                                }
                            }
                        } else {
                            configurazioni = objectMapper.createArrayNode();
                        }

                        Configurazione configurazione = new Configurazione(newId, selectedMarca, selectedModello, selectedColore, selectedMotorizzazione, selectedOptionals, finalTotalePrezzo, session.getEmail(), session.getConcessionario());
                        configurazioni.add(objectMapper.valueToTree(configurazione));

                        objectMapper.writeValue(file, configurazioni);
                        showSuccessAlert("Successo", "Configurazione salvata con successo.");

                        // Ritorno alla home page
                       /* Node source = (Node) event.getSource();
                        Stage currentStage = (Stage) source.getScene().getWindow();
                        changeScene("/com/example/configuratoreautoonline/Home-view.fxml", currentStage);*/

                    } catch (IOException e) {
                        e.printStackTrace();
                        showErrorAlert("Errore", "Errore durante il salvataggio della configurazione.");
                    }

                    this.path = getPercorsoIMGPerModello(selectedMarca, selectedModello) + getSecondaParteIMG(selectedOptionals, selectedColore);
                    loadImage(path);
                }
            });
        } else {
            showErrorAlert("Errore", "Seleziona marca, modello, colore e motorizzazione per configurare l'auto.");
        }
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }

    private void showSuccessAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }



    // Costruisce il percorso all'interno della directory del modello
    private String getSecondaParteIMG(List<String> optionalsForModello, String selectedColore) {
        StringBuilder risultato = new StringBuilder();
        risultato.append(selectedColore.split(" ")[0]);

        if (optionalsForModello.toString().contains("cerchi neri") && cerchiScuriCheck.isSelected()) {
            risultato.append("cerchineri");
        }
        if (optionalsForModello.toString().contains("cerchi maggiorati") && cerchiCheck.isSelected()) {
            risultato.append("cerchimaggiorati");
        }
        if (optionalsForModello.toString().contains("vetri oscurati") && vetriCheck.isSelected()) {
            risultato.append("vetrioscurati");
        }
        if (optionalsForModello.toString().contains("freni rossi") && pinzeCheck.isSelected()) {
            risultato.append("frenirossi");
        }
        risultato.append(".png");
        return risultato.toString().toLowerCase();
    }

    public int prezzoOptional() {
        int prezzoDaAggiungere = 0;

        // Verifica se il colore è selezionato e aggiungi il prezzo
        if (coloreComboBox.getValue() != null) {
            prezzoDaAggiungere += estraiPrezzoDaStringa(coloreComboBox.getValue());
        }

        // Verifica ogni checkbox se è selezionata e aggiungi il prezzo
        List<String> optionals = getOptionalsForModello(selectedMarca, modelloComboBox.getValue());

        for (String optional : optionals) {
            if (cerchiCheck.isSelected() && optional.contains("cerchi maggiorati")) {
                prezzoDaAggiungere += estraiPrezzoDaStringa(optional);
            }
            if (cerchiScuriCheck.isSelected() && optional.contains("cerchi neri")) {
                prezzoDaAggiungere += estraiPrezzoDaStringa(optional);
            }
            if(vetriCheck.isSelected() && optional.contains("vetri oscurati")){
                prezzoDaAggiungere += estraiPrezzoDaStringa(optional);
            }
            if (pinzeCheck.isSelected() && optional.contains("freni rossi")) {
                prezzoDaAggiungere += estraiPrezzoDaStringa(optional);
            }
            if (internoCheck.isSelected() && optional.contains("interni pelle")) {
                prezzoDaAggiungere += estraiPrezzoDaStringa(optional);
            }
            if (impiantoAudioCheck.isSelected() && optional.contains("impianto Audio maggiorato")) {
                prezzoDaAggiungere += estraiPrezzoDaStringa(optional);
            }
            if (abbonamentoCheck.isSelected() && optional.contains("Abbonamento annuale per ricarica")) {
                prezzoDaAggiungere += estraiPrezzoDaStringa(optional);
            }
            // Aggiungi altre condizioni per gli optional rimanenti come necessario
        }

        return prezzoDaAggiungere;
    }
    private int estraiPrezzoDaStringa(String testo) {
        int prezzo = 0;
        try {
            String prezzoString = testo.split("€")[1].trim();
            prezzo = Integer.parseInt(prezzoString);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            // Gestione dell'errore nel caso in cui il formato non sia corretto
            System.err.println("Errore nell'estrazione del prezzo da: " + testo);
        }
        return prezzo;
    }
    // Aggiorna l'immagine caricata in base al modello e al colore selezionati e optionals
    public void updateImage() {
        interniBtn.setText("Visualizza Interni");
        if (modelloComboBox.getValue() != null &&
                coloreComboBox.getValue() != null &&
                motorizzazioneComboBox.getValue() != null) {
            this.path = getPercorsoIMGPerModello(selectedMarca, modelloComboBox.getValue()) +
                    getSecondaParteIMG(getOptionalsForModello(selectedMarca, modelloComboBox.getValue()), coloreComboBox.getValue());
            loadImage(path);
        }
        stampaPrezzo(prezzoBase + prezzoOptional());
    }

    private void stampaPrezzo (int prezzoFinale)    {
            this.prezzo = prezzoFinale;
            prezzoLbl.setText(prezzoFinale + " €");
    }
    // Restituisce i modelli per la marca selezionata in una LISTA di stringhe
    private List<String> getModelliForMarca(String marca) {
        List<String> modelli = new ArrayList<>();
        JsonNode marcaNode = datiModelliAuto.get(marca.toLowerCase());
        if (marcaNode != null) {
            Iterator<JsonNode> modelliIterator = marcaNode.elements();
            while (modelliIterator.hasNext()) {
                JsonNode nextNode = modelliIterator.next();
                JsonNode modelliNode = nextNode.get("modelli");
                if (modelliNode != null && modelliNode.isArray() && modelliNode.size() > 0) {
                    JsonNode primoModello = modelliNode.get(0);
                    if (primoModello != null) {
                        primoModello.fieldNames().forEachRemaining(modelli::add);
                    }
                }
            }
        }
        return modelli;
    }
    // DAto il percorso dell'immagine, la carica
    private void loadImage(String path) {
        try {
            File file = new File(path);
            Image image = new Image(file.toURI().toString());
            carImageView.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // per tornare alla home
    @FXML
    private void handleHomeButton(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();

        changeScene("/com/example/configuratoreautoonline/Home-view.fxml", currentStage);
    }

    private void changeScene(String fxmlFile, Stage currentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            currentStage.setScene(scene);
            currentStage.show();
            currentStage.setFullScreen(true);
        } catch (Exception e) {
            showAlert("Error loading scene", "Cannot load scene from file: " + fxmlFile + "\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}
