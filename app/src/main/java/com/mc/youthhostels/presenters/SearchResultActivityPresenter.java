package com.mc.youthhostels.presenters;

import com.mc.youthhostels.R;
import com.mc.youthhostels.SearchResultActivity;
import com.mc.youthhostels.events.CurrencyChangedEvent;
import com.mc.youthhostels.events.FiltersButtonClickedEvent;
import com.mc.youthhostels.events.PropertiesFilteredEvent;
import com.mc.youthhostels.events.PropertiesFoundEvent;
import com.mc.youthhostels.events.SortButtonClickedEvent;
import com.mc.youthhostels.events.SortMethodSelectedEvent;
import com.mc.youthhostels.fragments.FilterFragment2;
import com.mc.youthhostels.fragments.SortChooserFragment;
import com.mc.youthhostels.model.SortMethod;
import com.mc.youthhostels.views.SearchResultActivityView;

import api.API;
import de.greenrobot.event.EventBus;
import entity.Generic.Currency;
import entity.Property.Properties;
import entity.Property.Search.SearchProperty;
import helper.App;
import helper.H;

import static entity.Property.Search.SearchProperty.SearchSource.CITY;

public class SearchResultActivityPresenter implements Presenter {

    private SearchResultActivityView searchResultActivityView;
    private SearchProperty searchProperty;

    public SearchResultActivityPresenter(SearchResultActivityView searchResultActivityView, SearchProperty searchProperty) {
        this.searchResultActivityView = searchResultActivityView;
        this.searchProperty = searchProperty;
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

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(PropertiesFoundEvent event) {
        SearchProperty searchProperty = event.getSearchProperty();
        searchResultActivityView.updateActionBar(searchProperty.getCity(), searchProperty.getDateInfo());
        this.searchProperty = searchProperty;
    }

    public void onEvent(SortMethodSelectedEvent event) {
        searchResultActivityView.closeChooser();
        searchResultActivityView.updateActionBar(searchProperty.getCity(), searchProperty.getDateInfo());
    }

    public void onEvent(PropertiesFilteredEvent event) {
        searchResultActivityView.closeChooser();
        searchResultActivityView.updateActionBar(searchProperty.getCity(), searchProperty.getDateInfo());
    }

    public void onEvent(SortButtonClickedEvent event) {
        searchResultActivityView.showFragment(new SortChooserFragment(), R.string.title_sort);
    }

    public void onEvent(FiltersButtonClickedEvent event) {
        SearchResultFragmentPresenter.filters.sync();
        searchResultActivityView.showFragment(new FilterFragment2(), R.string.filters);
    }

    public void onEvent(CurrencyChangedEvent event) {
        Currency.setCurrency(App.getInstance(), event.getCurrency());

        searchResultActivityView.closeChooser();
        searchResultActivityView.updateActionBar(searchProperty.getCity(), searchProperty.getDateInfo());

        searchResultActivityView.showLoading();

        API.getInstance(App.getInstance()).GetPropertiesByLocationAndDate(searchProperty, new API.IGetRealTimeObject() {
            @Override
            public void getData(final Object resultProperties) {
                H.runOnUi(new Runnable() {
                    @Override
                    public void run() {
                        searchResultActivityView.hideLoading();
                        Properties properties = (Properties) resultProperties;

                        searchProperty.setSearchSource(CITY);

                        if (properties.getProperties().size() > 0) {
                            searchProperty.setCountry(properties.getCityInfo().getCountry());
                            searchProperty.setCity(properties.getCityInfo().getCity());
                        }

                        EventBus.getDefault().post(new PropertiesFoundEvent(searchProperty, properties));
                    }
                });
            }

            @Override
            public void onError(String message) {
            }
        });
    }
}
