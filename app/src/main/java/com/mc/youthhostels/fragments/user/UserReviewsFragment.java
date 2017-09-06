package com.mc.youthhostels.fragments.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mc.youthhostels.R;
import com.mc.youthhostels.activities.AppActivity;
import com.mc.youthhostels.adapter.ReviewsAdapter;
import com.mc.youthhostels.adapter.UserReviewsAdapter;
import com.mc.youthhostels.entity.UserReview;
import com.mc.youthhostels.model.PropertyReview;
import com.mc.youthhostels.presenters.ReviewsPresenter;

import java.util.List;

import api.API;
import butterknife.Bind;
import butterknife.ButterKnife;
import entity.Property.Search.SearchProperty;
import helper.App;
import helper.H;

public class UserReviewsFragment extends Fragment {

    @Bind(R.id.list_reviews)
    RecyclerView listReviews;
    @Bind(R.id.txt_no_reviews)
    TextView noReviewsView;

    private UserReviewsAdapter adapter;

    public void showReviews(List<UserReview> reviews) {
        if (reviews.isEmpty()) {
            noReviewsView.setVisibility(View.VISIBLE);
        } else {
            noReviewsView.setVisibility(View.GONE);
        }

        adapter.setReviews(reviews);
        adapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_reviews, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listReviews.setLayoutManager(layoutManager);
        adapter = new UserReviewsAdapter();
        listReviews.setAdapter(adapter);

        showLoading();
        API.getInstance(App.getInstance()).getUserReviews(new API.IGetRealTimeObject() {
            @Override
            public void getData(final Object object) {
                if (getView() == null) {
                    return;
                }

                hideLoading();
                List<UserReview> reviews = (List<UserReview>) object;

                H.runOnUi(() -> showReviews(reviews));
            }

            @Override
            public void onError(String message) {
                hideLoading();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        H.logD("ReviewsFragment:on destroy view");
    }

    public void showLoading() {
        App.getInstance().showLoading();
    }

    public void hideLoading() {
        App.getInstance().hideLoading();
    }

    @Override
    public void onResume() {
        super.onResume();
        AppActivity activity = (AppActivity) getActivity();
        activity.setDefaultActionBar();
        activity.setActionBarTitle(H.getString(R.string.my_reviews));
    }
}

