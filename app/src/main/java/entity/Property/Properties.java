package entity.Property;

import android.support.annotation.Nullable;
import android.text.Html;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import entity.Generic.Districts.Districts;
import entity.Generic.GeoLocation;
import entity.Map.Landmark;
import entity.Map.Location.Distance.Distance;
import entity.Map.Location.Distance.DistanceHelper;

public class Properties implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String BUNDLE = "properties";

    private List<Property> mProperties;

    private Districts districts;
    private List<Landmark> landmarks;
    private CityInfo cityInfo;

    private List<Amenity> amenities;

    private PropertyCategories categories;

    public Properties() {
        mProperties = new ArrayList<>();
        cityInfo = new CityInfo();
        districts = new Districts();
        landmarks = new ArrayList<>();

        categories = new PropertyCategories();
    }

    public List<Property> getProperties() {
        return mProperties;
    }

    public void setProperties(List<Property> mProperties) {
        this.mProperties = mProperties;
    }

    public CityInfo getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(CityInfo cityInfo) {
        this.cityInfo = cityInfo;
    }

    public Districts getDistricts() {
        return districts;
    }

    public void setDistricts(Districts districts) {
        this.districts = districts;
    }

    public void setLandmarks(List<Landmark> landmarks) {
        this.landmarks = landmarks;
    }

    public PropertyCategories getCategories() {
        return categories;
    }

    public void setCategories(PropertyCategories categories) {
        this.categories = categories;
    }

    public String getCurrency() {
        String result = null;
        if (getProperties().size() > 0) {
            result = String.valueOf(Html.fromHtml(getProperties().get(0).getPropertyPrice().getDisplayPrice().getCurrency()));
        }
        return result;
    }

    public void calculateDistance(GeoLocation from) {

        for (int i = 0; i < getProperties().size(); i++) {
            Property lProperty = getProperties().get(i);
            if (from.getLatitude() == 0 || from.getLontitude() == 0) {
                lProperty.setDistanceToCurrentLocation(new Distance());
            } else {
                lProperty.setDistanceToCurrentLocation(DistanceHelper.getDistance(from, lProperty.getGeoLocation()));
            }
        }
    }

    @Nullable
    public GeoLocation getCityCenter() {
        for (Landmark landmark : landmarks) {
            if (landmark.isCityCenter()) {
                return landmark.getGeoLocation();
            }
        }

        return null;
    }

    public boolean isEmptyResults() {
        if (mProperties == null) {
            return false;
        }
        return mProperties.isEmpty();
    }

    public List<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<Amenity> amenities) {
        this.amenities = amenities;
    }
}
