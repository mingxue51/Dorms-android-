package com.mc.youthhostels.fragments.booking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mc.youthhostels.R;
import com.mc.youthhostels.events.booking.SkipStepClickedEvent;
import com.mc.youthhostels.fragments.BaseLoginFragment;
import com.mc.youthhostels.presenters.LoginPresenter;
import com.mc.youthhostels.views.LoginView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import helper.App;
import helper.H;

public class FastLoginFragment extends BaseLoginFragment implements LoginView {
    public static final String TAG = FastLoginFragment.class.getSimpleName();
    @Bind(R.id.txt_email)
    EditText email;
    @Bind(R.id.txt_password)
    EditText password;

    private LoginPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        H.logD("fast login on ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fast_sign_in, container, false);
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
    public void onGooglePlusClicked() {
        startGoogleAuth();
    }

    @OnClick(R.id.btn_skip_step)
    public void skipStepClicked() {
        EventBus.getDefault().post(new SkipStepClickedEvent());
    }

    @OnClick(R.id.btn_sign_in)
    public void signInClicked() {
        presenter.login(email.getText().toString(), password.getText().toString());
    }

    @Override
    public void showLoading() {
        App.getInstance().showLoading();
    }

    @Override
    public void hideLoading() {
        App.getInstance().hideLoading();
    }
}
