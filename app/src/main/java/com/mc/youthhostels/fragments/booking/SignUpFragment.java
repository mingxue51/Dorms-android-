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
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import api.API;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import helper.App;
import helper.H;
import helper.IGetRequest;

public class SignUpFragment extends Fragment implements Validator.ValidationListener {

    @NotEmpty(messageResId = R.string.field_is_required)
    @Bind(R.id.txt_first_name_sign)
    EditText firstName;

    @NotEmpty(messageResId = R.string.field_is_required)
    @Bind(R.id.txt_last_name_sign)
    EditText lastName;

    @Email(messageResId = R.string.invalid_email)
    @NotEmpty(messageResId = R.string.field_is_required)
    @Bind(R.id.txt_email_sign)
    EditText email;

    @Length(min = 8, max = 20, messageResId = R.string.error_length_password)
    @NotEmpty(messageResId = R.string.field_is_required)
    @Bind(R.id.txt_password)
    EditText password;

    @NotEmpty(messageResId = R.string.field_is_required)
    @Bind(R.id.txt_password_confirmation)
    EditText passwordConfirm;

    private Validator validator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
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
        activity.setActionBarTitle(H.getString(R.string.sign_up));
    }

    @Override
    public void onValidationSucceeded() {
        if (password.getText().toString().equalsIgnoreCase(
                passwordConfirm.getText().toString())) {
            signUp(firstName.getText().toString(), lastName.getText().toString(),
                    email.getText().toString(), password.getText().toString());
        } else {
            Toast.makeText(getContext(), H.getString(R.string.error_equals_passwords),
                    Toast.LENGTH_SHORT).show();
        }
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

    @OnClick(R.id.btn_sign_up)
    void singUpClick() {
        validator.validate();
    }

    public void signUp(String firstName, String lastName, String
                        email, String password) {
        final API api = API.getInstance(App.getInstance());
        api.signUp(firstName, lastName, email, password, new IGetRequest() {
            @Override
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
