package com.mc.youthhostels.fragments.booking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mc.youthhostels.MainActivity2;
import com.mc.youthhostels.R;
import com.mc.youthhostels.activities.AppActivity;
import com.mc.youthhostels.fragments.CurrencyChooserFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import entity.Generic.Currency;
import entity.Map.Location.Distance.Distance;
import helper.H;

public class SettingsFragment extends Fragment {

    @Bind(R.id.txt_lang_select)
    TextView language;

    @Bind(R.id.txt_currency)
    TextView currency;

    @Bind(R.id.kilometers_choose)
    TextView kilometers;

    @Bind(R.id.txt_miles_choose)
    TextView miles;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currency.setText(Currency.getCurrency(getContext()).getName());
    }

    @Override
    public void onResume() {
        super.onResume();
        H.logD("on resume in setting fragment");
        AppActivity activity = (AppActivity) getActivity();
        activity.setDefaultActionBar();
        activity.setActionBarTitle(H.getString(R.string.settings));
        final String distanceType = Distance.getDistanceType(getContext());
        final Distance.Unit byType = Distance.Unit.getByType(distanceType);
        if (byType == Distance.Unit.KM) {
            chooseKm();
        } else {
            chooseMill();
        }
    }

    @OnClick(R.id.kilometers_choose)
    void chooseKm() {
        kilometers.setTextColor(H.getColor(R.color.white));
        kilometers.setBackground(H.getDrawable(R.drawable.settings_choose_bg));
        miles.setTextColor(H.getColor(R.color.colorPrimary));
        miles.setBackgroundColor(H.getColor(R.color.white));
        Distance.setDistanceType(getContext(), Distance.Unit.KM.getShortName());
        // todo for test purpose
        H.paymentFakeMode = true;
    }

    @OnClick(R.id.txt_miles_choose)
    void chooseMill() {
        miles.setTextColor(H.getColor(R.color.white));
        miles.setBackground(H.getDrawable(R.drawable.settings_choose_bg));
        kilometers.setTextColor(H.getColor(R.color.colorPrimary));
        kilometers.setBackgroundColor(H.getColor(R.color.white));
        Distance.setDistanceType(getContext(), Distance.Unit.MILES.getShortName());
    }

    @OnClick(R.id.txt_currency)
    public void onCurrencyClick() {
        ((AppActivity)getActivity()).showFragment(new CurrencyChooserFragment());
    }
}
