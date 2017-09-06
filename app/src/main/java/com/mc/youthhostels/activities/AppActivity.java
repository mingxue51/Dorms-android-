package com.mc.youthhostels.activities;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.mc.youthhostels.LoadingView;
import com.mc.youthhostels.R;
import com.victor.loading.rotate.RotateLoading;

import helper.App;
import helper.H;

public class AppActivity extends AppCompatActivity implements LoadingView {
    public static final int PERMISSION_REQUEST_CODE = 1;

    private RotateLoading progress;
    private Runnable actionOnGrantAccess;

    @Override
    protected void onResume() {
        H.logD(getClass().getSimpleName() + " on resume");
        super.onResume();
        App.getInstance().setCurrentActivity(this);
        initProgress();
    }

    protected void initProgress() {
        progress = (RotateLoading)findViewById(R.id.rotate_loading);
    }

    @Override
    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        App.getInstance().setCurrentActivity(this);
    }

    private void clearReferences() {
        Activity currActivity = App.getInstance().getCurrentActivity();
        if (currActivity != null && currActivity.equals(this))
            App.getInstance().setCurrentActivity(null);
    }

    @Override
    public void showLoading() {
        runOnUiThread(() -> {
            if (progress != null) {
                progress.setVisibility(View.VISIBLE);
                progress.bringToFront();
                progress.start();
                H.logD("progress bar started");
            } else {
                H.logE("progress on show is null");
            }
        });
    }

    @Override
    public void hideLoading() {
        runOnUiThread(() -> {
            if (progress != null) {
                progress.setVisibility(View.INVISIBLE);
                progress.stop();
                H.logD("progress bar stopped");
            } else {
                H.logE("progress on hide is null");
            }
        });
    }

    private void hideLoading(Runnable endAction) {
        hideLoading();
        new Handler().postDelayed(endAction, 350);
    }

    public void hideLoadingAndBack(int backCount) {
        hideLoading(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < backCount; i++) {
                    onBackPressed();
                }
            }
        });
    }

    public void setActionBarTitle(String title) {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(title);
        }
    }

    private void setEnableMenuItem(Menu menu, int index, boolean enable) {
        menu.getItem(index).setCheckable(enable);
        menu.getItem(index).setVisible(enable);
    }

    protected void setEnableRightToolbarMenus(Menu menu, boolean enable) {
        if (menu == null) {
            return;
        }

        H.logD("set enable toolbar:" + enable);
        for (int i = 0; i < menu.size(); i++) {
            setEnableMenuItem(menu, i, enable);
        }
    }

    @Override
    public void onBackPressed() {
        H.logD("on back pressed started in app activity");
        H.cancellAllNetworkRequests();
        hideKeyboard();
        if (isLoading()) {
            hideLoading();
            H.logD("hide loading from on back pressed in app activity");
        } else {
            super.onBackPressed();
        }
    }

    public void hideFragment(Class fragmentClass) {
        getSupportFragmentManager().popBackStack(
                fragmentClass.getSimpleName(),
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void setDefaultActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setBackgroundColor(H.getColor(R.color.colorPrimary));
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Menu menu = toolbar.getMenu();
        setEnableRightToolbarMenus(menu, false);
    }

    public void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_content, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    @Nullable
    public Fragment getActiveFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        int index = getSupportFragmentManager().getBackStackEntryCount() - 1;
        FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(index);

        if (entry == null) {
            return null;
        }

        String tag = entry.getName();

        if (tag == null) {
            return null;
        }

        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    public void popAllFragments() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    public void setActionOnGrantAccess(Runnable actionOnGrantAccess) {
        this.actionOnGrantAccess = actionOnGrantAccess;
    }

    public void checkPermission(String permission, int requestCode, Runnable action, String suggestionText) {
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
                setActionOnGrantAccess(action);
                H.logD("start request permissions");
                ActivityCompat.requestPermissions(this,
                        new String[]{permission},
                        requestCode);
        } else {
            if (action != null) {
                action.run();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        H.logD("in on request permission result activity");
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    H.logD("permissions granted");
                    runActionOnGrantAccess();
                } else {
                    H.logD("permissions not granted");
                }
                return;
            }
        }
    }

    private void runActionOnGrantAccess() {
        if (actionOnGrantAccess != null) {
            actionOnGrantAccess.run();
            actionOnGrantAccess = null;
        }
    }

    public boolean isLoading() {
        return progress != null && progress.isStart();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (progress == null) {
            H.logE(new RuntimeException("progress is null on dispatch event"));
            return super.dispatchTouchEvent(event);
        }

        return progress.isStart() || super.dispatchTouchEvent(event);
    }

    public void hideKeyboard() {
        try {
            InputMethodManager input = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            View currentFocus = getCurrentFocus();
            if (currentFocus == null) {
                return;
            }
            if (currentFocus.getWindowToken() == null) {
                return;
            }

            input.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            H.logD("keyboard hided");
        } catch (Exception e) {
            H.logE(e);
        }
    }
}
