package my.base.parser;

import java.util.List;
import my.base.util.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {
    public static void decode(JSONParsable jsonParsable) throws ParseException, IllegalArgumentException {
        String data = jsonParsable.getJSONString();
        if (StringUtils.isNotBlank(data)) {
            try {
                jsonParsable.decodeFromJSON(new JSONObject(data));
                return;
            } catch (JSONException e) {
                throw new ParseException(e.getMessage());
            }
        }
        throw new IllegalArgumentException();
    }

    public static List<JSONParsable> decodeList(JSONParsable jsonParsable) throws ParseException, IllegalArgumentException {
        String data = jsonParsable.getJSONString();
        if (StringUtils.isNotBlank(data)) {
            try {
                return jsonParsable.decodeFromJSON(new JSONArray(data));
            } catch (JSONException e) {
                throw new ParseException(e.getMessage());
            }
        }
        throw new IllegalArgumentException();
    }
}
