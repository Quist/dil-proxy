package proxy.serializer;

import org.json.JSONObject;

import java.util.*;

public class ProxyMessage {

    private final String path;
    private final String method;
    private final Map<String, String> httpHeaders;


    private final Optional<String> body;
    private final Optional<String> query;

    public static class Builder {
        private final String path;
        private final String method;
        private final Map<String, String> httpHeaders;

        private Optional<String> query = Optional.empty();
        private Optional<String> body = Optional.empty();

        public Builder(String path, String method, JSONObject headers) {
            this.path = path;
            this.method = method;
            this.httpHeaders = getHeaders(headers);
        }

        public Builder query(String query) {
            this.query = Optional.ofNullable(query);
            return this;
        }

        public Builder body(String body) {
            this.body = Optional.of(body);
            return this;
        }

        public ProxyMessage build() {
            return new ProxyMessage(this);
        }

        private Map<String, String> getHeaders(JSONObject headers){
            Map<String, String> httpHeaders = new HashMap<>();
            Iterator<String> headerIterator = headers.keys();
            while (headerIterator.hasNext()) {
                String headerName = headerIterator.next();
                String headerValue = headers.getString(headerName);
                httpHeaders.put(headerName, headerValue);
            }
            return httpHeaders;
        }
    }

    private ProxyMessage(Builder builder) {
        path = builder.path;
        method = builder.method;
        httpHeaders =  builder.httpHeaders;

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

    public final Map<String, String> getHttpHeaders() {
        return httpHeaders;
    }

    public Optional<String> getQuery() {
        return query;
    }
}
