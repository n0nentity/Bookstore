package MVC;

public class BookstoreMVC {
    public static void main(String[] args) {
        BookstoreModel model = new BookstoreModel();
        BookstoreView view = new BookstoreView(model);
        BookstoreController controller = new BookstoreController(model,view);
        view.setVisible(true);
    }
}