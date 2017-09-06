package entity.Property.Search;

import android.widget.Spinner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import helper.H;


public class Suggestion {

    private int link_type;
    private String property_type;
    private String property_name;
    private String property_number;
    private String imageURL;
    private double prop_lng;
    private double prop_lat;
    private String hb_country;
    private String hb_city;
    private double city_lang;
    private double country_lang;
    private String continent_lang;
    private int district_id;
    private int landmark_id;
    private String district_name;
    private String landmark_name;
    private int country_id;
    private int city_id;
    private  int property_count;

    private Decorator decorator;


    public Suggestion() {
        decorator = new Decorator();
        property_count=0;
    }

    public int getLinkType() {
        return link_type;
    }

    public void setLinkType(int link_type) {
        this.link_type = link_type;
    }

    public String getProperty_type() {
        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public String getProperty_name() {
        return property_name;
    }

    public void setProperty_name(String property_name) {
        this.property_name = property_name;
    }

    public String getProperty_number() {
        return property_number;
    }

    public void setProperty_number(String property_number) {
        this.property_number = property_number;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public double getProp_lng() {
        return prop_lng;
    }

    public void setProp_lng(double prop_lng) {
        this.prop_lng = prop_lng;
    }

    public double getProp_lat() {
        return prop_lat;
    }

    public void setProp_lat(double prop_lat) {
        this.prop_lat = prop_lat;
    }

    public String getHb_country() {
        return hb_country;
    }

    public void setHb_country(String hb_country) {
        this.hb_country = hb_country;
    }

    public String getHb_city() {
        return hb_city;
    }

    public void setHb_city(String hb_city) {
        this.hb_city = hb_city;
    }

    public double getCity_lang() {
        return city_lang;
    }

    public void setCity_lang(double city_lang) {
        this.city_lang = city_lang;
    }

    public double getCountry_lang() {
        return country_lang;
    }

    public void setCountry_lang(double country_lang) {
        this.country_lang = country_lang;
    }

    public String getContinent_lang() {
        return continent_lang;
    }

    public void setContinent_lang(String continent_lang) {
        this.continent_lang = continent_lang;
    }

    public int getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }

    public int getLandmark_id() {
        return landmark_id;
    }

    public void setLandmark_id(int landmark_id) {
        this.landmark_id = landmark_id;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public String getLandmark_name() {
        return landmark_name;
    }

    public void setLandmark_name(String landmark_name) {
        this.landmark_name = landmark_name;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getProperty_count() {
        return property_count;
    }

    public void setProperty_count(int property_count) {
        this.property_count = property_count;
    }

    public void setJSON(JSONObject obj) {
        setLinkType(H.toInt(obj, "link_type"));
        setProperty_type(H.toString(obj, "property_type"));
        setProperty_name(H.toString(obj, "property_name"));
        setProperty_number(H.toString(obj, "property_number"));
        setImageURL(H.toString(obj, "imageURL"));
        setProp_lng(H.toInt(obj, "prop_lng"));
        setProp_lat(H.toInt(obj, "prop_lat"));
        setHb_country(H.toString(obj, "hb_country"));
        setHb_city(H.toString(obj, "hb_city"));
        setCity_lang(H.toInt(obj, "city_lang"));
        setCountry_lang(H.toInt(obj, "country_lang"));
        setContinent_lang(H.toString(obj, "continent_lang"));
        setDistrict_id(H.toInt(obj, "district_id"));
        setLandmark_id(H.toInt(obj, "landmark_id"));
        setDistrict_name(H.toString(obj, "district_name"));
        setLandmark_name(H.toString(obj, "landmark_name"));
        setCountry_id(H.toInt(obj, "country_id"));
        setCity_id(H.toInt(obj, "city_id"));
        setProperty_count(H.toInt(obj, "property_count"));
    }

    public static List<Suggestion> factoryList(Object json) {
        List<Suggestion> result = new ArrayList<>();

        if (json == null) {
            return result;
        }

        try {
            JSONArray array = (JSONArray) json;
            for (Object key : array) {
                JSONObject obj = (JSONObject) key;
                Suggestion entity = new Suggestion();
                entity.setJSON(obj);
                result.add(entity);
            }
        } catch (Exception e) {
            H.logE("Can't create Suggestions list from JSON Array.");
            H.logE(e.getMessage());
        }
        return result;
    }

    @Override
    public String toString() {
        return "CHANGEME";
    }

    public Decorator getDecorator() {
        return decorator;
    }

    public Suggestion setDecorator(Decorator decorator) {
        this.decorator = decorator;
        return this;
    }

    public class Decorator implements Serializable {
        private static final long serialVersionUID = 1L;

        protected int type;
        protected boolean separator;

        public int getType() {
            return type;
        }

        public Decorator setType(int type) {
            this.type = type;
            return this;
        }

        public boolean isSeparator() {
            return separator;
        }

        public Decorator setSeparator(boolean separator) {
            this.separator = separator;
            return this;
        }


        protected boolean isPropertyAPI(){
            if(!(getProperty_name().isEmpty()) && !(getProperty_type().isEmpty())){
                return true;
            }
            return false;
        }

        protected boolean isLandmarkAPI(){
            if((!getLandmark_name().isEmpty()) && getLandmark_id()>0){
                return true;
            }
            return false;
        }

        protected boolean isDistrictAPI(){
            if((!getDistrict_name().isEmpty()) && getDistrict_id()>0 ){
                return true;
            }
            return false;
        }




        public boolean isProperty(){
            if(getType()==Suggestions.PROPERTY){
                return true;
            }
            return false;
        }
        public boolean isCity(){
            if(getType()==Suggestions.CITY){
                return true;
            }
            return false;
        }
        public boolean isLandmark(){
            if(getType()==Suggestions.LANDMARK){
                return true;
            }
            return false;
        }
        public boolean isDistrict(){
            if(getType()==Suggestions.DISTRICT){
                return true;
            }
            return false;
        }

        public String getPropertyCityName() {
            StringBuilder s = new StringBuilder();

            if(!(getProperty_name().isEmpty())){
                s.append(getProperty_name());
            }

            if (!(getHb_city().isEmpty())) {
                s.append(", "+getHb_city());
            }

            if (!(getHb_country().isEmpty())) {
                s.append(", ");
            }
            return s.toString();
        }

        public String getDistrictName() {
            StringBuilder s = new StringBuilder();

            if(!(getDistrict_name().isEmpty())){
                s.append(getDistrict_name());
            }

            if (!(getHb_country().isEmpty())) {
                s.append(", ");
            }
            return s.toString();
        }


        public String getLandmarkName() {
            StringBuilder s = new StringBuilder();

            if(!(getLandmark_name().isEmpty())){
                s.append(getLandmark_name());
            }

            if (!(getHb_country().isEmpty())) {
                s.append(", ");
            }
            return s.toString();
        }

        public String getCountryName() {
            StringBuilder s = new StringBuilder();

            if(!(getHb_country().isEmpty())){
                s.append(getHb_country());
            }

                s.append(" ("+getProperty_count()+")");
            return s.toString();
        }

        public String getCityName() {
            String result = "";
            if (!(getHb_city().isEmpty())) {
                return getHb_city() + ", ";
            }
            return result;
        }

        /*public String getItemString() {
            String result = "";
            if(isProperty()){
                return getHb_city()+", "+getHb_country()+", "+getProperty_name();
            }
            if(isCity()){
                return getHb_city() + ", " + getHb_country()+"("+ getProperty_count()+")";
            }
            return result;
        }*/
    }
}
