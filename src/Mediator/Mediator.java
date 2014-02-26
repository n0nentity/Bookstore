package Mediator;

import Globals.Book;
import Globals.MyClassLoader;
import MethodLinkRepository.MethodLink;
import MethodLinkRepository.MethodLinkRepository;
import Persistence.Persistence;
import SoftwareRepository.SoftwareRepository;
import SoftwareRepository.JarMethodLink;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by HeierMi on 26.02.14.
 */
public class Mediator {
    private MethodLinkRepository methodLinkRepository = new MethodLinkRepository();
    private SoftwareRepository softwareRepository = new SoftwareRepository();
    private Parser parser = new Parser();

    private Book getBookByTitle(String title) {
        return Persistence.getInstance().select(title);
    }

    public String request(String request) {
        String response = null;

        HashMap<String,MethodLink> searchResults = methodLinkRepository.getMethodLinkSearch().search(request);

        if (searchResults != null && searchResults.size() == 1) {
            // parse and interpret

            Collection<MethodLink> methodLinks = searchResults.values();
            for (MethodLink methodLink : methodLinks) {
                if (parser.parse(request)) {
                    String[] guiFunctionParams = parser.getFunctionParams(request);

                    /*
                    public static void newBook(String title){}
                    public static void searchBook(String title){}
                    public static void updateBook(String oldTitle, String newTitle){}
                    public static void drop(String title){}
                    public static void sell(String title, int quantity){}
                    public static void buy(String title, int quantity){}
                    public static void undo(){}
                    */
                    Class[] softwareRepoParaTypes = null;
                    Object[] softwareRepoParaValues = null;

                    Book book = null;

                    if (methodLink.getGuiMethodName().equals("undo"))
                    {
                        //
                    }
                    else {
                        book = getBookByTitle(guiFunctionParams[0]);
                        if (methodLink.getGuiMethodName().equals("updateBook")) {
                            book.setTitle(guiFunctionParams[1]);
                        }
                        if (methodLink.getGuiMethodName().equals("sell")
                                || methodLink.getGuiMethodName().equals("buy")){
                            // multiple call???
                        }

                        softwareRepoParaTypes = new Class[1];
                        softwareRepoParaValues = new Object[1];

                        if (methodLink.getGuiMethodName().equals("search")) {
                            softwareRepoParaTypes[0] = String.class;
                            softwareRepoParaValues[0] = guiFunctionParams[0];
                        }
                        else
                        {
                            softwareRepoParaTypes[0] = Book.class;
                            softwareRepoParaValues[0] = book;
                        }
                    }

                    JarMethodLink method = softwareRepository.getMethods().get(methodLink.getLinkMethodName());
                    if (method != null) {
                        if (softwareRepoParaTypes == null)
                            MyClassLoader.methodFromJar(method.getPath(), method.getMethod().toString(), method.getMethod().getName());
                        else
                            MyClassLoader.methodFromJar(method.getPath(), method.getMethod().toString(), method.getMethod().getName(), softwareRepoParaTypes, softwareRepoParaValues);
                    }
                }
            }
        }
        else {
            response = request + " not found";
        }
        return response;
    }
}
