package com.mc.youthhostels.fragments;


import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.mc.youthhostels.R;
import com.mc.youthhostels.activities.AppActivity;
import com.mc.youthhostels.events.booking.FacebookLoginCompletedEvent;
import com.mc.youthhostels.social.SocialHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import api.API;
import de.greenrobot.event.EventBus;
import entity.User.UserProfile;
import entity.User.UserProfileMapper;
import helper.App;
import helper.H;
import helper.IGetRequest;

public class BaseLoginFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
                                                           GoogleApiClient.OnConnectionFailedListener {
    private static int RC_SIGN_IN = 0;
    protected CallbackManager callbackManager;
    protected GoogleApiClient googleApiClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        registerFacebookCallback();
        initGoogleClient();
    }

    private void initGoogleClient() {
        googleApiClient = new GoogleApiClient.Builder(App.getInstance().getBaseContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PLUS_LOGIN))
                .addScope(new Scope(Scopes.PLUS_ME))
                .build();
    }

    private void registerFacebookCallback() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        H.logD("facebook success login");

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,first_name,last_name,link,email,picture,gender,location");
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(final JSONObject jsonObject, GraphResponse graphResponse) {
                                if (jsonObject != null) {
                                    final String token = loginResult.getAccessToken().getToken();

                                    API.getInstance(App.getInstance().getApplicationContext()).doFacebookLogin(token, new IGetRequest() {
                                        @Override
                                        public void onSuccess(String message) {
                                            try {
                                                SocialHelper.saveMemberInfoFromFacebook(jsonObject);
                                                H.logD("user saved from facebook");
                                            } catch (JSONException e) {
                                                H.logE(e);
                                            }

                                            EventBus.getDefault().post(new FacebookLoginCompletedEvent());
                                        }

                                        @Override
                                        public void onError(String message) {
                                            H.showDialog(message);
                                        }
                                    });
                                }
                            }
                        });
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        H.logD("on facebook cancel");
                        //Toast.makeText(MainActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException e) {
                        if (e instanceof FacebookAuthorizationException) {
                            if (AccessToken.getCurrentAccessToken() != null) {
                                H.logD("try logout from different user");
                                LoginManager.getInstance().logOut();
                                startFacebookAuth();
                            }
                        } else {
                            H.logE(e);
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        H.logD("on activity result login fragment started");
        if (requestCode == RC_SIGN_IN) {
            googleApiClient.connect();
            H.logD("on activity result google");
        } else if (requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode())   {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            H.logD("on activity result facebook");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveMemberInfoFromGoogle() {
        final Person currentPerson = Plus.PeopleApi.getCurrentPerson(googleApiClient);
        if (currentPerson == null) {
            H.showDialog(H.getString(R.string.google_auth_error));
            return;
        }

        UserProfileMapper mapper = new UserProfileMapper(App.getInstance());

        mapper.deleteAllEntities();

        UserProfile userProfile = new UserProfile();

        userProfile.setEmail(Plus.AccountApi.getAccountName(googleApiClient));
        userProfile.setImage(currentPerson.getImage().getUrl());
        final String firstName = currentPerson.getName().getGivenName();
        userProfile.setFirst_name(firstName);
        final String lastName = currentPerson.getName().getFamilyName();
        userProfile.setLast_name(lastName);

        mapper.saveEntity(userProfile);

        H.logD("member info from google saved");
        EventBus.getDefault().post(new FacebookLoginCompletedEvent());
    }

    private class GetIdTokenTask extends AsyncTask<Void, Void, String> {
        final String SERVER_CLIENT_ID = "676388853281-q7i5aocgi95iku5fl2njo2jki9bive2a.apps.googleusercontent.com";

        @Override
        protected String doInBackground(Void... params) {
            String accountName = Plus.AccountApi.getAccountName(googleApiClient);
            String scope = "audience:server:client_id:" + SERVER_CLIENT_ID; // Not the app's client ID.

            try {
                return GoogleAuthUtil.getToken(App.getInstance(), accountName, scope);
            } catch (Exception e) {
                H.logE("Error retrieving ID token. " + e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                final Person currentPerson = Plus.PeopleApi.getCurrentPerson(googleApiClient);
                if (currentPerson == null) {
                    H.showDialog(H.getString(R.string.google_auth_error));
                    return;
                }
                API.getInstance(App.getInstance()).doGooglePlusLogin(result, new IGetRequest() {
                    @Override
                    public void onSuccess(String message) {
                        saveMemberInfoFromGoogle();
                    }

                    @Override
                    public void onError(String message) {
                        H.showDialog(message);
                    }
                });
            } else {
                // todo
                // There was some error getting the ID Token
                // ...
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        H.logD("on connection failed");

        if (connectionResult.hasResolution()) {
            H.logD("has resolution for google+ connection result");

            try {
                getActivity().startIntentSenderForResult(connectionResult.getResolution().getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);

            } catch (IntentSender.SendIntentException e) {
                H.logE("Could not resolve ConnectionResult." + e.getMessage());
                googleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        H.logD("on connected to google");
        try {
            new GetIdTokenTask().execute();
        } catch (Exception e) {
            H.logE("can't fetch token " + e);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        H.logD("connection suspended");
    }

    @Override
    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    protected void startFacebookAuth() {
        H.logD("start facebook auth");
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    protected void startGoogleAuth() {
        H.logD("start google auth");
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        googleApiClient.connect();
    }
}
