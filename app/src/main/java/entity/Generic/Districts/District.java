package entity.Generic.Districts;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mc.youthhostels.map.Location;
import com.mc.youthhostels.map.LocationType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import entity.Map.Marker.MarkerHelper;
import helper.H;

public class District implements Location, Serializable {
    private static final long serialVersionUID = 1L;


    private Decorator decorator;
    public JSON json;

    private Integer district_id;
    private String name;
    private String umId;
    private String original_name;
    private String slug;
    private Integer districtCount;
    private List<LatLng> cords = new ArrayList<>();

    public District() {
        district_id=0;
        name ="";
        umId ="";
        original_name="";

        slug="";
        districtCount=0;

        decorator = new Decorator();
        json = new JSONPropertyDetails();

    }

    public Decorator getDecorator() {
        return decorator;
    }

    public Integer getDistrict_id() {
        return district_id;
    }

    public District setDistrict_id(Integer district_id) {
        this.district_id = district_id;
        return this;
    }

    public String getName() {
        return name;
    }

    public District setName(String name) {
        this.name = name;
        return this;
    }

    public String getUmId() {
        return umId;
    }

    public void setUmId(String umId) {
        this.umId = umId;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public District setOriginal_name(String original_name) {
        this.original_name = original_name;
        return this;
    }

    public String getSlug() {
        return slug;
    }

    public District setSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public Integer getDistrictCount() {
        return districtCount;
    }

    public District setDistrictCount(Integer districtCount) {
        this.districtCount = districtCount;
        return this;
    }

    public class Decorator implements  Serializable{
        private static final long serialVersionUID = 1L;
/*        protected String getFormattedValue()
        {
            return value.toString() + " " + units;
        }*/
    }
    public interface JSON{
        public District fromJSON(JSONObject obj);
    }

    public class JSONPropertyDetails implements JSON, Serializable{
        private static final long serialVersionUID = 1L;
        @Override
        public District fromJSON(JSONObject obj) {
            setDistrict_id(H.toInt(obj,"district_id"));
            setName(H.toString(obj, "district_name"));
            setUmId(H.toString(obj, "um_id"));
            setOriginal_name(H.toString(obj, "original_name"));
            setSlug(H.toString(obj, "slug"));
            setDistrictCount(H.toInt(obj, "district_count"));
            return null;
        }
    }

    public static List<District> factoryList(Object json){
        List<District> result = new ArrayList<District>();
        try {
            JSONArray array = (JSONArray)json;
            for (Object key : array) {
                JSONObject obj = (JSONObject)key;
                District entity = new District();
                entity.json.fromJSON(obj);
                result.add(entity);
            }
        } catch(Exception e) {
            H.logE("Can't create District from JSON Array. " + e.toString());
        }
        return result;
    }

    @Override
    public LatLng getLocation() {
        return getCentroidLocation();
    }

    @Override
    public String getLocationId() {
        return String.valueOf(getDistrict_id());
    }

    @Override
    public LocationType getLocationType() {
        return LocationType.DISTRICT;
    }

    @Override
    public String getLocationName() {
        return "District:" + name;
    }

    @Override
    public MarkerOptions getMarkerOptions() {
        return MarkerHelper.getCityCentreOptions(getLocation());
    }

    public List<LatLng> getCords() {
        return cords;
    }

    public void setCords(List<LatLng> cords) {
        this.cords = cords;
    }

    private LatLng getCentroidLocation() {
        double averageLat = 0;
        double averageLng = 0;

        if (cords != null && cords.size() > 0) {
            for (LatLng latLng : cords) {
                averageLat += latLng.latitude;
                averageLng += latLng.longitude;
            }
            averageLat = averageLat / cords.size();
            averageLng = averageLng / cords.size();
        }

        H.logD("centroid location is : " + averageLat + " " + averageLng);

        return new LatLng(averageLat, averageLng);
    }
}
