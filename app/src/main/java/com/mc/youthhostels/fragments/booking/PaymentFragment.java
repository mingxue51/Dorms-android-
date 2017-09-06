package com.mc.youthhostels.fragments.booking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.mc.youthhostels.R;
import com.mc.youthhostels.activities.AppActivity;
import com.mc.youthhostels.entity.Order;
import com.mc.youthhostels.events.booking.ConfirmPaymentClickedEvent;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Max;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import entity.Generic.Content;
import helper.App;
import helper.H;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import service.Localization;

public class PaymentFragment extends Fragment implements Validator.ValidationListener {
    private static final int SCAN_REQUEST_CODE = 143;

    @Length(min = 3, max = 4, messageResId = R.string.field_is_required)
    @Bind(R.id.txt_security_code)
    EditText securityCode;

    @Bind(R.id.txt_total_to_be_paid_now)
    TextView totalToBePaidNow;

    @Bind(R.id.txt_terms)
    TextView textTerms;

    @Bind(R.id.txt_upon_arrival)
    TextView uponArrival;

    @Length(min = 16, max = 16, messageResId = R.string.credit_card_length_message)
    @Bind(R.id.txt_card_number)
    EditText cardNumber;

    @Min(value = 16, messageResId = R.string.invalid_year)
    @Bind(R.id.txt_yy)
    EditText year;

    @Max(value = 12, messageResId = R.string.invalid_month)
    @Bind(R.id.txt_mm)
    EditText month;

    @NotEmpty(messageResId = R.string.field_is_required)
    @Bind(R.id.txt_cardholder_name)
    EditText cardholderName;

    @Bind(R.id.spinner_card_types)
    Spinner cardTypesSpinner;

    private Validator validator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.icon_action_payment_lock);
        toolbar.setTitle(R.string.payment);

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setLogo(null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Order order = App.getInstance().getOrder();
        totalToBePaidNow.setText(Localization.getPriceLocalized(App.getInstance(),
                order.getPayNowPrice()));
        textTerms.append(" " + Localization.getPriceLocalized(App.getInstance(),
                order.getPayNowPrice()));
        uponArrival.setText(String.format(uponArrival.getText().toString(),
                Localization.getPriceLocalized(App.getInstance(),
                        order.getUponArrival())));

        cardholderName.setText(order.getFirstName().toUpperCase() + " " + order.getLastName().toUpperCase());

        if (H.paymentFakeMode) {
            cardholderName.setText("TestUser");
            cardNumber.setText("4715320629000001");
            securityCode.setText("123");
            month.setText("10");
            year.setText("16");
        }

        initCardTypeSpinner(order);
    }

    private void initCardTypeSpinner(Order order) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                                                          R.layout.spinner_text,
                                                          order.getCardTypesAvailable());
        adapter.setDropDownViewResource(R.layout.account_listdrop_layout);
        cardTypesSpinner.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_scan_credit_card)
    public void onScanPress(View v) {
        Intent scanIntent = new Intent(getActivity(), CardIOActivity.class);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true);
        startActivityForResult(scanIntent, SCAN_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        H.logD("start on activity result in payment fragment: requstCode" +
               requestCode + " resultCode" + resultCode);
        if (requestCode == SCAN_REQUEST_CODE) {
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                cardNumber.setText(scanResult.getFormattedCardNumber().replace(" ", ""));
                if (scanResult.isExpiryValid()) {
                    month.setText(String.valueOf(scanResult.expiryMonth));
                    year.setText(String.valueOf(scanResult.expiryYear).substring(2,4));
                }
                if (scanResult.cvv != null) {
                    securityCode.setText(String.valueOf(scanResult.cvv));
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onValidationSucceeded() {
        // todo add validation that any selected

        Order order = App.getInstance().getOrder();
        order.setCcv(securityCode.getText().toString());
        order.setCreditCardNumber(cardNumber.getText().toString().replace(" ", ""));
        order.setCardHolderNamee(cardholderName.getText().toString());
        order.setMonth(month.getText().toString());
        order.setYear(year.getText().toString());
        order.setCreditCardType(cardTypesSpinner.getSelectedItem().toString());

        EventBus.getDefault().post(new ConfirmPaymentClickedEvent());
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

    @OnClick(R.id.btn_confirm)
    public void onConfirmClicked() {
        validator.validate();
    }

    @OnClick(R.id.txt_terms)
    void openTermsFragment() {
        ContentFragment fragment = ContentFragment.newInstance(Content.TAC);
        AppActivity currentActivity = App.getInstance().getCurrentActivity();
        if (currentActivity != null) {
            currentActivity.showFragment(fragment);
        } else {
            H.logE("current activity is null while open tac");
        }
    }
}
