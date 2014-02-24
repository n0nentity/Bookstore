package DAO;

import Globals.Book;

/**
 * Created by HeierMi on 24.02.14.
 */
public interface IBookDAO {
    public void insert(Book book);
    public Book select(String uuid);
    public boolean update(Book book);
    public boolean delete(Book book);
}
