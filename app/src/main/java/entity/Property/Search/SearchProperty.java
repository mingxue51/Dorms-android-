package entity.Property.Search;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import entity.Generic.Currency;
import entity.Generic.GeoLocation;
import helper.App;
import helper.DT;


public class SearchProperty implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String BUNDLE = "search_properties";
    public static final int DEFAULT_NUMBER_OF_NIGHTS = 1;
    private static final int MAX_NIGHTS = 31;

    private String propertyId;
    private String city;
    private String country;

    private Date departureDate;
    private Date arrivalDate;

    private GeoLocation geoLocation;

    public SearchProperty() {
        city="";
        country="";
        propertyId = "";
        geoLocation = new GeoLocation();
        setDefaultDates();
    }

    public void setDepartureDate(Date start, int numOfNights) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(Calendar.DAY_OF_MONTH, numOfNights);
        setDepartureDate(calendar.getTime());
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    public String getArrivalDateForAPI() {
        return DT.getDateForAPI(getArrivalDate());
    }

    public int getNumNightsP() {
        return DT.calculateNumberOfNights(getArrivalDate(), getDepartureDate());
    }

    public boolean isValid() {
        return getNumNightsP() < MAX_NIGHTS && getNumNightsP() > 0;
    }

    private SearchSource searchSource = SearchSource.CITY;

    public SearchSource getSearchSource() {
        return searchSource;
    }

    public void setSearchSource(SearchSource searchSource) {
        this.searchSource = searchSource;
    }

    public String getDateInfo() {
        String dateInfo = DT.formateDate(arrivalDate) + " " + DT.formatDay(arrivalDate) + " - " + DT.formateDate(departureDate) + " " + DT.formatDay(departureDate);
        return dateInfo.toUpperCase();
    }

    public enum SearchSource {
        GEOCODES,
        CITY,
        UNKNOWN,
        PROPERTY
    }

    public String getCityAndCountry() {
        if(!(getCity().isEmpty()) && !(getCountry().isEmpty())) {
            return getCity() + ", " + getCountry();
        }

        if(getCity().isEmpty() && !(getCountry().isEmpty())) {
            return getCountry();
        }

        if(!(getCity().isEmpty()) && getCountry().isEmpty()) {
            return getCity();
        }

        return null;
    }

    public void setTonightDates(){
        setArrivalDate(DT.getCheckIn(0));
        setDepartureDate(DT.getCheckOut(0, 1));
    }

    public void setDefaultDates() {
        arrivalDate = DT.getDefaultCheckIn();
        departureDate = DT.getDefaultCheckOut();
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }
}
