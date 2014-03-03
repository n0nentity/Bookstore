package Persistence;

import Database.MySQL;
import Globals.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by HeierMi on 24.02.14.
 *
 * Persistence bietet die Methoden insert(book) :
 String, update(book) : String, update(book,#) :
 String, delete(book) und select (title) : Book an.
 */
public class Persistence implements IBookDAO {
    private static Persistence instance;

    public static Persistence getInstance() {
        if (instance == null)
            instance = new Persistence();
        return instance;
    }

    private Connection databaseConnection;
    private PreparedStatement preparedStatement = null;

    private static final String insertSQLStatement = "INSERT INTO book (uuid,title,quantity) VALUES (?,?,?)";
    private static final String selectSQLStatement = "SELECT uuid,title,quantity FROM book WHERE title = ?";
    private static final String selectAllSQLStatement = "SELECT uuid,title,quantity FROM book";
    private static final String updateSQLStatement = "UPDATE book SET title = ?, quantity = ? WHERE uuid = ?";
    private static final String deleteSQLStatement = "DELETE FROM book WHERE uuid = ?";

    public String insert(Book book) {
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
                return "insert completed successfully: " + book.getTitle();
            } else {
                System.out.println("book " + book.getTitle() + " exists");
                return "insert failed: " + book.getTitle() + " exists";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "insert failed: " + e.getMessage();
        }

        //System.out.println("---");
    }

    public String update(Book book) {
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
                return "update completed successfully: " + book.getTitle();
            } else {
                System.out.println("book " + book.getTitle() + " not exists");
                return "update failed: " + book.getTitle() + " exists";
            }
        } catch (Exception e) {
            try {
                databaseConnection.rollback();
                System.out.println(e.getMessage());
            } catch (Exception rbe) {
                rbe.getMessage();
            }
            return "update failed: " + e.getMessage();
        }
    }

    public String delete(Book book) {
        System.out.println("--- delete");
        System.out.println("book : " + book);

        try {
            if (exists(book)) {
                databaseConnection = MySQL.getInstance().getDatabaseConnection();
                preparedStatement = databaseConnection.prepareStatement(deleteSQLStatement);
                preparedStatement.setString(1, book.getUuid());
                preparedStatement.execute();
                return "delete completed successfully: " + book.getTitle();
            } else {
                System.out.println("book " + book.getTitle() + " not exists");
                return "delete failed: " + book.getTitle() + " exists";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "delete failed: " + e.getMessage();
        }
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
            if (resultSet != null && resultSet.next()) {
                service = new Book(resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getInt(3));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return service;
    }

    public ArrayList<Book> getBooks() {
        ArrayList<Book> result = new ArrayList<Book>();
        ResultSet resultSet = null;

        try {
            databaseConnection = MySQL.getInstance().getDatabaseConnection();
            preparedStatement = databaseConnection.prepareStatement(selectAllSQLStatement);
            preparedStatement.execute();
            resultSet = preparedStatement.getResultSet();
            while (resultSet != null && resultSet.next()) {
                result.add(new Book(resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getInt(3)));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return result;
    }
}
