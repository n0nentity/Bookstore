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
public class Application {

    public static void main(String[] args){
        final EventBus eventBus = new EventBus();
        Mediator mediator = new Mediator();

        new BookstoreModel(eventBus);
        Thread controller = new Thread(new BookstoreController(eventBus));

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                BookstoreGUI viewA = new BookstoreGUI(eventBus);
                viewA.show();

                BookstoreGUI viewB = new BookstoreGUI(eventBus);
                viewB.show();
            }
        });

        controller.start();
    }
}
