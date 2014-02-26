package GUI;

import ClientServer.Response;

public class ModelResultEvent {
    protected Response value;

    public ModelResultEvent(Response value) {
        this.value = value;
    }
}