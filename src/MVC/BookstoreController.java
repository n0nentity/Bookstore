package MVC;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BookstoreController {
    private BookstoreModel model;
    private BookstoreView view;

    public BookstoreController(BookstoreModel model, BookstoreView view) {
        this.model = model;
        this.view = view;
        this.view.addMultiplyListener(new MultiplyListener());
        this.view.addClearListener(new ClearListener());
    }

    private class MultiplyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String userInput = "";
            try {
                userInput = view.getUserInput();
                model.multiplyBy(userInput);
                view.setTotal(model.getTotal());
            } catch (NumberFormatException nfex) {
                view.showError("invalid input : '" + userInput + "'");
            }
        }
    }

    private class ClearListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            model.reset();
            view.reset();
        }
    }
}