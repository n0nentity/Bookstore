package GUI;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class BookstoreController implements Runnable {
    private EventBus eventBus;

    public BookstoreController(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void run() {
        eventBus.register(this);
    }

    @Subscribe
    public void handleRequestButtonEvent(RequestButtonEvent requestButtonEvent) {
        eventBus.post(new RequestEvent(requestButtonEvent.value));
    }

    @Subscribe
    public void handleModelResultEvent(ModelResultEvent modelResultEvent) {
        eventBus.post(new UpdateDisplayEvent(modelResultEvent.value));
    }
}