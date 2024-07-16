package com.example.configuratoreautoonline;

import javafx.event.Event;
import javafx.event.EventType;

// evento usato per gestire il login dell'utente
public class UserLoginEvent extends Event {
    public static final EventType<UserLoginEvent> LOGIN_SUCCESSFUL = new EventType<>(Event.ANY, "LOGIN_SUCCESSFUL");

    public UserLoginEvent() {
        super(LOGIN_SUCCESSFUL);
    }
}
