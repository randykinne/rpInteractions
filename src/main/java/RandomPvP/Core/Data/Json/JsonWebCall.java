package RandomPvP.Core.Data.Json;

import RandomPvP.Core.Util.NetworkUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class JsonWebCall {

    private String link;
    private JsonDataSet dataSet;

    public JsonWebCall(String link) {
        this.link = link;
    }

    public JsonDataSet call() {
        Future<HashMap<Integer, JSONObject>> task = Executors.newCachedThreadPool().submit(new Callable<HashMap<Integer, JSONObject>>() {
            @Override
            public HashMap<Integer, JSONObject> call() throws Exception {
                final HashMap<Integer, JSONObject> data = new HashMap<>();
                {
                    JSONArray array;
                    {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(link).openStream()));
                        StringBuilder sb = new StringBuilder();
                        {
                            String stored;
                            while ((stored = reader.readLine()) != null) {
                                sb.append(stored);
                            }
                        }

                        array = (JSONArray) new JSONParser().parse(sb.toString());
                    }
                    for (int i = 0; i < array.size(); i++) {
                        data.put(i, (JSONObject) array.get(i));
                    }
                }
                return data;
            }
        });

        try {
            dataSet = new JsonDataSet(task.get());

            return dataSet;
        } catch (Exception ex) {
            NetworkUtil.handleError(ex);
            return null;
        }
    }

    public JsonDataSet getDataSet() {
        return dataSet;
    }

}
