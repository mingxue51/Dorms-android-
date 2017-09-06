package com.mc.youthhostels.fragments.explore;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mc.youthhostels.MainActivity2;
import com.mc.youthhostels.R;
import com.mc.youthhostels.activities.AppActivity;
import com.mc.youthhostels.events.HereAndTonightClickedEvent;
import com.mc.youthhostels.fragments.SearchFragment;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import api.API;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import entity.Map.Location.CurrentLocations;
import entity.Map.Location.Distance.DistanceHelper;
import entity.Property.Search.SearchProperty;
import helper.App;
import helper.H;

public class ExploreFragment extends Fragment {
    @Bind(R.id.img_main_background)
    ImageView imgMainBackground;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showSearchFragment();
        setRandomBackground();
    }

    private void updateBackground() {
        ConnectivityManager cm = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if( ni != null && ni.getType() != ConnectivityManager.TYPE_WIFI ) {
            return;
        }

        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            H.logD("permission to retrieve background image not granted");
            return;
        }

        API.getInstance(App.getInstance()).GetCurrentLocationByGeocodes(DistanceHelper.getCurrentLocation(), new API.IGetRealTimeObject() {
            @Override
            public void getData(final Object object) {
                CurrentLocations currentLocations = (CurrentLocations) object;

                String cityImage = currentLocations.getCityImage();

                H.logD("city image fetched:" + cityImage);

                if (TextUtils.isEmpty(cityImage) || getView() == null) {
                    return;
                }

                H.runOnUi(() -> Glide.with(App.getInstance()).load(cityImage)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(imgMainBackground));
            }

            @Override
            public void onError(String message) {
            }
        });
    }

    private void showSearchFragment() {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.search_fragment_content,
                        SearchFragment.newInstance(new SearchProperty(), false))
                .commit();
    }

    @OnClick(R.id.btn_here_and_tonight)
    public void onHereAndTonightClicked() {
        // SearchPresenter handles this event
        EventBus.getDefault().post(new HereAndTonightClickedEvent());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateBackground();
    }

    private void setRandomBackground() {
        List<Drawable> images = Arrays.asList(H.getDrawable(R.drawable.main_background_1),
                                              H.getDrawable(R.drawable.main_background_2));
        imgMainBackground.setImageDrawable(H.getRandomFromList(images));
    }

    @Override
    public void onResume() {
        super.onResume();
        H.logD("explore fragment resume");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
