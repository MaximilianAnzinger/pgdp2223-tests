package pgdp.networking;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Random;

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

    public static <T, F> Method getIntMethod(T obj, String fieldName)
            throws NoSuchMethodException, IllegalAccessException {
        Method method = obj.getClass().getDeclaredMethod(fieldName, int.class);
        method.setAccessible(true);
        return method;
    }

    //
    // https://github.com/MaximilianAnzinger/pgdp2223-tests/pull/166/commits/17eff9398b48df73f6e5d3133055fe267da387f9
    //

    public static String generateString(Random rnd) {
		StringBuilder s = new StringBuilder();
		rnd.ints(rnd.nextInt(255), 97, 122).mapToObj(i -> (char) i).forEach(c -> s.append(c));
		return s.toString();
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
