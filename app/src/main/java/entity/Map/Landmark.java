package entity.Map;


import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mc.youthhostels.map.Location;
import com.mc.youthhostels.map.LocationType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import entity.Generic.GeoLocation;
import entity.Map.Marker.MarkerHelper;
import helper.H;

public class Landmark implements Location, Serializable{
    private static final long serialVersionUID = 1L;
    private static final String CITY_CENTER_TYPE =  "city_center";
    private static final String TRAIN_STATION_TYPE =  "train_station";

    private Integer id;
    private String name;
    private String type;
    private GeoLocation geoLocation;


    public Landmark(){
        name ="";
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    public boolean isCityCenter(){
        return getType().equalsIgnoreCase(CITY_CENTER_TYPE);
    }

    public boolean isTrainStation(){
        return getType().equalsIgnoreCase(TRAIN_STATION_TYPE);
    }

    public Landmark fromJSON(JSONObject obj) {
        setId(H.toInt(obj, "landmark_id"));
        setName(H.toString(obj, "landmark_name"));
        setType(H.toString(obj, "type"));
        setGeoLocation(new GeoLocation().landmark.setJSON(obj));
        return Landmark.this;
    }

    public static List<Landmark> factoryList(Object json) {
        ArrayList<Landmark> result = new ArrayList<>();
        try {
            JSONArray array = (JSONArray) json;
            for (Object key : array) {
                JSONObject obj = (JSONObject) key;
                Landmark entity = new Landmark();
                entity.fromJSON(obj);
                result.add(entity);
            }
        }catch (Exception e){

        }
        return result;
    }

    @Override
    public LatLng getLocation() {
        return getGeoLocation().toLatLng();
    }

    @Override
    public String getLocationId() {
        return String.valueOf(id);
    }

    @Override
    public LocationType getLocationType() {
        return LocationType.LANDMARK;
    }

    @Override
    public String getLocationName() {
        return name;
    }

    @Override
    public MarkerOptions getMarkerOptions() {
        return MarkerHelper.getCityCentreOptions(getLocation());
    }
}