package api;

import android.os.Bundle;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

import org.json.simple.JSONObject;

import java.util.HashMap;

import entity.User.User;
import entity.User.UserMapper;
import entity.User.UserProfileMapper;
import helper.App;
import helper.H;
import helper.IGetRequest;

public class Signout extends BaseTask implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;

    public Signout(API api,  IGetRequest callback) {
		super(callback);
        User user = User.getUserFromSession(getContext());
		HashMap<String, String> params = new HashMap<String, String>();
        params.put("member_number", user.getMember_id());
        params.put("session_id", user.getSession_id());
	    createParams("member_signout?api_key="+API.API_KEY, params);
	}

	@Override
	protected void onResponse(Object response) {
		JSONObject json = (JSONObject)response;
        if (json.containsKey("success")) {
            JSONObject responseObject = (JSONObject)json.get("success");
            boolean status = Boolean.parseBoolean(responseObject.get("success_status").toString());
            if(status){
                H.clearUserData();
                onSuccess(parseMessage(responseObject, "success_message"));

                mGoogleApiClient = new GoogleApiClient.Builder(App.getInstance().getBaseContext())
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(Plus.API)
                        .addScope(new Scope(Scopes.PLUS_LOGIN))
                        .addScope(new Scope(Scopes.PLUS_ME))
                        .build();

                LoginManager.getInstance().logOut();
            }else{
                // todo right-error-handle
                Toast.makeText(getContext(), "Please, Try again.", Toast.LENGTH_LONG).show();
            }
        }
	}

    // when mGoogleApiClient connect
    @Override
    public void onConnected(Bundle bundle) {
        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
        mGoogleApiClient.disconnect();
        H.logD("google api client disconnect");
    }

    @Override
    public void onConnectionSuspended(int i) {
        // todo right-error-handle
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // todo right-error-handle
    }
}
