package com.mc.youthhostels.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mc.youthhostels.R;
import com.mc.youthhostels.activities.AppActivity;
import com.mc.youthhostels.adapter.FavouriteAdapter;
import com.mc.youthhostels.adapter.PropertyAdapter;
import com.mc.youthhostels.presenters.FavouritePresenter;
import com.mc.youthhostels.views.FavouriteView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import entity.Property.Property;
import entity.Property.Search.SearchProperty;
import helper.App;
import helper.H;

public class FavouriteFragment extends Fragment implements FavouriteView {
    @Bind(R.id.list_properties)
    RecyclerView listProperties;

    private FavouriteAdapter favouriteAdapter;
    private FavouritePresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new FavouritePresenter(this);
        presenter.onCreate();

        favouriteAdapter = new FavouriteAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listProperties.setLayoutManager(layoutManager);
        listProperties.setAdapter(favouriteAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        AppActivity activity = (AppActivity) getActivity();
        activity.setDefaultActionBar();
        activity.setActionBarTitle(H.getString(R.string.my_favorites));

        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void showFavourites(List<Property> properties) {
        if (getView() == null) {
            return;
        }

        // todo handle 0 results
        favouriteAdapter.setProperties(properties);
        favouriteAdapter.notifyDataSetChanged();
    }

    @Override
    public void refresh() {
        favouriteAdapter.notifyDataSetChanged();
        H.logD("favourite adapter notify called");
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
    public void showFavoriteDialog(SearchProperty searchProperty, Property property) {
        FragmentManager fm = getChildFragmentManager();
        EditFavFragment editFavFragment = EditFavFragment.newInstance(searchProperty, property);
        editFavFragment.show(fm, "fragment_edit_name");
    }
}
