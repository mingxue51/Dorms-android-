package entity.Property;

import org.json.simple.JSONObject;

import java.io.Serializable;

import entity.Generic.GeoLocation;
import helper.H;

public class CityInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String mCountry;
    private String mCity;
    private GeoLocation mGeoLocation;

    private String mSmallCityImage;

    private JSON jsonPropertyList;

    public CityInfo() {
        mCountry = "";
        mCity = "";
        mGeoLocation = new GeoLocation();
        jsonPropertyList = new JSONPropertyList();
    }

    public String getCountry() {
        return mCountry;
    }

    public void setSmallCityImage(String smallCityImage) {
        this.mSmallCityImage = smallCityImage;
    }

    public String getSmallCityImage() {
        return mSmallCityImage;
    }

    public void setCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String mCity) {
        this.mCity = mCity;
    }

    public GeoLocation getGeoLocation() {
        return mGeoLocation;
    }

    public void setGeoLocation(GeoLocation mGeoLocation) {
        this.mGeoLocation = mGeoLocation;
    }

    public JSON getJsonPropertyList() {
        return jsonPropertyList;
    }

    public void setJsonPropertyList(JSON jsonPropertyList) {
        this.jsonPropertyList = jsonPropertyList;
    }

    public interface JSON {
        void fromJson(JSONObject obj);
    }

    public class JSONPropertyList implements JSON, Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public void fromJson(JSONObject obj) {
            setCity(H.toString(obj, "display_city"));
            setCountry(H.toString(obj, "display_country"));

            setSmallCityImage(H.toString(obj, "small_city_image"));

            if (mSmallCityImage == null || mSmallCityImage.isEmpty()) {
                String cityS3Image = H.toString(obj, "city_s3_image");

                if (cityS3Image != null && !cityS3Image.isEmpty()) {
                    mSmallCityImage = "http://www.youth-hostels.co.uk/city_images_small/" + mCountry + "/" + cityS3Image;
                }
            }

            setGeoLocation(new GeoLocation(H.toDouble(obj, "city_geo_lat"), H.toDouble(obj, "city_geo_lng")));
        }
    }
}
