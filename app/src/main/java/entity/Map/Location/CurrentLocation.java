package entity.Map.Location;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import entity.Generic.GeoLocation;
import entity.Map.Location.Distance.Distance;
import helper.H;

public class CurrentLocation implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer cityId;
    private String country;
    private String city;
    private String countryTranslated;
    private String cityTranslated;
    private Distance distance;

    private String verticalScreenImage;

    public CurrentLocation()
    {
        distance = new Distance();
    }

    public Integer getCityId() {
        return cityId;
    }

    public CurrentLocation setCityId(Integer cityId) {
        this.cityId = cityId;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public CurrentLocation setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getCity() {
        return city;
    }

    public CurrentLocation setCity(String city) {
        this.city = city;
        return this;
    }

    public String getCountryTranslated() {
        return countryTranslated;
    }

    public CurrentLocation setCountryTranslated(String country_translated) {
        this.countryTranslated = country_translated;
        return this;
    }

    public String getCityTranslated() {
        return cityTranslated;
    }

    public CurrentLocation setCityTranslated(String city_translated) {
        this.cityTranslated = city_translated;
        return this;
    }

    public Distance getDistance() {
        return distance;
    }

    public CurrentLocation setDistance(Distance distance) {
        this.distance = distance;
        return this;
    }

    public CurrentLocation fromJSON(JSONObject obj) {
        setCityId(H.toInt(obj, "city_id"));
        setCountry(H.toString(obj, "country"));
        setCity(H.toString(obj, "city"));
        setCountryTranslated(H.toString(obj, "country_translated"));
        setCityTranslated(H.toString(obj, "city_translated"));

        try {
            JSONObject multimedia = (JSONObject) obj.get("multimedia");
            setVerticalScreenImage(multimedia.get("vertical_screen_image").toString());
        } catch (Exception e) {
            // ignore this
        }

        return this;
    }

    public static List<CurrentLocation> factoryList(Object json) {
        List<CurrentLocation> result = new ArrayList<CurrentLocation>();
        try {
            JSONArray array = (JSONArray) json;
            for (Object key : array) {
                JSONObject obj = (JSONObject) key;
                CurrentLocation entity = new CurrentLocation();
                entity.fromJSON(obj);
                result.add(entity);
            }
        } catch (Exception e) {
            H.logE("Can't create Property list from JSON Array.");
            H.logE(e.getMessage());
        }
        return result;
    }

    public void setVerticalScreenImage(String verticalScreenImage) {
        this.verticalScreenImage = verticalScreenImage;
    }

    public String getVerticalScreenImage() {
        return verticalScreenImage;
    }
}
