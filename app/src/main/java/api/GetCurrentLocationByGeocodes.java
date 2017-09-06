package api;

import com.google.android.gms.maps.model.LatLng;
import com.mc.youthhostels.R;

import org.json.simple.JSONObject;

import java.util.HashMap;

import entity.Map.Location.CurrentLocation;
import entity.Map.Location.CurrentLocations;
import helper.IGetRequest;

public class GetCurrentLocationByGeocodes extends BaseTask {
    private API.IGetRealTimeObject mCallback;
    public GetCurrentLocationByGeocodes(API api, LatLng latLng, API.IGetRealTimeObject cbGetData) {
        super(new IGetRequest() {
            @Override
            public void onSuccess(String message) {}
            @Override
            public void onError(String message) {

            }
        });
        mCallback = cbGetData;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("lat", String.valueOf(latLng.latitude));
        params.put("lng", String.valueOf(latLng.longitude));
        params.put("num", "1");
        createParams("location_address?api_key=" + API.API_KEY, params);
    }

    @Override
    protected void onResponse(Object response) {
        JSONObject json = (JSONObject) response;
        if(json.containsKey("error_status")){
            mCallback.onError(getContext().getString(R.string.unavailable_location));
            return;
        }

        CurrentLocations currentLocations = new CurrentLocations();
        if (json.containsKey("address") ) {
            currentLocations.setCurrentLocations(CurrentLocation.factoryList(json.get("address")));
            if (currentLocations.getCurrentLocations().size()>0) {
                mCallback.getData(currentLocations);
            }else{
                mCallback.onError(getContext().getString(R.string.unavailable_location));
            }
        }else {
            mCallback.onError(getContext().getString(R.string.unavailable_location));
            return;
        }
    }
}
