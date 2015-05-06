package com.example.RestApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sanea on 06.05.15.
 */
public class JsonParser {
    private final JSONObject _json;

    public JsonParser(JSONObject json){
        _json = json;
    }

    public String GetJsonString(String objectName)
    {
        try {
            //Returns the value mapped by name if it exists, coercing it if necessary, or throws if no such mapping exists.
            return _json.getString(objectName);
        } catch (JSONException e) {
            return e.getMessage();
        }
    }

    public String GetJsonArrayElement(String arrayName, String objectName)
    {
        try {
            //Returns the value mapped by name if it exists and is a JSONArray, or throws otherwise.
            JSONArray jArray = _json.getJSONArray(arrayName);

            for (int i=0; i < jArray.length(); i++)
            {
                //Returns the value mapped by name if it exists and is a JSONObject, or throws otherwise.
                JSONObject oneObject = jArray.getJSONObject(i);

                return oneObject.getString(objectName);
            }

            return arrayName + " is empty";
        } catch (JSONException e) {
            return e.getMessage();
        }
    }
}
