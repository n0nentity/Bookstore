package Globals;

import java.util.UUID;

/**
 * Created by HeierMi on 24.02.14.
 * Book ist charakteristiert durch uuid,title, quantity
 */
public class Book implements Cloneable {
    public Book(String uuid, String title, int quantity) {
        this.uuid = uuid;
        this.title = title;
        this.quantity = quantity;
    }

    public Book() {
        //
    }

    public String getUuid() {
        if (uuid == null)
            this.uuid = UUID.randomUUID().toString();
        return uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Book{" +
                "uuid='" + uuid + '\'' +
                ", title='" + title + '\'' +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public Object clone() {
        return new Book(this.getUuid(), this.getTitle(), this.getQuantity());
    }

    private String uuid = null;
    private String title;
    private int quantity;
}
