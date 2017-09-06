package com.mc.youthhostels.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.mc.youthhostels.R;
import com.mc.youthhostels.activities.AppActivity;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import api.API;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import entity.User.User;
import entity.User.UserProfile;
import helper.App;
import helper.H;
import helper.IGetRequest;

public class ContactUsFragment extends Fragment implements Validator.ValidationListener {

    @Email(messageResId = R.string.invalid_email)
    @NotEmpty(messageResId = R.string.field_is_required)
    @Bind(R.id.txt_email_contact)
    EditText email;

    @NotEmpty(messageResId = R.string.field_is_required)
    @Bind(R.id.txt_name_contact)
    EditText name;

    @NotEmpty(messageResId = R.string.field_is_required)
    @Bind(R.id.txt_subject_contact)
    EditText subject;

    @NotEmpty(messageResId = R.string.field_is_required)
    @Bind(R.id.txt_message_contact)
    EditText message;

    private Validator validator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (User.isLogged()) {
            UserProfile userProfile = UserProfile.getUserProfile();
            email.setText(userProfile.getEmail());
            name.setText(userProfile.getFullName());
        }
    }

    @Override
    public void onValidationSucceeded() {
        contactUs(name.getText().toString(), email.getText().toString(),
                  message.getText().toString(), subject.getText().toString());
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

    @Override
    public void onResume() {
        super.onResume();
        AppActivity activity = (AppActivity) getActivity();
        activity.setDefaultActionBar();
        activity.setActionBarTitle(H.getString(R.string.contact_us));
    }

    public void contactUs(String fullName, String email, String
                          message, String subject ) {
        API api = API.getInstance(App.getInstance());
        App.getInstance().showLoading();
        api.contactUs(fullName,fullName, email, message,
                subject, new IGetRequest() {
                    @Override
                    public void onSuccess(String message) {
                        H.runOnUi(() -> {
                            App.getInstance().getCurrentActivity().hideLoadingAndBack(1);
                            H.logD("contact us caseId processed:" + message);
                            Toast.makeText(App.getInstance(), R.string.contact_us_finish_message, Toast.LENGTH_LONG).show();
                        });
                    }

                    @Override
                    public void onError(String message) {
                        App.getInstance().hideLoading();
                    }
                });
    }

    @OnClick(R.id.btn_send)
    public void onSendClicked() {
        validator.validate();
    }
}
