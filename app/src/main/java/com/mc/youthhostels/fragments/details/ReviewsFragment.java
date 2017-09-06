package com.mc.youthhostels.fragments.details;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mc.youthhostels.R;
import com.mc.youthhostels.adapter.ReviewsAdapter;
import com.mc.youthhostels.model.PropertyReview;
import com.mc.youthhostels.presenters.ReviewsPresenter;
import com.mc.youthhostels.views.ReviewView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import entity.Property.Search.SearchProperty;
import helper.App;
import helper.H;

public class ReviewsFragment extends Fragment implements ReviewView {

    @Bind(R.id.list_reviews)
    RecyclerView listReviews;
    @Bind(R.id.txt_no_reviews)
    TextView noReviewsView;

    private ReviewsPresenter presenter;
    private ReviewsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void showReviews(List<PropertyReview> reviews) {
        if (reviews.isEmpty()) {
            noReviewsView.setVisibility(View.VISIBLE);
        } else {
            noReviewsView.setVisibility(View.GONE);
        }

        adapter.setReviews(reviews);
        adapter.notifyDataSetChanged();
        layoutManager.scrollToPosition(0);
        H.logD("ReviewsFragment:reviews showed");
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
        H.logD("ReviewsFragment:on resume");
    }

    public static ReviewsFragment newInstance(SearchProperty searchProperty) {
        ReviewsFragment fragment = new ReviewsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SearchProperty.BUNDLE, searchProperty);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SearchProperty searchProperty = null;

        if (getArguments() != null) {
            searchProperty = (SearchProperty)getArguments().getSerializable(SearchProperty.BUNDLE);
        }

        presenter = new ReviewsPresenter(this, searchProperty.getPropertyId());
        presenter.onCreate();
        H.logD("ReviewsFragment:on create");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        H.logD("ReviewsFragment:on create view");
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutManager = new LinearLayoutManager(getActivity());
        listReviews.setLayoutManager(layoutManager);
        adapter = new ReviewsAdapter();
        listReviews.setAdapter(adapter);
        presenter.fetchReviews();
        H.logD("ReviewsFragment:on view created");
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
        H.logD("ReviewsFragment:on pause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        H.logD("ReviewsFragment:on destroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        H.logD("ReviewsFragment:on destroy view");
    }

    @Override
    public void showLoading() {
        App.getInstance().showLoading();
    }

    @Override
    public void hideLoading() {
        App.getInstance().hideLoading();
    }
}
