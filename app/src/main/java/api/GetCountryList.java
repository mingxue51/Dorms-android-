package api;

import android.widget.Toast;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import entity.Generic.Country;
import helper.IGetRequest;

public class GetCountryList extends BaseTask {

    public GetCountryList(API api, IGetRequest callback) {
        super(callback);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("session_id", "");
        createParams("select?api_key=" + API.API_KEY, params);
    }

    @Override
    protected void onResponse(Object response) {
        JSONObject json = (JSONObject) response;
        if (json.containsKey("COUNTRY_LIST")) {
            ArrayList<Country> items = Country.factoryList(json.get("COUNTRY_LIST"));
            if (!items.isEmpty()) {
                Country.update(items);
                onSuccess(null);
                //onSuccess(parseMessage(responseObject));
            } else {
                Toast.makeText(getContext(), "Please, Try again.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
