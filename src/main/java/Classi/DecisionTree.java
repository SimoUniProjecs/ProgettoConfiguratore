package Classi;

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
    /*public String predict(Map<String, String> input) {
        Nodo node = root;
        StringBuilder path = new StringBuilder();

        while (!node.isLeaf()) {
            path.append(node.part).append("/");
            String answer = input.get(node.part);
            node = node.branches.stream()
                    .filter(branch -> branch.part.equals(answer))
                    .findFirst()
                    .orElse(null);
            if (node == null) {
                return "Unknown"; // Gestione del caso in cui non esiste un ramo corrispondente
            }
        }
        path.append(node.part); // Aggiungi l'ultima parte (foglia)
        return path.toString();*/
    //}

    // Esempio di utilizzo
    public static void main(String[] args) {
        // Costruzione manuale dell'albero per la selezione degli optional di una macchina
        Nodo root = new Nodo("root", null);

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


        coloreRosso = new Nodo("Rosso", Stelvio);
        Nodo coloreBlu = new Nodo("Blu", Stelvio);
        coloreVerde = new Nodo("Verde", Stelvio);


        Stelvio.addBranch(coloreRosso, coloreBlu, coloreVerde);

        // Creazione dell'albero di decisione
        DecisionTree tree = new DecisionTree();
        tree.train(root);
        tree.printTree();




       /* String path = tree.predict(input);
        System.out.println("Path: " + path); // Output: Tipo di Carburante/Benzina/Cambio Automatico/Rosso*/
    }
}

