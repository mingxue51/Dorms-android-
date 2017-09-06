package service;

import android.location.Location;

public class LocationAddress{
	public static final int DECIMAL_PLACES = 4;  // decimal places of location value
	
	private Location mLocation;
    private double longtitude;
    private double latitude;

    private String address;
    private String city;
    private String cityNIndex;
    private String country;
    private String state;
    private String subAdministrative;
    private String countryCode;
    private String countryName;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Location getmLocation() {
        return mLocation;
    }

    public void setmLocation(Location mLocation) {
        this.mLocation = mLocation;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityNIndex() {
        return cityNIndex;
    }

    public void setCityNIndex(String cityNIndex) {
        this.cityNIndex = cityNIndex;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSubAdministrative() {
        return subAdministrative;
    }

    public void setSubAdministrative(String subAdministrative) {
        this.subAdministrative = subAdministrative;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
