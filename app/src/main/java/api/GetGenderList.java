package api;

import android.widget.Toast;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.List;

import entity.Generic.Gender;
import helper.IGetRequest;

public class GetGenderList extends BaseTask {

    public GetGenderList(API api, IGetRequest callback) {
        super(callback);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("session_id", "");
        createParams("gender?api_key=" + API.API_KEY, params);
    }

    @Override
    protected void onResponse(Object response) {
        JSONObject json = (JSONObject) response;
        if (json.containsKey("gender")) {
            List<Gender> items = Gender.factoryList(json.get("gender"));
            if (!items.isEmpty()) {
                Gender.update(items);
                onSuccess(null);
            } else {
                Toast.makeText(getContext(), "Please, Try again.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
