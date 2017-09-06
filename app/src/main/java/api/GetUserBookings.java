package api;

import com.mc.youthhostels.entity.Booking;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.List;

import entity.User.User;
import helper.App;
import helper.IGetRequest;

public class GetUserBookings extends BaseTask {
    private API.IGetRealTimeObject mCallback;
    public GetUserBookings(API api,API.IGetRealTimeObject cbGetData) {

        super(new IGetRequest() {
            @Override
            public void onSuccess(String message) {}
            @Override
            public void onError(String message) {}
        });

        mCallback = cbGetData;
        User user = User.getUserFromSession(getContext());
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("member_number", user.getMember_id());
        params.put("session_id", user.getSession_id());
        createParams("member_bookings?api_key=" + API.API_KEY, params);
    }

    @Override
    protected void onResponse(Object response) {
        JSONObject json = (JSONObject) response;
        if (json.containsKey("bookings")) {
            User user = User.getUserFromSession(App.getInstance());
            user.saveBookings(json.get("bookings").toString());
            List<Booking> bookings = Booking.factoryList(json.get("bookings"));
            mCallback.getData(bookings);
        }
    }

    protected void onError(Object response) {
        if (mCallback != null && response != null) {
            JSONObject result = ((JSONObject) response);
            if(result.containsKey("error")) {
                JSONObject responseObject = (JSONObject) result.get("error");
                mCallback.onError(parseMessage(responseObject,"error_message"));//String message = responseObject.containsKey("error_message") ? responseObject.get("error_message").toString() : mApi.getContext().getString(R.string.error_request);
            }
        }
    }
}
