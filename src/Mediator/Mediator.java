package Mediator;

import ClientServer.Request;
import GuiCommand.*;
import MethodLinkRepository.MethodLinkRepository;
import Persistence.Persistence;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by HeierMi on 26.02.14.
 * Mediator class
 * Undo functionality
 */
public class Mediator implements ITransaction{
    private HashMap<String, IPreviousTransactionToCareTaker> undoMap = new HashMap<>();
    private ServerThread serverThread;
    private Integer port = null;
    private MethodLinkRepository methodLinkRepository = new MethodLinkRepository(this);
    private Persistence persistence = new Persistence();


    public Mediator() {
        serverThread = new ServerThread(this);
        serverThread.start();
    }

    public int getPort() {
        if (port == null) {
            port = 9900;
        }
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public synchronized String undo(String uuid) {
        String result = null;
        IPreviousTransactionToCareTaker previousTransactionToCareTaker = undoMap.get(uuid);
        if (previousTransactionToCareTaker != null) {
            GuiCommand guiCommand = restorePreviousTransaction(uuid, previousTransactionToCareTaker);

            // execute undo functionality
            if (guiCommand != null) {
                if (guiCommand.getGuiCommandType() == GuiCommandType.newBook)
                    persistence.delete(guiCommand.getBook());
                if (guiCommand.getGuiCommandType() == GuiCommandType.drop)
                    persistence.insert(guiCommand.getBook());
                if (guiCommand.getGuiCommandType() == GuiCommandType.buy)
                    persistence.update(guiCommand.getBook());
                if (guiCommand.getGuiCommandType() == GuiCommandType.sell)
                    persistence.update(guiCommand.getBook());
                if (guiCommand.getGuiCommandType() == GuiCommandType.updateBook)
                    persistence.update(guiCommand.getBook());
                result = "undo successful: " + guiCommand.getBook();
            }
        }
        else {
            result = "undo not successful: no backup found";
        }
        undoMap.remove(uuid);

        return result;
    }

    public String request(Request request) {
        String result = null;
        if (methodLinkRepository != null)
            result = methodLinkRepository.request(request);
        return result;
    }

    public void backupTransaction(String uuid, GuiCommand guiCommand) {
        // delete other commands for the same book
        ArrayList<PreviousTransaction> previousTransactionArrayList = new ArrayList(undoMap.values());
        for (PreviousTransaction previousTransaction : previousTransactionArrayList) {
           if (previousTransaction.getGuiCommand().getBook().getUuid().equals(guiCommand.getBook().getUuid()))
                undoMap.remove(previousTransaction.getGuiCommand().getClientUuid());
        }

        IPreviousTransactionToCareTaker previousTransactionToCareTaker = backupLastTransaction(uuid, guiCommand);
        if (undoMap.containsKey(uuid))
            undoMap.remove(uuid);
        undoMap.put(uuid, previousTransactionToCareTaker);


    }

    @Override
    public IPreviousTransactionToCareTaker backupLastTransaction(String uuid, GuiCommand guiCommand) {
        return new PreviousTransaction(guiCommand);
    }

    @Override
    public GuiCommand restorePreviousTransaction(String uuid, IPreviousTransactionToCareTaker memento) {
        return ((IPreviousTransactionToOriginator)memento).getGuiCommand();
    }
}
