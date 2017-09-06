package com.mc.youthhostels.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mc.youthhostels.R;
import com.mc.youthhostels.entity.filters.Filters;
import com.mc.youthhostels.events.PropertiesFilteredEvent;
import com.mc.youthhostels.presenters.SearchResultFragmentPresenter;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.math.BigDecimal;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import helper.App;
import helper.H;
import service.Localization;

public class FilterFragment2 extends Fragment {

    @Bind(R.id.txt_price_min)
    TextView priceMin;
    @Bind(R.id.txt_price_max)
    TextView priceMax;

    private RangeSeekBar<Double> rangeSeekBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        ButterKnife.bind(this, view);

        getChildFragmentManager().beginTransaction()
                .add(R.id.fragment_filter_types, new TypeChooserFragment())
                .commit();
        getChildFragmentManager().beginTransaction()
                .add(R.id.fragment_filter_room_types, new RoomTypesChooserFragment())
                .commit();
        getChildFragmentManager().beginTransaction()
                .add(R.id.fragment_filter_amenities, new AmenityChooserFragment())
                .commit();

        Filters filters = SearchResultFragmentPresenter.filters;
        initPriceSeekBar(view, filters);

        return view;
    }

    private void initPriceSeekBar(View view, Filters filters) {
        rangeSeekBar = new RangeSeekBar<>(getActivity());
        rangeSeekBar.setRangeValues(filters.getMinPrice(), filters.getMaxPrice());
        rangeSeekBar.setSelectedMinValue(filters.getSelectedMinPrice());
        rangeSeekBar.setSelectedMaxValue(filters.getSelectedMaxPrice());
        rangeSeekBar.setTextAboveThumbsColorResource(R.color.colorPrimary);
        rangeSeekBar.setTextAboveThumbsColorResource(R.color.transparent);

        String minFormat = H.getString(R.string.filter_minimum);
        String maxFormat = H.getString(R.string.filter_maximum);

        priceMin.setText(String.format(minFormat, Localization.getPriceLocalized(App.getInstance(), BigDecimal.valueOf(filters.getMinPrice()))));
        priceMax.setText(String.format(maxFormat, Localization.getPriceLocalized(App.getInstance(), BigDecimal.valueOf(filters.getMaxPrice()))));

        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Double>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Double minValue, Double maxValue) {
                priceMin.setText(String.format(minFormat,
                                               Localization.getPriceLocalized(App.getInstance(),
                                                                              BigDecimal.valueOf(minValue))));
                priceMax.setText(String.format(maxFormat,
                        Localization.getPriceLocalized(App.getInstance(),
                                BigDecimal.valueOf(maxValue))));
            }
        });

        ((ViewGroup)view.findViewById(R.id.lyt_rsb)).addView(rangeSeekBar);
        H.logD("range seek bar added: " +
                String.valueOf(filters.getSelectedMinPrice()) +
                ", " +
                String.valueOf(filters.getSelectedMaxPrice()));
    }

    @OnClick(R.id.btn_apply_filters)
    public void onApplyClicked() {
        if (rangeSeekBar != null) {
            Filters filters = SearchResultFragmentPresenter.filters;
            filters.setSelectedMinPrice(rangeSeekBar.getSelectedMinValue());
            filters.setSelectedMaxPrice(rangeSeekBar.getSelectedMaxValue());
        } else {
            H.logE("range seek bar is null");
        }

        EventBus.getDefault().post(new PropertiesFilteredEvent());
    }
}
