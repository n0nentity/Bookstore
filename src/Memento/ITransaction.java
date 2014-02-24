package Memento;

import Globals.Book;

public interface ITransaction {
    public IPreviousTransactionToCareTaker backupLastTransaction();
    public void restorePreviousTransaction(IPreviousTransactionToCareTaker memento);
}