package api;

import android.widget.Toast;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.List;

import entity.Generic.Currency;
import helper.IGetRequest;

public class GetCurrencyList extends BaseTask {

    public GetCurrencyList(API api, IGetRequest callback) {
        super(callback);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("session_id", "");
        createParams("currency?api_key=" + API.API_KEY, params);
    }

    @Override
    protected void onResponse(Object response) {
        JSONObject json = (JSONObject) response;
        if (json.containsKey("currency")) {
            List<Currency> items = Currency.getList(json.get("currency"));
            if (!items.isEmpty()) {
                Currency.update(getContext(), items);
                onSuccess(null);
            } else {
                Toast.makeText(getContext(), "Please, Try again.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
