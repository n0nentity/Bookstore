package GUI;

import ClientServer.Response;

public class UpdateDisplayEvent {
    protected Response value;

    public UpdateDisplayEvent(Response value) {
        this.value = value;
    }
}