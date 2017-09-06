package helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;
import android.widget.Toast;

import com.mc.youthhostels.R;
import com.facebook.FacebookSdk;
import com.google.android.gms.analytics.ExceptionReporter;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mc.youthhostels.activities.AppActivity;
import com.mc.youthhostels.entity.Order;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import service.Localization;

public class App extends MultiDexApplication {
    private static App app;

    private volatile Activity currentActivity;
    private Tracker gaTracker;
    private OkHttpClient okHttpClient;
    private Order order;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        initOkHttpClient();
        FacebookSdk.sdkInitialize(getApplicationContext());

        // hotfix for prevent long first search start
        // Error getting yesterday/today/tomorrow for as: U_MISSING_RESOURCE_ERROR
        new Thread(() -> {
            Localization.getLocaleFromCurrencyCode("random");
        }).start();

        try {
            getDefaultTracker().setAppVersion(getBaseContext().getPackageManager()
                    .getPackageInfo(getBaseContext().getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            getDefaultTracker().setAppVersion("noVersion");
        }

        final Thread.UncaughtExceptionHandler exceptionHandler = new ExceptionReporter(
                getDefaultTracker(),
                Thread.getDefaultUncaughtExceptionHandler(),
                getApplicationContext());

        Thread.setDefaultUncaughtExceptionHandler((thread, ex) -> {
            try {
                String message = ex.getMessage();

                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                ex.printStackTrace(printWriter);
                String stackTrace = stringWriter.toString();

                sendReport(message + '\n' + stackTrace);
            } catch (Exception e) {
                H.logE(e.getMessage());
            }
            exceptionHandler.uncaughtException(thread, ex);
        });
    }

    private void initOkHttpClient() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .build();
    }

    private String getReportHeader() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = simpleDateFormat.format(new Date());

        return android.os.Build.MODEL + '\n' + currentDate + '\n';
    }

    public void sendReport(String report) {
        String reportMessage = getReportHeader() + report;

        getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Exceptions " + android.os.Build.MODEL)
                .setAction("report")
                .setLabel(reportMessage)
                .build());
    }

    public static App getInstance() {
        return app;
    }

    public AppActivity getCurrentActivity(){
        return (AppActivity)currentActivity;
    }

    public synchronized void setCurrentActivity(Activity mCurrentActivity){
        this.currentActivity = mCurrentActivity;

        if (mCurrentActivity != null) {
            getDefaultTracker().setScreenName(mCurrentActivity.getLocalClassName());
            getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("MOVE")
                    .setAction("moveTo")
                    .setLabel(mCurrentActivity.getLocalClassName())
                    .build());
        }
    }

    synchronized public Tracker getDefaultTracker() {
        if (gaTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            gaTracker = analytics.newTracker(getResources().getString(R.string.analytics_id));
        }
        return gaTracker;
    }

    public void showLoading() {
        if (getCurrentActivity() != null) {
            getCurrentActivity().showLoading();
        } else {
            H.logE("trying to call show loading on null reference");
        }
    }

    public void hideLoading() {
        if (getCurrentActivity() != null) {
            getCurrentActivity().hideLoading();
        } else {
            H.logE("trying to call hide loading on null reference");
        }
    }

    public void showToast(final String text) {
        App.getInstance().getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void resetOrder() {
        order = null;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public boolean isNetworkAvailable() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
