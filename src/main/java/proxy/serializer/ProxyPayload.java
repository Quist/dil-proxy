package proxy.serializer;

public class ProxyPayload {

    private final String body;
    private final String path;

    public ProxyPayload(String path, String body) {
        this.body = body;
        this.path = path;
    }

    public String getBody() {
        return body;
    }

    public String getPath() {
        return path;
    }
}
