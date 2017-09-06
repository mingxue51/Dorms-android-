package api;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.List;

import entity.Property.Properties;
import entity.Property.Search.SearchCity;
import entity.Property.Search.Suggestion;
import helper.IGetRequest;

public class GetSuggestions extends BaseTask {
    private API.IGetRealTime mCallback;
    public GetSuggestions(API api, SearchCity srchCity, API.IGetRealTime cbGetData) {

        super(new IGetRequest() {
            @Override
            public void onSuccess(String message) {}
            @Override
            public void onError(String message) {}
        });

        mCallback = cbGetData;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("term",srchCity.getTerm());
        params.put("filter",srchCity.getFilter());
        createParams("search_suggest?api_key=" + API.API_KEY, params);
    }

    @Override
    protected void onResponse(Object response) {
        JSONObject json = (JSONObject) response;
        if (json.containsKey("suggestions")) {
            Properties properties = new Properties();
            List<Suggestion> items = Suggestion.factoryList(json.get("suggestions"));
            if (!items.isEmpty()) {
                mCallback.getData(items);
            } else {
                //TODO: throws error
                //Toast.makeText(getContext(), "Please, Try again.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
