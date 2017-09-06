package service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import helper.H;
import helper.Session;

public class ApplicationService extends Service {

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    private static final int NOTIFICATION_ID = 0x1337E;
    private PointInfo mPointInfo;
    private IServiceClient mClient;

    private BaseLocationListener mGpsLocationListener;
    private LocationManager mGpsLocationManager;

    Context mContext;

    public ApplicationService(Context context) {
        mContext = context;
        mGpsLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mGpsLocationListener = new BaseLocationListener(this);
        mPointInfo = new PointInfo(this);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        mClient = null;
        mPointInfo = new PointInfo(this);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        prepareToStart();
    }

    public class ApplicationBinder extends Binder {
        public ApplicationService getService() {
            return ApplicationService.this;
        }
    }

    private void prepareToStart() {
        startLocationManager();
        mLocationTracker.sendEmptyMessage(H.REQUEST.TRACK_LOCATION);
    }

    public void startLocationManager() {
        CoreSettings.init(getApplicationContext());
        mGpsLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        CheckGps();
        boolean isEnabledGPS = Session.getBooleanValue(getApplicationContext(), "enabled_gsm");
        if (mGpsLocationManager != null && isEnabledGPS) {
            mGpsLocationListener = new BaseLocationListener(this);
            mGpsLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    CoreSettings.getFrequencyPoll(), 0, mGpsLocationListener);
            mGpsLocationManager.addGpsStatusListener(mGpsLocationListener);
        } else {
            H.logI("No available location providers");
            mTrackProvider.sendEmptyMessage(H.REQUEST.TRACK_PROVIDER);
        }
    }

    public void stopLocationManager() {
        if (mGpsLocationListener != null) {
            mGpsLocationManager.removeUpdates(mGpsLocationListener);
            mGpsLocationManager.removeGpsStatusListener(mGpsLocationListener);
            mGpsLocationListener = null;
            Session.setBooleanValue(getApplicationContext(), "enabled_gsm", false);
        }
    }

    private Handler mLocationTracker = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case H.REQUEST.TRACK_LOCATION:
                    CoreSettings.init(getApplicationContext());
                    long delay = CoreSettings.getFrequencyPoll();
                    enableGPS();
                    CheckGps();
                    boolean isEnabledGPS = Session.getBooleanValue(getApplicationContext(), "enabled_gsm");
                    if (mGpsLocationListener == null && isEnabledGPS) {
                        startLocationManager();
                    }
                    if (mPointInfo.isReady()) {
                        mPointInfo.resetReady();
                        Date dt = new Date();
                        H.logD(dt.toLocaleString() + " : " + mPointInfo.getLatitude() + ":" + mPointInfo.getLongitude());
                    }
                    sendMessageDelayed(obtainMessage(H.REQUEST.TRACK_LOCATION), delay);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    public static boolean isServiceRunning(Context context) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo service : services) {
            if (service.service.getShortClassName().toLowerCase().contains("youthhostelservice")) {
                return true;
            }
        }
        return false;
    }

    private Handler mTrackProvider = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case H.REQUEST.TRACK_PROVIDER: {
                    CoreSettings.init(getApplicationContext());
                    enableGPS();
                    CheckGps();
                    boolean isEnabledGPS = Session.getBooleanValue(getApplicationContext(), "enabled_gsm");
                    if (isEnabledGPS) {
                        mTrackProvider.removeMessages(H.REQUEST.TRACK_PROVIDER);
                        stopLocationManager();
                        startLocationManager();
                    } else {
                        long period = CoreSettings.getCheckProviderPeriod();
                        sendMessageDelayed(obtainMessage(H.REQUEST.TRACK_PROVIDER), period);
                    }
                }
                default:
                    super.handleMessage(msg);
            }
        }
    };

    public void onGpsStatusEvent(int status) {
    }

    public void onLocationChanged(Location location) {
        if (mClient != null) {
            mClient.onLocationUpdate(location);            // Update user interface
        }
        mPointInfo.setLocation(location);
    }

    protected void enableGPS() {
        boolean isEnabledGPS = Session.getBooleanValue(getApplicationContext(), "enabled_gsm");
        if (mGpsLocationManager != null
                && CoreSettings.getAutoEnableGPS() && !isEnabledGPS) {
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); //$NON-NLS-1$//$NON-NLS-2$
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3")); //$NON-NLS-1$
            sendBroadcast(poke);
            Session.setBooleanValue(getApplicationContext(), "enabled_gsm", true);
        }
    }

    private void CheckGps() {
        boolean value = mGpsLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Session.setBooleanValue(getApplicationContext(), "enabled_gsm", value);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public Boolean isAbleToLocate(String provider) {
        Boolean result = false;
        if (mGpsLocationManager.isProviderEnabled(provider)) {
            result = true;
        }
        return result;
    }

    public PointInfo getLocation(String provider) {
        mGpsLocationManager.requestLocationUpdates(provider, MIN_TIME_BW_UPDATES, MIN_TIME_BW_UPDATES, mGpsLocationListener);
        mPointInfo.setLocation(mGpsLocationManager.getLastKnownLocation(provider));
        return mPointInfo;
    }

    public LocationAddress getCurrentLocation(PointInfo point) {
        LocationAddress locationAddress = null;
        try {

            if (point != null) {
                List<Address> addresses = null;
                Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(point.getLatitude(), point.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                locationAddress = new LocationAddress();
                if (addresses != null && addresses.size() > 0) {
                    locationAddress.setAddress(addresses.get(0).getAddressLine(0));
                    locationAddress.setCity(addresses.get(0).getLocality());
                    locationAddress.setCityNIndex(addresses.get(0).getAddressLine(1));
                    locationAddress.setCountry(addresses.get(0).getCountryName());
                    locationAddress.setState(addresses.get(0).getAdminArea());
                    locationAddress.setSubAdministrative(addresses.get(0).getSubAdminArea());
                    locationAddress.setCountryCode(addresses.get(0).getCountryCode());
                    locationAddress.setCountryName(addresses.get(0).getCountryName());
                    locationAddress.setLatitude(addresses.get(0).getLatitude());
                    locationAddress.setLongtitude(addresses.get(0).getLongitude());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return locationAddress;
    }
}
