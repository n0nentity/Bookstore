package Mediator;

import Globals.Book;

public interface IPreviousTransactionToOriginator {
    public GuiCommand getGuiCommand();
    public Book getBook();
}