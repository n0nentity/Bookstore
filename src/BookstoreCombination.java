import GUI.BookstoreController;
import GUI.BookstoreGUI;
import GUI.BookstoreModel;
import Mediator.Mediator;
import Settings.SettingsHandler;
import com.google.common.eventbus.EventBus;

import javax.swing.*;

/**
 * Created by HeierMi on 24.02.14.
 */
public class BookstoreCombination {

    public static void main(String[] args){
        final EventBus eventBus = new EventBus();
        Mediator mediator = new Mediator();
        mediator.setPort(9900);

        final BookstoreModel bookstoreModel = new BookstoreModel(eventBus);
        Thread controller = new Thread(new BookstoreController(eventBus));

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                BookstoreGUI viewA = new BookstoreGUI(eventBus, bookstoreModel);
                viewA.show();

                BookstoreGUI viewB = new BookstoreGUI(eventBus, bookstoreModel);
                viewB.show();
            }
        });

        controller.start();
    }
}
