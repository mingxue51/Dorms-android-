package com.mc.youthhostels.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

import entity.Property.PropertyReviewItem;
import helper.H;

public class PropertyReview {
    private String authorName;
    private String date;
    private String authorCountry;
    private String review;
    private String reviewTranslated;
    private double rating;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthorCountry() {
        return authorCountry;
    }

    public void setAuthorCountry(String authorCountry) {
        this.authorCountry = authorCountry;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getReviewTranslated() {
        return reviewTranslated;
    }

    public void setReviewTranslated(String reviewTranslated) {
        this.reviewTranslated = reviewTranslated;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setJSON(JSONObject obj) {
        setAuthorName(H.toString(obj, "author_name"));
        setDate(H.toString(obj, "review_date"));
        setAuthorCountry(H.toString(obj, "author_country"));
        setReview(H.toString(obj, "review_likebest"));
        setReviewTranslated(H.toString(obj, "review_likebest_translated"));
        setRating(H.toDouble(obj, "review_rating"));
    }

    public static List<PropertyReview> factoryList(Object json) {
        List<PropertyReview> result = new ArrayList<>();
        try {
            JSONArray array = (JSONArray) json;
            for (Object key : array) {
                JSONObject obj = (JSONObject) key;
                PropertyReview entity = new PropertyReview();
                entity.setJSON(obj);
                result.add(entity);
            }
        } catch (Exception e) {
            H.logE("Can't create Property list from JSON Array.");
            H.logE(e.getMessage());
        }
        return result;
    }
}
