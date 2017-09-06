package entity.Property;

import android.text.TextUtils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import helper.H;

public class Amenity implements Serializable{
    private static final long serialVersionUID = 1L;

    private String id;
    private String facilityId;
    private String type;
    private String name;
    private boolean isFree;

    public boolean isMostPopular() {
        return mostPopular;
    }

    public void setMostPopular(boolean mostPopular) {
        this.mostPopular = mostPopular;
    }

    private boolean mostPopular;

    public Amenity setFacilityId(String facilityId) {
        this.facilityId = facilityId;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void fromJSON(JSONObject obj) {
        id = H.toString(obj, "amenity_id");
        setFacilityId(H.toString(obj, "facility_id"));
        setType(H.toString(obj, "type"));
        name = H.toString(obj, "facility_name");
        isFree =  obj != null &&
                obj.containsKey("id_to_display") &&
                !TextUtils.isEmpty(H.toString(obj, "id_to_display"));
    }

    public static List<Amenity> factoryList(Object json) {
        List<Amenity> result = new ArrayList<>();
        try {
            if (json instanceof JSONArray) {
                JSONArray array = (JSONArray) json;
                for (Object key : array) {
                    JSONObject obj = (JSONObject) key;
                    Amenity entity = new Amenity();
                    entity.fromJSON(obj);
                    result.add(entity);
                }
            }

            if (json instanceof JSONObject) {
                JSONObject array = (JSONObject) json;
                Iterator<String> iter = array.keySet().iterator();
                while (iter.hasNext()) {
                    String key = iter.next();
                    JSONObject obj = (JSONObject) array.get(key);
                    Amenity entity = new Amenity();
                    entity.fromJSON(obj);
                    result.add(entity);
                }
            }
        } catch (Exception e) {
            H.logE("Can't parse amenities.");
            H.logE(e);
        }
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public boolean isFree() {
        return isFree;
    }
}
