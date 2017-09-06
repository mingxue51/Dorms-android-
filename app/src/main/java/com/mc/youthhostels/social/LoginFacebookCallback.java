package com.mc.youthhostels.social;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import api.API;
import helper.App;
import helper.H;
import helper.IGetRequest;

public class LoginFacebookCallback implements FacebookCallback<LoginResult> {

    SocialCallback socialCallback;

    public LoginFacebookCallback(@NonNull SocialCallback socialCallback) {
        this.socialCallback = socialCallback;
    }

    @Override
    public void onSuccess(final LoginResult loginResult) {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,first_name,last_name,link,email,picture,gender,location");
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), (jsonObject, graphResponse) -> {
            if (jsonObject != null) {
                final String token = loginResult.getAccessToken().getToken();

                API.getInstance(App.getInstance().getApplicationContext()).doFacebookLogin(token, new IGetRequest() {
                    @Override
                    public void onSuccess(String message) {
                        socialCallback.onLoginSuccess(jsonObject);
                    }

                    @Override
                    public void onError(String message) {
                    }
                });
            }
        });
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onCancel() {
    }

    @Override
    public void onError(FacebookException e) {
        // todo right-error-handle
        H.logE(e.getMessage());
    }
}
