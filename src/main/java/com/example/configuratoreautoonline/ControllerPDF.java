package com.example.configuratoreautoonline;

/**
 * import com.itextpdf.kernel.pdf.PdfDocument;
 * import com.itextpdf.kernel.pdf.PdfWriter;
 * import com.itextpdf.layout.Document;
 * import com.itextpdf.layout.element.Paragraph;
 */

import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

public class ControllerPDF {

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

    @FXML
    public void initialize() {

        coloreBaseMenuItem.setOnAction(event -> setColore.setText("Base"));
        coloreBluMenuItem.setOnAction(event -> setColore.setText("Blu"));
        coloreBiancoMenuItem.setOnAction(event -> setColore.setText("Bianco"));

        freniBaseMenuItem.setOnAction(event -> setColoreFreni.setText("Base"));
        freniBluMenuItem.setOnAction(event -> setColoreFreni.setText("Blu"));
        freniBiancoMenuItem.setOnAction(event -> setColoreFreni.setText("Bianco"));
    }
}

