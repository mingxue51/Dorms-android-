package com.mc.youthhostels.views;

import android.support.v4.app.Fragment;

import com.mc.youthhostels.LoadingView;

public interface SearchResultActivityView extends LoadingView {
   void updateActionBar(String title, String subtitle);
   void showFragment(Fragment fragment, int titleId);
   void closeChooser();
}