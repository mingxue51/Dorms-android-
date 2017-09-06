package api;

import org.json.simple.JSONObject;

import java.util.HashMap;

import entity.Property.Property;
import helper.IGetRequest;

public class GetPropertyDetails extends BaseTask {
    private API.IGetRealTimeObject cb;
    public GetPropertyDetails(API api, String propertyNumber, API.IGetRealTimeObject callback) {

        super(new IGetRequest() {
            @Override
            public void onSuccess(String message) {}
            @Override
            public void onError(String message) {}
        });
        cb = callback;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("property_number", propertyNumber);
        createParams("property?api_key=" + API.API_KEY, params);
    }

    @Override
    protected void onResponse(Object response) {
        JSONObject json = (JSONObject) response;
        if (json.containsKey("property_name") && json.containsKey("property_number") && json.containsKey("property_details")) {
            Property property = new Property();
            property.setJSONByPropertyCall(json);
            cb.getData(property);
        }
    }
}
