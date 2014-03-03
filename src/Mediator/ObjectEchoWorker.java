package Mediator;

import ClientServer.Request;
import ClientServer.Response;
import ClientServer.Server;

import java.net.Socket;

/**
 * Created by HeierMi on 26.02.14.
 */
public class ObjectEchoWorker extends Thread {
    Mediator mediator;
    Server server;

    public ObjectEchoWorker(Mediator mediator, Server server) {
        this.mediator = mediator;
        this.server = server;
    }

    public void run(){
        try{
            Request request = server.receiveRequest();

            String s = mediator.request(request);

            Response response = new Response(request.getUuid(), s);

            server.sendResponse(response);
        }
        catch( Exception e ) {

        }
    }
}
