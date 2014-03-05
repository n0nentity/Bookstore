package GuiCommand;

import Globals.Book;

/**
 * Created by HeierMi on 04.03.14.
 * Represents gui command
 */
public class GuiCommand {
    private GuiCommandType guiCommandType;
    private Book book;
    private String clientUuid;

    public GuiCommand() {
    }

    public GuiCommand(GuiCommandType guiCommandType, Book book, String clientUuid) {
        this.guiCommandType = guiCommandType;
        this.book = book;
        this.clientUuid = clientUuid;
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

    public String getClientUuid() {
        return clientUuid;
    }

    public void setClientUuid(String clientUuid) {
        this.clientUuid = clientUuid;
    }
}
