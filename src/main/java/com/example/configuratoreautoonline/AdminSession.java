package com.example.configuratoreautoonline;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class AdminSession extends SecretarySession {
    private static AdminSession instance;
    private SimpleIntegerProperty permessi;

    public AdminSession() {
        super();
        permessi = new SimpleIntegerProperty();

    }

    public static AdminSession getInstance() {
        if (instance == null) {
            instance = new AdminSession();
        }
        return instance;
    }

    public int getPermessi() {
        return permessi.get();
    }

    public void setPermessi() {
        this.permessi.set(3);
    }

}
