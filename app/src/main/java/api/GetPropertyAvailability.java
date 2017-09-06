package api;

import com.mc.youthhostels.entity.Order;

import org.json.simple.JSONObject;

import java.util.HashMap;

import entity.Generic.Currency;
import entity.Property.Search.SearchProperty;
import helper.H;
import helper.IGetRequest;

public class GetPropertyAvailability extends BaseTask {
    private API.IGetRealTimeObject mCallback;
    public GetPropertyAvailability(API api, SearchProperty searchProperty, API.IGetRealTimeObject cbGetData) {

        super(new IGetRequest() {
            @Override
            public void onSuccess(String message) {}

            @Override
            public void onError(String message) {}
        });

        mCallback = cbGetData;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("property_number", String.valueOf(searchProperty.getPropertyId()));
        params.put("date_start", searchProperty.getArrivalDateForAPI());
        params.put("num_nights", String.valueOf(searchProperty.getNumNightsP()));
        params.put("currency", Currency.getCurrency().getCode());
        createParams("property_availability?api_key=" + API.API_KEY, params);
    }

    @Override
    protected void onResponse(Object response) {
        JSONObject json = (JSONObject) response;
        if (json.containsKey("rooms") && json.size() > 0) {
            Order order = new Order();
            order.parseRooms(json.get("rooms"));

            if (json.containsKey("property_cards")) {
                order.parseCardTypes(json.get("property_cards"));
            }

            if (order.getCardTypesAvailable() == null || order.getCardTypesAvailable().isEmpty()) {
                throw new RuntimeException("available card types not found");
            }

            for (String str : order.getCardTypesAvailable()) {
                H.logD(str);
            }

            H.runOnUi(new Runnable() {
                @Override
                public void run() {
                    mCallback.getData(order);
                }
            });
        }
    }

    @Override
    protected void onError(Object response) {
        mCallback.onError("");
    }
}
