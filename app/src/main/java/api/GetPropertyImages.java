package api;

import org.json.simple.JSONObject;

import java.util.HashMap;

import entity.Image.ImageUrl;
import entity.Image.Images;
import entity.Property.Property;
import helper.IGetRequest;

public class GetPropertyImages extends BaseTask {
    private API.IGetRealTimeObject mCallback;

    public GetPropertyImages(API api, Property property, API.IGetRealTimeObject cbGetData) {

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
        params.put("property_number", property.getPropertyNumber());
        createParams("property_images?api_key=" + API.API_KEY, params);
    }

    @Override
    protected void onResponse(Object response) {
        JSONObject json = (JSONObject) response;
        Images images = new Images();
        if (json.containsKey("thumbnails") && json.size() > 0) {
            images.setThumbnail(ImageUrl.factoryList(json.get("thumbnails")));
        }
        if (json.containsKey("main_pics") && json.size() > 0) {
            images.setMainImages(ImageUrl.factoryList(json.get("main_pics")));
        }
        mCallback.getData(images);
    }
}
