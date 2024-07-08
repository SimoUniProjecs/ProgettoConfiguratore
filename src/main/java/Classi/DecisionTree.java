package Classi;

import java.util.Arrays;
import java.util.List;

public class DecisionTree {
    Nodo root;

    // Metodo per addestrare l'albero
    public void train(Nodo root) {
        this.root = root;
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
            return "Errore: Nodo senza figli nel percorso";
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

        Nodo coloreRosso = new Nodo("Rosso", Giulia);
        Nodo coloreGrigio = new Nodo("Grigio", Giulia);
        Nodo coloreVerde = new Nodo("Verde", Giulia);

        Giulia.addBranch(coloreRosso, coloreGrigio, coloreVerde);

        Nodo RS3 = new Nodo("RS3", audi);
        Nodo RS4 = new Nodo("RS4", audi);

        audi.addBranch(RS3,RS4);

        coloreRosso = new Nodo("Rosso", Stelvio);
        Nodo coloreBlu = new Nodo("Blu", Stelvio);
        coloreVerde = new Nodo("Verde", Stelvio);


        Stelvio.addBranch(coloreRosso, coloreBlu, coloreVerde);


        Nodo coloreGiallo = new Nodo("Giallo", RS3);
        coloreGrigio = new Nodo("Grigio", RS3);
        Nodo coloreNero = new Nodo("Nero", RS3);
        RS3.addBranch(coloreNero, coloreGiallo, coloreGrigio);

        Nodo coloreBianco = new Nodo("Bianco", RS4);
        coloreGrigio = new Nodo("Grigio", RS4);
        coloreBlu = new Nodo("Blu", RS4);
        RS3.addBranch(coloreBianco, coloreBlu, coloreGrigio);
        // Creazione dell'albero di decisione
        DecisionTree tree = new DecisionTree();
        tree.train(root);
        tree.printTree();



        List<String> nodePath = Arrays.asList("img","ALFA", "GIULIA", "Rosso");
        System.out.println(nodePath);
        String path = tree.predict(nodePath);
        if (path.startsWith("Errore")) {
            System.out.println(path);
        } else {
            System.out.println("Path: " + path);
        }

    }
}

