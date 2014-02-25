package Transaction;

public interface ITransaction {
    public IPreviousTransactionToCareTaker backupLastTransaction();
    public void restorePreviousTransaction(IPreviousTransactionToCareTaker memento);
}