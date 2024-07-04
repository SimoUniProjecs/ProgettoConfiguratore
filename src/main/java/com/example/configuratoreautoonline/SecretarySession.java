package com.example.configuratoreautoonline;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class SecretarySession extends UserSession {
    private static SecretarySession instance;

    private SimpleIntegerProperty permessi;

    public SecretarySession() {
        super();

        permessi = new SimpleIntegerProperty();

    }

    public static SecretarySession getInstance() {
        if (instance == null) {
            instance = new SecretarySession();
        }
        return instance;
    }

    public int getPermessi() {
        return permessi.get();
    }

    public void setPermessi() {
        this.permessi.set(2);
    }

}
