package api;

import android.widget.Toast;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.List;

import entity.Generic.Country;
import helper.IGetRequest;

public class GetCitiyNCountryList extends BaseTask {

    public GetCitiyNCountryList(API api, IGetRequest callback) {
        super(callback);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("session_id", "");
        createParams("location?api_key=" + API.API_KEY, params);
    }

    @Override
    protected void onResponse(Object response) {
        JSONObject json = (JSONObject) response;
        if (json.containsKey("COUNTRY_LIST")) {
            List<Country> items = Country.factoryList(json.get("COUNTRY_LIST"));
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
