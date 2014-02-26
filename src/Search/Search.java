package Search;

import Globals.Book;
import Persistence.Persistence;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by HeierMi on 24.02.14.
 */
public class Search {
    public Book[] search(String title) {
        ArrayList<Book> books = Persistence.getInstance().getBooks();

        BookSearch hashMapSearch = new BookSearch();

        for (Book book : books) {
            hashMapSearch.put(book.getTitle(), book);
        }

        HashMap<String,Book> resultHashMap = hashMapSearch.search(title);

        Book[] result = null;
        if (resultHashMap != null) {
            result = new Book[resultHashMap.values().size()];
            resultHashMap.values().toArray(result);
        }
        return result;
    }
}
