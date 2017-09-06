package api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;
import com.facebook.login.LoginManager;
import com.mc.youthhostels.R;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import helper.App;
import helper.H;
import helper.IGetRequest;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public abstract class BaseTask implements Runnable {

    private static volatile Set<Call> runningRequests = new HashSet<>();

    public static final int ERROR_STATUS = 0;
    public static final int SUCCESS_STATUS = 1;
    public static final String INTERNAL_OBJECT = "internal_object";

    private IGetRequest callback;

    private Map<String, String> postParams;
    private Object response;
    private String action;

    public BaseTask(IGetRequest callback) {
        this.callback = callback;
        postParams = new HashMap<>();
    }

    public BaseTask() {
        postParams = new HashMap<>();
    }

    protected abstract void onResponse(Object response);

    @Nullable
    private String getErrorMessage(Object response) {
        if (response == null) {
            H.logD("response is null in getErrorMessage");
            return null;
        }

        String errorMessage = null;
        JSONObject result = ((JSONObject) response);
        if (result.containsKey("error")) {
            JSONObject responseObject = (JSONObject) result.get("error");
            errorMessage = parseMessage(responseObject, "error_message");
        }

        return errorMessage;
    }

    protected void onError(Object response) {
        String errorMessage = getErrorMessage(response);
        H.logD("error message is:" + errorMessage);
        if (callback != null && !TextUtils.isEmpty(errorMessage)) {
            H.logD("start on error callback in base task");
            callback.onError(errorMessage);
        }

        if (errorMessage != null) {
            Crashlytics.logException(new RuntimeException(errorMessage));
        } else {
            Crashlytics.logException(new RuntimeException("error message is null"));
        }

        if (response != null) {
            App.getInstance().sendReport(response.toString() + '\n' + H.URL + "/" + action);
        }

        if (this instanceof BookProperty) {
            showDialog(errorMessage);
            return;
        }

        showDialog(R.string.something_went_wrong);
    }

    protected void onSuccess(String message) {
        if (callback != null) {
            callback.onSuccess(message);
        }
    }

    public Context getContext() {
        return App.getInstance().getCurrentActivity();
    }

    @Override
    public void run() {
        if (!App.getInstance().isNetworkAvailable()) {
            App.getInstance().hideLoading();
            H.showDialog(H.getString(R.string.no_internet_connection));
            return;
        }

        RequestBody formBody = fillParams();
        Request request = new Request.Builder()
                .url(H.URL + "/" + action)
                .post(formBody)
                .build();

        H.logD(H.URL + "/" + action + ":" + postParams.toString());

        final Call call = App.getInstance().getOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call request, IOException e) {
                if (request != null && request.isCanceled()) {
                    return;
                }

                H.logE(e.getMessage() + request.toString());
                H.logE(e);
                showDialog(R.string.no_internet_connection);
                onPostExecute(ERROR_STATUS);
                cancelRequest(call);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                H.logD(response.toString());
                // session is expired
                if (response.code() == 403) {
                    H.clearUserData();
                    LoginManager.getInstance().logOut();
                    H.logE("403 code. user data cleared");
                    return;
                }

                if (response.code() >= 300) {
                    H.logE("api returned " + response.code() + "");
                    showDialog(R.string.something_went_wrong);
                    return;
                }

                String responseJson = response.body().string();
                JSONObject json = (JSONObject) JSONValue.parse(responseJson);

                if (json != null) {
                    H.logD(json.toJSONString());
                } else {
                    H.logE("json is null");
                }

                int status = ERROR_STATUS;
                if (json != null && json.containsKey("RESPONSE")) {
                    BaseTask.this.response = json.get("RESPONSE");
                    if(BaseTask.this.response instanceof JSONArray){
                        JSONObject obj = new JSONObject();
                        obj.put(BaseTask.INTERNAL_OBJECT, BaseTask.this.response);
                        BaseTask.this.response = obj;
                    }

                    JSONObject responseObject = (JSONObject) BaseTask.this.response;
                    if (!responseObject.containsKey("error")) {
                        status = SUCCESS_STATUS;
                        H.logD("response success status set");
                    } else {
                        H.logD("response error status set");
                    }
                } else {
                    H.logD("json doesn't contain RESPONSE key");
                }

                onPostExecute(status);
                removeRequest(call);
            }
        });

        addRequest(call);
    }

    @NonNull
    private RequestBody fillParams() {
        FormBody.Builder formEncodingBuilder = new FormBody.Builder();
        if (postParams != null) {
            for (String key : postParams.keySet()) {
                if (postParams.get(key) == null) {
                    H.logE("key parameter is null: " + key);
                    continue;
                }
                formEncodingBuilder.add(key, postParams.get(key));
            }
        }
        return formEncodingBuilder.build();
    }

    private void showDialog(int messageResource) {
        showDialog(H.getString(messageResource));
    }

    private void showDialog(String message) {
        App.getInstance().hideLoading();

        if (!TextUtils.isEmpty(message)) {
            H.showDialog(message);
        }
    }

    protected void onPostExecute(Integer result) {
        H.logD("start on post execute");
        if (response != null) {
            switch (result) {
                case SUCCESS_STATUS:
                    onResponse(response);
                    break;
                default:
                    onError(response);
                    break;
            }
        } else {
            JSONObject json = new JSONObject();
            json.put("message",H.getString(R.string.error_request));
            onError(json);
        }
    }

    protected void createParams(String action, Map<String, String> params) {
        this.action = action;
        if (params != null) {
            postParams.putAll(params);
        }
    }

    protected String parseMessage(JSONObject json, String field) {
        String message = "";
        if (json.containsKey(field)) {
            message="";
            Object messages = json.get(field);
            JSONArray array = (JSONArray) messages;
            for (Object key : array) {
                String obj = (String) key;
                message = message + key.toString();
            }
        }
        return message;
    }

    private static synchronized void addRequest(Call request) {
        runningRequests.add(request);
    }

    public static synchronized void cancelAllRequests() {
        if (runningRequests == null || runningRequests.isEmpty()) {
            H.logD("no requests to cancel");
            return;
        }

        H.logD("start cancelling requests");
        for (Call request : runningRequests) {
            cancelRequest(request);
        }
    }

    private static synchronized void cancelRequest(Call request) {
        if (request == null) {
            H.logD("request is null, can't be cancelled");
            removeRequest(request);
            return;
        }

        if (!request.isCanceled()) {
            request.cancel();
            H.logD("request cancelled : " + request.toString());
        } else {
            H.logD("request already cancelled");
        }

        removeRequest(request);
    }

    private static synchronized void removeRequest(Call request) {
        if (request == null || runningRequests == null) {
            return;
        }

        runningRequests.remove(request);
    }
}
