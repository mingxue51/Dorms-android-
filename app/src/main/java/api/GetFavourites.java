package api;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.List;

import entity.Property.Property;
import entity.User.User;

public class GetFavourites extends BaseTask {
    private final API.IGetRealTimeObject callback;

    public GetFavourites(API api, API.IGetRealTimeObject callback) {
        super(null);
        this.callback = callback;
        User user = User.getUserFromSession(getContext());
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("member_number", user.getMember_id());
        params.put("session_id", user.getSession_id());
        createParams("member_favorite?api_key=" + API.API_KEY, params);
    }

    @Override
    protected void onResponse(Object response) {
        JSONObject json = (JSONObject) response;
        if (json.containsKey("favorites")) {
            List<Property> favorites = Property.favouritesFactoryList((JSONArray) json.get("favorites"));
            callback.getData(favorites);
        }
    }

    protected void onError(Object response) {
        callback.onError("");
    }
}
