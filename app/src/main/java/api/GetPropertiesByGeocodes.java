package api;

import com.mc.youthhostels.R;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import entity.Generic.Currency;
import entity.Generic.Districts.District;
import entity.Generic.Districts.Districts;
import entity.Map.Landmark;
import entity.Property.Amenity;
import entity.Property.CityInfo;
import entity.Property.Properties;
import entity.Property.Property;
import entity.Property.PropertyCategories;
import entity.Property.Search.SearchProperty;
import helper.H;
import helper.IGetRequest;

public class GetPropertiesByGeocodes extends BaseTask {
    private API.IGetRealTimeObject mCallback;
    public GetPropertiesByGeocodes(API api, SearchProperty query, API.IGetRealTimeObject cbGetData) {
        super(new IGetRequest() {
            @Override
            public void onSuccess(String message) {}
            @Override
            public void onError(String message) {
            }
        });
        mCallback = cbGetData;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("lat", String.valueOf(query.getGeoLocation().getLatitude()));
        params.put("lng", String.valueOf(query.getGeoLocation().getLontitude()));
        params.put("datestart", query.getArrivalDateForAPI());
        params.put("numnights", String.valueOf(query.getNumNightsP()));
        params.put("type", "geocode");

        String currencyCode = Currency.getCurrency().getCode();

        createParams("property_search2?api_key=" + API.API_KEY+"&"+"currency="+currencyCode, params);
    }

    @Override
    protected void onResponse(Object response) {
        JSONObject json = (JSONObject) response;
        if (json.containsKey("api_error_msg") && H.toBoolean(json, "propertyType")) {
            mCallback.onError(getContext().getString(R.string.no_properties_found));
            return;
        }
        if (json.containsKey("property_list")) {
            Properties property = new Properties();
            Districts districts = new Districts();
            List<Landmark> landmarks = new ArrayList<>();
            List<Amenity> amenities = new ArrayList<>();

            if (json.containsKey("city_info")) {
                CityInfo cityInfo = new CityInfo();
                cityInfo.getJsonPropertyList().fromJson((JSONObject) json.get("city_info"));
                property.setCityInfo(cityInfo);
            }

            if (json.containsKey("city_landmarks")) {
                landmarks = Landmark.factoryList(json.get("city_landmarks"));
                property.setLandmarks(landmarks);
            }

            if (json.containsKey("city_districts")) {
                districts.setDistricts(District.factoryList(json.get("city_districts")));
                property.setDistricts(districts);
            }

            if (json.containsKey("most_popular_amenities")) {
                List<Amenity> mostPopularAmenities = Amenity.factoryList(json.get("most_popular_amenities"));

                for (Amenity amenity: mostPopularAmenities) {
                    amenity.setMostPopular(true);
                }
                amenities.addAll(mostPopularAmenities);
            }

            if (json.containsKey("city_amenities")) {
                List<Amenity> cityAmenities = Amenity.factoryList(json.get("city_amenities"));
                amenities.addAll(cityAmenities);
            }

            property.setAmenities(amenities);

            PropertyCategories categories = new PropertyCategories();
            if (json.containsKey("property_category")) {
                categories.setCategories(PropertyCategories.factoryList(json.get("property_category")));
            }
            property.setCategories(categories);

            property.setProperties(Property.factoryList(json.get("property_list"), districts, landmarks, amenities));
            mCallback.getData(property);
        }
        //mCallback.onError(getContext().getString(R.string.no_properties_found));
    }


}
