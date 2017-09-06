package com.mc.youthhostels.fragments;


import com.mc.youthhostels.R;
import com.mc.youthhostels.dialog.Checkable;

import java.util.ArrayList;
import java.util.List;

import entity.Generic.Currency;

public class CurrencyChooserFragment extends ChooserFragment {

    @Override
    List<Checkable> getData() {
        List<Checkable> checkables = new ArrayList<>();

        for (Currency currency : Currency.getCurrencies()) {
            String currentCurrencyCode = Currency.getCurrency(getContext()).getCode();
            String currencyCode = currency.getCode();
            currency.setChecked(currentCurrencyCode.equals(currencyCode));

            checkables.add(currency);
        }

        return checkables;
    }

    @Override
    boolean isScrollable() {
        return true;
    }

    @Override
    int getLayoutResource() {
        return R.layout.fragment_chooser;
    }
}
