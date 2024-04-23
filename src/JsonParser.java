import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JsonParser {

    public static JsonObject parseString(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, JsonObject.class);
    }
}
