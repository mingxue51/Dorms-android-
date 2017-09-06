package com.mc.youthhostels.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mc.youthhostels.R;
import com.mc.youthhostels.model.PropertyReview;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import helper.App;


public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private List<PropertyReview> reviews = new ArrayList<>();

    public ReviewsAdapter() {
    }

    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(App.getInstance().getCurrentActivity())
                                  .inflate(R.layout.item_property_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ViewHolder holder, int position) {
        PropertyReview review = reviews.get(position);
        holder.authorName.setText(review.getAuthorName());
        holder.date.setText(review.getDate());
        holder.overallRating.setText(String.valueOf((int)review.getRating()) + "%");
        holder.reviewText.setText(review.getReview());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void setReviews(List<PropertyReview> reviews) {
        this.reviews = reviews;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txt_author_name)
        TextView authorName;
        @Bind(R.id.txt_date)
        TextView date;
        @Bind(R.id.txt_overall_rating)
        TextView overallRating;
        @Bind(R.id.txt_review_text)
        TextView reviewText;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
