package com.mc.youthhostels.fragments.booking;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.mc.youthhostels.R;
import com.mc.youthhostels.entity.Order;
import com.mc.youthhostels.events.booking.NextOnPersonDetailsClickedEvent;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import entity.Generic.Country;
import entity.User.User;
import entity.User.UserProfile;
import entity.User.UserProfileMapper;
import helper.App;
import helper.H;

public class PersonalInformationFragment extends Fragment implements Validator.ValidationListener{
    @NotEmpty(messageResId = R.string.field_is_required)
    @Bind(R.id.txt_first_name)
    EditText firstName;

    @NotEmpty(messageResId = R.string.field_is_required)
    @Bind(R.id.txt_last_name)
    EditText lastName;

    @Email(messageResId = R.string.invalid_email)
    @NotEmpty(messageResId = R.string.field_is_required)
    @Bind(R.id.txt_email)
    EditText email;

    @NotEmpty(messageResId = R.string.field_is_required)
    @Bind(R.id.txt_nationality)
    AutoCompleteTextView nationality;

    @NotEmpty(messageResId = R.string.field_is_required)
    @Bind(R.id.txt_phone_number)
    EditText phoneNumber;

    @Bind(R.id.spinner_check_in)
    Spinner checkInSpinner;

    private Validator validator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_information, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (User.isLogged()) {
            UserProfileMapper mapper = new UserProfileMapper(getActivity());
            UserProfile userProfile = mapper.getUserProfile();
            firstName.setText(userProfile.getFirst_name());
            lastName.setText(userProfile.getLast_name());
            String userEmail = userProfile.getEmail();
            setUpEmail(userEmail);
            nationality.setText(userProfile.getHome_country());
            phoneNumber.setText(userProfile.getPhone_number());
        }

        initNationalityAutoComplete();
        initCheckInSpinner();
    }

    private void setUpEmail(String userEmail) {
        email.setTextColor(H.getColor(R.color.grey_checkable));
        email.setText(userEmail);
        email.setInputType(0);
        email.setClickable(false);
        email.setFocusable(false);
        email.setKeyListener(null);
    }

    @Override
    public void onResume() {
        super.onResume();

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.personal_information);

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    private void initNationalityAutoComplete() {
        AutoCompleteTextView autoCompleteTextView;

        List<String> countriesNames = Country.getCountriesNames();
        String[] names = countriesNames.toArray(new String[countriesNames.size()]);

        autoCompleteTextView = (AutoCompleteTextView) getView().findViewById(R.id.txt_nationality);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
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

    private void initCheckInSpinner() {
        List<String> timesList = getCheckinTimes();
        String[] times = timesList.toArray(new String[timesList.size()]);

        Spinner spinner = (Spinner) getView().findViewById(R.id.spinner_check_in);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.item_spinner_simple, R.id.txt_text_item, times);
        spinner.setAdapter(adapter);
        spinner.setSelection(3);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static ArrayList<String> getCheckinTimes() {
        ArrayList<String> checkInList = new ArrayList<String>();
        checkInList.add("11:00");
        checkInList.add("12:00");
        checkInList.add("13:00");
        checkInList.add("14:00");
        checkInList.add("15:00");
        checkInList.add("16:00");
        checkInList.add("17:00");
        checkInList.add("18:00");
        checkInList.add("19:00");
        checkInList.add("20:00");
        checkInList.add("21:00");
        checkInList.add("22:00");
        checkInList.add("23:00");
        return checkInList;
    }

    @OnClick(R.id.btn_next)
    public void onNextClicked() {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        Order order = App.getInstance().getOrder();
        order.setFirstName(firstName.getText().toString());
        order.setLastName(lastName.getText().toString());
        order.setEmail(email.getText().toString());
        order.setNationality(nationality.getText().toString());
        order.setPhoneNumber(phoneNumber.getText().toString());
        order.setCheckInTime((String)checkInSpinner.getSelectedItem());

        EventBus.getDefault().post(new NextOnPersonDetailsClickedEvent());
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            }
        }
    }
}
