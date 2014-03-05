package MethodLinkRepository;

import ClientServer.Request;
import Globals.Book;
import Globals.MyClassLoader;
import GuiCommand.GuiCommand;
import GuiCommand.GuiCommandType;
import Mediator.*;
import Persistence.Persistence;
import SoftwareRepository.JarMethodLink;
import SoftwareRepository.SoftwareRepository;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by HeierMi on 26.02.14.
 * method link repository
 */
public class MethodLinkRepository {
    private MethodLinkSearch methodLinkSearch;
    private Parser parser = new Parser();
    private Mediator mediator;
    private SoftwareRepository softwareRepository = new SoftwareRepository();

    public MethodLinkRepository(Mediator mediator) {
        this.mediator = mediator;
    }

    /**
     * organize method links (gui to internal)
     * @return
     */
    public MethodLinkSearch getMethodLinkSearch() {
        if (methodLinkSearch == null) {
            methodLinkSearch = new MethodLinkSearch();
            /*
            Über die GUI sind die Anfragen newBook(title),
            searchBook(title), updateBook(oldTitle,newTitle),
            drop(title), sell(title,quantity), buy(title,quantity)
            und undo() verfügbar.

            Persistence bietet die Methoden insert(book) :
            String, update(book) : String, update(book,#) :
            String, delete(book) und selectByTitle (title) : Book an.
            Transaction bietet die Methoden buy(book) :
            String und sell(book) : String, und undo() : String
            an. Die Methode undo() macht die letzte
            Transaktion aus der korrespond. GUI rückgängig
            Search bietet die Methode search(title) : Book[]
            für die Suche (reguläre Ausdrücke) nach title an.
             */
            MethodLink methodLink;
            methodLink = new MethodLink("newBook(title)", "insert(book)");
            methodLinkSearch.put(methodLink.getGuiMethodName(), methodLink);
            methodLink = new MethodLink("searchBook(title)", "search(title) : Book[]");
            methodLinkSearch.put(methodLink.getGuiMethodName(), methodLink);
            methodLink = new MethodLink("updateBook(oldTitle,newTitle)", "update(book)");
            methodLinkSearch.put(methodLink.getGuiMethodName(), methodLink);
            methodLink = new MethodLink("drop(title)", "delete(book)");
            methodLinkSearch.put(methodLink.getGuiMethodName(), methodLink);
            methodLink = new MethodLink("sell(title,quantity)", "sell(book)");
            methodLinkSearch.put(methodLink.getGuiMethodName(), methodLink);
            methodLink = new MethodLink("buy(title,quantity)", "buy(book)");
            methodLinkSearch.put(methodLink.getGuiMethodName(), methodLink);
            methodLink = new MethodLink("undo()", "undo()");
            methodLinkSearch.put(methodLink.getGuiMethodName(), methodLink);
        }
        return methodLinkSearch;
    }

    private Book getBookByTitle(String title) {
        return Persistence.getInstance().selectByTitle(title);
    }

    /**
     * handle request from gui
     * @param request
     * @return
     */
    public String request(Request request) {
        String response = null;

        String requestString = request.getRequest();

        if (requestString.indexOf("(") != -1) {
            HashMap<String,MethodLink> searchResults = getMethodLinkSearch().search(requestString.substring(0, requestString.indexOf("(")));

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
                            response = mediator.undo(request.getUuid().toString());
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
                                mediator.backupTransaction(request.getUuid().toString(), guiCommand);
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
}
