package com.mc.youthhostels.fragments.signin;

import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.facebook.CallbackManager;
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
import com.mc.youthhostels.MainActivity2;
import com.mc.youthhostels.R;
import com.mc.youthhostels.activities.AppActivity;
import com.mc.youthhostels.events.booking.FacebookLoginCompletedEvent;
import com.mc.youthhostels.fragments.BaseLoginFragment;
import com.mc.youthhostels.fragments.booking.PasswordRecoveryFragment;
import com.mc.youthhostels.fragments.booking.SignUpFragment;
import com.mc.youthhostels.presenters.LoginPresenter;
import com.mc.youthhostels.social.SocialHelper;
import com.mc.youthhostels.views.LoginView;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import api.API;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import helper.App;
import helper.H;


public class LoginFragment extends BaseLoginFragment implements LoginView,
                                                       Validator.ValidationListener {
    public static final String TAG = LoginFragment.class.getSimpleName();
    @Email(messageResId = R.string.invalid_email)
    @NotEmpty(messageResId = R.string.field_is_required)
    @Bind(R.id.txt_email)
    EditText email;
    @NotEmpty(messageResId = R.string.field_is_required)
    @Bind(R.id.txt_password)
    EditText password;

    private LoginPresenter presenter;
    private Validator validator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        validator = new Validator(this);
        validator.setValidationListener(this);
        presenter = new LoginPresenter(this);
        presenter.onCreate();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
        ((AppActivity)getActivity()).setDefaultActionBar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void showLoginError() {
    }

    @OnClick(R.id.card_facebook)
    public void onFacebookClicked() {
        startFacebookAuth();
    }

    @OnClick(R.id.card_google)
    public void onGoogleClicked() {
        H.logD("on google button clicked");
        startGoogleAuth();
    }

    @OnClick(R.id.btn_sign_in)
    public void signInClicked() {
        validator.validate();
    }

    @OnClick(R.id.btn_forgot_password)
    void forgotClick() {
        ((MainActivity2) getActivity()).showFragment(new PasswordRecoveryFragment());
    }

    @OnClick(R.id.btn_register)
    void registerClick() {
        ((MainActivity2) getActivity()).showFragment(new SignUpFragment());
    }

    @Override
    public void showLoading() {
        App.getInstance().showLoading();
    }

    @Override
    public void hideLoading() {
        App.getInstance().hideLoading();
    }

    @Override
    public void onValidationSucceeded() {
        presenter.login(email.getText().toString(), password.getText().toString());
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            }
        }
    }
}

