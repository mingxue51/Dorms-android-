package com.mc.youthhostels.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.mc.youthhostels.R;
import com.mc.youthhostels.events.details.PropertyMapClicked;
import com.mc.youthhostels.events.details.PropertyReviewsClicked;
import com.mc.youthhostels.fragments.PropertyDetailsFragment;
import com.mc.youthhostels.fragments.details.ReviewsFragment;
import com.mc.youthhostels.fragments.map.MapFragment;
import com.mc.youthhostels.helper.JsonHelper;

import api.API;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import entity.Property.Property;
import entity.Property.Search.SearchProperty;
import entity.User.User;
import helper.App;
import helper.H;
import helper.IGetRequest;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PropertyDetailsActivity2 extends AppActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    @Bind(R.id.slider)
    SliderLayout slider;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    private Property property;
    private SearchProperty searchProperty;

    public static void goTo(Property property, SearchProperty searchProperty) {
        Intent intent = new Intent(App.getInstance(), PropertyDetailsActivity2.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Property.BUNDLE, property);
        bundle.putSerializable(SearchProperty.BUNDLE, searchProperty);
        intent.putExtras(bundle);

        App.getInstance().getCurrentActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            property = ((Property) bundle.getSerializable(Property.BUNDLE));
            searchProperty = ((SearchProperty) bundle.getSerializable(SearchProperty.BUNDLE));

            JsonHelper.save(property);
            JsonHelper.save(searchProperty);
            H.logD("property, searchproperty saved");
        }

        if (property == null) {
            property = (Property)JsonHelper.restore(Property.class);
            H.logD("property restored");
        }

        if (searchProperty == null) {
            searchProperty = (SearchProperty)JsonHelper.restore(SearchProperty.class);
            H.logD("searchproperty restored");
        }

        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), "property.images");
        supportPostponeEnterTransition();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                onBackPressed();
            }
        });

        property.setDepartureDate(searchProperty.getDepartureDate());
        property.setArrivalDate(searchProperty.getArrivalDate());

        setFabImage(property.isFavourite());

        collapsingToolbar.setTitle(property.getPropertyName());
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        initSlider();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_content, PropertyDetailsFragment.newInstance(property))
                .commit();
    }

    private void initSlider() {
        for (String url : property.getBigImages()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    //.description(name)
                    .image(url)
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);

            slider.addSlider(textSliderView);
        }

        slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(1500);
        slider.addOnPageChangeListener(this);
        slider.stopAutoCycle();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        H.logD("on slide click");
    }

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     *                             Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    @Override
    public void onPageSelected(int position) {

    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPagerEx#SCROLL_STATE_IDLE
     * @see ViewPagerEx#SCROLL_STATE_DRAGGING
     * @see ViewPagerEx#SCROLL_STATE_SETTLING
     */
    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @OnClick(R.id.btn_book_now)
    public void bookNow() {
        BookingActivity.goTo(property, searchProperty);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(PropertyReviewsClicked event) {
        AppBarLayout appBarLayout = (AppBarLayout)findViewById(R.id.app_bar_layout);
        appBarLayout.setExpanded(false);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_content, ReviewsFragment.newInstance(searchProperty))
                .addToBackStack(null)
                .commit();
    }

    public void onEvent(PropertyMapClicked event) {
        AppBarLayout appBarLayout = (AppBarLayout)findViewById(R.id.app_bar_layout);
        appBarLayout.setExpanded(false);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_content, MapFragment.newInstance(property, false))
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @OnClick(R.id.fab)
    void fabClick() {
        if (!User.isLogged()) {
            Toast.makeText(App.getInstance(), H.getString(R.string.please_sign_in),
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (!property.isFavourite()) {
            App.getInstance().showLoading();
            addToFavorite();
        } else {
            App.getInstance().showLoading();
            deleteFromFavorite();
        }
    }

    private void addToFavorite() {
        API.getInstance(App.getInstance()).addToFavourite(property, new API.IGetRealTimeObject() {
            @Override
            public void getData(Object object) {
                if (property != null) {
                    property.setFavourite(true);
                    property.setFavouriteId(((String) object));
                    H.logD("property added to favourite");
                    setFabImage(true);
                    App.getInstance().hideLoading();
                }
            }

            @Override
            public void onError(String message) {
                App.getInstance().hideLoading();
                H.logE(message);
            }
        });
    }

    private void deleteFromFavorite() {
        API.getInstance(App.getInstance()).deleteFavorite(property, new IGetRequest() {
            @Override
            public void onSuccess(String message) {
                if (property != null) {
                    property.setFavourite(false);
                    H.logD("property deleted from favourites");
                    setFabImage(false);
                    App.getInstance().hideLoading();
                }
            }

            @Override
            public void onError(String message) {
                App.getInstance().hideLoading();
            }
        });
    }

    private void setFabImage(boolean isFavourite) {
        if (fab == null) {
            return;
        }
        if (isFavourite) {
            H.runOnUi(() -> fab.setImageResource(R.drawable.icon_favourite_white));
            H.logD("new drawable for enabled fab state set");
        } else {
            H.runOnUi(() -> fab.setImageResource(R.drawable.icon_favourite_disabled));
            H.logD("new drawable for disabled fab state set");
        }
    }
}

