package api;

import org.json.simple.JSONObject;

import java.util.HashMap;

import entity.Generic.Content;
import helper.IGetRequest;

public class GetContent extends BaseTask {
    private API.IGetRealTimeObject mCallback;


    public GetContent(API api, String query, API.IGetRealTimeObject cbGetData) {

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
        params.put("category", query);
        createParams("content?api_key=" + API.API_KEY, params);
    }

    @Override
    protected void onResponse(Object response) {
        JSONObject json = (JSONObject) response;
        Content content = Content.fromJSON(json);
        mCallback.getData(content);
    }
}
