package Mediator;

/**
 * Created by HeierMi on 03.03.14.

 public static void newBook(String title){}
 public static void searchBook(String title){}
 public static void updateBook(String oldTitle, String newTitle){}
 public static void drop(String title){}
 public static void sell(String title, int quantity){}
 public static void buy(String title, int quantity){}
 public static void undo(){}
 */
public enum GuiCommandType {
    newBook, searchBook, updateBook, drop, sell, buy, undo
}
