package pgdp.trains.processing.utils;

import org.json.JSONObject;
import pgdp.trains.connections.TrainConnection;
import pgdp.trains.processing.resources.JSONDataset;

public class JSONParser {
  private static final JSONObject iceBasicDataset;
  private static final TrainConnection iceBasicParsed;
  private static final JSONObject str29CanceledDataset;
  private static final TrainConnection str29CanceledParsed;
  private static final JSONObject re8BasicDataset;
  private static final TrainConnection re8BasicParsed;

  static {
    iceBasicDataset = new JSONObject(JSONDataset.iceBasicBlob);
    str29CanceledDataset = new JSONObject(JSONDataset.str29CanceledBlob);
    re8BasicDataset = new JSONObject(JSONDataset.re8BasicBlob);

    iceBasicParsed = TrainConnection.parseFromJSON(iceBasicDataset);
    str29CanceledParsed = TrainConnection.parseFromJSON(str29CanceledDataset);
    re8BasicParsed = TrainConnection.parseFromJSON(re8BasicDataset);
  }

  public static TrainConnection getTrainConnectionFromJSON(JSONObject obj) {
    return TrainConnection.parseFromJSON(obj);
  }

  public static TrainConnection getIceBasicParsed() {
    return iceBasicParsed;
  }

  public static TrainConnection getStr29CanceledParsed() {
    return str29CanceledParsed;
  }

  public static TrainConnection getRe8BasicParsed() {
    return re8BasicParsed;
  }

  public static JSONObject getIceBasicDataset() {
    return iceBasicDataset;
  }

  public static JSONObject getStr29CanceledDataset() {
    return str29CanceledDataset;
  }


  public static JSONObject getRe8BasicDataset() {
    return re8BasicDataset;
  }
}
