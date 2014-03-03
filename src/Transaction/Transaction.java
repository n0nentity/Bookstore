package Transaction;

import Globals.Book;
import Persistence.Persistence;

public class Transaction {
    public String buy(Book book) {
        book.setQuantity(book.getQuantity()+1);

        Persistence.getInstance().update(book);

        return book + " buy";
    }

    public String sell(Book book) {
        book.setQuantity(book.getQuantity()-1);

        Persistence.getInstance().update(book);

        return book + " sell";
    }
}