package api;

import org.json.simple.JSONObject;
import java.util.HashMap;
import helper.IGetRequest;

public class ForgetPassword extends BaseTask {

    public ForgetPassword(API api, String email, IGetRequest callback) {
        super(callback);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        createParams("forget_password?api_key="+API.API_KEY, params);
    }

    @Override
    protected void onResponse(Object response) {
        JSONObject json = (JSONObject)response;
        if (json.containsKey("success")) {
            JSONObject responseObject = (JSONObject) json.get("success");
            boolean status = Boolean.parseBoolean(responseObject.get("success_status").toString());
            if (status) {
                onSuccess(parseMessage(responseObject, "success_message"));
            }
        }
    }
}
