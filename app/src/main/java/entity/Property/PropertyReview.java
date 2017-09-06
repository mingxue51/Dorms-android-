package entity.Property;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

import helper.H;

public class PropertyReview {
    public void PropertyReview(){
        reviews = new ArrayList<PropertyReviewItem>();
    }
    private ArrayList<PropertyReviewItem> reviews;

    private int review_count;
    private int reviews_translation_available;

    public PropertyReview(){
        reviews = new ArrayList<PropertyReviewItem>();
    }

    public ArrayList<PropertyReviewItem> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<PropertyReviewItem> reviews) {
        this.reviews = reviews;
    }

    public int getReview_count() {
        return review_count;
    }

    public void setReview_count(int review_count) {
        this.review_count = review_count;
    }

    public int getReviews_translation_available() {
        return reviews_translation_available;
    }

    public void setReviews_translation_available(int reviews_translation_available) {
        this.reviews_translation_available = reviews_translation_available;
    }

    public void setJSON(JSONObject obj) {
        setReviews_translation_available(H.toInt(obj, "reviews_translation_available"));
        setReview_count(H.toInt(obj, "review_count"));
    }

    public static ArrayList<PropertyReviewItem> factoryList(Object json) {
        ArrayList<PropertyReviewItem> result = new ArrayList<PropertyReviewItem>();
        try {
            JSONArray array = (JSONArray) json;
            for (Object key : array) {
                JSONObject obj = (JSONObject) key;
                PropertyReviewItem entity = new PropertyReviewItem();
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
