package Mediator;

import Globals.Book;

public class PreviousTransaction implements IPreviousTransactionToCareTaker, IPreviousTransactionToOriginator {
    private Book book;
    private GuiCommand guiCommand;

    public PreviousTransaction(Book book, GuiCommand guiCommand) {
        this.book = book;
        this.guiCommand = guiCommand;
    }

    public Book getBook() {
        return book;
    }

    public GuiCommand getGuiCommand() {
        return guiCommand;
    }
}