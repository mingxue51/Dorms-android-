package com.mc.youthhostels;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mc.youthhostels.activities.AppActivity;
import com.mc.youthhostels.events.SortMethodSelectedEvent;
import com.mc.youthhostels.fragments.CurrencyChooserFragment;
import com.mc.youthhostels.fragments.map.MapFragment;
import com.mc.youthhostels.fragments.SearchResultFragment;
import com.mc.youthhostels.helper.JsonHelper;
import com.mc.youthhostels.presenters.SearchResultActivityPresenter;
import com.mc.youthhostels.views.SearchResultActivityView;

import de.greenrobot.event.EventBus;
import entity.Generic.Currency;
import entity.Property.Properties;
import entity.Property.Search.SearchProperty;
import helper.H;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SearchResultActivity extends AppActivity implements SearchResultActivityView {

    private Properties properties;
    private SearchProperty searchProperty;
    private SearchResultActivityPresenter presenter;
    private Menu menu;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);
        getDataFromBundle();

        ViewPager vpPager = (ViewPager) findViewById(R.id.view_pager);
        MainPagerAdapter adapterViewPager = new MainPagerAdapter(getSupportFragmentManager(), properties, searchProperty);
        vpPager.setAdapter(adapterViewPager);

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        updateActionBar(searchProperty.getCity(), searchProperty.getDateInfo());

        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        presenter = new SearchResultActivityPresenter(this, searchProperty);
        presenter.onCreate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_results, this.menu);

        initCurrencyIcon(menu, R.drawable.icon_currency_rectangle_no_fill);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void initCurrencyIcon(final Menu menu, int iconCurrencyRectangle) {
        MenuItem item = menu.findItem(R.id.menu_currency);
        item.setActionView(R.layout.menu_currency_layout);
        View view = item.getActionView();

        ImageView iconImageView = (ImageView)view.findViewById(R.id.icon_view);
        iconImageView.setImageDrawable(ContextCompat.getDrawable(this, iconCurrencyRectangle));

        view.findViewById(R.id.lyt_currency).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new CurrencyChooserFragment(), R.string.title_currency);
            }
        });

        TextView currency = (TextView)view.findViewById(R.id.txt_currency);
        currency.setText(Currency.getCurrency().getCode());
        currency.setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    @Override
    public void showFragment(Fragment fragment, int titleId) {
        (findViewById(R.id.chooser_fragment_content)).bringToFront();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.chooser_fragment_content, fragment)
                .addToBackStack(null)
                .commit();

        tabLayout.setVisibility(View.GONE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
        toolbar.setNavigationIcon(R.drawable.icon_close);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // close operation goes trough presenter
                EventBus.getDefault().post(new SortMethodSelectedEvent(null));
            }
        });

        toolbar.setTitle(getString(titleId));
        toolbar.setSubtitle("");
        setEnableRightToolbarMenus(menu, false);
    }

    @Override
    public void closeChooser() {
        getSupportFragmentManager().popBackStack();
        restoreToolbar();
    }

    private void restoreToolbar() {
        tabLayout.setVisibility(View.VISIBLE);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setEnableRightToolbarMenus(menu, true);
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void updateActionBar(String title, String subtitle) {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(title);
            supportActionBar.setSubtitle(subtitle);
        }

        if (menu != null && menu.findItem(R.id.menu_currency) != null) {
            MenuItem item = menu.findItem(R.id.menu_currency);
            View view = item.getActionView();
            TextView currency = (TextView) view.findViewById(R.id.txt_currency);
            currency.setText(Currency.getCurrency().getCode());
        }
    }

    public static void goTo(Context context, Properties properties, SearchProperty searchProperty) {
        Intent i = new Intent(context, SearchResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Properties.BUNDLE, properties);
        bundle.putSerializable(SearchProperty.BUNDLE, searchProperty);
        i.putExtras(bundle);

        context.startActivity(i);
    }

    protected void getDataFromBundle() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            properties = ((Properties) bundle.getSerializable(Properties.BUNDLE));
            searchProperty = ((SearchProperty) bundle.getSerializable(SearchProperty.BUNDLE));

            JsonHelper.save(properties);
            JsonHelper.save(searchProperty);
        }

        if (properties == null) {
            properties = (Properties)JsonHelper.restore(Properties.class);
            H.logD("properties restored");
        }

        if (searchProperty == null) {
            searchProperty = (SearchProperty)JsonHelper.restore(SearchProperty.class);
            H.logD("searchproperty restored");
        }
    }

    public static class MainPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;
        private Properties properties;
        private SearchProperty searchProperty;

        public MainPagerAdapter(FragmentManager fragmentManager, Properties properties, SearchProperty searchProperty) {
            super(fragmentManager);
            this.properties = properties;
            this.searchProperty = searchProperty;
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    SearchResultFragment searchResultFragment = new SearchResultFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Properties.BUNDLE, properties);
                    bundle.putSerializable(SearchProperty.BUNDLE, searchProperty);
                    searchResultFragment.setArguments(bundle);
                    return searchResultFragment;
                case 1:
                    return MapFragment.newInstance(properties, searchProperty);
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return H.getString(R.string.list);
                case 1:
                    return H.getString(R.string.map);
                default:
                    return "";
            }
        }
    }

    @Override
    public void onBackPressed() {
        H.logD("on back pressed in search result activity");
        SearchResultFragment searchResultFragment = (SearchResultFragment)getSupportFragmentManager().findFragmentByTag("android:switcher:" + String.valueOf(R.id.view_pager) + ":0");
        if (searchResultFragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
            searchResultFragment.hideRefineSearch();
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                EventBus.getDefault().post(new SortMethodSelectedEvent(null));
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
