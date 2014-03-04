import GUI.BookstoreController;
import GUI.BookstoreModel;
import com.google.common.eventbus.EventBus;

import javax.swing.*;

/**
 * Created by HeierMi on 04.03.14.
 */
public class BookstoreGUI {
    public static void main(String[] args){
        final EventBus eventBus = new EventBus();

        final BookstoreModel bookstoreModel = new BookstoreModel(eventBus);
        Thread controller = new Thread(new BookstoreController(eventBus));

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUI.BookstoreGUI view = new GUI.BookstoreGUI(eventBus, bookstoreModel);
                view.show();
            }
        });

        controller.start();
    }
}
