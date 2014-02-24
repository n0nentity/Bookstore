package Memento;

import Globals.Book;

public interface IPreviousTransactionToOriginator {
    public Book getBook();
    public int getQuantity();
}