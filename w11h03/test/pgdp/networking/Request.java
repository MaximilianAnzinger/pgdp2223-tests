package pgdp.networking;

import com.sun.net.httpserver.Headers;

import java.net.URI;
import java.nio.charset.StandardCharsets;

public record Request(String requestMethod, Headers requestHeaders, URI requestURI, byte[] body) {

    public Request(String requestMethod, Headers requestHeaders, URI requestURI, String bodyUTF8) {
        this(requestMethod, requestHeaders, requestURI, bodyUTF8.getBytes(StandardCharsets.UTF_8));
    }

    public String getBodyUTF8() {
        return new String(body, StandardCharsets.UTF_8);
    }
}
