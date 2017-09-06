package api;

import com.mc.youthhostels.entity.UserReview;

import org.json.simple.JSONObject;

import java.util.HashMap;

public class PostPropertyReview extends BaseTask {
    private API.IGetRealTimeObject mCallback;

    public PostPropertyReview(API api, UserReview review, API.IGetRealTimeObject cbGetData) {
        super(null);
        mCallback = cbGetData;

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("booking_number", String.valueOf(review.getBookingReference()));

        params.put("short_comment", review.getTitle());
        params.put("comment", review.getReview());

        params.put("recommend","1");
        params.put("recommend_backpackers", "0");
        params.put("recommend_adventure", "0");
        params.put("recommend_sightseeing", "0");
        params.put("recommend_under_35","0");
        params.put("recommend_35_55","0");
        params.put("recommend_over_55", "0");
        params.put("recommend_fam_childs", "0");
        params.put("recommend_fam_teens", "0");
        params.put("recommend_no_car",  "0");
        params.put("recommend_romantic", "0");
        params.put("recommend_girls", "0");
        params.put("recommend_invalidity", "0");
        params.put("recommend_pets", "0");

        params.put("star-rating-atmosphere",String.valueOf(review.getRatingAtmosphere()));
        params.put("star-rating-staff", String.valueOf(review.getRatingStaff()));
        params.put("star-rating-location", String.valueOf(review.getRatingLocation()));
        params.put("star-rating-cleanliness",String.valueOf(review.getRatingCleanliness()));
        params.put("star-rating-facilities", String.valueOf(review.getRatingFacilities()));
        params.put("star-rating-safety", String.valueOf(review.getRatingSafety()));
        params.put("star-rating-value", String.valueOf(review.getRatingValue()));

        createParams("submit_property_review?api_key=" + API.API_KEY, params);
    }

    @Override
    protected void onResponse(Object response) {
        JSONObject json = (JSONObject) response;
        if (json.containsKey("success")) {
            JSONObject responseObject = (JSONObject) json.get("success");
            if (Boolean.parseBoolean(responseObject.get("success_status").toString())) {
                mCallback.getData(parseMessage(responseObject, "success_message"));
            } else {
            }
        } else {
        }
    }
}

