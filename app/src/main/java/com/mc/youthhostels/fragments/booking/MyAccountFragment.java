package com.mc.youthhostels.fragments.booking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import entity.Generic.Country;
import entity.User.User;
import entity.User.UserProfile;
import entity.User.UserProfileMapper;
import helper.App;
import helper.H;
import helper.IGetRequest;

public class MyAccountFragment extends Fragment implements Validator.ValidationListener{

    @NotEmpty(messageResId = R.string.field_is_required)
    @Bind(R.id.txt_first_name)
    EditText firstName;

    @NotEmpty(messageResId = R.string.field_is_required)
    @Bind(R.id.txt_last_name)
    EditText lastName;

    @Bind(R.id.account_gender)
    Spinner gender;

    @Email(messageResId = R.string.invalid_email)
    @NotEmpty(messageResId = R.string.field_is_required)
    @Bind(R.id.txt_email_account)
    EditText email;

    //@NotEmpty(messageResId = R.string.field_is_required)
    @Bind(R.id.edt_curr_passw)
    EditText currentPassword;

    //@NotEmpty(messageResId = R.string.field_is_required)
    @Bind(R.id.edt_new_passw)
    EditText newPassword;

    //@NotEmpty(messageResId = R.string.field_is_required)
    @Bind(R.id.edt_confirm_passw)
    EditText newPasswordConfirmation;

    @NotEmpty(messageResId = R.string.field_is_required)
    @Bind(R.id.my_account_nationality)
    AutoCompleteTextView nationality;

    @Bind(R.id.img_user_avatar)
    ImageView avatar;

    private Validator validator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);
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
            firstName.setText(userProfile.getFirst_name());
            lastName.setText(userProfile.getLast_name());
            nationality.setText(userProfile.getHome_country());
            email.setText(userProfile.getEmail());
            gender.setSelection(userProfile.getGender_id());

            String avatarUrl = userProfile.getImage();
            if (!TextUtils.isEmpty(avatarUrl)) {
                Glide.with(App.getInstance()).load(avatarUrl)
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(avatar);
            }
        }

        initNationalityAutoComplete();
        initGenderProfile();
    }

    @Override
    public void onResume() {
        super.onResume();
        AppActivity activity = (AppActivity) getActivity();
        activity.setDefaultActionBar();
        activity.setActionBarTitle(H.getString(R.string.my_account));
    }

    @Override
    public void onValidationSucceeded() {
        // all user data is valid
        UserProfileMapper mapper = new UserProfileMapper(getContext());
        UserProfile userProfile = mapper.getUserProfile();
        final API api = API.getInstance(App.getInstance());
        api.updateProfile(userProfile, new IGetRequest() {
            @Override
            public void onSuccess(String message) {
                if (User.isLogged()) {
                    UserProfile userProfile = UserProfile.getUserProfile();
                    userProfile.setFirst_name(firstName.getText().toString());
                    userProfile.setLast_name(lastName.getText().toString());
                    int genderId = gender.toString().equalsIgnoreCase(H.getString(R.string.gender_account_male)) ? 0 : 1;
                    userProfile.setGender_id(genderId);
                    userProfile.setEmail(email.getText().toString());
                    userProfile.setHome_country(nationality.getText().toString());
                    mapper.saveEntity(userProfile);

                    H.runOnUi(() -> Toast.makeText(getContext(),
                                                   R.string.profile_updated,
                                                   Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onError(String message) {
            }
        });
    }

    @OnClick(R.id.btn_account_save)
    public void saveChangesClicked() {
        validator.validate();
    }

    @OnClick(R.id.btn_account_change_password)
    public void changePasswordClicked() {
        UserProfileMapper mapper = new UserProfileMapper(getContext());
        UserProfile userProfile = mapper.getUserProfile();
        if (User.isLogged()) {
            if (currentPassword.getText().toString().equals(userProfile.getPassword())) {
                if (newPassword.getText().toString().equals(
                        newPasswordConfirmation.getText().toString())) {
                    final API api = API.getInstance(App.getInstance());
                    api.ChangePassword(currentPassword.getText().toString(),
                            newPassword.getText().toString(), new IGetRequest() {
                                @Override
                                public void onSuccess(String message) {
                                    H.runOnUi(() -> Toast.makeText(getContext(), R.string.password_changed,
                                            Toast.LENGTH_SHORT).show());
                                }

                                @Override
                                public void onError(String message) {
                                }
                            });
                }
            } else {
                Toast.makeText(getContext(), R.string.password_not_equals, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.delete_my_account)
    public void deleteAccount() {
        UserProfileMapper mapper = new UserProfileMapper(App.getInstance().getApplicationContext());
        if (User.isLogged()) {
            final API api = API.getInstance(App.getInstance());
            api.cancelAccount(new IGetRequest() {
                @Override
                public void onSuccess(String message) {
                    getActivity().onBackPressed();
                    mapper.deleteAllEntities();
                }

                @Override
                public void onError(String message) {
                }
            });
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

    private void initNationalityAutoComplete() {
        AutoCompleteTextView autoCompleteTextView =
                (AutoCompleteTextView) getView().findViewById(R.id.my_account_nationality);

        List<String> countriesNames = Country.getCountriesNames();
        String[] names = countriesNames.toArray(new String[countriesNames.size()]);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                R.layout.account_listdrop_layout,
                names);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                App.getInstance().getCurrentActivity().hideKeyboard();
            }
        });
    }

    private void initGenderProfile() {
        String[] data = getResources().getStringArray(R.array.gender);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner_text, data);
        adapter.setDropDownViewResource(R.layout.account_listdrop_layout);
        gender.setAdapter(adapter);
    }
}
