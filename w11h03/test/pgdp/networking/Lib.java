package pgdp.networking;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

public class Lib {
    //
    // https://github.com/MaximilianAnzinger/pgdp2223-tests/blob/main/w10h02/test/pgdp/teams/Lib.java
    //

    public static <T, F> Field getField(T obj, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    public static <T, F> Method getMethod(T obj, String fieldName)
            throws NoSuchMethodException, IllegalAccessException {
        Method method = obj.getClass().getDeclaredMethod(fieldName);
        method.setAccessible(true);
        return method;
    }

    //
    // Set user private/ public
    //

    public static boolean makePublic(DataHandler dataHandler, boolean value)
            throws NoSuchFieldException, IllegalAccessException {
        var serverAddress = (String) getField(dataHandler, "serverAddress").get(dataHandler);
        var client = (HttpClient) getField(dataHandler, "client").get(dataHandler);

        //
        // TEMPLATE CODE
        //

        HttpRequest request = HttpRequest
                .newBuilder(URI.create("http://" + serverAddress + "/api/user/me/setpublic/" + value))
                .header("Authorization", "Bearer " + dataHandler.requestToken())
                .GET()
                .build();

        try {
            client.send(request, BodyHandlers.ofString());
            return true;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }
}
