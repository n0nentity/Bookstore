package DAO;

import Database.MySQL;
import Globals.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by HeierMi on 24.02.14.
 *
 * Persistence bietet die Methoden insert(book) :
 String, update(book) : String, update(book,#) :
 String, delete(book) und select (title) : Book an.
 */
public class Persistence implements IBookDAO {
    private Connection databaseConnection;
    private PreparedStatement preparedStatement = null;

    private static final String insertSQLStatement = "INSERT INTO book (uuid,title,quantity) VALUES (?,?,?)";
    private static final String selectSQLStatement = "SELECT uuid,title,quantity FROM book WHERE name = ?";
    private static final String updateSQLStatement = "UPDATE book SET title = ?, quantity = ? WHERE uuid = ?";
    private static final String deleteSQLStatement = "DELETE FROM book WHERE uuid = ?";

    public void insert(Book book) {
        System.out.println("--- insert");
        System.out.println("book : " + book);

        try {
            if (!exists(book)) {
                databaseConnection = MySQL.getInstance().getDatabaseConnection();
                preparedStatement = databaseConnection.prepareStatement(insertSQLStatement);
                preparedStatement.setString(1,book.getUuid());
                preparedStatement.setString(2,book.getTitle());
                preparedStatement.setInt(3,book.getQuantity());
                preparedStatement.execute();
            } else {
                System.out.println("book " + book.getTitle() + " exists");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("---");
    }

    public boolean update(Book book) {
        System.out.println("--- update");
        System.out.println("book : " + book);

        try {
            if (exists(book)) {
                databaseConnection = MySQL.getInstance().getDatabaseConnection();
                databaseConnection.setAutoCommit(false);
                preparedStatement = databaseConnection.prepareStatement(updateSQLStatement);
                preparedStatement.setString(1,book.getTitle());
                preparedStatement.setInt(2,book.getQuantity());
                preparedStatement.setString(3,book.getUuid());
                preparedStatement.execute();
                databaseConnection.commit();
                return true;
            } else {
                System.out.println("book " + book.getTitle() + " not exists");
            }
        } catch (Exception e) {
            try {
                databaseConnection.rollback();
                System.out.println(e.getMessage());
            } catch (Exception rbe) {
                rbe.getMessage();
            }
        }

        System.out.println("---");

        return false;
    }

    public boolean delete(Book book) {
        System.out.println("--- delete");
        System.out.println("book : " + book);

        try {
            if (exists(book)) {
                databaseConnection = MySQL.getInstance().getDatabaseConnection();
                preparedStatement = databaseConnection.prepareStatement(deleteSQLStatement);
                preparedStatement.setString(1, book.getUuid());
                preparedStatement.execute();
                return true;
            } else {
                System.out.println("book " + book.getTitle() + " not exists");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("---");

        return false;
    }

    public boolean exists(Book book) {
        return exists(book.getTitle());
    }

    public boolean exists(String title) {
        return select(title) != null ? true : false;
    }

    public Book select(String title) {
        Book service = null;
        ResultSet resultSet = null;

        try {
            databaseConnection = MySQL.getInstance().getDatabaseConnection();
            preparedStatement = databaseConnection.prepareStatement(selectSQLStatement);
            preparedStatement.setString(1,title);
            preparedStatement.execute();
            resultSet = preparedStatement.getResultSet();
            if (resultSet.next() && resultSet != null) {
                service = new Book(resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getInt(3));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return service;
    }
}
