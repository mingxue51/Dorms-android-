package service;

import android.content.Context;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import entity.Generic.Currency;
import helper.App;
import helper.Root;

public class Localization extends Root {

    public Localization(Context context) {
        super(context);
    }

    public static Currency getFormattedCurrency(Context context) {
        Currency currency= new Currency();
        NumberFormat priceFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        String s = priceFormat.getCurrency().getSymbol();
        currency.setSymbol(s);
        currency.setCode(priceFormat.getCurrency().getCurrencyCode());
        return currency;

/*
        // This formats currency values as the user expects to read them (default locale).
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        NumberFormat priceFormat = NumberFormat.getCurrencyInstance(H.getCurrentLocale(context));
*/
        // This specifies the actual currency that the value is in, and provides the currency symbol.

/*      java.util.Currency currency1 = java.util.Currency.getInstance(Locale.getDefault());
        String curSymbol = currency1.getCurrencyCode();
          NumberFormat priceFormat = NumberFormat.getCurrencyInstance(H.getCurrentLocale(context));
        String sa = priceFormat.getCurrency().getSymbol();
        //String l = Language.getLanguage(context);
        java.util.Currency currency = java.util.Currency.getInstance(s);
        currency = java.util.Currency.getInstance(H.getCurrentLocale(context));
        String symbol = currency.getSymbol();
*/
        // Our fix is to use the US locale as default for the symbol, unless the currency is USD
        // and the locale is NOT the US, in which case we know it should be US$.
//        String symbol;
/*
        if (isoCurrencyCode.equalsIgnoreCase("usd") && !Locale.getDefault().equals(Locale.US)) {
            symbol = "US$";
        } else {
            symbol = currency.getSymbol(Locale.US); // US locale has the best symbol formatting table.
        }*/
    }

    public static Currency getFormattedCurrencyStringByCode(Context context, String currencyCode) {
        java.util.Currency currency = java.util.Currency.getInstance(currencyCode);

        Currency currency_= new Currency();
        currency_.setSymbol(currency.getSymbol());
        currency_.setCode(currency.getCurrencyCode());

        return currency_;
    }

    public static Locale getCurrentLocale(Context context){
        return context.getResources().getConfiguration().locale;
    }

    private static Map<String, NumberFormat> numberFormatCache = new HashMap<String, NumberFormat>();

    public static String getPriceLocalized(Context context, BigDecimal price){
        final String currentCurrencyCode = Currency.getCurrency().getCode();

        return getFormattedPrice(price, currentCurrencyCode);
    }

    public static String getFormattedPrice(BigDecimal price, String currencyCode) {
        NumberFormat priceFormat = numberFormatCache.get(currencyCode);

        if (priceFormat == null) {
            Locale currencyLocale = getLocaleFromCurrencyCode(currencyCode);

            if (currencyLocale == null) {
                currencyLocale = getCurrentLocale(App.getInstance());
            }

            priceFormat = NumberFormat.getCurrencyInstance(currencyLocale);
            priceFormat.setCurrency(java.util.Currency.getInstance(currencyCode));
            numberFormatCache.put(currencyCode, priceFormat);
        }

        return priceFormat.format(price);
    }

    public static Locale getLocaleFromCurrencyCode(String currencyCode) {
        for (Locale locale : NumberFormat.getAvailableLocales()) {
            String code = NumberFormat.getCurrencyInstance(locale).getCurrency().getCurrencyCode();
            if (currencyCode.equals(code)) {
                return locale;
            }
        }
        return null;
    }
}
