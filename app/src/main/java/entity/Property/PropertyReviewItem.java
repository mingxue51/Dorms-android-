package entity.Property;

import org.json.simple.JSONObject;

import helper.H;

public class PropertyReviewItem {
    private String author_name;
    private String review_date;
    private String author_country;
    private String review_likebest;
    private String review_likebest_translated;
    private double review_rating;
    private String likedBest_translatedError;

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getReview_date() {
        return review_date;
    }

    public void setReview_date(String review_date) {
        this.review_date = review_date;
    }

    public String getAuthor_country() {
        return author_country;
    }

    public void setAuthor_country(String author_country) {
        this.author_country = author_country;
    }

    public String getReview_likebest() {
        return review_likebest;
    }

    public void setReview_likebest(String review_likebest) {
        this.review_likebest = review_likebest;
    }

    public String getReview_likebest_translated() {
        return review_likebest_translated;
    }

    public void setReview_likebest_translated(String review_likebest_translated) {
        this.review_likebest_translated = review_likebest_translated;
    }

    public double getReview_rating() {
        return review_rating;
    }

    public void setReview_rating(double review_rating) {
        this.review_rating = review_rating;
    }

    public String getLikedBest_translatedError() {
        return likedBest_translatedError;
    }

    public void setLikedBest_translatedError(String likedBest_translatedError) {
        this.likedBest_translatedError = likedBest_translatedError;
    }
    public void setJSON(JSONObject obj) {
        setAuthor_name(H.toString(obj, "author_name"));
        setReview_date(H.toString(obj, "review_date"));
        setAuthor_country(H.toString(obj, "author_country"));
        setReview_likebest(H.toString(obj, "review_likebest"));
        setReview_likebest_translated(H.toString(obj, "review_likebest_translated"));
        setReview_rating(H.toDouble(obj, "review_rating"));
    }
}
