package com.mc.youthhostels.fragments.user;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mc.youthhostels.R;
import com.mc.youthhostels.activities.AppActivity;
import com.mc.youthhostels.presenters.UserBookingPresenter;

import helper.H;

public class UserBookingsTabsFragment extends Fragment {
    private UserBookingPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookings, container, false);

        ViewPager vpPager = (ViewPager) view.findViewById(R.id.view_pager);
        UserBookingPagerAdapter adapterViewPager = new UserBookingPagerAdapter(getChildFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);

        presenter = new UserBookingPresenter();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
        AppActivity activity = (AppActivity) getActivity();
        activity.setDefaultActionBar();
        activity.setActionBarTitle(H.getString(R.string.my_bookings));
    }

    public static class UserBookingPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;

        public UserBookingPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return UserBookingFragment.newInstance(true);
                case 1:
                    return UserBookingFragment.newInstance(false);
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return H.getString(R.string.bookings_new);
                case 1:
                    return H.getString(R.string.bookings_past);
                default:
                    return "";
            }
        }
    }
}

