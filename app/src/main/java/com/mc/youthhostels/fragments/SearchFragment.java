package com.mc.youthhostels.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mc.youthhostels.CalendarFragment;
import com.mc.youthhostels.R;
import com.mc.youthhostels.dialog.SearchPlaceDialogN;
import com.mc.youthhostels.presenters.SearchPresenter;
import com.mc.youthhostels.views.SearchView;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import entity.Property.Search.SearchProperty;
import helper.App;
import helper.DT;
import helper.H;

public class SearchFragment extends Fragment implements SearchView {
    private static final String SHOW_ADDITION_BOARD_TAG = "show_addition_board";
    public static final String TAG = SearchFragment.class.getSimpleName();

    @Bind(R.id.check_in_day)
    TextView checkInDay;
    @Bind(R.id.check_in_month)
    TextView checkInMonth;
    @Bind(R.id.check_in_week_day)
    TextView checkInWeekDay;
    @Bind(R.id.check_out_day)
    TextView checkOutDay;
    @Bind(R.id.check_out_month)
    TextView checkOutMonth;
    @Bind(R.id.check_out_week_day)
    TextView checkOutWeekDay;
    @Bind(R.id.search_city_name)
    TextView searchCityName;
    @Bind(R.id.layout_search_addition_board)
    View additionBoard;

    private SearchPresenter searchPresenter;
    private SearchPlaceDialogN searchPlaceDialog;
    private boolean showAdditionBoard;

    public static SearchFragment newInstance(SearchProperty searchProperty,
                                             boolean showAdditionBoard) {
        SearchFragment searchFragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SearchProperty.BUNDLE, searchProperty);
        bundle.putBoolean(SHOW_ADDITION_BOARD_TAG, showAdditionBoard);
        searchFragment.setArguments(bundle);
        return searchFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        SearchProperty searchProperty = null;

        if (savedInstanceState == null) {
            searchProperty = (SearchProperty)getArguments().getSerializable(SearchProperty.BUNDLE);
            showAdditionBoard = getArguments().getBoolean(SHOW_ADDITION_BOARD_TAG);
        }

        if (searchProperty == null) {
            searchProperty = new SearchProperty();
        }

        searchPresenter = new SearchPresenter(this, searchProperty);
        searchPresenter.onCreate();
        searchPlaceDialog = new SearchPlaceDialogN();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchPresenter.refresh();
        if (showAdditionBoard) {
            additionBoard.setVisibility(View.VISIBLE);
        } else {
            additionBoard.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.lyt_dates)
    public void onDatesClick(View v) {
        CalendarFragment.newInstance(searchPresenter.getArrivalDate(),
                searchPresenter.getDepartureDate())
                .show(getChildFragmentManager(), CalendarFragment.TAG);
    }

    @OnClick(R.id.search_header_id)
    public void onSearchHeaderClicked(View v) {
        if (!App.getInstance().isNetworkAvailable()) {
            App.getInstance().hideLoading();
            H.showDialog(H.getString(R.string.no_internet_connection));
            return;
        }

        H.logD("show search input dialog");
        showLocationInputDialog();
    }

    @OnClick(R.id.img_magnifier)
    public void onSearchHeaderMagnifierClicked(View v) {
        onSearchHeaderClicked(v);
    }

    @OnClick(R.id.btn_search)
    public void onSearch(View v) {
        H.logD("on search button clicked");
        searchPresenter.onSearchButtonClicked();
    }

    @OnClick(R.id.btn_current_location)
    public void onCurrentLocationClicked() {
        H.logD("on current location clicked");
        searchPresenter.onCurrentLocationClicked();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

        searchPresenter.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        searchPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        searchPresenter.onPause();
    }

    private void setCheckInDate(Date checkIn) {
        checkInDay.setText(DT.formatDay(checkIn));
        checkInMonth.setText(DT.formateDate(checkIn).toUpperCase());
        checkInWeekDay.setText(DT.formateWeekDate(checkIn));
    }

    private void setCheckOutDate(Date checkOut) {
        checkOutDay.setText(DT.formatDay(checkOut));
        checkOutMonth.setText(DT.formateDate(checkOut).toUpperCase());
        checkOutWeekDay.setText(DT.formateWeekDate(checkOut));
    }

    @Override
    public void setDates(Date checkIn, Date checkOut) {
        setCheckInDate(checkIn);
        setCheckOutDate(checkOut);
    }

    @Override
    public void setLocationText(String location) {
        if (!TextUtils.isEmpty(location)) {
            searchCityName.setText(location);
        } else {
            H.logD("location text in input search is empty");
        }
    }

    @Override
    public void showOnlyDates() {
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
    public void showLocationInputDialog() {
        searchPlaceDialog.show(getFragmentManager().beginTransaction(),
                SearchPlaceDialogN.TAG);
    }

    @Override
    public void setAdditionBoardVisible(boolean visible) {
        if (visible) {
            additionBoard.setVisibility(View.VISIBLE);
        } else {
            additionBoard.setVisibility(View.GONE);
        }
    }

    @Override
    public void hide() {
        // no need for this fragment
    }

    @Override
    public void closeLocationInputDialog() {
        searchPlaceDialog.dismiss();
    }
}
