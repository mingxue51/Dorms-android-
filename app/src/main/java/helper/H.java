package helper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.mc.youthhostels.R;

import org.json.simple.JSONObject;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import api.BaseTask;
import entity.Generic.Currency;
import entity.Generic.Language;
import entity.User.UserMapper;
import entity.User.UserProfileMapper;

public class H {
    private static final String TAG = "YouthHostel";
    private static final int LOG_LEVEL = 5;
    public static final int THUMB_SIZE = 80;

    public static final boolean TEST = false;
    public static boolean paymentFakeMode = false;
    private static final boolean EXTEND_LOGS = false;

    public static String APP_PATH;
    public static String APP_TEMP;
    public static String PHOTOS_PATH;
    public static String THUMBS_PATH;

    public static Typeface getCustomFont(Context context){
        return Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Regular.ttf");
    }

    public static boolean isStringNotNullEmpty(String string){
        boolean result=false;
        if (string != null && !string.isEmpty()){
            result = true;
        }
        return result;
    }

    public static void logE(String message) {
        Crashlytics.logException(new RuntimeException(message));
        H.logE("", message);
    }

    public static void logE(String label, String message) {
        if (LOG_LEVEL >= 1) {
            Log.e(TAG, addLabelToLog(label) + message);
        }
    }

    public static void logW(String message) {
        H.logW("", message);
    }

    @SuppressWarnings("unused")
    public static void logW(String label, String message) {
        if (LOG_LEVEL >= 2) {
            Log.w(TAG, addLabelToLog(label) + message);
        }
    }

    public static void logI(String message) {
        H.logI("", message);
    }

    @SuppressWarnings("unused")
    public static void logI(String label, String message) {
        if (LOG_LEVEL >= 3) {
            Log.i(TAG, addLabelToLog(label) + message);
        }
    }

    public static void logD(String message) {
        H.logD("", message);
    }

    @SuppressWarnings("unused")
    public static void logD(String label, String message) {
        if (LOG_LEVEL >= 4) {
            if (EXTEND_LOGS) {
                int maxLogSize = 300;

                for (int i = 0; i <= message.length() / maxLogSize; i++) {
                    int start = i * maxLogSize;
                    int end = (i + 1) * maxLogSize;
                    end = end > message.length() ? message.length() : end;
                    Log.d(TAG, message.substring(start, end));
                }
            } else {
                Log.d(TAG, label + message);
            }
        }
    }

    public static void logV(String message) {
        H.logV("", message);
    }

    @SuppressWarnings("unused")
    public static void logV(String label, String message) {
        if (LOG_LEVEL >= 5) {
            Log.v(TAG, addLabelToLog(label) + message);
        }
    }

    private static String addLabelToLog(String label) {
        return label = label.length() > 0 ? label + " : " : "";
    }

    public static void CreateAppFolders(Context context) {
        try {
            APP_PATH = "/data/data/" + getPacketName(context) + "/";
            PHOTOS_PATH = APP_PATH + "photos/";
            THUMBS_PATH = APP_PATH + "thumbs/";
            APP_TEMP = APP_PATH + "temp/";
            File file = new File(APP_PATH);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(PHOTOS_PATH);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(THUMBS_PATH);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(APP_TEMP);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            H.logE(e.getMessage());
        }
    }

    public static String getPacketName(Context context) {
        String name = "";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.packageName;
        } catch (Exception e) {
        }
        return name;
    }



    public static BigDecimal toBigDecimal(JSONObject json, String key) {
        return toBigDecimal(json, key, new BigDecimal(0.00));
    }

    public static BigDecimal toBigDecimal(JSONObject json, String key, BigDecimal options) {
        BigDecimal result = options;
        if (json != null && key.trim().length() > 0 && json.containsKey(key)) {
            try {
                result = new BigDecimal(json.get(key).toString());
            } catch (Exception e) {
            }
        }
        return result;
    }


    public static double toDouble(JSONObject json, String key) {
        return toDouble(json, key, 0.0D);
    }

    public static double toDouble(JSONObject json, String key, double options) {
        double result = options;
        if (json != null && key.trim().length() > 0 && json.containsKey(key)) {
            try {
                result = Double.parseDouble(json.get(key).toString());
            } catch (Exception e) {
            }
        }
        return result;
    }

    public static int toInt(JSONObject json, String key) {
        return toInt(json, key, 0);
    }

    public static int toInt(JSONObject json, String key, int options) {
        int result = options;
        if (json != null && key.trim().length() > 0 && json.containsKey(key)) {
            try {
                result = Integer.parseInt(json.get(key).toString());
            } catch (Exception e) {
            }
        }
        return result;
    }

    public static long toLong(JSONObject json, String key) {
        return toLong(json, key, 0);
    }

    public static long toLong(JSONObject json, String key, long options) {
        long result = options;
        if (json != null && key.trim().length() > 0 && json.containsKey(key)) {
            try {
                result = Long.parseLong(json.get(key).toString());
            } catch (Exception e) {
            }
        }
        return result;
    }

    public static String toString(JSONObject json, String key) {
        return toString(json, key, "");
    }

    public static String toString(JSONObject json, String key, String options) {
        String result = "";
        if (json != null && key.trim().length() > 0 && json.containsKey(key)) {
            try {
                result = json.get(key).toString();
            } catch (Exception e) {
            }
        }
        return result;
    }

    public static Boolean toBoolean(JSONObject json, String key) {
        return toBoolean(json, key, false);
    }

    public static Boolean toBoolean(JSONObject json, String key, Boolean options) {
        Boolean result = false;
        if (json != null && key.trim().length() > 0 && json.containsKey(key)) {
            try {
                int val = Integer.parseInt(json.get(key).toString());
                result = val == 1 ? true : false;
            } catch (Exception e) {
            }
        }
        return result;
    }

    public static void logE(Exception e) {
        if (e == null) {
            H.logE("exception is null");
            return;
        }

        Crashlytics.logException(e);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String stackTrace = stringWriter.toString();
        H.logE(stackTrace);
    }

    public static final String URL = "https://www.dorms.com/api";

    public class REQUEST {
        public static final int TRACK_LOCATION = 5001;
        public static final int TRACK_PROVIDER = 5002;
    }

    public static void runOnUi(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public static String getString(int resource) {
        return App.getInstance().getResources().getString(resource);
    }

    public static Drawable getDrawable(int drawable) {
        return ContextCompat.getDrawable(App.getInstance(), drawable);
    }

    public static int getColor(int colorId) {
        return ContextCompat.getColor(App.getInstance(), colorId);
    }

    public static String getUserIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            H.logE(ex);
        }
        return "";
    }

    public static <T> T getRandomFromList(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        int index = new Random().nextInt(list.size());
        return list.get(index);
    }

    public static volatile boolean isDialogShowed = false;

    public static synchronized boolean isDialogShowed() {
        return isDialogShowed;
    }

    public static synchronized void setDialogShowed(boolean value) {
        isDialogShowed = value;
    }

    public static void showDialog(String message) {
        showDialog(message, null);
    }

    public static void showDialog(String message, Runnable action) {
        H.logD("try hide loading in show dialog");
        App.getInstance().hideLoading();

        if (TextUtils.isEmpty(message)) {
            H.logE(new RuntimeException("message for showDialog dialog is empty"));
            return;
        }

        H.runOnUi(() -> {
            if (isDialogShowed()) {
                H.logD("dialog has already showed");
                return;
            }
            setDialogShowed(true);

            final android.app.Dialog dialog = new android.app.Dialog(App.getInstance().getCurrentActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_app_alert);
            Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog_ok);
            dialogButton.setOnClickListener(v -> {
                dialogButton.setBackgroundColor(H.getColor(R.color.gray_light));
                dialog.dismiss();
            });

            TextView messageView = (TextView) dialog.findViewById(R.id.txt_dialog_message);
            messageView.setText(message);
            dialog.show();

            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    H.logD("dialog showed");
                }
            });

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    setDialogShowed(false);
                    H.logD("dialog on cancel called");
                }
            });

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    setDialogShowed(false);
                    if (action != null) {
                        H.logD("on dismiss action start calling...");
                        action.run();
                    }
                    H.logD("dialog on dismiss called");
                }
            });
        });
    }

    public static void setStarColor(RatingBar ratingBar, int emptyStar, int fillStar) {
        LayerDrawable layerDrawable = (LayerDrawable) ratingBar.getProgressDrawable();
        DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(0)),
                emptyStar);
        DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(1)),
                fillStar);
        DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(2)),
                fillStar);
    }

    public static void showLoading() {
        App.getInstance().showLoading();
    }

    public static void hideLoading() {
        App.getInstance().hideLoading();
    }

    public static void clearUserData() {
        UserMapper mapper = new UserMapper(App.getInstance());
        mapper.deleteAllEntities();
        mapper.clearSession();

        UserProfileMapper profileMapper = new UserProfileMapper(App.getInstance());
        profileMapper.deleteAllEntities();
        profileMapper.clearSession();
    }

    public static void cancellAllNetworkRequests() {
        BaseTask.cancelAllRequests();
    }
}
