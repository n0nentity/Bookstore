package Transaction;

import Globals.Book;

public class Transaction implements ITransaction {
    private Book book;
    private int quantity;
    private IPreviousTransactionToCareTaker previousTransactionToCareTaker;

    public String buy(Book book) {
        this.book = book;
        this.quantity = book.getQuantity();
        previousTransactionToCareTaker = backupLastTransaction();

        book.setQuantity(quantity+1);
        return book + " buy";
    }

    public String sell(Book book) {
        this.book = book;
        this.quantity = book.getQuantity();
        previousTransactionToCareTaker = backupLastTransaction();

        book.setQuantity(quantity-1);
        return book + " sell";
    }

    public String undo() {
        if (previousTransactionToCareTaker != null) {
            restorePreviousTransaction(previousTransactionToCareTaker);
            book.setQuantity(quantity);
        }

        previousTransactionToCareTaker = null;
        return " undo";
    }

    @Override
    public IPreviousTransactionToCareTaker backupLastTransaction() {
        return new PreviousTransaction(book, quantity);
    }

    @Override
    public void restorePreviousTransaction(IPreviousTransactionToCareTaker memento) {
        this.book = ((IPreviousTransactionToOriginator)memento).getBook();
        this.quantity = ((IPreviousTransactionToOriginator)memento).getQuantity();
    }
}