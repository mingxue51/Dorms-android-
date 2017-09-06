package com.mc.youthhostels.fragments.booking;

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
import helper.App;
import helper.H;
import helper.IGetRequest;

public class PasswordRecoveryFragment extends Fragment implements Validator.ValidationListener {

    @Email(messageResId = R.string.invalid_email)
    @NotEmpty(messageResId = R.string.field_is_required)
    @Bind(R.id.email_recover)
    EditText email;

    private Validator validator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recovery, container, false);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        AppActivity activity = (AppActivity) getActivity();
        activity.setDefaultActionBar();
        activity.setActionBarTitle(H.getString(R.string.recover_password));
    }

    @Override
    public void onValidationSucceeded() {
        ForgetPassword(email.getText().toString());
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

    @OnClick(R.id.btn_recover)
    void recoveryClick() {
        validator.validate();
    }

    public void ForgetPassword(String
            email) {
        final API api = API.getInstance(App.getInstance());
        api.forgetPassword(email, new IGetRequest() {
            public void onSuccess(String message) {
                H.runOnUi(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String message) {
                H.runOnUi(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
