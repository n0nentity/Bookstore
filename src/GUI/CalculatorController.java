package GUI;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class CalculatorController implements Runnable {
    private EventBus eventBus;

    public CalculatorController(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void run() {
        eventBus.register(this);
    }

    @Subscribe
    public void handleAdditionButtonEvent(RequestButtonEvent additionButtonEvent) {
        eventBus.post(new RequestEvent(additionButtonEvent.value));
    }

    @Subscribe
    public void handleModelResultEvent(ModelResultEvent modelResultEvent) {
        eventBus.post(new UpdateDisplayEvent(modelResultEvent.value));
    }
}