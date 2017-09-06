package com.mc.youthhostels.entity;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

import helper.H;

public class UserReview {
    private String id;
    private String bookingReference;
    private String title;
    private String review;

    private int ratingAtmosphere;
    private int ratingStaff;
    private int ratingLocation;
    private int ratingCleanliness;
    private int ratingFacilities;
    private int ratingSafety;
    private int ratingValue;

    private String propertyRating;
    private String propertyPreview;
    private String propertyName;

    public void setJSON(JSONObject obj) {
        propertyName = H.toString(obj, "property_name");
        propertyPreview = H.toString(obj, "property_preview_image");
        propertyRating = H.toString(obj, "property_rating");

        ratingAtmosphere = H.toInt(obj, "rating_atmosphere");
        ratingStaff = H.toInt(obj, "rating_staff");
        ratingLocation = H.toInt(obj, "rating_location");
        ratingCleanliness = H.toInt(obj, "rating_cleanliness");
        ratingFacilities = H.toInt(obj, "rating_facilities");
        ratingSafety = H.toInt(obj, "rating_safety");
        ratingValue = H.toInt(obj, "rating_value");

        id = H.toString(obj, "id");
        bookingReference = H.toString(obj, "booking_reference");
        title = H.toString(obj, "review_title");
        review = H.toString(obj, "review");
    }

    public static List<UserReview> factoryList(Object json) {
        List<UserReview> userReviews = new ArrayList<>();
        try {
            JSONArray array = (JSONArray) json;
            for (Object key : array) {
                JSONObject obj = (JSONObject) key;
                UserReview userReview = new UserReview();
                userReview.setJSON(obj);
                userReviews.add(userReview);
            }
        } catch (Exception e) {
            H.logE("Can't create property reviews from JSON Array.");
            H.logE(e.getMessage());
        }
        return userReviews;
    }

    public String getId() {
        return id;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public String getTitle() {
        return title;
    }

    public String getReview() {
        return review;
    }

    public int getRatingAtmosphere() {
        return ratingAtmosphere;
    }

    public int getRatingStaff() {
        return ratingStaff;
    }

    public int getRatingLocation() {
        return ratingLocation;
    }

    public int getRatingCleanliness() {
        return ratingCleanliness;
    }

    public int getRatingFacilities() {
        return ratingFacilities;
    }

    public int getRatingSafety() {
        return ratingSafety;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public String getPropertyRating() {
        return propertyRating;
    }

    public String getPropertyPreview() {
        return propertyPreview;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setRatingAtmosphere(int ratingAtmosphere) {
        this.ratingAtmosphere = ratingAtmosphere;
    }

    public void setRatingStaff(int ratingStaff) {
        this.ratingStaff = ratingStaff;
    }

    public void setRatingLocation(int ratingLocation) {
        this.ratingLocation = ratingLocation;
    }

    public void setRatingCleanliness(int ratingCleanliness) {
        this.ratingCleanliness = ratingCleanliness;
    }

    public void setRatingFacilities(int ratingFacilities) {
        this.ratingFacilities = ratingFacilities;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public void setRatingSafety(int ratingSafety) {
        this.ratingSafety = ratingSafety;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }
}
