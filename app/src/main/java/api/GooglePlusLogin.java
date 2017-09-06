package api;

import android.widget.Toast;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

import entity.User.User;
import entity.User.UserProfileMapper;
import helper.IGetRequest;

/**
 * Created by dpr on 16/07/15.
 */
public class GooglePlusLogin extends BaseTask {
    private static final String URL = "member_signin?api_key="+API.API_KEY;

    public GooglePlusLogin(API api, String accessToken, IGetRequest callback) {
        super(callback);

        Map<String, String> params = new HashMap();

        params.put("access_token", accessToken);
        params.put("type", "google");

        createParams(URL, params);
    }

    @Override
    protected void onResponse(Object response) {
        JSONObject json = (JSONObject)response;
        if (json.containsKey("success")) {
            JSONObject responseObject = (JSONObject)json.get("success");
            boolean status = Boolean.parseBoolean(responseObject.get("success_status").toString());
            if(status){
                UserProfileMapper mapper = new UserProfileMapper(getContext());

                User user = new User();
                user.setJSON(responseObject);
                mapper.saveToSession(User.SESSION_NAME, user.getJSON());

                onSuccess(parseMessage(responseObject,"success"));
            }else{
                Toast.makeText(getContext(), "Please, Try again.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
