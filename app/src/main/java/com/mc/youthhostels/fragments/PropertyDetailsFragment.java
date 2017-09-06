package com.mc.youthhostels.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mc.youthhostels.R;
import com.mc.youthhostels.events.details.PropertyMapClicked;
import com.mc.youthhostels.events.details.PropertyReviewsClicked;
import com.mc.youthhostels.fragments.map.MapFragment;
import com.mc.youthhostels.customviews.ExpandableTextView;
import com.mc.youthhostels.model.PropertyReview;
import com.mc.youthhostels.presenters.ReviewsPresenter;
import com.mc.youthhostels.views.ReviewView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import entity.Property.CancellationPolicy;
import entity.Property.Facility;
import entity.Property.Property;
import helper.H;

public class PropertyDetailsFragment extends Fragment implements ReviewView {

    @Bind(R.id.txt_property_street)
    TextView propertyStreet;
    @Bind(R.id.txt_overall_rating)
    TextView overallRating;
    @Bind(R.id.txt_property_name)
    TextView propertyName;
    @Bind(R.id.txt_property_type)
    TextView propertyType;
    @Bind(R.id.btn_reviews)
    Button buttonReviews;
    @Bind(R.id.lyt_rating_board)
    View ratingBoard;

    private Property property;
    private ReviewsPresenter reviewsPresenter;

    public static PropertyDetailsFragment newInstance(Property property) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(Property.BUNDLE, property);

        PropertyDetailsFragment propertyDetailsFragment = new PropertyDetailsFragment();
        propertyDetailsFragment.setArguments(bundle);

        return propertyDetailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();

        if (arguments != null) {
            property = (Property) arguments.getSerializable(Property.BUNDLE);
        }

        if (property != null) {
            reviewsPresenter = new ReviewsPresenter(this, property.getPropertyNumber());
            reviewsPresenter.onCreate();
        } else {
            H.logE("property null while init reviews presenter");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_property_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        reviewsPresenter.onResume();
        reviewsPresenter.fetchReviews();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        reviewsPresenter.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        reviewsPresenter.onPause();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        propertyStreet.setText(property.getFullAddressString());

        int rating = property.getPropertyRatings().getOverall();
        if (rating != 0) {
            ratingBoard.setVisibility(View.VISIBLE);
            overallRating.setText(String.format("%s %s%%", property.getPropertyRatings().getRating(),
                    String.valueOf(rating)));
            initRatings();
        }

        initDetails();

        propertyName.setText(property.getPropertyName());
        propertyType.setText(property.getPropertyType().toUpperCase());

        showMap();
    }

    private void showMap() {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_map, MapFragment.newInstance(property, false))
                .addToBackStack(null)
                .commit();
    }

    private void initRatings() {
        ViewGroup layout = (ViewGroup) getView().findViewById(R.id.lyt_ratings);
        layout.setVisibility(View.VISIBLE);
        layout.removeAllViews();

        // todo add it when api ready
        //layout.addView(getRatingView(property.getPropertyRatings().getSafety(), R.string.safety));
        layout.addView(getRatingView(property.getPropertyRatings().getLocation(), R.string.location));
        layout.addView(getRatingView(property.getPropertyRatings().getStaff(), R.string.staff));
        layout.addView(getRatingView(property.getPropertyRatings().getFacilities(), R.string.facilities));
        layout.addView(getRatingView(property.getPropertyRatings().getAtmosphere(), R.string.atmosphere));
        layout.addView(getRatingView(property.getPropertyRatings().getCleanliness(), R.string.cleanliness));
    }

    private View getRatingView(int rating, int ratingNameId) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_rating, null);
        RatingViewHolder holder = new RatingViewHolder(view);
        holder.ratingName.setText(H.getString(ratingNameId));
        holder.ratingValue.setText(String.format("%s%%", String.valueOf(rating)));

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.ratingEnabledView.getLayoutParams();
        layoutParams.weight = rating;
        holder.ratingEnabledView.setLayoutParams(layoutParams);
        holder.ratingEnabledView.requestLayout();

        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) holder.ratingDisabledView.getLayoutParams();
        layoutParams1.weight = 100 - rating;
        layoutParams1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams1.width = 0;

        holder.ratingDisabledView.setLayoutParams(layoutParams1);
        holder.ratingDisabledView.requestLayout();

        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                height);

        view.setLayoutParams(params);

        return view;
    }

    private static class PropertyInfo {
        private String detailName;
        private String detailContent;

        public PropertyInfo(String detailName, String detailContent) {
            this.detailName = detailName;
            this.detailContent = detailContent;
        }

        public String getDetailName() {
            return detailName;
        }

        public String getDetailContent() {
            return detailContent;
        }
    }

    private void initDetails() {
        ViewGroup layout = (ViewGroup) getView().findViewById(R.id.lyt_details);
        layout.removeAllViews();

        List<Facility> facilities = property.getFacilities();
        List<PropertyInfo> info = new ArrayList<>(10);

        if (facilities != null && facilities.size() > 0) {

            StringBuilder sb = new StringBuilder();
            for (Facility facility : facilities) {
                sb.append(facility.getName()).append(", ");
            }

            sb.setLength(sb.length() - 2);
            info.add(new PropertyInfo(H.getString(R.string.ui_facilities).toUpperCase(), sb.toString()));
        }

        addInfo(info, R.string.ui_property_details_information, property.getDescription());
        addInfo(info, R.string.ui_property_details_directions, property.getDirections());
        addInfo(info, R.string.ui_property_details_important_info, property.getImportantInformation());

        CancellationPolicy cancelPolicy = property.getCancelPolicy();
        if (cancelPolicy != null) {
            addInfo(info, R.string.ui_property_details_cancellation_policy, cancelPolicy.getDescription());
        } else {
            H.logE("cancel policy is");
        }

        for (PropertyInfo propertyInfo : info) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_property_detail, null);
            DetailViewHolder holder = new DetailViewHolder(view);
            holder.txtDetailName.setText(propertyInfo.getDetailName().toUpperCase());
            holder.txtDetailContent.setText(Html.fromHtml(propertyInfo.getDetailContent()));
            layout.addView(view);
        }
    }

    private void addInfo(List<PropertyInfo> infos, int nameId, String content) {
        if (content != null && content.length() > 0) {
            infos.add(new PropertyInfo(H.getString(nameId), content));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    static class DetailViewHolder {
        @Bind(R.id.txt_detail_name)
        TextView txtDetailName;
        @Bind(R.id.txt_detail_content)
        ExpandableTextView txtDetailContent;

        DetailViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class RatingViewHolder {
        @Bind(R.id.view_ratings_enabled)
        View ratingEnabledView;
        @Bind(R.id.view_ratings_disabled)
        View ratingDisabledView;
        @Bind(R.id.txt_rating_name)
        TextView ratingName;
        @Bind(R.id.txt_rating_value)
        TextView ratingValue;

        RatingViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public void showReviews(List<PropertyReview> reviews) {
        if (buttonReviews == null) {
            return;
        }

        if (reviews == null) {
            return;
        }

        if (reviews.isEmpty()) {
            return;
        }

        buttonReviews.setVisibility(View.VISIBLE);
        buttonReviews.setText(String.format(buttonReviews.getText().toString(),
                String.valueOf(reviews.size())));
        buttonReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new PropertyReviewsClicked());
            }
        });
    }

    @OnClick(R.id.fragment_map)
    public void onMapClicked() {
        EventBus.getDefault().post(new PropertyMapClicked());
    }


    @OnClick(R.id.lyt_street)
    public void onStreetClicked() {
        onMapClicked();
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }
}
