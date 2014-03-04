package Mediator;

import ClientServer.Request;
import Globals.Book;
import Globals.MyClassLoader;
import MethodLinkRepository.MethodLink;
import MethodLinkRepository.MethodLinkRepository;
import Persistence.Persistence;
import SoftwareRepository.SoftwareRepository;
import SoftwareRepository.JarMethodLink;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by HeierMi on 26.02.14.
 */
public class Mediator implements ITransaction {
    public Mediator() {
        serverThread = new ServerThread(this);
        serverThread.start();
    }

    private HashMap<String, IPreviousTransactionToCareTaker> undoMap = new HashMap<>();
    private ServerThread serverThread;
    private Integer port = null;
    private MethodLinkRepository methodLinkRepository = new MethodLinkRepository();
    private SoftwareRepository softwareRepository = new SoftwareRepository();
    private Parser parser = new Parser();

    public int getPort() {
        if (port == null) {
            port = 9900;
        }
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private Book getBookByTitle(String title) {
        return Persistence.getInstance().select(title);
    }

    public String request(Request request) {
        String response = null;

        String requestString = request.getRequest();

        if (requestString.indexOf("(") != -1) {
            HashMap<String,MethodLink> searchResults = methodLinkRepository.getMethodLinkSearch().search(requestString.substring(0, requestString.indexOf("(")));

            if (searchResults != null && searchResults.size() == 1) {
                // parse and interpret

                Collection<MethodLink> methodLinks = searchResults.values();

                for (MethodLink methodLink : methodLinks) {

                    // update request string for correct function syntax
                    requestString = requestString.substring(requestString.indexOf("("), requestString.length());
                    requestString = methodLink.getGuiMethodName() + requestString;

                    if (parser.parse(requestString)) {
                        String[] guiFunctionParams = parser.getFunctionParams(requestString);

                        Class[] softwareRepoParaTypes = null;
                        Object[] softwareRepoParaValues = null;

                        Book book = null;

                        if (methodLink.getGuiMethodName().equals(GuiCommandType.undo.toString()))
                        {
                            response = undo(request.getUuid().toString());
                            /*
                            JarMethodLink method = softwareRepository.getMethods().get(methodLink.getLinkMethodName());
                            //
                            MyClassLoader.methodFromJar(method.getPath(), method.getClassName().toString(), method.getMethod().getName());
                            */
                        }
                        else {
                            GuiCommand guiCommand = new GuiCommand();
                            if (methodLink.getGuiMethodName().equals(GuiCommandType.buy.toString()))
                                guiCommand.setGuiCommandType(GuiCommandType.buy);
                            if (methodLink.getGuiMethodName().equals(GuiCommandType.sell.toString()))
                                guiCommand.setGuiCommandType(GuiCommandType.sell);
                            if (methodLink.getGuiMethodName().equals(GuiCommandType.updateBook.toString()))
                                guiCommand.setGuiCommandType(GuiCommandType.updateBook);
                            if (methodLink.getGuiMethodName().equals(GuiCommandType.newBook.toString()))
                                guiCommand.setGuiCommandType(GuiCommandType.newBook);
                            if (methodLink.getGuiMethodName().equals(GuiCommandType.drop.toString()))
                                guiCommand.setGuiCommandType(GuiCommandType.drop);
                            if (methodLink.getGuiMethodName().equals(GuiCommandType.searchBook.toString()))
                                guiCommand.setGuiCommandType(GuiCommandType.searchBook);


                            book = getBookByTitle(guiFunctionParams[0]);

                            if (guiCommand.getGuiCommandType() == GuiCommandType.newBook)
                                book = new Book(null, guiFunctionParams[0], 1);

                            if (book != null)
                                guiCommand.setBook((Book)book.clone());

                            softwareRepoParaTypes = new Class[1];
                            softwareRepoParaValues = new Object[1];

                            if (guiCommand.getGuiCommandType() == GuiCommandType.searchBook) {
                                softwareRepoParaTypes[0] = String.class;
                                softwareRepoParaValues[0] = guiFunctionParams[0];
                            }
                            else
                            {
                                if (book != null) {
                                    softwareRepoParaTypes[0] = Book.class;
                                    softwareRepoParaValues[0] = book;
                                }
                                else {
                                    return "book " + guiFunctionParams[0] + " not found";
                                }
                            }

                            if (guiCommand.getGuiCommandType() == GuiCommandType.updateBook) {
                                book.setTitle(guiFunctionParams[1]);
                            }

                            int quantity = 1;

                            if ((guiCommand.getGuiCommandType() == GuiCommandType.sell)
                                    || (guiCommand.getGuiCommandType() == GuiCommandType.buy)){
                                quantity = Integer.parseInt(guiFunctionParams[1].trim());
                            }

                            while (quantity > 0) {
                                JarMethodLink method = softwareRepository.getMethods().get(methodLink.getLinkMethodName());
                                if (method != null) {
                                    Object result = MyClassLoader.methodFromJar(method.getPath(), method.getClassName().toString(), method.getMethod().getName(), softwareRepoParaTypes, softwareRepoParaValues);
                                    if (result.getClass().isArray()) {
                                        Book[] res = (Book[])result;
                                        response = "search: ";
                                        for (int i = 0; i < res.length; i++) {
                                            response += res[i].toString() + ", ";
                                        }
                                    }
                                    else
                                        response = "" + result.toString();
                                }
                                quantity--;
                            }

                            if (guiCommand.getBook() != null) {
                                IPreviousTransactionToCareTaker previousTransactionToCareTaker = backupLastTransaction(request.getUuid().toString(), guiCommand);
                                if (undoMap.containsKey(request.getUuid().toString()))
                                    undoMap.remove(request.getUuid().toString());
                                undoMap.put(request.getUuid().toString(), previousTransactionToCareTaker);
                            }
                        }
                    }
                    else {
                        response = "method " + requestString + " syntax incorrect";
                    }
                }
            }
            else {
                response = "method " + requestString + " not found";
            }
        }
        else {
            response = "method " + requestString + " syntax incorrect";
        }
        return response;
    }

    public String undo(String uuid) {
        IPreviousTransactionToCareTaker previousTransactionToCareTaker = undoMap.get(uuid);
        if (previousTransactionToCareTaker != null) {
            GuiCommand guiCommand = restorePreviousTransaction(uuid, previousTransactionToCareTaker);
            if (guiCommand != null) {
                if (guiCommand.getGuiCommandType() == GuiCommandType.newBook)
                    Persistence.getInstance().delete(guiCommand.getBook());
                if (guiCommand.getGuiCommandType() == GuiCommandType.drop)
                    Persistence.getInstance().insert(guiCommand.getBook());
                if (guiCommand.getGuiCommandType() == GuiCommandType.buy)
                    Persistence.getInstance().update(guiCommand.getBook());
                if (guiCommand.getGuiCommandType() == GuiCommandType.sell)
                    Persistence.getInstance().update(guiCommand.getBook());
                if (guiCommand.getGuiCommandType() == GuiCommandType.updateBook)
                    Persistence.getInstance().update(guiCommand.getBook());
            }
        }
        undoMap.remove(uuid);

        return " undo";
    }

    @Override
    public IPreviousTransactionToCareTaker backupLastTransaction(String uuid, GuiCommand guiCommand) {
        return new PreviousTransaction(guiCommand);
    }

    @Override
    public GuiCommand restorePreviousTransaction(String uuid, IPreviousTransactionToCareTaker memento) {
        return ((IPreviousTransactionToOriginator)memento).getGuiCommand();
    }
}
