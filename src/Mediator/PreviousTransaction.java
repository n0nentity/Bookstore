package Mediator;

import GuiCommand.*;

public class PreviousTransaction implements IPreviousTransactionToCareTaker, IPreviousTransactionToOriginator {
    private GuiCommand guiCommand;

    public PreviousTransaction(GuiCommand guiCommand) {
        this.guiCommand = guiCommand;
    }

    public GuiCommand getGuiCommand() {
        return this.guiCommand;
    }
}