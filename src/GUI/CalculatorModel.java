package GUI;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class CalculatorModel {
    private String result;
    private EventBus eventBus;

    public CalculatorModel(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    @Subscribe
    public void handleAddEvent(RequestEvent addEvent) {
        result += addEvent.value;
        reportResult(result);
    }

    private void reportResult(String value) {
        eventBus.post(new ModelResultEvent(value));
    }
}