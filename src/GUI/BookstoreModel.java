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
    private String serverName = null;
    private Integer serverPort = null;

    public String getServerName() {
        if (serverName == null)
            serverName = "localhost";
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getServerPort() {
        if (serverPort == null)
            serverPort = 9910;
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public BookstoreModel(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    @Subscribe
    public void handleRequestEvent(RequestEvent requestEvent) {
        //result += addEvent.value;
        //result += ".test";
        //reportResult(result);

        if (client != null) {
            client.closeConnection();
            client = null;
        }
        Client client = new Client();
        if(client.connectToServer(getServerName(), getServerPort())){
            if (client.sendRequest(requestEvent.value)) {
                reportResult(client.receiveResponse());
            }
            else {
                reportResult(new Response(requestEvent.value.getUuid(), "send request failed"));
            }
        }
        else {
            System.out.println("No Connection!");
            reportResult(new Response(requestEvent.value.getUuid(), "no connection to server"));
        }

        client.closeConnection();
    }

    private void reportResult(Response value) {
        eventBus.post(new ModelResultEvent(value));
    }
}