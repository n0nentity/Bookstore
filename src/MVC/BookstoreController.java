package MVC;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BookstoreController {
    private BookstoreModel model;
    private BookstoreView view;

    public BookstoreController(BookstoreModel model, BookstoreView view) {
        this.model = model;
        this.view = view;
        this.view.addRequestListener(new RequestListener());
    }

    private class RequestListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String userInput = "";
            try {
                userInput = view.getUserInput();
                model.request(userInput);
                view.setTotal(model.getRequestResult());
            } catch (NumberFormatException nfex) {
                view.showError("invalid input : '" + userInput + "'");
            }
        }
    }
}