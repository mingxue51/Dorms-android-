package com.mc.youthhostels.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mc.youthhostels.R;
import com.mc.youthhostels.entity.UserReview;
import com.mc.youthhostels.managers.WrappableGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import entity.User.UserProfile;
import helper.App;
import helper.H;

public class UserReviewsAdapter extends RecyclerView.Adapter<UserReviewsAdapter.ViewHolder> {

    private List<UserReview> reviews = new ArrayList<>();

    public UserReviewsAdapter() {
    }

    @Override
    public UserReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(App.getInstance().getCurrentActivity())
                                  .inflate(R.layout.item_user_property_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserReviewsAdapter.ViewHolder holder, int position) {
        UserReview userReview = reviews.get(position);

        WrappableGridLayoutManager layoutManager = new WrappableGridLayoutManager(App.getInstance(),
                2,
                false);

        holder.list.setLayoutManager(layoutManager);
        UserRatingAdapter adapter = new UserRatingAdapter(getRatings(userReview));
        holder.list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (TextUtils.isEmpty(userReview.getTitle())) {
            holder.reviewTitle.setVisibility(View.GONE);
        } else {
            holder.reviewTitle.setVisibility(View.VISIBLE);
            holder.reviewTitle.setText(userReview.getTitle());
        }

        holder.review.setText(userReview.getReview());

        holder.authorName.setText(UserProfile.getUserProfile().getFullName());

        Glide.with(App.getInstance()).load(userReview.getPropertyPreview())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.propertyPreviewImg);

        holder.propertyName.setText(userReview.getPropertyName());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void setReviews(List<UserReview> reviews) {
        this.reviews = reviews;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txt_author_name)
        TextView authorName;
        @Bind(R.id.txt_property_name)
        TextView propertyName;
        @Bind(R.id.txt_property_rating)
        TextView propertyRating;
        @Bind(R.id.txt_review_text)
        TextView review;
        @Bind(R.id.txt_review_title)
        TextView reviewTitle;
        @Bind(R.id.list_ratings)
        RecyclerView list;
        @Bind(R.id.img_property_preview)
        ImageView propertyPreviewImg;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class Rating {
        private int typeId;
        private float value;

        public Rating(int typeId, float value) {
            this.typeId = typeId;
            this.value = value;
        }

        public int getTypeId() {
            return typeId;
        }

        public float getValue() {
            return value;
        }
    }

    private List<Rating> getRatings(UserReview userReview) {
        List<Rating> ratings = new ArrayList<>();
        ratings.add(new Rating(R.string.atmosphere, (float)userReview.getRatingAtmosphere()/ 2));
        ratings.add(new Rating(R.string.cleanliness, (float)userReview.getRatingCleanliness()/ 2));
        ratings.add(new Rating(R.string.facilities, (float)userReview.getRatingFacilities()/ 2));
        ratings.add(new Rating(R.string.location, (float)userReview.getRatingLocation()/ 2));
        ratings.add(new Rating(R.string.staff, (float)userReview.getRatingStaff() / 2));
        // todo add it later
        //ratings.add(new Rating(R.string.safety, (float)userReview.getRatingSafety()/ 2));
        ratings.add(new Rating(R.string.value, (float)userReview.getRatingValue()/ 2));
        return ratings;
    }

    class UserRatingAdapter extends RecyclerView.Adapter<UserRatingAdapter.RatingViewHolder> {
        List<Rating> ratings = new ArrayList<>();

        public UserRatingAdapter(List<Rating> ratings) {
            this.ratings = ratings;
        }

        @Override
        public RatingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(App.getInstance().getCurrentActivity())
                                      .inflate(R.layout.item_user_rating, parent, false);
            return new RatingViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RatingViewHolder holder, int position) {
            Rating rating = ratings.get(position);
            H.setStarColor(holder.ratingBar, H.getColor(R.color.gray), H.getColor(R.color.stars));
            holder.ratingBar.setRating(rating.getValue());
            holder.type.setText(H.getString(rating.getTypeId()));
        }

        @Override
        public int getItemCount() {
            return ratings.size();
        }

        class RatingViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.txt_rating_type)
            TextView type;
            @Bind(R.id.rating_bar)
            RatingBar ratingBar;

            public RatingViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}
