package Transaction;

import Globals.Book;
import Persistence.Persistence;

public class Transaction {
    private Persistence persistence = new Persistence();

    public String buy(Book book) {
        book.setQuantity(book.getQuantity()+1);

        persistence.update(book);

        return book + " buy";
    }

    public String sell(Book book) {
        book.setQuantity(book.getQuantity()-1);

        persistence.update(book);

        return book + " sell";
    }
}