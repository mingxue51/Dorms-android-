package api;

import android.widget.Toast;

import org.json.simple.JSONObject;

import java.util.HashMap;

import entity.Property.Property;
import entity.User.User;
import helper.IGetRequest;

public class DeleteFavorite extends BaseTask {
	public DeleteFavorite(API api, Property property, IGetRequest callback) {
		super(callback);
        User user = User.getUserFromSession(getContext());
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("member_number", user.getMember_id());
        params.put("session_id", user.getSession_id());
        params.put("favorite_id", String.valueOf(property.getFavouriteId()));
        createParams("remove_favorite_property?api_key="+API.API_KEY, params);
	}

	@Override
	protected void onResponse(Object response) {
		JSONObject json = (JSONObject)response;
        if (json.containsKey("success")) {
            JSONObject responseObject = (JSONObject)json.get("success");
            if(Boolean.parseBoolean(responseObject.get("success_status").toString())){
                onSuccess(parseMessage(responseObject,"success_message"));
            }else{
                Toast.makeText(getContext(), "Please, Try again.", Toast.LENGTH_LONG).show();
            }
        }
	}
}
