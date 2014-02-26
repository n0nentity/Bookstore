package ClientServer;

import java.util.UUID;
import java.io.Serializable;

/**
 * Created by HeierMi on 26.02.14.
 */
public class Request implements Serializable {
    public Request() {
    }

    public Request(UUID uuid) {
        this.uuid = uuid;
    }

    public Request(UUID uuid, String request) {
        this.uuid = uuid;
        this.request = request;
    }

    private UUID uuid;
    private String request;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
