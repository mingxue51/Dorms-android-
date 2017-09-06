package api;

import com.mc.youthhostels.entity.Order;

import org.json.simple.JSONObject;

import java.util.HashMap;

import entity.Generic.Currency;
import helper.DT;
import helper.IGetRequest;

public class BookProperty extends BaseTask {
    private API.IGetRealTimeObject mCallback;

    public BookProperty(API api, Order order, API.IGetRealTimeObject cbGetData) {
        super(null);
        mCallback = cbGetData;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("customer_amount", String.valueOf(order.getPayNowPrice()));
        params.put("amount", "" + String.valueOf(order.getTotalPrice()));
        params.put("currency", Currency.getCurrency().getCode());
        params.put("ccexpiry_month", order.getMonth());
        params.put("ccexpiry_year", order.getYear());
        params.put("ccname", order.getCardHolderNamee());
        params.put("ccnumber", order.getCreditCardNumber());
        params.put("cctype", order.getCreditCardType());
        params.put("ccvalidfrom_year", "");
        params.put("cvv", order.getCcv());
        params.put("issueno", "");
        params.put("firstname", order.getFirstName());
        params.put("lastname", order.getLastName());
        params.put("nationality", order.getNationality());
        params.put("phone_number", order.getPhoneNumber());
        params.put("email_address", order.getEmail());
        params.put("mail_subscribe", "true");
        params.put("female_count", String.valueOf(order.getTotalGuests()));
        params.put("male_count", "0");
        params.put("persons", order.getPersons());
        params.put("nights", String.valueOf(order.getNightsCount()));
        params.put("date_start", DT.getDateForAPI(order.getArrivalDate()));
        params.put("arrival_time", order.getCheckInTime());
        params.put("card_type", "American Express,JCB,Mastercard,Visa");
        params.put("property_name", order.getPropertyName());
        params.put("property_number", String.valueOf(order.getPropertyNumber()));
        params.put("rooms", order.getRoomsNumbers());
        params.put("settle_currency", "GBP");
        params.put("sign_me_up", "0");
        params.put("sms", "none");
        params.put("refresh", "false");
        createParams("property_booking?api_key=" + API.API_KEY, params);
    }

    @Override
    protected void onResponse(Object response) {
        JSONObject json = (JSONObject)response;
        if (json.containsKey("success")) {
            JSONObject responseObject = (JSONObject)json.get("success");
            boolean status = Boolean.parseBoolean(responseObject.get("success_status").toString());
            if(status){
                mCallback.getData(null);
            }
        }
    }
}

