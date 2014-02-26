package GUI;

import javax.swing.*;

import com.google.common.eventbus.EventBus;

public class EventBusApplication {
    public static void main(String[] args) {
        final EventBus eventBus = new EventBus();
        new CalculatorModel(eventBus);
        Thread controller = new Thread(new CalculatorController(eventBus));

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