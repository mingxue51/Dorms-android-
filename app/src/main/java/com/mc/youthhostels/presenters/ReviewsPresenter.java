package com.mc.youthhostels.presenters;



import com.mc.youthhostels.model.PropertyReview;
import com.mc.youthhostels.views.ReviewView;

import java.util.List;

import api.API;
import entity.Property.Property;
import entity.Property.Search.SearchProperty;
import helper.App;
import helper.H;

public class ReviewsPresenter implements Presenter {

    private ReviewView reviewView;
    private String propertyNumber;

    public ReviewsPresenter(ReviewView reviewView, String propertyNumber) {
        this.reviewView = reviewView;
        this.propertyNumber = propertyNumber;
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onPause() {
    }

    public void fetchReviews() {
        H.logD("start fetching reviews");
        reviewView.showLoading();
        API.getInstance(App.getInstance()).GetPropertyReviews(propertyNumber, new API.IGetRealTimeObject() {
            @Override
            public void getData(final Object object) {
                H.logD("reviews fetched");
                reviewView.hideLoading();
                List<PropertyReview> reviews = (List<PropertyReview>) object;

                H.runOnUi(() -> reviewView.showReviews(reviews));
            }

            @Override
            public void onError(String message) {
                reviewView.hideLoading();
            }
        });
    }
}
