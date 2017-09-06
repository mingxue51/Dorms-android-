package api;

import com.mc.youthhostels.entity.UserReview;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.List;

import entity.User.User;

public class GetUserReviews extends BaseTask {
    private API.IGetRealTimeObject callback;
    public GetUserReviews(API api,API.IGetRealTimeObject callback) {
        super(null);

        this.callback = callback;
        User user = User.getUserFromSession(getContext());
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("member_number", user.getMember_id());
        params.put("session_id", user.getSession_id());
        createParams("member_reviews?api_key=" + API.API_KEY, params);
    }

    @Override
    protected void onResponse(Object response) {
        JSONObject json = (JSONObject) response;
        if (json.containsKey("reviews")) {
            List<UserReview> item = UserReview.factoryList(json.get("reviews"));
            callback.getData(item);
        }
    }

    protected void onError(Object response) {
        if (callback != null && response != null) {
            JSONObject result = ((JSONObject) response);
            if(result.containsKey("error")) {
                JSONObject responseObject = (JSONObject) result.get("error");
                callback.onError(parseMessage(responseObject,"error_message"));
            }
        }
    }
}
