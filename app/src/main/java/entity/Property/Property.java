package entity.Property;


import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mc.youthhostels.map.Location;
import com.mc.youthhostels.map.LocationType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import entity.Generic.Currency;
import entity.Generic.Districts.District;
import entity.Generic.Districts.Districts;
import entity.Generic.GeoLocation;
import entity.Generic.Price;
import entity.Image.Images;
import entity.Map.Landmark;
import entity.Map.Location.Distance.Distance;
import entity.Map.Location.Distance.DistanceHelper;
import entity.Map.Marker.MarkerHelper;
import helper.App;
import helper.DT;
import helper.H;
import service.Localization;

public class Property implements Serializable, Location {
    private static final long serialVersionUID = 1L;
    public static final String BUNDLE = "property";

    private String propertyNumber;
    private String propertyName;
    private String description;
    private String importantInformation;
    private Images images;
    private String propertyType;
    private Address propertyAddress;
    private String favouriteId;
    private String favouriteNote;

    private GeoLocation geoLocation;

    private Ratings propertyRatings;
    private Prices propertyPrice;
    private Currency currency;

    private List<Amenity> amenities;

    private CancellationPolicy cancelPolicy;
    private PropertyCheckInOut checkInOut;
    private List<PropertyExtrasPurchasable> extrasPurchasable;

    private List<Landmark> landmarks;
    private List<Landmark> cityLandmarks = new ArrayList<>();
    private List<District> cityDistricts = new ArrayList<>();

    private String directions;
    private List<Facility> facilities;

    private String propertyUrl;
    private String mType;
    private String propertyTypeTranslate;

    private Distance mDistanceToCurrentLocation;
    private Districts mDistricts;

    private String propertyEmail;
    private String propertyTelephone;

    public Property() {
        propertyPrice = new Prices();
        images = new Images();
        propertyRatings = new Ratings();
        mDistanceToCurrentLocation = new Distance();
        facilities = new ArrayList<>();
        amenities = new ArrayList<>();
        mDistricts = new Districts();
        landmarks = new ArrayList<>();
        extrasPurchasable = new ArrayList<>();
        propertyTypeTranslate = "";
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPropertyName(String property_name) {
        this.propertyName = property_name;
    }

    public String getPropertyNumber() {
        return propertyNumber;
    }

    public void setPropertyNumber(String property_number) {
        this.propertyNumber = property_number;
    }

    public String getImportantInformation() {
        return importantInformation;
    }

    public void setImportantInformation(String importantInformation) {
        this.importantInformation = importantInformation;
    }

    public List<Landmark> getLandmarks() {
        return landmarks;
    }

    public void setLandmarks(List<Landmark> landmarks) {
        this.landmarks = landmarks;
    }

    public Ratings getPropertyRatings() {
        return propertyRatings;
    }

    public void setPropertyRatings(Ratings property_ratings) {
        this.propertyRatings = property_ratings;
    }

    public Address getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(Address address) {
        this.propertyAddress = address;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    public Prices getPropertyPrice() {
        return propertyPrice;
    }

    public void setPropertyPrice(Prices propertyPrice) {
        this.propertyPrice = propertyPrice;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getPropertyTypeTranslate() {
        return propertyTypeTranslate;
    }

    public Property setPropertyTypeTranslate(String propertyTypeTranslate) {
        this.propertyTypeTranslate = propertyTypeTranslate;
        return this;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public CancellationPolicy getCancelPolicy() {
        return cancelPolicy;
    }

    public void setCancelPolicy(CancellationPolicy cancelPolicy) {
        this.cancelPolicy = cancelPolicy;
    }

    public PropertyCheckInOut getCheckInOut() {
        return checkInOut;
    }

    public void setCheckInOut(PropertyCheckInOut checkInOut) {
        this.checkInOut = checkInOut;
    }

    public List<PropertyExtrasPurchasable> getExtrasPurchasable() {
        return extrasPurchasable;
    }

    public void setExtrasPurchasable(List<PropertyExtrasPurchasable> extrasPurchasable) {
        this.extrasPurchasable = extrasPurchasable;
    }

    public String getDirections() {
        return directions;
    }

    public List<Facility> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<Facility> facilities) {
        this.facilities = facilities;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public Distance getDistanceToCurrentLocation() {
        return mDistanceToCurrentLocation;
    }

    public void setDistanceToCurrentLocation(Distance mDistanceToCurrentLocation) {
        this.mDistanceToCurrentLocation = mDistanceToCurrentLocation;
    }

    public void setAmenities(List<Amenity> amenities) {
        this.amenities = amenities;
    }

    public List<Amenity> getAmenitiesForProperty() {
        return amenities;
    }

    public Districts getDistricts() {
        return mDistricts;
    }

    public Property setDistricts(Districts mDistricts) {
        this.mDistricts = mDistricts;
        return this;
    }

    public String getPropertyUrl() {
        return propertyUrl;
    }

    public Property setPropertyUrl(String propertyUrl) {
        this.propertyUrl = propertyUrl;
        return this;
    }

    public Property setJSON(JSONObject obj) {
        setPropertyName(H.toString(obj, "propertyName"));
        setDescription(H.toString(obj, "shortDescription"));
        setPropertyNumber(H.toString(obj, "propertyNumber"));
        setPropertyType(H.toString(obj, "propertyType"));
        return this;
    }

    List<String> smallImages = new ArrayList<>();
    List<String> bigImages = new ArrayList<>();

    public List<String> getSmallImages() {
        return smallImages;
    }

    public String getSmallPreview() {
        if (smallImages != null && smallImages.size() > 0) {
            return smallImages.get(0);
        }
        return null;
    }

    public String getBigPreview() {
        if (bigImages != null && bigImages.size() > 0) {
            String imageUrl = bigImages.get(0);
            if (imageUrl != null && !imageUrl.startsWith("http")) {
                imageUrl = "http://" + imageUrl;
            }

            return imageUrl;
        }
        return null;
    }

    public void setSmallImages(List<String> smallImages) {
        this.smallImages = smallImages;
    }

    public List<String> getBigImages() {
        return bigImages;
    }

    public void setBigImages(List<String> bigImages) {
        this.bigImages = bigImages;
    }

    public Property setJSONByPropertyCall(JSONObject obj) {
        setPropertyName(H.toString(obj, "property_name"));
        setPropertyNumber(H.toString(obj, "property_number"));

        setPropertyEmail(H.toString(obj, "property_email"));
        setPropertyTelephone(H.toString(obj, "property_tel"));

        double geoLongitude = H.toDouble(obj, "geo_longitude");
        double geoLatitude = H.toDouble(obj, "geo_latitude");

        setGeoLocation(new GeoLocation(geoLongitude, geoLatitude));

        if (obj.containsKey("property_thumb_url")) {
            bigImages.add(H.toString(obj, "property_thumb_url"));
        }

        if (obj.containsKey("property_type")) {
            propertyType = H.toString(obj, "property_type");
        }

        if (obj.containsKey("property_details")) {
            JSONObject details = (JSONObject)obj.get("property_details");
            JSONArray bigImagesJsonArray = (JSONArray) details.get("BIGIMAGES");

            if (bigImagesJsonArray.size() > 0) {
                bigImages.clear();
                for (int i = 0; i < bigImagesJsonArray.size(); i++) {
                    bigImages.add(bigImagesJsonArray.get(i).toString());
                }
            }

            if (details.containsKey("ADDRESS")) {
                Address address = new Address();
                address.setJSON((JSONObject)details.get("ADDRESS"));
                setPropertyAddress(address);
                H.logD("property address parsed");
            }

            if (obj.containsKey("google_map_address")) {
                if (propertyAddress != null) {
                    propertyAddress.setFullAdress(H.toString(obj, "google_map_address"));
                }
            }


        }

        parseDetails(obj);

        Object propertyDistricts = obj.get("property_districts");
        if (propertyDistricts != null) {
            setDistricts(new Districts().setDistricts(District.factoryList(propertyDistricts)));
        }

        parseRatings(obj);

        parseCityLandmarks(obj);
        parseCityDistricts(obj);

        return this;
    }

    private void parseCityDistricts(JSONObject obj) {
        if (obj.containsKey("city_districts")) {
            Object cityDistricts = obj.get("city_districts");

            if (cityDistricts == null) {
                return;
            }

            setCityDistricts(District.factoryList(cityDistricts));
        }
    }

    private void parseCityLandmarks(JSONObject obj) {
        if (obj.containsKey("city_landmarks")) {
            Object cityLandmarks = obj.get("city_landmarks");

            if (cityLandmarks == null) {
                return;
            }

            setCityLandmarks(Landmark.factoryList(cityLandmarks));
        }
    }

    private void parseRatings(JSONObject obj) {
        if (obj.containsKey("property_ratings")) {
            Ratings ratings = new Ratings();
            JSONObject propertyRatings = (JSONObject) obj.get("property_ratings");

            if (propertyRatings == null) {
                return;
            }

            ratings.jsonPropertyList.setJSON(propertyRatings);
            setPropertyRatings(ratings);
        }
    }

    private void parseDetails(JSONObject obj) {
        if (obj.containsKey("property_details")) {
            JSONObject lDetails = (JSONObject) obj.get("property_details");

            if (lDetails == null) {
                H.logE("property details is null");
                return;
            }

            if (lDetails.containsKey("GPS")) {
                GeoLocation geo = new GeoLocation();
                geo.fromJSONByProperty((JSONObject) lDetails.get("GPS"));
                setGeoLocation(geo);
            }

            if (lDetails.containsKey("CANCELLATIONINFORMATION")) {
                setCancelPolicy(new CancellationPolicy().json.setJSON((JSONObject) lDetails.get("CANCELLATIONINFORMATION")));
            }
            if (lDetails.containsKey("CHECKINTIMES")) {
                //setCheckInOut(new PropertyCheckInOut().json.setJSON((JSONObject) lDetails.get("CHECKINTIMES")));
            }
            setDescription(H.toString(lDetails, "LONGDESCRIPTION"));
            setImportantInformation(H.toString(lDetails, "IMPORTANTINFORMATION"));

            // todo add this when price format for extras will be ok
            // if (lDetails.containsKey("PROPERTYEXTRAS_purchasable")) {
            //    setExtrasPurchasable(PropertyExtrasPurchasable.factoryList((Object) lDetails.get("PROPERTYEXTRAS_purchasable")));
            // }

            setDirections(H.toString(lDetails, "LOCALATTRACTIONS"));

            if (lDetails.containsKey("FEATURES")) {
                setFacilities(Facility.factoryList((Object) lDetails.get("FEATURES")));
            }

            setPropertyType(H.toString(lDetails, "TYPE"));
        }
    }

    private Date arrivalDate;
    private Date departureDate;
    private boolean isFavourite;

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public void setFavouriteId(String favouriteId) {
        this.favouriteId = favouriteId;
    }

    private void setJsonFromFavourite(JSONObject obj) {
        propertyNumber = H.toString(obj, "property_number");

        if (!(propertyNumber.startsWith("HB") || propertyNumber.startsWith("HC"))) {
            propertyNumber = "HC" + propertyNumber;
        }

        bigImages.add(H.toString(obj, "imageURL"));
        arrivalDate = DT.getDateFromString(H.toString(obj, "arrival_date"));

        int nights = H.toInt(obj, "nights");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(arrivalDate);
        DT.resetCalendar(calendar);
        calendar.add(Calendar.DAY_OF_MONTH, nights);

        departureDate = new Date(calendar.getTimeInMillis());
        propertyName = H.toString(obj, "name");
        propertyType = H.toString(obj, "property_type");
        favouriteId = H.toString(obj, "id");
        favouriteNote = H.toString(obj, "notes");
        isFavourite = true;
    }

    public String getFavouriteId() {
        return favouriteId;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public static List<Property> favouritesFactoryList(JSONArray array) {
        ArrayList<Property> properties = new ArrayList<>();
        try {
            for (Object key : array) {
                JSONObject obj = (JSONObject) key;
                Property property = new Property();
                property.setJsonFromFavourite(obj);
                properties.add(property);
            }
        } catch (Exception e) {
            H.logE("Can't create Property list from JSON favourites Array. " + e.getMessage());
        }
        return properties;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public static ArrayList<Property> factoryList(Object propertiesJsonArray, Districts districts, List<Landmark> cityLandmarks, List<Amenity> amenities) {
        ArrayList<Property> result = new ArrayList<Property>();
        try {
            JSONArray array = (JSONArray) propertiesJsonArray;

            H.logD("FETCHED: " + array.size());

            for (Object key : array) {
                JSONObject obj = (JSONObject) key;
                Property entity = new Property();
                entity.setJSON(obj);

                entity.setPropertyUrl(H.toString(obj, "property_page_url"));
                entity.setPropertyType(H.toString(obj, "propertyType"));
                entity.setPropertyTypeTranslate(H.toString(obj, "propertyTypeTranslate"));

                Images images = new Images();
                JSONObject s = (JSONObject) obj.get("PropertyImages");
                JSONObject s1 = (JSONObject) s.get("PropertyImage");
                entity.setImages(images.setJSON(s1));


                JSONObject addressJson = (JSONObject)obj.get("address");
                Address address = new Address();
                address.setCity(H.toString(addressJson, "city"));
                address.setCountry(H.toString(addressJson, "country"));
                address.setStreet1(H.toString(addressJson, "street1"));
                address.setZip(H.toString(addressJson, "zip"));
                entity.setPropertyAddress(address);

                if (obj.containsKey("Geo")) {
                    GeoLocation geo = new GeoLocation();
                    geo.setJSON((JSONObject) obj.get("Geo"));
                    entity.setGeoLocation(geo);
                }

                if (obj.containsKey("landmarks")) {
                    JSONArray propertyLandmarksArray = (JSONArray) obj.get("landmarks");

                    List<Landmark> propertyLandmarksList = new ArrayList<>();

                    for (Object landmarkObj : propertyLandmarksArray) {
                        JSONObject landmark = (JSONObject) landmarkObj;
                        String landmarkId = H.toString(landmark, "lid");


                        for (Landmark cityLandmark : cityLandmarks) {
                            if (cityLandmark.getId().equals(Integer.valueOf(landmarkId))) {
                                propertyLandmarksList.add(cityLandmark);
                            }
                        }

                    }

                    entity.setLandmarks(propertyLandmarksList);

                }

                if (obj.containsKey("amenities")) {
                    List<Amenity> propertyAmenities = new ArrayList<>();

                    JSONArray amenitiesJsonArray = (JSONArray)obj.get("amenities");
                    for (Object amenityIdObj : amenitiesJsonArray) {
                        String amenityId = (String) amenityIdObj;

                        for (Amenity amenity : amenities) {
                            if (amenity.getFacilityId().equals(amenityId)) {
                                propertyAmenities.add(amenity);
                            }
                        }
                    }

                    entity.setAmenities(propertyAmenities);
                }

                Prices propertyPrice = new Prices();

                Price displayPrice = new Price();
                displayPrice.setPrice(H.toBigDecimal(obj, "display_price"));
                displayPrice.setCurrency(H.toString(obj, "display_currency"));
                propertyPrice.setDisplayPrice(displayPrice);

                Price price = new Price();
                price.setPrice(H.toBigDecimal(obj, "display_private_price"));
                price.setCurrency(H.toString(obj, "display_currency"));
                propertyPrice.setDisplayPrivatePrice(price);

                Price priceDorm = new Price();
                priceDorm.setPrice(H.toBigDecimal(obj, "display_shared_price"));
                priceDorm.setCurrency(H.toString(obj, "display_currency"));
                propertyPrice.setDisplaySharedPrice(priceDorm);

                entity.setPropertyPrice(propertyPrice);


                Currency currency = new Currency();
                currency.setSymbol((H.toString(obj, "display_currency")));
                currency.setCode((H.toString(obj, "currency_code")));
                entity.setCurrency(currency);

                Ratings ratings = new Ratings();
                ratings.setOverall(H.toInt(obj, "overall_rating"));
                ratings.setRating(H.toString(obj, "rating"));
                if (obj.containsKey("ratings")) {
                    ratings.jsonPropertyList.setJSON((JSONObject) obj.get("ratings"));
                }
                entity.setPropertyRatings(ratings);

                H.logD("PROP: " + entity.getPropertyName());

                // todo parsing districs
                //entity.setDistricts(new Districts().setDistricts(District.factoryList(obj.get("districts"))));

                result.add(entity);
            }
        } catch (Exception e) {
            H.logE(e);
        }
        return result;
    }

    @Override
    public String toString() {
        return this.propertyName;
    }

    public void calculateDistanceLocalProperty(GeoLocation from) {
        setDistanceToCurrentLocation(DistanceHelper.getDistance(from, getGeoLocation()));
    }

    public String getFullAddressString() {
        return getPropertyAddress().getFullAddressString();
    }

    public boolean isDorms() {
        return getPropertyPrice().getDisplaySharedPrice().getPrice().compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isPrivateRooms() {
        return getPropertyPrice().getDisplayPrivatePrice().getPrice().compareTo(BigDecimal.ZERO) > 0;
    }

    public Double getPriceValue() {
        Price price = new Price(getPropertyPrice().getDisplayPrice().getPrice(), getCurrency());
        return price.getPrice().doubleValue();
    }

    public String getPropertyEmail() {
        return propertyEmail;
    }

    public void setPropertyEmail(String propertyEmail) {
        this.propertyEmail = propertyEmail;
    }

    public String getPropertyTelephone() {
        return propertyTelephone;
    }

    public void setPropertyTelephone(String propertyTelephone) {
        this.propertyTelephone = propertyTelephone;
    }

    public boolean isNearCityCenter() {
        if (landmarks == null || landmarks.isEmpty()) {
            return false;
        }

        for (Landmark landmark : landmarks) {
            if (landmark.isCityCenter()) {
                return true;
            }
        }

        return false;
    }

    public List<Landmark> getCityLandmarks() {
        return cityLandmarks;
    }

    public void setCityLandmarks(List<Landmark> cityLandmarks) {
        this.cityLandmarks = cityLandmarks;
    }

    public List<District> getCityDistricts() {
        return cityDistricts;
    }

    public void setCityDistricts(List<District> cityDistricts) {
        this.cityDistricts = cityDistricts;
    }

    public String getFavouriteNote() {
        return favouriteNote;
    }

    public void setFavouriteNote(String favouriteNote) {
        this.favouriteNote = favouriteNote;
    }

    @Override
    public LatLng getLocation() {
        return getGeoLocation().toLatLng();
    }

    @Override
    public String getLocationId() {
        return String.valueOf(getPropertyNumber());
    }

    @Override
    public LocationType getLocationType() {
        return LocationType.PROPERTY;
    }

    @Override
    public String getLocationName() {
        return toString();
    }

    @Override
    public MarkerOptions getMarkerOptions() {
        return MarkerHelper.gePropertyMarkerOptions(getLocation());
    }

    public List<Location> getLocations() {
        List<Location> locations = new ArrayList<>();

        locations.add(this);

        List<District> cityDistricts = getCityDistricts();
        if (cityDistricts != null) {
            for (District district : cityDistricts) {
                locations.add(district);
            }
        }

        List<Landmark> cityLandmarks = getCityLandmarks();
        if (cityLandmarks != null) {
            for (Landmark landmark : cityLandmarks) {
                locations.add(landmark);
            }
        }

        return locations;
    }

    public String getPrivatePriceString() {
        return Localization.getPriceLocalized(App.getInstance(), getPropertyPrice().getDisplayPrivatePrice().getPrice());
    }

    public String getDormPriceString() {
        return Localization.getPriceLocalized(App.getInstance(), getPropertyPrice().getDisplaySharedPrice().getPrice());
    }

    private BigDecimal getPrivatePrice() {
        return getPropertyPrice().getDisplayPrivatePrice().getPrice();
    }

    private BigDecimal getDormPrice() {
        return getPropertyPrice().getDisplaySharedPrice().getPrice();
    }

    public Double getComparedPrice() {
        BigDecimal privatePrice = getPrivatePrice();
        BigDecimal dormsPrice = getDormPrice();
        if (privatePrice.compareTo(BigDecimal.ZERO) > 0 &&
            dormsPrice.compareTo(BigDecimal.ZERO) > 0) {
            return Math.min(privatePrice.doubleValue(), dormsPrice.doubleValue());
        } else {
            if (privatePrice.compareTo(BigDecimal.ZERO) > 0) {
                return privatePrice.doubleValue();
            } else {
                return dormsPrice.doubleValue();
            }
        }
    }

    @Nullable
    public String getFreeAmenities() {
        if (amenities == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder(30);
        for (Amenity amenity : amenities) {
            if (amenity != null && amenity.isFree()) {
                if (sb.length() > 0) {
                    sb.append(", ").append(amenity.getName());
                } else {
                    sb.append(amenity.getName());
                }
            }
        }

        return sb.toString();
    }
}
