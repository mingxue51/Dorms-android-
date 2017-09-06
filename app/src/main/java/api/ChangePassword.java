package api;

import android.widget.Toast;

import org.json.simple.JSONObject;
import java.util.HashMap;

import entity.User.User;
import entity.User.UserMapper;
import helper.IGetRequest;

public class ChangePassword extends BaseTask {
	
	public ChangePassword(API api, String oldPassword, String newPassword, IGetRequest callback) {
		super(callback);
        User user = User.getUserFromSession(getContext());
		HashMap<String, String> params = new HashMap<String, String>();
        params.put("old_password", oldPassword);
        params.put("new_password", newPassword);
        params.put("member_number", user.getMember_id());
        params.put("session_id", user.getSession_id());
        createParams("member_update_password?api_key="+API.API_KEY, params);
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

                User user = new User();
                user.setJSON((JSONObject)json.get("user"));
                mapper.saveEntity(user);
                mapper.saveToSession(User.SESSION_NAME, user.getJSON());

                //onSuccess(null);
                onSuccess(parseMessage(responseObject, "success_message"));
            }else{
                Toast.makeText(getContext(), "Please, Try again.", Toast.LENGTH_LONG).show();
            }
		}
	}
}
