package proxy.serializer;

import java.util.Optional;

public class ProxyPayload {

    private final String path;
    private final String method;

    private final Optional<String> body;
    private final Optional<String> query;

    public static class Builder {
        private final String path;
        private final String method;

        private Optional<String> query = Optional.empty();
        private Optional<String> body = Optional.empty();

        public Builder(String path, String method) {
            this.path = path;
            this.method = method;
        }

        public Builder query(String query) {
            this.query = Optional.ofNullable(query);
            return this;
        }

        public Builder body(String body) {
            this.body = Optional.of(body);
            return this;
        }

        public ProxyPayload build() {
            return new ProxyPayload(this);
        }
    }

    private ProxyPayload(Builder builder) {
        path = builder.path;
        method = builder.method;

        body = builder.body;
        query = builder.query;
    }

    public Optional<String> getBody() {
        return body;
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }


    public Optional<String> getQuery() {
        return query;
    }
}
