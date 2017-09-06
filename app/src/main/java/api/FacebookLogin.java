package api;

import android.content.res.Resources;
import android.widget.Toast;

import com.mc.youthhostels.R;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

import entity.User.User;
import entity.User.UserProfileMapper;
import helper.App;
import helper.IGetRequest;

public class FacebookLogin extends BaseTask {
    private static final String URL = "member_signin?api_key="+API.API_KEY;

    public FacebookLogin(API api, String accessToken, IGetRequest callback) {
        super(callback);

        Map<String, String> params = new HashMap();
        final Resources resources = App.getInstance().getResources();

        params.put("access_token", accessToken);
        params.put("app_android_id", resources.getString(R.string.facebook_app_id));
        params.put("app_android_secret", resources.getString(R.string.facebook_secret));
        params.put("type", "facebook");

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
