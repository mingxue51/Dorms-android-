package entity.Generic;

import com.google.android.gms.maps.model.LatLng;

import org.json.simple.JSONObject;

import java.io.Serializable;

import helper.H;

public class GeoLocation implements Serializable {
    private static final long serialVersionUID = 1L;

    public JSONAPILandmark landmark;

    private double latitude;
    private double longitude;


    public GeoLocation() {
        longitude=0;
        latitude=0;
        landmark = new JSONAPILandmark();
    }

    public GeoLocation(double pLongitude, double pLatitude) {
        latitude = pLatitude;
        longitude = pLongitude;
        landmark = new JSONAPILandmark();
    }

    public double getLontitude() {
        return longitude;
    }

    public void setLontitude(double lontitude) {
        this.longitude = lontitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


    public void fromJSONByProperty(JSONObject obj) {
        setLatitude(H.toDouble(obj, "LAT"));
        setLontitude(H.toDouble(obj, "LON"));
    }
    public void setJSON(JSONObject obj) {
        setLatitude(H.toDouble(obj, "Latitude"));
        setLontitude(H.toDouble(obj, "Longitude"));
    }

    public class JSONAPILandmark implements Serializable {
        private static final long serialVersionUID = 1L;

        public GeoLocation setJSON(JSONObject obj) {
            //GeoLocation entity = new GeoLocation();
            setLatitude(H.toDouble(obj, "geo_latitude"));
            setLontitude(H.toDouble(obj, "geo_longitude"));
            return GeoLocation.this;
        }

    }

    public class Fcd implements Serializable {
        private static final long serialVersionUID = 1L;

        public GeoLocation getCurrentLocation(){
            return new GeoLocation();
        }
    }

    public LatLng toLatLng() {
        return new LatLng(latitude, longitude);
    }
}

