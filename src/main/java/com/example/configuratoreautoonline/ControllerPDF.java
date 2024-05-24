package com.example.configuratoreautoonline;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ControllerPDF {

    @FXML
    private Button scaricaConfigurazioneButton;
    @FXML
    private CheckBox vetriOscuratiCheckBox;
    @FXML
    private CheckBox cerchiMaggioratiCheckBox;
    @FXML
    private MenuButton setColore;
    @FXML
    private MenuButton setColoreFreni;
    @FXML
    private MenuItem coloreBaseMenuItem;
    @FXML
    private MenuItem coloreBluMenuItem;
    @FXML
    private MenuItem coloreBiancoMenuItem;
    @FXML
    private MenuItem freniBaseMenuItem;
    @FXML
    private MenuItem freniBluMenuItem;
    @FXML
    private MenuItem freniBiancoMenuItem;

    private String coloreAuto = "Base";
    private String coloreFreni = "Base";

    @FXML
    public void initialize() {
        scaricaConfigurazioneButton.setOnAction(event -> {
            try {
                generatePDF();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        coloreBaseMenuItem.setOnAction(event -> setColore.setText("Base"));
        coloreBluMenuItem.setOnAction(event -> setColore.setText("Blu"));
        coloreBiancoMenuItem.setOnAction(event -> setColore.setText("Bianco"));

        freniBaseMenuItem.setOnAction(event -> setColoreFreni.setText("Base"));
        freniBluMenuItem.setOnAction(event -> setColoreFreni.setText("Blu"));
        freniBiancoMenuItem.setOnAction(event -> setColoreFreni.setText("Bianco"));
    }

    private void generatePDF() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));
        fileChooser.setInitialFileName("configurazione.pdf");
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try (PdfWriter writer = new PdfWriter(file.getAbsolutePath());
                 PdfDocument pdf = new PdfDocument(writer);
                 Document document = new Document(pdf)) {

                document.add(new Paragraph("Informazioni di configurazione:"));
                document.add(new Paragraph("Configurazione vetri oscurati: " + (vetriOscuratiCheckBox.isSelected() ? "Sì" : "No")));
                document.add(new Paragraph("Configurazione cerchi maggiorati: " + (cerchiMaggioratiCheckBox.isSelected() ? "Sì" : "No")));
                document.add(new Paragraph("Colore auto: " + setColore.getText()));
                document.add(new Paragraph("Colore freni: " + setColoreFreni.getText()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}

