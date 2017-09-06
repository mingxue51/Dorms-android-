package com.mc.youthhostels.presenters;

import android.content.Intent;
import android.support.v4.content.IntentCompat;

import com.mc.youthhostels.MainActivity2;
import com.mc.youthhostels.SearchResultActivity;
import com.mc.youthhostels.activities.AppActivity;
import com.mc.youthhostels.activities.PropertyDetailsActivity2;
import com.mc.youthhostels.events.CurrencyChangedEvent;
import com.mc.youthhostels.events.PropertiesFoundEvent;
import com.mc.youthhostels.events.PropertySearchFoundEvent;
import com.mc.youthhostels.events.booking.DoneClickedEvent;
import com.mc.youthhostels.events.booking.FacebookLoginCompletedEvent;
import com.mc.youthhostels.views.MainView;

import de.greenrobot.event.EventBus;
import entity.Generic.Currency;
import entity.Property.Properties;
import entity.Property.Search.SearchProperty;
import helper.App;
import helper.H;


public class MainActivityPresenter implements Presenter {

    private MainView mainView;

    public MainActivityPresenter(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onResume() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
    }

    public void onEvent(PropertiesFoundEvent event) {
        moveToSearchResultsActivity(event.getProperties(), event.getSearchProperty());
    }

    public void onEvent(PropertySearchFoundEvent event) {
        H.logD("main presenter: move to property details");
        PropertyDetailsActivity2.goTo(event.getProperty(), event.getSearchProperty());
    }

    public void onEvent(FacebookLoginCompletedEvent event) {
        H.runOnUi(() -> mainView.openDesiredFragment());
    }

    public void onEvent(DoneClickedEvent event) {
        AppActivity currentActivity = App.getInstance().getCurrentActivity();
        if (currentActivity != null) {
            currentActivity.onBackPressed();
        }
    }

    public void onEvent(CurrencyChangedEvent event) {
        Currency.setCurrency(App.getInstance(), event.getCurrency());
        mainView.closeChooserFragment();
        mainView.updateMenus();
    }

    private void moveToSearchResultsActivity(Properties properties, SearchProperty searchProperty) {
        H.logD("main presenter: move to search results");
        SearchResultActivity.goTo(App.getInstance().getCurrentActivity(), properties, searchProperty);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
    }
}
