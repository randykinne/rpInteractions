package RandomPvP.Core.Data.Json;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class JsonDataSet {

    private HashMap<Integer, JSONObject> data = new HashMap<>();

    public JsonDataSet(HashMap<Integer, JSONObject> data) {
        this.data = data;
    }

    public JSONObject getWhere(int location) {
        return data.get(location);
    }

    public JSONObject getWhere(String key, String val) {
        for(int i=0; i < data.size(); i++) {
            JSONObject obj = data.get(i);
            if(obj.get(key).equals(val)) {
                return obj;
            }
        }

        return null;
    }

}
