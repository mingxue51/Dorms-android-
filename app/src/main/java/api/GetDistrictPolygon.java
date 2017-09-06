package api;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

import helper.IGetRequest;

public class GetDistrictPolygon extends BaseTask {
    private static final String ACTION_URL = "district_polygon_points?api_key="+API.API_KEY;

    public GetDistrictPolygon(API api, String districtId, IGetRequest callback) {
        super(callback);

        Map<String, String> params = new HashMap<>();
        params.put("district_id", districtId);
        createParams(ACTION_URL, params);
    }

    @Override
    protected void onResponse(Object response) {
        JSONObject json = (JSONObject)response;
        if (json.containsKey("points")) {
            onSuccess((String)json.get("points"));
        }
    }
}
