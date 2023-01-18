package pgdp.networking;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.*;
import java.net.http.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class HTTPClientMock extends HttpClient {
    private final ArrayDeque<Response> responses = new ArrayDeque<>();
    private final ArrayDeque<Consumer<HttpRequest>> assertions = new ArrayDeque<>();

    public HTTPClientMock respond(int statusCode, String response) {
        if (assertions.size() == responses.size()) assertions.add(r -> {});
        responses.add(new Response(statusCode, response));
        return this;
    }

    public HTTPClientMock assertNextRequest(Consumer<HttpRequest> assertion) {
        assertions.add(assertion);
        return this;
    }

    @Override
    public Optional<CookieHandler> cookieHandler() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Duration> connectTimeout() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Redirect followRedirects() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<ProxySelector> proxy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SSLContext sslContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SSLParameters sslParameters() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Authenticator> authenticator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Version version() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Executor> executor() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> HttpResponse<T> send(HttpRequest httpRequest, HttpResponse.BodyHandler<T> bodyHandler) throws IOException, InterruptedException {
        var assertion = assertions.poll();
        var response = responses.poll();
        if (response == null) {
            throw new RuntimeException("Did not expected request: " + httpRequest);
        }
        assertion.accept(httpRequest);
        response.setRequest(httpRequest);
        return (HttpResponse<T>) response;
    }

    @Override
    public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest httpRequest, HttpResponse.BodyHandler<T> bodyHandler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest httpRequest, HttpResponse.BodyHandler<T> bodyHandler, HttpResponse.PushPromiseHandler<T> pushPromiseHandler) {
        throw new UnsupportedOperationException();
    }

    private static class Response implements HttpResponse<String> {
        private final int statusCode;
        private final String body;
        private HttpRequest request;

        public Response(int statusCode, String body) {
            this.statusCode = statusCode;
            this.body = body;
        }

        @Override
        public int statusCode() {
            return statusCode;
        }

        @Override
        public HttpRequest request() {
            return request;
        }

        @Override
        public Optional<HttpResponse<String>> previousResponse() {
            return Optional.empty();
        }

        @Override
        public HttpHeaders headers() {
            return HttpHeaders.of(Map.of(), (s, s2) -> true);
        }

        @Override
        public String body() {
            return body;
        }

        @Override
        public Optional<SSLSession> sslSession() {
            return Optional.empty();
        }

        @Override
        public URI uri() {
            return request.uri();
        }

        @Override
        public Version version() {
            return Version.HTTP_2;
        }

        public void setRequest(HttpRequest request) {
            this.request = request;
        }
    }

}
