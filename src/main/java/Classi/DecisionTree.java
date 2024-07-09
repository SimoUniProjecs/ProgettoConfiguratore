package Classi;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class DecisionTree {
    Nodo root;

    Boolean [] optionals = new Boolean[5];

    String[] optiString;



    public DecisionTree(Nodo root, String marca, String modello) {
        this.root = root;
        for(int i = 0; i < 5; i++){
            optionals[i] = false;
        }
        loadOptionalsFromJson("public/res/data/datiModelliAuto.json",marca, modello);
    }

    private void loadOptionalsFromJson(String jsonFilePath, String marca, String modello) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(new File(jsonFilePath));
            JsonNode datiAutoUsate = rootNode.get("datiAutoUsate").get(0);
            JsonNode marcaNode = datiAutoUsate.get(marca.toLowerCase());
            if (marcaNode != null) {
                JsonNode modelliNode = marcaNode.get(0).get("modelli").get(0).get(modello);
                if (modelliNode != null) {
                    JsonNode optionalsNode = modelliNode.get("optionals");
                    optiString = new String[optionalsNode.size()];
                    for (int i = 0; i < optionalsNode.size(); i++) {
                        optiString[i] = optionalsNode.get(i).asText();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            optiString = new String[]{"_A", "_B", "_C", "_D", "_E"}; // Valori di default in caso di errore
        }
    }




    // Metodo per stampare l'intero albero
    public void printTree() {
        printNode(root, "", true);
    }

    private void printNode(Nodo node, String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + (node.part == null ? "root" : node.part));
        for (int i = 0; i < node.branches.size() - 1; i++) {
            printNode(node.branches.get(i), prefix + (isTail ? "    " : "│   "), false);
        }
        if (!node.branches.isEmpty()) {
            printNode(node.branches.get(node.branches.size() - 1), prefix + (isTail ? "    " : "│   "), true);
        }
    }
    // Metodo per fare previsioni e ottenere il percorso completo
    public String predict(List<String> nodePath) {
        return predictRecursive(root, nodePath, 0);
    }

    private String predictRecursive(Nodo node, List<String> nodePath, int index) {
        // Se l'indice è uguale alla dimensione della lista nodePath, siamo arrivati alla fine del percorso desiderato
        if (index == nodePath.size()) {
            // Verifica se il nodo corrente è una foglia
            if (node.isLeaf()) {
                return buildPath(nodePath, index - 1); // Costruisci il percorso completo fino a questo punto
            } else {
                return "Errore: Il percorso richiesto non raggiunge una foglia";
            }
        }

        // Verifica se il nodo corrente ha il part corretto
        if (!node.part.equals(nodePath.get(index))) {
            return "Errore: Nodo non trovato nel percorso";
        }

        // Prosegui nella ricerca nei rami
        if (!node.branches.isEmpty()) {
            // Cerca il prossimo nodo nella lista nodePath tra i figli del nodo corrente
            String nextNode = nodePath.get(index + 1);
            Nodo next = node.branches.stream()
                    .filter(branch -> branch.part.equals(nextNode))
                    .findFirst()
                    .orElse(null);
            if (next == null) {
                return "Errore: Figlio non trovato nel percorso";
            }
            return predictRecursive(next, nodePath, index+1);
        } else {
            return buildPath(nodePath, index);
        }
    }


    private String buildPath(List<String> nodePath, int endIndex) {
        StringBuilder path = new StringBuilder();
        for (int i = 0; i <= endIndex; i++) {
            path.append(nodePath.get(i));
            if (i < endIndex) {
                path.append("/");
            }
        }
        for(int i=0; i<5; i++){
            if(optionals[i] == true){

                path.append(optiString[i]);
            }
        }
        return path.toString();
    }




    // Esempio di utilizzo
    public static void main(String[] args) {
        // Costruzione manuale dell'albero per la selezione degli optional di una macchina
        Nodo root = new Nodo("img", null);

        Nodo bmw = new Nodo("BMW", root);
        Nodo audi = new Nodo("AUDI", root);
        Nodo alfa = new Nodo("ALFA", root);

        root.addBranch(bmw);
        root.addBranch(audi);
        root.addBranch(alfa);

        Nodo Giulia = new Nodo("GIULIA", alfa);
        Nodo Stelvio = new Nodo("STELVIO", alfa);

        alfa.addBranch(Stelvio, Giulia);

        Nodo VerdeGiulia = new Nodo("Verde", Giulia);
        Nodo GrigioGiulia = new Nodo("Grigio", Giulia);
        Nodo RossoGiulia = new Nodo("Rosso", Giulia);

        Giulia.addBranch(VerdeGiulia, GrigioGiulia, RossoGiulia);

        Nodo RS3 = new Nodo("RS3", audi);
        Nodo RS4 = new Nodo("RS4", audi);

        audi.addBranch(RS3, RS4);

        Nodo RossoStelvio = new Nodo("Rosso", Stelvio);
        Nodo BluStelvio = new Nodo("Blu", Stelvio);
        Nodo VerdeStelvio = new Nodo("Verde", Stelvio);

        Stelvio.addBranch(RossoStelvio, BluStelvio, VerdeStelvio);

        RS3.addBranch(new Nodo("Nero", RS3), new Nodo("Grigio", RS3), new Nodo("Giallo", RS3));
        RS4.addBranch(new Nodo("Bianco", RS4), new Nodo("Blu", RS4), new Nodo("Grigio", RS4));

        Nodo M2 = new Nodo("M2", bmw);
        Nodo XM = new Nodo("XM", bmw);

        bmw.addBranch(M2, XM);

        M2.addBranch(new Nodo("Azzurro", M2), new Nodo("Grigio", M2), new Nodo("Rosso", M2));
        XM.addBranch(new Nodo("Base", XM));

        // Aggiungi nodi "RuoteGrandi" e "RuoteBase" per ogni colore
        for (Nodo colore : new Nodo[]{VerdeGiulia, GrigioGiulia, RossoGiulia, RossoStelvio, BluStelvio, VerdeStelvio}) {
            colore.addBranch(new Nodo("RuoteGrandi", colore), new Nodo("RuoteBase", colore));
        }
        DecisionTree tree = new DecisionTree(root,  "ALFA", "GIULIA");

        tree.printTree();



        List<String> nodePath = Arrays.asList("img", "ALFA", "GIULIA", "Verde", "RuoteBase");
        //System.out.println(nodePath);
        String path = tree.predict(nodePath);
        if (path.startsWith("Errore")) {
            System.out.println(path);
        } else {
            System.out.println("Path: " + path);
        }



    }
}

