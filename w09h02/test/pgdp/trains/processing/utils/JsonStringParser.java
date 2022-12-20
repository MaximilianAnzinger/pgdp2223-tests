package pgdp.trains.processing.utils;

import org.json.JSONObject;
import pgdp.trains.connections.TrainConnection;
import pgdp.trains.processing.resources.JsonStrings;

public class JsonStringParser {

    private static TrainConnection ice881 = parseString(JsonStrings.ice881);
    private static TrainConnection re8 = parseString(JsonStrings.re8);
    private static TrainConnection str29 = parseString(JsonStrings.str29);

    private static TrainConnection str42 = parseString(JsonStrings.str42);

    public static TrainConnection getICE881() {
        return ice881;
    }

    public static TrainConnection getRe8() {
        return re8;
    }

    public static TrainConnection getStr29() {
        return str29;
    }

    public static TrainConnection getStr42() {
        return str42;
    }

    private static TrainConnection parseString(String json) {
        return TrainConnection.parseFromJSON(new JSONObject(json));
    }
}
