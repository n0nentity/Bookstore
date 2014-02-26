package ClientServer;

import java.util.UUID;

/**
 * Created by HeierMi on 26.02.14.
 */
public class Response {
    public Response(UUID uuid) {
        this.uuid = uuid;
    }

    public Response(UUID uuid, String response) {
        this.uuid = uuid;
        this.response = response;
    }

    private UUID uuid;
    private String response;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
