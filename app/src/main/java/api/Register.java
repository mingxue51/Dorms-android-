package api;

import android.widget.Toast;

import org.json.simple.JSONObject;

import java.util.HashMap;

import entity.User.User;
import entity.User.UserMapper;
import entity.User.UserProfile;
import entity.User.UserProfileMapper;
import helper.IGetRequest;

public class Register extends BaseTask {
	
	public Register(API api, UserProfile profile, IGetRequest callback) {
		super(callback);
		HashMap<String, String> params = new HashMap<String, String>();
        params.put("first_name", profile.getFirst_name());
        params.put("last_name", profile.getLast_name());
        params.put("email", profile.getEmail());
		params.put("password", profile.getPassword());
        params.put("mail_subscription", (String.valueOf(profile.getMail_subscription())).toUpperCase());
        createParams("member_signup?api_key="+API.API_KEY, params);
	}

	@Override
	protected void onResponse(Object response) {
		JSONObject json = (JSONObject)response;
		if (json.containsKey("success")) {
            JSONObject responseObject = (JSONObject)json.get("success");
            boolean status = Boolean.parseBoolean(responseObject.get("success_status").toString());
            if(status){
                UserMapper mapper = new UserMapper(getContext());
                mapper.deleteAllEntities();
                UserProfileMapper profileMapper = new UserProfileMapper(getContext());
                profileMapper.deleteAllEntities();

                User user = new User();
                user.setJSON((JSONObject)json.get("user"));
                mapper.saveEntity(user);
                mapper.saveToSession(User.SESSION_NAME, user.getJSON());
                onSuccess(parseMessage(responseObject,"success_message"));
                //onSuccess(null);

            }else{
                Toast.makeText(getContext(), "Please, Try again.", Toast.LENGTH_LONG).show();
            }
		}
	}
}
