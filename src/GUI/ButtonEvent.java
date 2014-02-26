package GUI;

import ClientServer.Request;

public abstract class ButtonEvent {
    protected Request value;

    public ButtonEvent(Request value) {
        this.value = value;
    }
}