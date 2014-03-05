package Search;

import Globals.Book;
import Persistence.Persistence;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by HeierMi on 24.02.14.
 * Search class
 */
public class Search {
    private Persistence persistence = new Persistence();

    /**
     * search for Books with overgiven title in database using direct search, regex and soundex
     * @param title
     * @return
     */
    public Book[] search(String title) {
        ArrayList<Book> books = persistence.getBooks();

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
