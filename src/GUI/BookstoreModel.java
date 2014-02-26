package GUI;

import ClientServer.Client;
import ClientServer.Request;
import ClientServer.Response;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class BookstoreModel {
    private String result;
    private EventBus eventBus;
    private Client client;

    public BookstoreModel(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    @Subscribe
    public void handleAddEvent(RequestEvent addEvent) {
        //result += addEvent.value;
        //result += ".test";
        //reportResult(result);

        if (client != null) {
            client.closeConnection();
            client = null;
        }
        Client client = new Client();
        if(client.connectToServer("localhost", 9910)){
            if (client.sendRequest(addEvent.value)) {
                reportResult(client.receiveResponse());
            }
            else {
                reportResult(new Response(addEvent.value.getUuid(), "send request failed"));
            }
        }
        else {
            System.out.println("No Connection!");
            reportResult(new Response(addEvent.value.getUuid(), "no connection to server"));
        }

        client.closeConnection();
    }

    private void reportResult(Response value) {
        eventBus.post(new ModelResultEvent(value));
    }
}