package Memento;

import Globals.Book;

public class PreviousTransaction implements IPreviousTransactionToCareTaker, IPreviousTransactionToOriginator {
    private Book book;
    private int quantity;

    public PreviousTransaction(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public Book getBook() {
        return book;
    }

    public int getQuantity() {
        return quantity;
    }
}