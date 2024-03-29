package Mediator;

import GuiCommand.*;

public interface ITransaction {
    public IPreviousTransactionToCareTaker backupLastTransaction(String uuid, GuiCommand guiCommand);
    public GuiCommand restorePreviousTransaction(String uuid, IPreviousTransactionToCareTaker memento);
}