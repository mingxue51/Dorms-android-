package entity.Property;

import org.json.simple.JSONObject;

import java.io.Serializable;

import helper.H;

public class Ratings implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String BUNDLE = "ratings";

    public static final int DECIMAL_PLACES = 2;

    public JSONPropertyList jsonPropertyList;
    public Decorator decorator;

    private String rating;
    private int overall;
    private int atmosphere;
    private int staff;
    private int location;
    private int cleanliness;
    private int facilities;
    private int safety;
    private int fun;
    private int value;

    private boolean recommend;
    private boolean recommend_backpackers;
    private boolean recommend_adventure;
    private boolean recommend_sightseeing;
    private boolean recommend_under_35;
    private boolean recommend_35_55;
    private boolean recommend_over_55;
    private boolean recommend_fam_child;
    private boolean recommend_fam_teens;
    private boolean recommend_no_car;
    private boolean recommend_romantic;
    private boolean recommend_girls;
    private boolean recommend_invalidity;
    private boolean recommend_pets;


    public Ratings() {
        overall = 0;
        rating = "";
        jsonPropertyList = new JSONPropertyList();
        decorator = new Decorator();
    }

    public String getRating() {
        return rating;
    }



    public Ratings setRating(String rating) {
        this.rating = rating;
        return this;
    }

    public int getOverall() {
        return overall;
    }

    public void setOverall(int overall) {
        this.overall = overall;
    }

    public int getAtmosphere() {
        return atmosphere;
    }

    public void setAtmosphere(int atmosphere) {
        this.atmosphere = atmosphere;
    }

    public int getStaff() {
        return staff;
    }

    public void setStaff(int staff) {
        this.staff = staff;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getCleanliness() {
        return cleanliness;
    }

    public void setCleanliness(int cleanliness) {
        this.cleanliness = cleanliness;
    }

    public int getFacilities() {
        return facilities;
    }

    public void setFacilities(int facilities) {
        this.facilities = facilities;
    }

    public int getSafety() {
        return safety;
    }

    public void setSafety(int safety) {
        this.safety = safety;
    }

    public int getFun() {
        return fun;
    }

    public void setFun(int fun) {
        this.fun = fun;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public boolean isRecommend_backpackers() {
        return recommend_backpackers;
    }

    public void setRecommend_backpackers(boolean recommend_backpackers) {
        this.recommend_backpackers = recommend_backpackers;
    }

    public boolean isRecommend_adventure() {
        return recommend_adventure;
    }

    public void setRecommend_adventure(boolean recommend_adventure) {
        this.recommend_adventure = recommend_adventure;
    }

    public boolean isRecommend_sightseeing() {
        return recommend_sightseeing;
    }

    public void setRecommend_sightseeing(boolean recommend_sightseeing) {
        this.recommend_sightseeing = recommend_sightseeing;
    }

    public boolean isRecommend_under_35() {
        return recommend_under_35;
    }

    public void setRecommend_under_35(boolean recommend_under_35) {
        this.recommend_under_35 = recommend_under_35;
    }

    public boolean isRecommend_35_55() {
        return recommend_35_55;
    }

    public void setRecommend_35_55(boolean recommend_35_55) {
        this.recommend_35_55 = recommend_35_55;
    }

    public boolean isRecommend_over_55() {
        return recommend_over_55;
    }

    public void setRecommend_over_55(boolean recommend_over_55) {
        this.recommend_over_55 = recommend_over_55;
    }

    public boolean isRecommend_fam_child() {
        return recommend_fam_child;
    }

    public void setRecommend_fam_child(boolean recommend_fam_child) {
        this.recommend_fam_child = recommend_fam_child;
    }

    public boolean isRecommend_fam_teens() {
        return recommend_fam_teens;
    }

    public void setRecommend_fam_teens(boolean recommend_fam_teens) {
        this.recommend_fam_teens = recommend_fam_teens;
    }

    public boolean isRecommend_no_car() {
        return recommend_no_car;
    }

    public void setRecommend_no_car(boolean recommend_no_car) {
        this.recommend_no_car = recommend_no_car;
    }

    public boolean isRecommend_romantic() {
        return recommend_romantic;
    }

    public void setRecommend_romantic(boolean recommend_romantic) {
        this.recommend_romantic = recommend_romantic;
    }

    public boolean isRecommend_girls() {
        return recommend_girls;
    }

    public void setRecommend_girls(boolean recommend_girls) {
        this.recommend_girls = recommend_girls;
    }

    public boolean isRecommend_invalidity() {
        return recommend_invalidity;
    }

    public void setRecommend_invalidity(boolean recommend_invalidity) {
        this.recommend_invalidity = recommend_invalidity;
    }

    public boolean isRecommend_pets() {
        return recommend_pets;
    }

    public void setRecommend_pets(boolean recommend_pets) {
        this.recommend_pets = recommend_pets;
    }

    public Ratings setJSON(JSONObject obj) {
        setAtmosphere(H.toInt(obj, "user_atmosphere_rating"));
        setStaff(H.toInt(obj, "user_staff_rating"));
        setLocation(H.toInt(obj, "user_location_rating"));
        setCleanliness(H.toInt(obj, "user_cleanliness_rating"));
        setFacilities(H.toInt(obj, "user_facilities_rating"));
        setSafety(H.toInt(obj, "user_safety_rating"));
        setValue(H.toInt(obj, "user_value_rating"));
        return this;
    }

    public class JSONPropertyList implements Serializable {
        private static final long serialVersionUID = 1L;

        public Ratings setJSON(JSONObject obj) {
            setAtmosphere(H.toInt(obj, "atmosphere"));
            setStaff(H.toInt(obj, "staff"));
            setLocation(H.toInt(obj, "location"));
            setCleanliness(H.toInt(obj, "cleanliness"));
            setFacilities(H.toInt(obj, "facilities"));
            setSafety(H.toInt(obj, "safety"));
            setValue(H.toInt(obj, "value"));
            return Ratings.this;
        }
    }

    public class Decorator implements Serializable {
        private static final long serialVersionUID = 1L;

        public String getOverallString() {
            String result = "";
            if (overall != 0) result = String.valueOf(overall);
            return result;
        }
        public String getOverallStringPercent() {
            String result = "";
            if (!getOverallString().isEmpty()) result = getOverallString()+"%";
            return  result;
        }

        public String getRatingValuePercent(Integer value) {
            String result = "";
            if (!value.equals(0)) result = value+"%";
            return  result;
        }

    }

}
