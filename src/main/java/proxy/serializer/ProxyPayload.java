package proxy.serializer;

public class ProxyPayload {

    private final String path;
    private final String method;
    private final String body;

    public ProxyPayload(String path, String method, String body) {
        this.path = path;
        this.method = method;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }
}
