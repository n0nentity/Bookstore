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
                    if (parser.parse(requestString)) {
                        String[] guiFunctionParams = parser.getFunctionParams(requestString);

                        Class[] softwareRepoParaTypes = null;
                        Object[] softwareRepoParaValues = null;

                        Book book = null;

                        if (methodLink.getGuiMethodName().equals(GuiCommand.undo.toString()))
                        {
                            undo();
                            /*
                            JarMethodLink method = softwareRepository.getMethods().get(methodLink.getLinkMethodName());
                            //
                            MyClassLoader.methodFromJar(method.getPath(), method.getClassName().toString(), method.getMethod().getName());
                            */
                        }
                        else {
                            book = getBookByTitle(guiFunctionParams[0]);
                            if (methodLink.getGuiMethodName().equals(GuiCommand.newBook.toString()))
                                book = new Book(null, guiFunctionParams[0], 1);

                            softwareRepoParaTypes = new Class[1];
                            softwareRepoParaValues = new Object[1];

                            if (methodLink.getGuiMethodName().equals(GuiCommand.searchBook.toString())) {
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

                            if (methodLink.getGuiMethodName().equals("updateBook")) {
                                book.setTitle(guiFunctionParams[1]);
                            }

                            int quantity = 1;

                            if (methodLink.getGuiMethodName().equals(GuiCommand.sell.toString())
                                    || methodLink.getGuiMethodName().equals(GuiCommand.buy.toString())){
                                quantity = Integer.parseInt(guiFunctionParams[1]);
                            }

                            while (quantity > 0) {
                                JarMethodLink method = softwareRepository.getMethods().get(methodLink.getLinkMethodName());
                                if (method != null) {
                                    Object result = MyClassLoader.methodFromJar(method.getPath(), method.getClassName().toString(), method.getMethod().getName(), softwareRepoParaTypes, softwareRepoParaValues);
                                    response = "" + result;
                                }
                                quantity--;
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

    private Book book;
    private GuiCommand guiCommand;
    private IPreviousTransactionToCareTaker previousTransactionToCareTaker;

    public String undo() {
        if (previousTransactionToCareTaker != null) {
            restorePreviousTransaction(previousTransactionToCareTaker);
        }

        previousTransactionToCareTaker = null;
        return " undo";
    }

    @Override
    public IPreviousTransactionToCareTaker backupLastTransaction() {
        return new PreviousTransaction(book, guiCommand);
    }

    @Override
    public void restorePreviousTransaction(IPreviousTransactionToCareTaker memento) {
        this.book = ((IPreviousTransactionToOriginator)memento).getBook();
        this.guiCommand = ((IPreviousTransactionToOriginator)memento).getGuiCommand();
    }
}
