package GuiCommand;

import Globals.Book;

/**
 * Created by HeierMi on 04.03.14.
 * Represents gui command
 */
public class GuiCommand {
    private GuiCommandType guiCommandType;
    private Book book;

    public GuiCommand() {
    }

    public GuiCommand(GuiCommandType guiCommandType, Book book) {
        this.guiCommandType = guiCommandType;
        this.book = book;
    }

    public GuiCommandType getGuiCommandType() {
        return guiCommandType;
    }

    public void setGuiCommandType(GuiCommandType guiCommandType) {
        this.guiCommandType = guiCommandType;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
