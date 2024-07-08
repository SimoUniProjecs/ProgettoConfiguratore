package Classi;


import java.util.ArrayList;
import java.util.List;

public class Nodo {
    String part;
    List<Nodo> branches;
    Nodo padre;

    // Costruttore per i nodi decisionali
    public Nodo(String part, Nodo padre) {
        this.part = part;
        this.branches = new ArrayList<>();
        this.padre = padre;
    }

    public boolean isLeaf() {
        return this.branches.isEmpty();
    }

    public void addBranch(Nodo... node) {
        for(Nodo n: node)
            this.branches.add(n);
    }
}
