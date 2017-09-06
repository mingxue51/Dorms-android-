package entity.Property;

import org.json.simple.JSONObject;

import java.io.Serializable;

import helper.H;

public class Address implements Serializable{
    private static final long serialVersionUID = 1L;

    private String street1;
    private String street2;
    private String street3;
    private String state;
    private String zip;
    private String country;
    private String city;
    private String fullAdress;

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getStreet3() {
        return street3;
    }

    public void setStreet3(String street3) {
        this.street3 = street3;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setJSON(JSONObject obj) {
        setCity(H.toString(obj, "CITY"));
        setCountry(H.toString(obj, "COUNTRY"));
        setStreet1(H.toString(obj, "STREET1"));
        setZip(H.toString(obj, "ZIP"));
    }

    public String getFullAddressString(){
        return fullAdress;
    }

    public String getFullAdress() {
        return fullAdress;
    }

    public void setFullAdress(String fullAdress) {
        this.fullAdress = fullAdress;
    }
}
