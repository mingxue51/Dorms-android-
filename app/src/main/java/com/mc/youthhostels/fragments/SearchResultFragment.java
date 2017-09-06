package com.mc.youthhostels.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mc.youthhostels.R;
import com.mc.youthhostels.adapter.PropertyAdapter;
import com.mc.youthhostels.events.FiltersButtonClickedEvent;
import com.mc.youthhostels.events.SortButtonClickedEvent;
import com.mc.youthhostels.presenters.SearchResultFragmentPresenter;
import com.mc.youthhostels.views.SearchResultView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import entity.Property.Properties;
import entity.Property.Property;
import entity.Property.Search.SearchProperty;
import helper.App;
import helper.H;

public class SearchResultFragment extends Fragment implements SearchResultView {

    public static Properties test;
    public static final String TAG = SearchResultFragment.class.getSimpleName();

    @Bind(R.id.list_properties)
    RecyclerView listProperties;

    private PropertyAdapter propertyAdapter;
    private SearchResultFragmentPresenter searchResultPresenter;
    private Properties properties;
    private SearchProperty searchProperty;
    private LinearLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_results, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutManager = new LinearLayoutManager(getActivity());
        listProperties.setLayoutManager(layoutManager);
        listProperties.setAdapter(propertyAdapter);

        listProperties.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (getChildFragmentManager().getBackStackEntryCount() > 0) {
                    hideRefineSearch();
                }
            }
        });

        showResults(properties.getProperties());
    }

    private void showSearchFragment() {
        getChildFragmentManager().beginTransaction()
                .add(R.id.search_fragment_content,
                     SearchFragment.newInstance(searchProperty, true),
                     SearchFragment.TAG)
                .addToBackStack(SearchFragment.TAG)
                .commit();
        getChildFragmentManager().executePendingTransactions();
        H.logD("search fragment added");
    }

    @Override
    public void onResume() {
        super.onResume();
        searchResultPresenter.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        properties = (Properties) getArguments().getSerializable(Properties.BUNDLE);
        test = properties;
        searchProperty = (SearchProperty) getArguments().getSerializable(SearchProperty.BUNDLE);

        propertyAdapter = new PropertyAdapter(getActivity());
        searchResultPresenter = new SearchResultFragmentPresenter(this, searchProperty, properties.getProperties());
        searchResultPresenter.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        searchResultPresenter.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        searchResultPresenter.onPause();
    }

    @Override
    public void showResults(List<Property> properties) {
        propertyAdapter.setProperties(properties);
        propertyAdapter.notifyDataSetChanged();
        layoutManager.scrollToPosition(0);
        H.logD("stories showed");
    }

    @Override
    public void refresh() {
        propertyAdapter.notifyDataSetChanged();
        H.logD("properties refreshed in search result fragment");
    }

    @Override
    public void showLoading() {
        App.getInstance().showLoading();
    }

    @Override
    public void hideLoading() {
        App.getInstance().hideLoading();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_search)
    public void onSearchClick() {
        H.logD("on refine search clicked");
        showSearchFragment();
    }

    @OnClick(R.id.btn_sort)
    public void onSortClick() {
        H.logD("on sort method clicked");
        EventBus.getDefault().post(new SortButtonClickedEvent());
    }

    @OnClick(R.id.btn_filters)
    public void onFiltersClick() {
        H.logD("on sort method clicked");
        EventBus.getDefault().post(new FiltersButtonClickedEvent());
    }

    @Override
    public void hideRefineSearch() {
        getChildFragmentManager().popBackStack();
    }

    @Override
    public boolean isRefineSearchOnScreen() {
        return getChildFragmentManager().getBackStackEntryCount() > 0;
    }

    @Override
    public void showFavoriteDialog(SearchProperty searchProperty, Property property) {
        FragmentManager fm = getChildFragmentManager();
        EditFavFragment editFavFragment = EditFavFragment.newInstance(searchProperty, property);
        editFavFragment.show(fm, "fragment_edit_name");
    }
}
