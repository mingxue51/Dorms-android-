package api;

import android.widget.Toast;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import entity.Generic.Language;
import helper.IGetRequest;

public class GetLanguageList extends BaseTask {

    public GetLanguageList(API api, IGetRequest callback) {
        super(callback);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("session_id", "");
        createParams("language?api_key=" + API.API_KEY, params);
    }

    @Override
    protected void onResponse(Object response) {
        JSONObject json = (JSONObject) response;
        if (json.containsKey("language")) {
            ArrayList<Language> items = Language.factoryList(json.get("language"));
            if (!items.isEmpty()) {
                Language.update(items);
                onSuccess(null);
            } else {
                Toast.makeText(getContext(), "Please, Try again.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
