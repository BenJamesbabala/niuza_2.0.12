package my.base.parser;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public interface JSONParsable {
    List<JSONParsable> decodeFromJSON(JSONArray jSONArray);

    void decodeFromJSON(JSONObject jSONObject) throws JSONException;

    String getJSONString();

    void setJSONString(String str);

    void setRoot(String str);
}
