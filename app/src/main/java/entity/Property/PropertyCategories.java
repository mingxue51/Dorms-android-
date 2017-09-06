package entity.Property;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import helper.H;

public class PropertyCategories implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, String> categories;


    public PropertyCategories() {

        categories = new HashMap<String, String>();
    }

    public Map<String, String> getCategories() {
        return categories;
    }

    public PropertyCategories setCategories(Map<String, String> categories) {
        this.categories = categories;
        return this;
    }


    public static  Map<String, String> factoryList(Object json) {
        Map<String, String> result = new HashMap<String, String>();
        try {
            if (json instanceof JSONArray) {
                JSONArray array = (JSONArray) json;
                for (Object key : array) {
                    JSONObject obj = (JSONObject) key;
                    result.put(H.toString(obj, "key"),H.toString(obj, "value"));
                }
            }
        } catch (Exception e) {
            H.logE("Can't create Property list from JSON Array.");
            H.logE(e.getMessage());
        }
        return result;
    }

    public String getTitleForFilterByKey(String key){
        try {
            String value = categories.get(key);
            if(!value.isEmpty()){
                return value;
            }
        }catch (Exception e){

        }
        return key;
    }
}
