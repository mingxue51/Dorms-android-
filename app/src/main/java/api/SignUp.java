package api;

import org.json.simple.JSONObject;
import java.util.HashMap;

import helper.IGetRequest;

public class SignUp extends  BaseTask {
    public SignUp(API api, String firstName, String lastName,
                  String email, String password, IGetRequest callback) {
        super(null);

        HashMap<String, String> params = new HashMap<>();
        params.put("first_name", firstName);
        params.put("last_name", lastName);
        params.put("email", email);
        params.put("password", password);
        params.put("mail_subscription", "FALSE");

        createParams("member_signup?api_key=" + API.API_KEY, params);
    }

    @Override
    protected void onResponse(Object response) {
        JSONObject json = (JSONObject) response;
        if (json.containsKey("success_status")) {
            JSONObject responseObject = (JSONObject) json.get("success");
            boolean status = Boolean.parseBoolean(responseObject.get("success_status").toString());
            if (status) {
                onSuccess(parseMessage(responseObject, "success_message"));
            }
        }
    }
}

