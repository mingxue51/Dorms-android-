package com.mc.youthhostels.presenters;


import com.mc.youthhostels.R;
import com.mc.youthhostels.events.booking.FacebookLoginCompletedEvent;
import com.mc.youthhostels.views.LoginView;

import api.API;
import de.greenrobot.event.EventBus;
import entity.User.UserProfile;
import entity.User.UserProfileMapper;
import helper.App;
import helper.H;
import helper.IGetRequest;

public class LoginPresenter implements Presenter {

    private LoginView loginView;

    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
        loginView = null;
    }

    @Override
    public void onPause() {
    }

    public void login(String email, String password) {
        loginView.showLoading();
        final API api = API.getInstance(App.getInstance());
        api.login(email, password, new IGetRequest() {
            @Override
            public void onError(String message) {
                loginView.hideLoading();
            }

            @Override
            public void onSuccess(String message) {
                api.GetUserProfile(new IGetRequest() {
                    @Override
                    public void onError(String message) {
                        App.getInstance().showToast(H.getString(R.string.invalid_login));
                    }

                    @Override
                    public void onSuccess(String message) {
                        UserProfileMapper mapper = new UserProfileMapper(App.getInstance());
                        UserProfile userProfile = mapper.getUserProfile();
                        userProfile.setEmail(email);
                        mapper.saveEntity(userProfile);

                        loginView.hideLoading();
                        EventBus.getDefault().post(new FacebookLoginCompletedEvent());
                    }
                });
            }
        });
    }
}
