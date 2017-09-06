package api;

import android.text.TextUtils;

import org.json.simple.JSONObject;

import java.util.Date;
import java.util.HashMap;

import entity.Property.Property;
import entity.User.User;
import helper.DT;
import helper.H;

public class AddToFavourite extends BaseTask {
    private API.IGetRealTimeObject callback;
    public AddToFavourite(API api,
                          String favouriteId,
                          Date arrivalDate,
                          Date departureDate,
                          String propertyNumber,
                          String notes,
                          API.IGetRealTimeObject callback) {
        super(null);
        this.callback = callback;

        HashMap<String, String> params = new HashMap<String, String>();
        if (favouriteId != null) {
            params.put("favorite_id", favouriteId);
        }

        params.put("property_number", propertyNumber);
        params.put("departure_date", DT.getDateForAPI(departureDate));
        params.put("arrival_date", DT.getDateForAPI(arrivalDate));

        if (!TextUtils.isEmpty(notes)) {
            params.put("notes", notes);
        }

        User user = User.getUserFromSession(getContext());
        params.put("member_number", user.getMember_id());
        params.put("session_id", user.getSession_id());
        createParams("property_to_favorite?api_key=" + API.API_KEY, params);
    }

    @Override
    protected void onResponse(Object response) {
        JSONObject json = (JSONObject)response;
        if (json.containsKey("success")) {
            JSONObject responseObject = (JSONObject)json.get("success");
            if(Boolean.parseBoolean(responseObject.get("success_status").toString())){
                callback.getData(H.toString(responseObject, "favorite_id"));
            }
        }
    }
}

