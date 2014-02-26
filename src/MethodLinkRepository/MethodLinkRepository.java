package MethodLinkRepository;

/**
 * Created by HeierMi on 26.02.14.
 */
public class MethodLinkRepository {
    private MethodLinkSearch methodLinkSearch;

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
            String, delete(book) und select (title) : Book an.
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
}
