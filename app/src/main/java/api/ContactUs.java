package api;

import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import entity.Generic.Currency;
import helper.H;
import helper.IGetRequest;

public class ContactUs extends BaseTask {

    // todo refactor this
    private IGetRequest callback;

    public ContactUs(API api, String firstName, String lastName,
                     String email, String message,
                     String subject, IGetRequest callback) {
        super(callback);
        this.callback = callback;
        HashMap<String, String> params = new HashMap<>();
        params.put("first_name", firstName);
        params.put("last_name", lastName);
        params.put("email", email);
        params.put("message", message);
        params.put("subject", subject);
        params.put("ip", H.getUserIpAddress());
        params.put("meta_info", getMetaInfo());
        createParams("contact_us?api_key=" + API.API_KEY, params);
    }

    @Override
    protected void onResponse(Object response) {
        JSONObject json = (JSONObject) response;
        if (json.containsKey("success")) {
            String caseId = H.toString(json, "case_id");
            callback.onSuccess(caseId);
        }
    }

    private String getMetaInfo() {
        Locale deviceLocale = getContext().getResources().getConfiguration().locale;
        return "currency - " + getCurrency() + ":" +
                "locale - " + deviceLocale.toString() + ":" +
                "version - " + getVersion() + ":" +
                "model - "  + getDeviceName();
    }

    private String getCurrency(){
       return Currency.getCurrency(getContext()).getName();
    }

    private String getVersion() {
        try {
            return getContext().getPackageManager().getPackageInfo(
                    getContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            H.logE(e);
        }
        return "Not detected";
    }

    private String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }
}
