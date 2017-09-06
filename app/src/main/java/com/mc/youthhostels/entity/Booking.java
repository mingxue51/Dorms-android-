package com.mc.youthhostels.entity;


import android.support.annotation.Nullable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import entity.Property.Property;
import helper.H;

public class Booking implements Serializable {
    public static final String BUNDLE = "booking";

    private String apiProviderId;
    private String referenceNumber;

    private String arrivalDate;
    private int numNights;
    private int numGuests;
    private boolean hasReview;

    private String currencyCode;
    private BigDecimal chargedPrice;
    private BigDecimal totalPrice;
    private BigDecimal onArrivalPrice;

    private Property property;
    private String fullAddress;

    public void fromJson(JSONObject obj) {
        totalPrice = H.toBigDecimal(obj, "property_grand_total");
        onArrivalPrice = H.toBigDecimal(obj, "property_amount_due");

        property = new Property();
        property.setJSONByPropertyCall(obj);

        referenceNumber = H.toString(obj, "booking_reference");
        apiProviderId = H.toString(obj, "API_booked");
        numNights = H.toInt(obj, "num_nights");
        numGuests = H.toInt(obj, "number_of_guests");
        arrivalDate = H.toString(obj, "arrival_date_time");
        currencyCode = H.toString(obj, "amount_charged_currency");
        hasReview = H.toInt(obj, "has_review") != 0;

        if (obj.containsKey("property_city") &&
            obj.containsKey("property_country") &&
            obj.containsKey("property_address1")) {
            fullAddress = H.toString(obj, "property_country") + ", " +
                          H.toString(obj, "property_city") + ", " +
                          H.toString(obj, "property_address1");
        }

        chargedPrice = totalPrice.subtract(onArrivalPrice);
    }

    public static List<Booking> factoryList(Object json) {
        List<Booking> bookings = new ArrayList<>();
        try {
            JSONArray array = (JSONArray) json;
            for (Object key : array) {
                JSONObject obj = (JSONObject) key;

                Booking booking = new Booking();
                booking.fromJson(obj);

                bookings.add(booking);
            }
        } catch (Exception e) {
            H.logE("Can't create bookings from JSON Array.");
            H.logE(e.getMessage());
        }

        return bookings;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public int getNumNights() {
        return numNights;
    }

    public int getNumGuests() {
        return numGuests;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public BigDecimal getChargedPrice() {
        return chargedPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public BigDecimal getOnArrivalPrice() {
        return onArrivalPrice;
    }

    public Property getProperty() {
        return property;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    @Nullable
    public String getFullAddress() {
        return fullAddress;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "apiProviderId='" + apiProviderId + '\'' +
                ", referenceNumber='" + referenceNumber + '\'' +
                ", arrivalDate='" + arrivalDate + '\'' +
                ", numNights=" + numNights +
                ", numGuests=" + numGuests +
                ", currencyCode='" + currencyCode + '\'' +
                ", chargedPrice=" + chargedPrice +
                ", totalPrice=" + totalPrice +
                ", onArrivalPrice=" + onArrivalPrice +
                ", property=" + property +
                '}';
    }

    public boolean hasReview() {
        return hasReview;
    }
}
