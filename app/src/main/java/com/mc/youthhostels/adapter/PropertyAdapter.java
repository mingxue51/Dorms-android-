package com.mc.youthhostels.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mc.youthhostels.R;
import com.mc.youthhostels.events.AddToFavouriteClickedEvent;
import com.mc.youthhostels.events.PropertyPreviewClicked;
import com.mc.youthhostels.events.booking.CheckAvailabilityClickedEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import entity.Image.ImageUrl;
import entity.Property.Property;
import helper.H;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.ViewHolder> {
    private List<Property> properties = new ArrayList<>();
    private Context context;

    public PropertyAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_property, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Property property = properties.get(position);

        holder.propertyName.setText(property.getPropertyName());
        holder.propertyType.setText(property.getPropertyType());

        int overallRating = property.getPropertyRatings().getOverall();
        holder.ratingBoard.setVisibility(overallRating == 0 ? View.GONE : View.VISIBLE);
        if (overallRating != 0) {
            holder.propertyMark.setText(String.format("%s %s%%",property.getPropertyRatings().getRating(),
                    String.valueOf(overallRating)));
        }

        holder.cardProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new PropertyPreviewClicked(property));
            }
        });

        List<ImageUrl> mainImages = property.getImages().getMainImages();
        String url;
        if (mainImages != null && mainImages.size() > 0) {
            url = mainImages.get(0).getUrl();
        } else {
            url = property.getBigPreview();
        }

        if (url != null) {
            Glide.with(context).load(url).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.preview);
        } else {
            H.logE("url for property preview is null in property adapter");
        }

        holder.addToFavouriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new AddToFavouriteClickedEvent(property));
            }
        });

        holder.checkAvailabilityBtn.setOnClickListener(v -> {
            EventBus.getDefault().post(new CheckAvailabilityClickedEvent(property));
        });

        holder.checkAvailabilityBtn.setVisibility(View.GONE);

        if (property.isFavourite()) {
            holder.addToFavouriteBtn.setImageDrawable(H.getDrawable(R.drawable.icon_favourite_enabled));
        } else {
            holder.addToFavouriteBtn.setImageDrawable(H.getDrawable(R.drawable.icon_favourite_disabled));
        }

        holder.dorms.setVisibility(View.GONE);
        holder.privateRooms.setVisibility(View.GONE);

        if (property.isDorms()) {
            holder.dorms.setVisibility(View.VISIBLE);
            holder.priceDorms.setText(property.getDormPriceString());
        }

        if (property.isPrivateRooms()) {
            holder.privateRooms.setVisibility(View.VISIBLE);
            holder.pricePrivate.setText(property.getPrivatePriceString());
        }

        String freeAmenities = property.getFreeAmenities();
        if (TextUtils.isEmpty(freeAmenities)) {
            holder.freeAmenities.setVisibility(View.GONE);
            holder.freeAmenities.setText("");
            holder.freeTag.setVisibility(View.GONE);
        } else {
            holder.freeAmenities.setText(freeAmenities);
            holder.freeAmenities.setVisibility(View.VISIBLE);
            holder.freeTag.setVisibility(View.VISIBLE);
        }
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    @Override
    public int getItemCount() {
        return properties.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txt_property_name)
        TextView propertyName;
        @Bind(R.id.txt_property_type)
        TextView propertyType;
        @Bind(R.id.txt_property_mark)
        TextView propertyMark;
        @Bind(R.id.img_property_item_preview_big)
        ImageView preview;

        @Bind(R.id.card_property)
        View cardProperty;

        @Bind(R.id.txt_price_search_result_value_dorms)
        TextView priceDorms;
        @Bind(R.id.txt_price_search_result_value_private)
        TextView pricePrivate;

        @Bind(R.id.lyt_dorms)
        View dorms;
        @Bind(R.id.lyt_private)
        View privateRooms;

        @Bind(R.id.lyt_rating)
        View ratingBoard;

        @Bind(R.id.btn_add_to_favourite)
        ImageView addToFavouriteBtn;

        @Bind(R.id.btn_check_availability)
        Button checkAvailabilityBtn;

        @Bind(R.id.txt_free_amenities)
        TextView freeAmenities;

        @Bind(R.id.card_free_tag)
        View freeTag;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
