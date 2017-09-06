package api;

import android.widget.Toast;

import org.json.simple.JSONObject;

import java.util.HashMap;

import entity.User.User;
import entity.User.UserMapper;
import entity.User.UserProfileMapper;
import helper.IGetRequest;

public class Login extends BaseTask {

	public Login(API api, String login, String password, IGetRequest callback) {
		super(callback);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("login", login);
		params.put("password", password);
        params.put("type", "");

        createParams("member_signin?api_key="+API.API_KEY, params);
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
                user.setJSON(responseObject);
                mapper.saveEntity(user);
                mapper.saveToSession(User.SESSION_NAME, user.getJSON());

                UserProfileMapper profileMapper = new UserProfileMapper(getContext());
                profileMapper.deleteAllEntities();

                onSuccess(parseMessage(responseObject,"success_message"));
            }else{
                Toast.makeText(getContext(), "Please, Try again.", Toast.LENGTH_LONG).show();
            }
        }
	}
}
