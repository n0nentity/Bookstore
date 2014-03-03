package Mediator;

import ClientServer.Server;

import javax.print.attribute.standard.Media;

/**
 * Created by HeierMi on 26.02.14.
 */
public class ServerThread extends Thread {
    Mediator mediator;

    public ServerThread(Mediator mediator) {
        this.mediator = mediator;
    }

    private Server server = new Server();

    public void run(){
        while (true) {
            if(server.waitForClient(mediator.getPort(), 10)){
                new ObjectEchoWorker(this.mediator, server).start();
            }
        }
    }
}
