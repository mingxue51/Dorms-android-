package com.mc.youthhostels.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mc.youthhostels.CalendarFragment;
import com.mc.youthhostels.R;
import com.mc.youthhostels.presenters.SearchPresenter;
import com.mc.youthhostels.views.SearchView;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import entity.Property.Property;
import entity.Property.Search.SearchProperty;
import helper.App;
import helper.DT;
import helper.H;

public class EditFavFragment extends DialogFragment implements SearchView {

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

    @Bind(R.id.layout_modify)
    View modifyBoard;
    @Bind(R.id.layout_add)
    View addBoard;

    @Bind(R.id.txt_note)
    EditText note;

    private SearchPresenter searchPresenter;
    private Property property;

    public EditFavFragment() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modify_fav, container);
        ButterKnife.bind(this, view);

        SearchProperty searchProperty = null;
        if (savedInstanceState == null) {
            searchProperty = (SearchProperty)getArguments().getSerializable(SearchProperty.BUNDLE);
            property = (Property)getArguments().getSerializable(Property.BUNDLE);
        }
        if (searchProperty == null) {
            searchProperty = new SearchProperty();
        }
        searchPresenter = new SearchPresenter(this, searchProperty);
        searchPresenter.onCreate();

        return view;
    }

    @OnClick(R.id.lyt_dates)
    public void onDatesClick(View v) {
        CalendarFragment.newInstance(searchPresenter.getArrivalDate(),
                searchPresenter.getDepartureDate())
                .show(getChildFragmentManager(), CalendarFragment.TAG);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchPresenter.refresh();

        if (property == null) {
            H.logE("property is null");
            return;
        }

        if (!TextUtils.isEmpty(property.getFavouriteNote())) {
            note.setText(property.getFavouriteNote());
        }

        if (property.getFavouriteId() != null) {
            modifyBoard.setVisibility(View.VISIBLE);
            addBoard.setVisibility(View.GONE);
        } else {
            modifyBoard.setVisibility(View.GONE);
            addBoard.setVisibility(View.VISIBLE);
        }
    }

    public static EditFavFragment newInstance(SearchProperty searchProperty, Property property) {
        EditFavFragment editFavFragment = new EditFavFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SearchProperty.BUNDLE, searchProperty);
        bundle.putSerializable(Property.BUNDLE, property);

        editFavFragment.setArguments(bundle);
        return editFavFragment;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        searchPresenter.onDestroy();
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
    }

    @Override
    public void showOnlyDates() {
    }

    @Override
    public void showLocationInputDialog() {
    }

    @Override
    public void closeLocationInputDialog() {
    }

    @Override
    public void setAdditionBoardVisible(boolean visible) {
    }

    @Override
    public void showLoading() {
        App.getInstance().showLoading();
    }

    @Override
    public void hideLoading() {
        App.getInstance().showLoading();
    }

    @OnClick(R.id.btn_add)
    public void onAddButtonClicked() {
        property.setFavouriteNote(note.getText().toString());
        searchPresenter.onAddFavouriteClicked(property);
    }

    @OnClick(R.id.btn_save)
    public void onSaveButtonClicked() {
        onAddButtonClicked();
    }

    @OnClick(R.id.btn_delete)
    public void onDeleteButtonClicked() {
        searchPresenter.onDeleteFavouriteClicked(property);
    }

    @Override
    public void hide() {
        dismiss();
    }
}
