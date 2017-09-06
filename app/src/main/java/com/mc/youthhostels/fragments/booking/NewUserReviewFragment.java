package com.mc.youthhostels.fragments.booking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mc.youthhostels.R;
import com.mc.youthhostels.activities.AppActivity;
import com.mc.youthhostels.entity.Booking;
import com.mc.youthhostels.entity.UserReview;

import api.API;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import entity.Property.Property;
import helper.App;
import helper.H;

public class NewUserReviewFragment extends Fragment {
    private Booking booking;

    @Bind(R.id.txt_property_type)
    TextView propertyType;

    @Bind(R.id.txt_property_name)
    TextView propertyName;

    @Bind(R.id.txt_review)
    EditText reviewText;

    @Bind(R.id.txt_title_review)
    EditText titleText;

    @Bind(R.id.atmosphere_rating_bar)
    RatingBar atmosphereBar;

    @Bind(R.id.facilities_rating_bar)
    RatingBar facilitiesBar;

    // todo we will handle it next versions
//    @Bind(R.id.safety_rating_bar)
//    RatingBar safetyBar;

    @Bind(R.id.value_rating_bar)
    RatingBar valueBar;

    @Bind(R.id.cleanliness_rating_bar)
    RatingBar cleanlinessBar;

    @Bind(R.id.location_rating_bar)
    RatingBar locationBar;

    @Bind(R.id.staff_rating_bar)
    RatingBar staffBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_review, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public static NewUserReviewFragment newInstance(Booking booking) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Booking.BUNDLE, booking);
        NewUserReviewFragment fragment = new NewUserReviewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            booking = (Booking)arguments.getSerializable(Booking.BUNDLE);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Property property = booking.getProperty();
        propertyName.setText(property.getPropertyName());
        propertyType.setText(property.getPropertyType());
        initRatingBars();
    }

    private void initRatingBars() {
        H.setStarColor(atmosphereBar, H.getColor(R.color.gray), H.getColor(R.color.stars));
        H.setStarColor(facilitiesBar, H.getColor(R.color.gray), H.getColor(R.color.stars));
        H.setStarColor(staffBar, H.getColor(R.color.gray), H.getColor(R.color.stars));
        H.setStarColor(valueBar, H.getColor(R.color.gray), H.getColor(R.color.stars));
        H.setStarColor(cleanlinessBar, H.getColor(R.color.gray), H.getColor(R.color.stars));
        H.setStarColor(locationBar, H.getColor(R.color.gray), H.getColor(R.color.stars));
    }

    @Override
    public void onResume() {
        super.onResume();
        AppActivity activity = (AppActivity) getActivity();
        activity.setDefaultActionBar();
        activity.setActionBarTitle(H.getString(R.string.leave_your_review));
    }

    @OnClick(R.id.btn_submit_review)
    public void onSubmitClick() {
        H.logD("submit review clicked");

        UserReview userReview = new UserReview();
        userReview.setRatingAtmosphere((int)atmosphereBar.getRating() * 2);
        userReview.setRatingCleanliness((int) cleanlinessBar.getRating() * 2);
        userReview.setRatingValue((int)valueBar.getRating() * 2);
        userReview.setRatingFacilities((int)facilitiesBar.getRating() * 2);
        userReview.setRatingLocation((int)locationBar.getRating() * 2);
        userReview.setRatingStaff((int)staffBar.getRating() * 2);

        userReview.setBookingReference(booking.getReferenceNumber());
        userReview.setTitle(titleText.getText().toString());
        userReview.setReview(reviewText.getText().toString());

        H.showLoading();
        API.getInstance(App.getInstance()).postPropertyReview(userReview, new API.IGetRealTimeObject() {
            @Override
            public void getData(Object object) {
                H.hideLoading();
                H.runOnUi(new Runnable() {
                    @Override
                    public void run() {
                        App.getInstance().getCurrentActivity().hideLoadingAndBack(1);
                        App.getInstance().showToast(H.getString(R.string.thanks_for_feedback_message));
                    }
                });
            }
            @Override
            public void onError(String message) {
                H.hideLoading();
                H.logE(message);
            }
        });
    }
}
