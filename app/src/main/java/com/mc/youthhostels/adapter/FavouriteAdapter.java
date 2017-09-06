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
import com.mc.youthhostels.events.BookFavouriteClickedEvent;
import com.mc.youthhostels.events.PropertyPreviewClicked;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import entity.Image.ImageUrl;
import entity.Property.Property;
import helper.H;

public class FavouriteAdapter  extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {
    private List<Property> properties = new ArrayList<>();
    private Context context;

    public FavouriteAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Property property = properties.get(position);

        holder.propertyName.setText(property.getPropertyName());
        holder.propertyType.setText(property.getPropertyType());

        if (TextUtils.isEmpty(property.getFavouriteNote())) {
            holder.note.setVisibility(View.GONE);
        } else {
            holder.note.setText(property.getFavouriteNote());
            holder.note.setVisibility(View.VISIBLE);
        }

        holder.bookBtn.setOnClickListener(v ->
                EventBus.getDefault().post(new BookFavouriteClickedEvent(property))
        );

        holder.cardProperty.setOnClickListener(v ->
                EventBus.getDefault().post(new PropertyPreviewClicked(property))
        );

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

        holder.modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new AddToFavouriteClickedEvent(property));
            }
        });

        if (property.isFavourite()) {
            holder.addToFavouriteBtn.setImageDrawable(H.getDrawable(R.drawable.icon_favourite_enabled));
            holder.cardProperty.setAlpha(1.0f);
            holder.cardProperty.setEnabled(true);
        } else {
            holder.addToFavouriteBtn.setImageDrawable(H.getDrawable(R.drawable.icon_favourite_disabled));
            holder.cardProperty.setAlpha(0.5f);
            holder.cardProperty.setEnabled(false);
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
        @Bind(R.id.img_property_preview)
        ImageView preview;

        @Bind(R.id.card_property)
        View cardProperty;

        @Bind(R.id.btn_add_to_favourite)
        ImageView addToFavouriteBtn;

        @Bind(R.id.btn_modify)
        Button modifyBtn;

        @Bind(R.id.btn_book)
        Button bookBtn;

        @Bind(R.id.txt_note)
        TextView note;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
