package com.mc.youthhostels;


import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.mc.youthhostels.activities.AppActivity;
import com.mc.youthhostels.fragments.ContactUsFragment;
import com.mc.youthhostels.fragments.CurrencyChooserFragment;
import com.mc.youthhostels.fragments.DirectionsFragment;
import com.mc.youthhostels.fragments.FaqFragment;
import com.mc.youthhostels.fragments.FavouriteFragment;
import com.mc.youthhostels.fragments.booking.ContentFragment;
import com.mc.youthhostels.fragments.booking.MyAccountFragment;
import com.mc.youthhostels.fragments.booking.SettingsFragment;
import com.mc.youthhostels.fragments.booking.SuccessfulPaymentFragment;
import com.mc.youthhostels.fragments.explore.ExploreFragment;
import com.mc.youthhostels.fragments.signin.LoginFragment;
import com.mc.youthhostels.fragments.user.UserBookingsTabsFragment;
import com.mc.youthhostels.fragments.user.UserReviewsFragment;
import com.mc.youthhostels.presenters.MainActivityPresenter;
import com.mc.youthhostels.views.MainView;

import api.API;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import entity.Generic.Content;
import entity.Generic.Currency;
import entity.Generic.Language;
import entity.User.User;
import entity.User.UserMapper;
import entity.User.UserProfile;
import helper.App;
import helper.H;
import helper.IGetRequest;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Logger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity2 extends AppActivity implements MainView {

    private Menu menu;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Bind(R.id.navigation_view)
    NavigationView navigationView;

    @Bind(R.id.lyt_directions)
    LinearLayout takeMeToHostelLayout;

    private MainActivityPresenter presenter;
    private Fragment desired;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        //todo move to application class
        Language.switchApplicationLanguage(Language.getLanguage(this), getBaseContext());
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initToolbar();

        presenter = new MainActivityPresenter(this);
        presenter.onCreate();

        showExploreFragment();

        // check if user booked property
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getBoolean("success")) {
                showFragment(new SuccessfulPaymentFragment());
            }
        }
    }

    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_menu);
        }
    }

    private void showExploreFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_explore, new ExploreFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, this.menu);
        int iconCurrencyRectangle = R.drawable.icon_currency_rectangle;
        initCurrencyIcon(menu, iconCurrencyRectangle);
        return true;
    }

    private void initCurrencyIcon(final Menu menu, int iconCurrencyRectangle) {
        MenuItem item = menu.findItem(R.id.menu_currency);
        item.setActionView(R.layout.menu_currency_layout);
        View view = item.getActionView();

        ImageView iconImageView = (ImageView)view.findViewById(R.id.icon_view);
        iconImageView.setImageDrawable(ContextCompat.getDrawable(this, iconCurrencyRectangle));

        view.findViewById(R.id.lyt_currency).setOnClickListener(v -> {
            showCurrencyChooser();
        });

        TextView currency = (TextView)view.findViewById(R.id.txt_currency);
        currency.setText(Currency.getCurrency().getCode());
    }

    public void showCurrencyChooser() {
        H.logD("menu currency clicked");
        showFragment(new CurrencyChooserFragment());
        setDefaultActionBar();
        setActionBarTitle(H.getString(R.string.title_currency));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(navigationView);
                return true;
            case R.id.menu_language:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void restoreToolbar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(getBaseContext(), android.R.color.transparent));
        toolbar.setVisibility(View.VISIBLE);
        setEnableRightToolbarMenus(menu, true);
        toolbar.setNavigationIcon(R.drawable.icon_menu);
        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(navigationView));
        H.logD("toolbar restored");
    }

    private void setupDrawerLayout() {
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            desired = null;
            switch (menuItem.getItemId()) {
                case R.id.drawer_sign_in:
                    showFragment(User.isLogged() ? new MyAccountFragment(): new LoginFragment());
                    break;
                case R.id.drawer_explore:
                    getSupportFragmentManager().popBackStack();
                    break;
                case R.id.drawer_my_favorites:
                    goTo(new FavouriteFragment(), true);
                    break;
                case R.id.drawer_my_bookings:
                    goTo(new UserBookingsTabsFragment(), true);
                    break;
                case R.id.drawer_my_directions:
                    goTo(new DirectionsFragment(), true);
                    break;
                case R.id.drawer_my_reviews:
                    goTo(new UserReviewsFragment(), true);
                    break;
                case R.id.drawer_settings:
                    showFragment(new SettingsFragment());
                    break;
                case R.id.drawer_contact_us:
                    goTo(new ContactUsFragment(), false);
                    break;
                case R.id.drawer_help:
                    goTo(new FaqFragment(), false);
                    break;
                case R.id.drawer_privacy:
                    showFragment(ContentFragment.newInstance(Content.PRIVACY));
                    break;
                case R.id.drawer_terms:
                    showFragment(ContentFragment.newInstance(Content.TAC));
                    break;
                case R.id.drawer_sign_out:
                    doLogout();
                    return true;
            }
            drawerLayout.closeDrawers();
            return false;
        });
        updateNavigationItems();
    }

    private void updateNavigationItems() {
        Menu menu = navigationView.getMenu();
        MenuItem signOutItem = menu.findItem(R.id.drawer_sign_out);
        signOutItem.setEnabled(User.isLogged());
        signOutItem.setVisible(User.isLogged());

        int itemsColor = User.isLogged() ? H.getColor(R.color.colorPrimary) :
                                           H.getColor(R.color.grey_checkable);

        for (int i = 0; i < menu.size(); i++) {
            if (i >= 2 && i <= 4) {
                setNavigationItemColor(menu.getItem(i), itemsColor);
            } else {
                setNavigationItemColor(menu.getItem(i), H.getColor(R.color.colorPrimary));
            }
        }

//        todo handle it correctly
//        for (int i = 0; i < menu.size(); i++) {
//            if (menu.getItem(i).isChecked()) {
//                setNavigationItemColor(menu.getItem(i), H.getColor(R.color.green_action));
//            }
//        }

        MenuItem signInItem = menu.findItem(R.id.drawer_sign_in);
        if (User.isLogged()) {
            UserProfile userProfile = UserProfile.getUserProfile();
            String fullName = userProfile.getFirst_name() + " " +  userProfile.getLast_name();
            signInItem.setTitle(fullName);
        } else {
            signInItem.setTitle(H.getString(R.string.drawer_sign_in));
        }

        int takeMeToHostelColor = User.isLogged() ? H.getColor(R.color.green_action) :
                                                    H.getColor(R.color.grey_checkable);
        takeMeToHostelLayout.setBackgroundColor(takeMeToHostelColor);
        // todo fix that
        takeMeToHostelLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.lyt_directions)
    public void onTakeMeToHostelClicked() {
        H.logD("takeMeToHostel clicked");
        drawerLayout.closeDrawers();

        if (User.isLogged()) {
            // todo move to directions
        } else {
            showFragment(new LoginFragment());
        }
    }

    private void setNavigationItemColor(MenuItem item, int color){
        item.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        SpannableString s = new SpannableString(item.getTitle());
        s.setSpan(new ForegroundColorSpan(color), 0, s.length(), 0);
        item.setTitle(s);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupDrawerLayout();
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

    private void goTo(Fragment fragment, boolean isAuthRequired) {
        H.logD("is user logged:" + User.isLogged());

        if (isAuthRequired) {
            this.desired = fragment;
            if (!User.isLogged()) {
                showFragment(new LoginFragment());
            } else {
                showFragment(fragment);
            }
        } else {
            showFragment(fragment);
        }
    }

    private void doLogout() {
        showLoading();
        final UserMapper mapper = new UserMapper(App.getInstance());
        mapper.deleteAllEntities();
        API api = API.getInstance(App.getInstance());
        api.Signout(new IGetRequest() {
            @Override
            public void onError(String message) {
                hideLoading();
                mapper.clearSession();
            }

            @Override
            public void onSuccess(String message) {
                mapper.clearSession();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        updateNavigationItems();
                    }
                });
            }
        });
    }

    @Override
    public void closeChooserFragment() {
        onBackPressed();
    }

    @Override
    public void updateMenus() {
        MenuItem item = menu.getItem(1);
        View view = item.getActionView();
        TextView currency = (TextView)view.findViewById(R.id.txt_currency);
        currency.setText(Currency.getCurrency().getCode());
    }

    @Override
    public void onBackPressed() {
        H.logD("main activity on back pressed");
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            restoreToolbar();
            updateNavigationItems();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void openDesiredFragment() {
        onBackPressed();

        if (desired != null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_content, desired)
                    .addToBackStack(null)
                    .commit();
            desired = null;
        } else {
            updateNavigationItems();
            drawerLayout.openDrawer(navigationView);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        H.logD("on activity result started");
        Fragment fragment = getActiveFragment();
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
