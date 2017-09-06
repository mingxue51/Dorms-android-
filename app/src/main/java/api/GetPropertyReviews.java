package api;

import android.widget.Toast;

import com.mc.youthhostels.model.PropertyReview;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.List;

import helper.IGetRequest;

public class GetPropertyReviews extends BaseTask {
    private API.IGetRealTimeObject mCallback;

    public GetPropertyReviews(API api, String propertyNumber, API.IGetRealTimeObject cbGetData) {

        super(new IGetRequest() {
            @Override
            public void onSuccess(String message) {
            }

            @Override
            public void onError(String message) {
            }
        });

        mCallback = cbGetData;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("property_number", propertyNumber);
        createParams("property_reviews?api_key=" + API.API_KEY, params);
    }

    @Override
    protected void onResponse(Object response) {
        JSONObject json = (JSONObject) response;
        if (json.containsKey("user_reviews")) {
            List<PropertyReview> reviews = PropertyReview.factoryList(json.get("user_reviews"));
            mCallback.getData(reviews);
        } else {
            Toast.makeText(getContext(), "Please, Try again.", Toast.LENGTH_LONG).show();
        }
    }
}
