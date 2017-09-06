package com.mc.youthhostels.views;


import com.mc.youthhostels.model.PropertyReview;

import java.util.List;

import com.mc.youthhostels.LoadingView;

public interface ReviewView extends LoadingView {
    void showReviews(List<PropertyReview> reviews);
}
