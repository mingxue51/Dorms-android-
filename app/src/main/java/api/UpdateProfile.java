package api;

import android.widget.Toast;

import org.json.simple.JSONObject;

import java.util.HashMap;

import entity.User.User;
import entity.User.UserProfile;
import helper.IGetRequest;

public class UpdateProfile extends BaseTask {
	
	public UpdateProfile(API api, UserProfile profile,IGetRequest callback) {
		super(callback);
        User user = User.getUserFromSession(getContext());
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("first_name", profile.getFirst_name());
        params.put("last_name", profile.getLast_name());
        params.put("phone_number", String.valueOf(profile.getPhone_number()));
		params.put("home_country", profile.getHome_country());
        params.put("favorite_currency", String.valueOf(profile.getFavorite_currency()));
        params.put("gender_id", String.valueOf(profile.getGender_id()));
        params.put("website", profile.getWebsite());
        params.put("mail_subscription", (String.valueOf(profile.getMail_subscription())).toUpperCase());
        params.put("favorite_lang_id", String.valueOf(profile.getFavorite_lang_id()));
        params.put("member_number", user.getMember_id());
        params.put("session_id", user.getSession_id());
        createParams("member_update_profile?api_key="+API.API_KEY, params);
	}

	@Override
	protected void onResponse(Object response) {
		JSONObject json = (JSONObject)response;
		if (json.containsKey("success")) {
            JSONObject responseObject = (JSONObject)json.get("success");
            boolean status = Boolean.parseBoolean(responseObject.get("success_status").toString());
            if(status){
                onSuccess(parseMessage(responseObject,"success_message"));
            }else{
                Toast.makeText(getContext(), "Please, Try again.", Toast.LENGTH_LONG).show();
            }
		}
	}

}
