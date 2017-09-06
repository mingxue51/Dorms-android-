package com.mc.youthhostels.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.mc.youthhostels.MainActivity2;
import com.mc.youthhostels.R;
import com.mc.youthhostels.fragments.PropertyOrderFragment;
import com.mc.youthhostels.fragments.booking.FastLoginFragment;
import com.mc.youthhostels.fragments.booking.PaymentFragment;
import com.mc.youthhostels.fragments.booking.PersonalInformationFragment;
import com.mc.youthhostels.presenters.BookingPresenter;
import com.mc.youthhostels.views.BookingView;

import entity.Property.Property;
import entity.Property.Search.SearchProperty;
import helper.App;
import helper.H;
import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BookingActivity extends AppActivity implements BookingView {
    public static final  String success = "success";

    private Toolbar toolbar;
    private BookingPresenter presenter;
    private Property property;
    private SearchProperty searchProperty;

    public static void goTo(Property property, SearchProperty searchProperty) {
        Intent intent = new Intent(App.getInstance(), BookingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Property.BUNDLE, property);
        bundle.putSerializable(SearchProperty.BUNDLE, searchProperty);
        intent.putExtras(bundle);
        App.getInstance().getCurrentActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        initProgress();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            property = ((Property) bundle.getSerializable(Property.BUNDLE));
            searchProperty = ((SearchProperty) bundle.getSerializable(SearchProperty.BUNDLE));
        }

        presenter = new BookingPresenter(this);
        presenter.onCreate();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        showOrderFragment();
        // todo move apprater to other place
        //initAppRater();
    }

    private void initAppRater() {
        AppRate.with(this)
                .setInstallDays(0) // default 10, 0 means install day.
                .setLaunchTimes(5) // default 10
                .setRemindInterval(2) // default 1
                .setShowLaterButton(true) // default true
                .setDebug(false) // default false
                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                    @Override
                    public void onClickButton(int which) {
                        Log.d(BookingActivity.class.getName(), Integer.toString(which));
                    }
                })
                .monitor();

        // Show a dialog if meets conditions
        AppRate.showRateDialogIfMeetsConditions(this);


    }

    private void showOrderFragment() {
        if (property != null && searchProperty != null) {
            initToolbar();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_content, PropertyOrderFragment.newInstance(property, searchProperty))
                    .commit();
            H.logD("fragment order transaction finished");
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void showSuccessfulPayment() {
        H.logD("successful payment");
        // go to main activity
        Intent intent = new Intent(App.getInstance().getApplicationContext(), MainActivity2.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(success, true);
        App.getInstance().getCurrentActivity().startActivity(intent);
    }

    @Override
    public void showPayment() {
        showFragment(new PaymentFragment());
    }

    @Override
    public void showPersonDetails() {
        showFragment(new PersonalInformationFragment());
    }

    @Override
    public void showFastLogin() {
        showFragment(new FastLoginFragment());
    }
}
