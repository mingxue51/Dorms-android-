package entity.Map.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import entity.Generic.GeoLocation;

public class CurrentLocations implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<CurrentLocation> currentLocations;

    public CurrentLocations() {
        currentLocations = new ArrayList<>();
    }

    public List<CurrentLocation> getCurrentLocations() {
        return currentLocations;
    }

    public void setCurrentLocations(List<CurrentLocation> currentLocations) {
        this.currentLocations = currentLocations;
    }

    public String getCity(){
        if(isContainAny()){
            return  getCurrentLocations().get(0).getCityTranslated();
        } else {
            return "";
        }
    }

    public String getCountry(){
        if(isContainAny()){
            return  getCurrentLocations().get(0).getCountryTranslated();
        } else {
            return "";
        }
    }

    public String getCityImage() {
        if (isContainAny()) {
            return currentLocations.get(0).getVerticalScreenImage();
        } else {
            return "";
        }
    }

    public boolean isContainAny() {
        return currentLocations != null && currentLocations.size() > 0 && currentLocations.get(0) != null;
    }
}
