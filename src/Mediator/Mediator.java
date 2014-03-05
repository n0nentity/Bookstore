package Mediator;

import ClientServer.Request;
import GuiCommand.*;
import MethodLinkRepository.MethodLinkRepository;
import Persistence.Persistence;

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

    public String undo(String uuid) {
        IPreviousTransactionToCareTaker previousTransactionToCareTaker = undoMap.get(uuid);
        if (previousTransactionToCareTaker != null) {
            GuiCommand guiCommand = restorePreviousTransaction(uuid, previousTransactionToCareTaker);
            if (guiCommand != null) {
                if (guiCommand.getGuiCommandType() == GuiCommandType.newBook)
                    Persistence.getInstance().delete(guiCommand.getBook());
                if (guiCommand.getGuiCommandType() == GuiCommandType.drop)
                    Persistence.getInstance().insert(guiCommand.getBook());
                if (guiCommand.getGuiCommandType() == GuiCommandType.buy)
                    Persistence.getInstance().update(guiCommand.getBook());
                if (guiCommand.getGuiCommandType() == GuiCommandType.sell)
                    Persistence.getInstance().update(guiCommand.getBook());
                if (guiCommand.getGuiCommandType() == GuiCommandType.updateBook)
                    Persistence.getInstance().update(guiCommand.getBook());
            }
        }
        undoMap.remove(uuid);

        return " undo";
    }

    public String request(Request request) {
        String result = null;
        if (methodLinkRepository != null)
            result = methodLinkRepository.request(request);
        return result;
    }

    public void backupTransaction(String uuid, GuiCommand guiCommand) {
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
