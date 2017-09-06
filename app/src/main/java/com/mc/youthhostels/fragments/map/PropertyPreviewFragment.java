package com.mc.youthhostels.fragments.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mc.youthhostels.R;
import com.mc.youthhostels.events.PropertyPreviewClicked;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import entity.Property.Property;
import helper.App;

public class PropertyPreviewFragment extends Fragment {
    @Bind(R.id.txt_property_name)
    TextView propertyName;
    @Bind(R.id.txt_property_mark)
    TextView propertyMark;
    @Bind(R.id.img_property_preview)
    ImageView preview;
    @Bind(R.id.lyt_dorms)
    View dorms;
    @Bind(R.id.lyt_private)
    View privateRooms;
    @Bind(R.id.txt_price_search_result_value_dorms)
    TextView priceDorms;
    @Bind(R.id.txt_price_search_result_value_private)
    TextView pricePrivate;
    @Bind(R.id.id_star)
    ImageView star;

    private Property property;

    public static PropertyPreviewFragment newInstance(Property property) {
        PropertyPreviewFragment fragment = new PropertyPreviewFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(Property.BUNDLE, property);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            property = (Property) arguments.getSerializable(Property.BUNDLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_property_preview, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        propertyName.setText(property.getPropertyName());

        int overall = property.getPropertyRatings().getOverall();
        if (overall == 0) {
            hideRating();
        } else {
            propertyMark.setText(String.format("%s %s%%", property.getPropertyRatings().getRating(),
                    String.valueOf(overall)));
        }

//        holder.cardProperty.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EventBus.getDefault().post(new SearchResultPropertyClickedEvent(property));
//            }
//        });

        String url = property.getImages().getMainImages().get(0).getUrl();
        Glide.with(App.getInstance()).load(url).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.RESULT).into(preview);

        if (!property.isDorms()) {
            dorms.setVisibility(View.GONE);
        } else {
            priceDorms.setText(property.getDormPriceString());
        }

        if (!property.isPrivateRooms()) {
            privateRooms.setVisibility(View.GONE);
        } else {
            pricePrivate.setText(property.getPrivatePriceString());
        }

        if (property.isDorms() && property.isPrivateRooms()) {
            hideRating();
        }
    }

    private void hideRating() {
        propertyMark.setVisibility(View.GONE);
        star.setVisibility(View.GONE);
    }

    @OnClick(R.id.lyt_property_preview)
    public void onPreviewClicked() {
        EventBus.getDefault().post(new PropertyPreviewClicked(property));
    }

    @OnClick(R.id.btn_book_now)
    public void onBookNowClicked() {
        EventBus.getDefault().post(new PropertyPreviewClicked(property));
    }
}
