package pgdp.networking;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Util {
    public static final int PORT = 1337;

    private Util() {
        throw new UnsupportedOperationException();
    }

    /**
     * Inspect a {@link DataHandler} method by returning a predefined response and retrieving the {@link Request} sent by the {@link DataHandler} as well as the return value of the method call
     * <p>
     * This method uses the provided {@link DataHandler} object
     *
     * @param endpoint     The endpoint that is expected to get called when {@code fn} gets executed
     * @param code         Statuscode of the response
     * @param responseBody Body of the response
     * @param fn           A {@link Function} that calls a {@link DataHandler} method which in turn sends a {@link java.net.http.HttpRequest} to this proxy
     * @param dh           The {@link DataHandler} to be used in fn
     * @param <T>          Type of the return value of {@code fn}
     * @return A {@link Pair} of the return value of {@code fn} and a {@link Request} object containing the relevant content of the request made by the {@link DataHandler}
     */
    public static <T> Pair<T, Request> inspectEndpointWithDataHandler(String endpoint, int code, String responseBody, Function<DataHandler, T> fn, DataHandler dh) throws IOException {
        AtomicReference<Request> req = new AtomicReference<>();

        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress("127.0.0.1", PORT), 0);
        server.start();

        server.createContext(endpoint, exchange -> {
            req.set(new Request(exchange.getRequestMethod(), exchange.getRequestHeaders(), exchange.getRequestURI(), exchange.getRequestBody().readAllBytes()));

            byte[] body = responseBody.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(code, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        });

        T result = fn.apply(getProxiedDataHandler());

        server.removeContext(endpoint);

        Request request;
        assertNotNull(request = req.get(), "provided function did not send request to specified endpoint");

        server.stop(0);

        return Pair.of(result, request);
    }

    /**
     * Inspect a {@link DataHandler} method by returning a predefined response and retrieving the {@link Request} sent by the {@link DataHandler} as well as the return value of the method call
     * <p>
     * This method always uses a new {@link DataHandler} object
     *
     * @param endpoint     The endpoint that is expected to get called when {@code fn} gets executed
     * @param code         Statuscode of the response
     * @param responseBody Body of the response
     * @param fn           A {@link Function} that calls a {@link DataHandler} method which in turn sends a {@link java.net.http.HttpRequest} to this proxy
     * @param <T>          Type of the return value of {@code fn}
     * @return A {@link Pair} of the return value of {@code fn} and a {@link Request} object containing the relevant content of the request made by the {@link DataHandler}
     */
    public static <T> Pair<T, Request> inspectEndpoint(String endpoint, int code, String responseBody, Function<DataHandler, T> fn) throws IOException {
        DataHandler dh = getProxiedDataHandler();
        Pair<T, Request> result = inspectEndpointWithDataHandler(endpoint, code, responseBody, fn, dh);
        dh.close();
        return result;
    }

    public static DataHandler getProxiedDataHandler() {
        DataHandler dh = new DataHandler();
        dh.setClient(getProxiedClient());
        return dh;
    }

    private static HttpClient getProxiedClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .proxy(ProxySelector.of(new InetSocketAddress("127.0.0.1", PORT)))
                .build();
    }

    public record Request(String requestMethod, Headers requestHeaders, URI requestURI, byte[] body) {

        public Request(String requestMethod, Headers requestHeaders, URI requestURI, String bodyUTF8) {
            this(requestMethod, requestHeaders, requestURI, bodyUTF8.getBytes(StandardCharsets.UTF_8));
        }

        public String getBodyUTF8() {
            return new String(body, StandardCharsets.UTF_8);
        }
    }

    public static String callRequestToken(DataHandler dh, String username, String password) {
        try {
            Method m = dh.getClass().getDeclaredMethod("requestToken", String.class, String.class);
            m.setAccessible(true);
            return (String) m.invoke(dh, username, password);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
