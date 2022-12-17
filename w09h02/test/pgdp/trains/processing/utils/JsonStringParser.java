package pgdp.trains.processing.utils;

import org.json.JSONObject;
import pgdp.trains.connections.TrainConnection;
import pgdp.trains.processing.resources.JsonStrings;

public class JsonStringParser {

    private static TrainConnection ice881 = TrainConnection.parseFromJSON(new JSONObject(JsonStrings.ice881));
    private static TrainConnection re8 = TrainConnection.parseFromJSON(new JSONObject(JsonStrings.re8));
    private static TrainConnection str29 = TrainConnection.parseFromJSON(new JSONObject(JsonStrings.str29));

    public static TrainConnection getICE881() {
        return ice881;
    }

    public static TrainConnection getRe8() {
        return re8;
    }

    public static TrainConnection getStr29() {
        return str29;
    }
}
