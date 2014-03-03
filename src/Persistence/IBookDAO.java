package Persistence;

import Globals.Book;

/**
 * Created by HeierMi on 24.02.14.
 */
public interface IBookDAO {
    public String insert(Book book);
    public Book select(String uuid);
    public String update(Book book);
    public String delete(Book book);
}
