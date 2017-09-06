package com.mc.youthhostels;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.mc.youthhostels.events.DatesChosenEvent;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarPickerView.OnDateSelectedListener;
import com.squareup.timessquare.CalendarPickerView.SelectionMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.greenrobot.event.EventBus;
import helper.DT;

public class CalendarFragment extends DialogFragment {
    public static final String ARG_CHECKIN = "checkindate";
    public static final String ARG_CHECKOUT = "checkoutdate";
    private static final String CURRENT_LISTENER = "currentlistener";
    private static final int MAX_NIGHTS = 30;
    public static final String TAG = "CalendarFragment";
    private CalendarPickerView mCalendar;
    private Date mCheckin;
    private Date mCheckout;
    private int mNumberOfNights;
    private OnDateRangeChosenListener mOnDateRangeChosenListener;
    private Button mSearchButton;

    private Date selectedCheckin;
    private Date selectedCheckout;

    public static CalendarFragment newInstance(Bundle paramBundle, OnDateRangeChosenListener listener) {
        CalendarFragment localCalendarFragment = new CalendarFragment();
        if(listener != null) {
            localCalendarFragment.mOnDateRangeChosenListener = listener;
        }
        localCalendarFragment.setArguments(paramBundle);
        return localCalendarFragment;
    }

    public static CalendarFragment newInstance(Date checkIn, Date checkOut) {
        CalendarFragment calendarFragment = new CalendarFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(CalendarFragment.ARG_CHECKIN, checkIn.getTime());
        bundle.putLong(CalendarFragment.ARG_CHECKOUT, checkOut.getTime());
        calendarFragment.setArguments(bundle);
        return calendarFragment;
    }

    private void updateView() {
        this.mSearchButton.setEnabled(false);
        Dialog localDialog = getDialog();
        if ((this.mCheckin != null) && (this.mCheckout != null)) {
            if (this.mNumberOfNights <= MAX_NIGHTS) {
                Resources localResources = getResources();
                localDialog.setTitle(localResources.getQuantityString(R.plurals.calendar_selected_dates, this.mNumberOfNights, this.mNumberOfNights));
                this.selectedCheckin = this.mCheckin;
                this.selectedCheckout = this.mCheckout;
                this.mCheckin = null;
                this.mCheckout = null;
                this.mSearchButton.setEnabled(true);
                return;
            }
            localDialog.setTitle(getString(R.string.select_checkin));
            this.mCheckin = null;
            this.mCheckout = null;
            this.mNumberOfNights = 0;
            return;
        }
        if (this.mCheckin != null) {
            localDialog.setTitle(getString(R.string.select_checkout));
            return;
        }
        localDialog.setTitle(getString(R.string.select_checkin));
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialogStyle);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        View localView = paramLayoutInflater.inflate(R.layout.fragment_calendar, paramViewGroup, false);
        getDialog().setTitle(getString(R.string.select_dates));
        Calendar localCalendar1 = Calendar.getInstance();
        localCalendar1.add(Calendar.YEAR, 1);
        Calendar localCalendar2 = Calendar.getInstance();
        localCalendar2.add(Calendar.DAY_OF_MONTH, 1);
        Calendar localCalendar3 = Calendar.getInstance();
        localCalendar3.setTime(new Date());
        long l1;
        long l2;
        Date localDate;

        if (paramBundle == null) {
            l1 = getArguments().getLong(ARG_CHECKIN);
            l2 = getArguments().getLong(ARG_CHECKOUT);
        } else {
            l1 = paramBundle.getLong(ARG_CHECKIN);
            l2 = paramBundle.getLong(ARG_CHECKOUT);
        }
        localDate = new Date(l1);
        this.mCalendar = ((CalendarPickerView) localView.findViewById(R.id.calendar_view));
        if ((l1 == 0L) || (l2 == 0L)) {
            if (l1 != 0L) {
                if (DT.isDateInThePast(localDate)) {
                    this.mCheckin = localCalendar3.getTime();
                } else {
                    this.mCheckin = new Date(l1);
                }
                this.mCalendar.init(new Date(), localCalendar1.getTime()).inMode(SelectionMode.RANGE).withSelectedDate(this.mCheckin);
            }
        } else {

            this.mCheckin = new Date(l1);
            this.mCheckout = new Date(l2);

            ArrayList localArrayList = new ArrayList();
            localArrayList.add(this.mCheckin);
            localArrayList.add(this.mCheckout);
            this.mCalendar.init(new Date(), localCalendar1.getTime()).inMode(SelectionMode.RANGE).withSelectedDates(localArrayList);
        }

        this.selectedCheckin = this.mCheckin;
        this.selectedCheckout = this.mCheckout;
        this.mSearchButton = ((Button) localView.findViewById(R.id.calendar_search_button));
        this.mSearchButton.setOnClickListener(new OnClickListener() {
            public void onClick(View paramAnonymousView) {
                CalendarFragment.this.getDialog().dismiss();

                EventBus.getDefault().post(new DatesChosenEvent(selectedCheckin, selectedCheckout));

                if (mOnDateRangeChosenListener == null) {
                    return;
                }
                CalendarFragment.this.mOnDateRangeChosenListener.onDateRangeChosen(CalendarFragment.this.selectedCheckin, CalendarFragment.this.selectedCheckout);
            }
        });
        if ((this.mCheckin != null) && (this.mCheckout != null)) {
            this.mNumberOfNights = DT.calculateNumberOfNights(this.mCheckin, this.mCheckout);
        }
        updateView();
        this.mCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            public void onDateSelected(Date paramAnonymousDate) {
                if (CalendarFragment.this.mCheckin == null) {
                    mCheckin = paramAnonymousDate;
                    CalendarFragment.this.mCalendar.fixDialogDimens();
                    CalendarFragment.this.updateView();
                } else {
                    if (CalendarFragment.this.mCheckin.equals(paramAnonymousDate)) {
                        /* CalendarFragment.access$302(CalendarFragment.this, null); */

                    } else if (CalendarFragment.this.mCheckin.before(paramAnonymousDate)) {
                        // CalendarFragment.access$402(CalendarFragment.this, paramAnonymousDate);
                        //CalendarFragment.access$502(CalendarFragment.this, SearchService.calculateNumberOfNights(CalendarFragment.this.mCheckin, CalendarFragment.this.mCheckout));
                        mCheckout = paramAnonymousDate;
                        mNumberOfNights = DT.calculateNumberOfNights(mCheckin, mCheckout);
                        updateView();
                        //SearchService.calculateNumberOfNights(CalendarFragment.this.mCheckin, CalendarFragment.this.mCheckout);

                    } else {
                        mCheckout = null;
                        mCheckin = paramAnonymousDate;
                        //  CalendarFragment.access$302(CalendarFragment.this, paramAnonymousDate);
                        //CalendarFragment.access$402(CalendarFragment.this, null);

                    }
                }
            }

            public void onDateUnselected(Date paramAnonymousDate) {
            }
        });
        return localView;

    }

    public void onSaveInstanceState(Bundle paramBundle) {
        if (this.selectedCheckin != null)
            paramBundle.putLong(ARG_CHECKIN, this.selectedCheckin.getTime());
        if (this.selectedCheckout != null)
            paramBundle.putLong(ARG_CHECKOUT, this.selectedCheckout.getTime());
        super.onSaveInstanceState(paramBundle);
    }

    public interface OnDateRangeChosenListener {
        void onDateRangeChosen(Date paramDate1, Date paramDate2);
    }
}
