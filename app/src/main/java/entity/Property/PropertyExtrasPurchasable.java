package entity.Property;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import entity.Generic.PPrice;
import helper.H;

public class PropertyExtrasPurchasable implements Serializable {
    private static final long serialVersionUID = 1L;


    public JSON json;

    private String title;
    private PPrice price;

    public PropertyExtrasPurchasable() {
        json = new JSON();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PPrice getPrice() {
        return price;
    }

    public void setPrice(PPrice price) {
        this.price = price;
    }

    public class JSON implements Serializable {
        private static final long serialVersionUID = 1L;

        public List<PropertyExtrasPurchasable> fromJSON(JSONObject obj) {
            ArrayList<PropertyExtrasPurchasable> result = new ArrayList<PropertyExtrasPurchasable>();

            PropertyCheckInOut cancelPolicy = new PropertyCheckInOut();
            cancelPolicy.setStartsAt(H.toString(obj, "CHECKIN"));
            cancelPolicy.setEndsAt(H.toString(obj, "CHECKOUT"));
            return result;
        }

    }

    public static List<PropertyExtrasPurchasable> factoryList(Object json) {
        ArrayList<PropertyExtrasPurchasable> result = new ArrayList<PropertyExtrasPurchasable>();
        try {
            if (json instanceof JSONArray) {
                JSONArray array = (JSONArray) json;
                for (Object o : array) {
                    String key = (String) o;
                PropertyExtrasPurchasable entity = new PropertyExtrasPurchasable();
                entity.setTitle(key);
                result.add(entity);
                }
            }
            if (json instanceof JSONObject) {
                JSONObject array = (JSONObject) json;
                Iterator<String> iter = array.keySet().iterator();
                while (iter.hasNext()) {
                    String key = iter.next();
                    String value = (String) array.get(key);
                    PropertyExtrasPurchasable entity = new PropertyExtrasPurchasable();
                    entity.setTitle(key);
                    entity.setPrice(new PPrice(new BigDecimal(value)));
                    result.add(entity);
                }
            }
        } catch (Exception e) {
            H.logE("Can't create Property list from JSON Array.");
            H.logE(e.getMessage());
        }
        return result;
    }
}

