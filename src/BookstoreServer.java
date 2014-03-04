import Mediator.Mediator;

/**
 * Created by HeierMi on 04.03.14.
 */
public class BookstoreServer {
    public static void main(String[] args){
        Mediator mediator = new Mediator();
        mediator.setPort(9900);
    }
}
