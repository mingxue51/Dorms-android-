package entity.Generic;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Spinner;

import com.mc.youthhostels.dialog.Checkable;
import com.mc.youthhostels.events.CurrencyChangedEvent;

import junit.framework.Assert;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import helper.App;
import helper.H;
import helper.Session;
import service.Localization;

public class Currency implements Checkable, Serializable{
    private static final long serialVersionUID = 1L;

    private Integer currencyId;
    private String code;
    private String symbol;
    private String descriptionEn;
    private boolean checked;

    public Currency(){
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSymbol() {
        if((symbol != null) && (!symbol.isEmpty()))return symbol;
        else return code;
    }

    public Currency setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public static void setCurrency(Context pContext, Currency currency){
        Session.setStringValue(pContext, Session.SELECTED_CURRENCY_CODE, currency.getCode());//Session.setStringValue(pContext, Session.SELECTED_CURRENCY_SYMBOL, currency.getSymbol());
    }

    public static void clearCurrency(Context pContext){
        Session.setStringValue(pContext, Session.SELECTED_CURRENCY_CODE, "");
    }

    public static Currency getCurrency(Context pContext){
        String currencyCode = Session.getStringValue(pContext, Session.SELECTED_CURRENCY_CODE);        //String currency_symbol = Session.getStringValue(pContext, Session.SELECTED_CURRENCY_SYMBOL); && currency_symbol!=null && !currency_symbol.isEmpty()
        if(currencyCode != null && !currencyCode.isEmpty() ){
            return Localization.getFormattedCurrencyStringByCode(pContext,currencyCode);
        }

        return Localization.getFormattedCurrency(pContext);
    }

    public static Currency getCurrency() {
        return getCurrency(App.getInstance());
    }

    public Currency(Integer currencyId, String code, String symbol, String descriptionEn) {
        this.currencyId = currencyId;
        this.code = code;
        this.symbol = symbol;
        this.descriptionEn = descriptionEn;
    }

    public void setJSON(JSONObject obj) {
        setCurrencyId(H.toInt(obj, "currency_id"));
        setCode(H.toString(obj, "currency_code"));
        setSymbol(H.toString(obj, "symbol"));
        setDescriptionEn(H.toString(obj, "description_en"));
    }

    public static List<Currency> getList(Object json) {
        ArrayList<Currency> result = new ArrayList<Currency>();
        try {
            JSONArray array = (JSONArray) json;
            for (Object key : array) {
                JSONObject obj = (JSONObject) key;
                Currency entity = new Currency();
                entity.setJSON(obj);
                result.add(entity);
            }
        } catch (Exception e) {
            H.logE("Can't create Currency list from JSON Array.");
            H.logE(e.getMessage());
        }
        return result;
    }

    public String getSpinnerString(){
        return this.code + "-" + this.descriptionEn;
    }

    @Override
    public String toString() {
        return  this.getSpinnerString();
    }


    public static int getSelectionIndexById(Spinner spinner,List<Currency> currencies, int id)
    {
        int index = 0;
        for (int i=0;i<currencies.size();i++){
            if(id == currencies.get(i).getCurrencyId()) {
                String s = currencies.get(i).getSpinnerString();
                if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(s)) {
                    index = i;
                    i = spinner.getCount();//will stop the loop, kind of break, by making condition false
                }
            }
        }
        return index;
    }

    private static final List<Currency> currencies = new ArrayList<Currency>();

    static {
        currencies.add(new Currency(4, "EUR", "€", "Euro"));
        currencies.add(new Currency(5, "USD", "$", "US Dollar"));
        currencies.add(new Currency(2, "GBP", "£", "British Pound"));
        currencies.add(new Currency(3, "CAD", "", "Canadian Dollar"));
        currencies.add(new Currency(1, "AUD", "", "Australian Dollar"));
        currencies.add(new Currency(6, "ALL", "", "Albanian Lek"));
        currencies.add(new Currency(7, "DZD", "", "Algerian Dinar  "));
        currencies.add(new Currency(8, "ARS", "", "Argentine Peso"));
        currencies.add(new Currency(9, "AMD", "", "Armenian Dram"));
        currencies.add(new Currency(10, "AZN", "", "Azerbaijan Manat"));
        currencies.add(new Currency(11, "BSD", "", "Bahamian Dollar"));
        currencies.add(new Currency(12, "BHD", "", "Bahraini Dinar"));
        currencies.add(new Currency(13, "BDT", "", "Bangladeshi Taka"));
        currencies.add(new Currency(14, "BZD", "", "Belize Dollar"));
        currencies.add(new Currency(15, "BOB", "", "Bolivian Boliviano"));
        currencies.add(new Currency(16, "BRL", "R$", "Brazilian Real"));
        currencies.add(new Currency(17, "BND", "", "Brunei Dollar"));
        currencies.add(new Currency(18, "XOF", "", "CFA Franc BCEAO"));
        currencies.add(new Currency(19, "XAF", "", "CFA Franc BEAC"));
        currencies.add(new Currency(20, "XPF", "", "CFP Franc"));
        currencies.add(new Currency(21, "KHR", "", "Cambodian Riel"));
        currencies.add(new Currency(22, "CLP", "", "Chilean Peso"));
        currencies.add(new Currency(23, "CNY", "", "Chinese Yuan Renminbi"));
        currencies.add(new Currency(24, "COP", "", "Colombian Peso"));
        currencies.add(new Currency(25, "KMF", "", "Comoros Franc"));
        currencies.add(new Currency(26, "CRC", "", "Costa Rican Colon"));
        currencies.add(new Currency(27, "HRK", "", "Croatian Kuna"));
        currencies.add(new Currency(30, "CZK", "", "Czech Koruna"));
        currencies.add(new Currency(33, "DKK", "", "Danish Krone"));
        currencies.add(new Currency(31, "DJF", "", "Djibouti Franc"));
        currencies.add(new Currency(32, "DOP", "", "Dominican R. Peso"));
        currencies.add(new Currency(100, "XCD", "", "East Caribbean Dollar"));
        currencies.add(new Currency(34, "EGP", "", "Egyptian Pound"));
        currencies.add(new Currency(35, "EEK", "", "Estonian Kroon"));
        currencies.add(new Currency(36, "FJD", "", "Fiji Dollar"));
        currencies.add(new Currency(37, "HNL", "", "Honduran Lempira"));
        currencies.add(new Currency(38, "HKD", "", "Hong Kong Dollar"));
        currencies.add(new Currency(39, "HUF", "", "Hungarian Forint"));
        currencies.add(new Currency(40, "ISK", "", "Iceland Krona"));
        currencies.add(new Currency(41, "INR", "", "Indian Rupee"));
        currencies.add(new Currency(42, "IDR", "", "Indonesian Rupiah"));
        currencies.add(new Currency(43, "ILS", "", "Israeli New Shekel"));
        currencies.add(new Currency(44, "JPY", "", "Japanese Yen"));
        currencies.add(new Currency(45, "JOD", "", "Jordanian Dinar"));
        currencies.add(new Currency(46, "KZT", "", "Kazakhstan Tenge"));
        currencies.add(new Currency(47, "KES", "", "Kenyan Shilling"));
        currencies.add(new Currency(48, "KWD", "", "Kuwaiti Dinar"));
        currencies.add(new Currency(49, "LAK", "", "Lao Kip"));
        currencies.add(new Currency(50, "LVL", "", "Latvian Lats"));
        currencies.add(new Currency(51, "LBP", "", "Lebanese Pound"));
        currencies.add(new Currency(52, "LTL", "", "Lithuanian Litas"));
        currencies.add(new Currency(53, "MYR", "", "Malaysian Ringgit"));
        currencies.add(new Currency(101, "MRO", "", "Mauritanian Ouguiya"));
        currencies.add(new Currency(55, "MUR", "", "Mauritius Rupee"));
        currencies.add(new Currency(56, "MXN", "", "Mexican Peso"));
        currencies.add(new Currency(57, "MNT", "", "Mongolian Tugrik"));
        currencies.add(new Currency(58, "MAD", "", "Moroccan Dirham"));
        currencies.add(new Currency(59, "NAD", "", "Namibia Dollar"));
        currencies.add(new Currency(60, "NPR", "", "Nepalese Rupee"));
        currencies.add(new Currency(61, "NZD", "", "New Zealand Dollar"));
        currencies.add(new Currency(62, "NIO", "", "Nicaraguan Cordoba Oro"));
        currencies.add(new Currency(63, "NOK", "", "Norwegian Kroner"));
        currencies.add(new Currency(64, "OMR", "", "Omani Rial"));
        currencies.add(new Currency(65, "PKR", "", "Pakistan Rupee"));
        currencies.add(new Currency(66, "PGK", "", "Papua New Guinea Kina"));
        currencies.add(new Currency(67, "PEN", "", "Peruvian Nuevo Sol"));
        currencies.add(new Currency(68, "PHP", "", "Philippine Peso"));
        currencies.add(new Currency(69, "PLN", "", "Polish Zloty"));
        currencies.add(new Currency(70, "QAR", "", "Qatari Rial"));
        currencies.add(new Currency(71, "RON", "", "Romanian New Lei"));
        currencies.add(new Currency(72, "RUB", "", "Russian Rouble"));
        currencies.add(new Currency(102, "RWF", "", "Rwandan Franc"));
        currencies.add(new Currency(73, "WST", "", "Samoan Tala"));
        currencies.add(new Currency(74, "SAR", "", "Saudi Riyal"));
        currencies.add(new Currency(75, "SGD", "", "Singapore Dollar"));
        currencies.add(new Currency(77, "SOS", "", "Somali Shilling"));
        currencies.add(new Currency(78, "ZAR", "", "South African Rand"));
        currencies.add(new Currency(79, "KRW", "", "South-Korean Won"));
        currencies.add(new Currency(80, "LKR", "", "Sri Lanka Rupee"));
        currencies.add(new Currency(81, "SZL", "", "Swaziland Lilangeni"));
        currencies.add(new Currency(82, "SEK", "", "Swedish Krona"));
        currencies.add(new Currency(83, "CHF", "", "Swiss Franc"));
        currencies.add(new Currency(84, "TWD", "", "Taiwan Dollar"));
        currencies.add(new Currency(85, "TZS", "", "Tanzanian Shilling"));
        currencies.add(new Currency(86, "THB", "", "Thai Baht"));
        currencies.add(new Currency(87, "TOP", "", "Tonga Pa'anga"));
        currencies.add(new Currency(88, "TTD", "", "Trinidad/Tobago Dollar"));
        currencies.add(new Currency(89, "TND", "", "Tunisian Dinar"));
        currencies.add(new Currency(90, "TRY", "", "Turkish Lira"));
        currencies.add(new Currency(91, "AED", "", "UAE Dirham"));
        currencies.add(new Currency(92, "UGX", "", "Uganda Shilling"));
        currencies.add(new Currency(93, "UAH", "", "Ukraine Hryvnia"));
        currencies.add(new Currency(94, "UYU", "", "Uruguayan Peso"));
        currencies.add(new Currency(95, "VUV", "", "Vanuatu Vatu"));
        currencies.add(new Currency(97, "VND", "", "Vietnamese Dong"));
    }

    public static List<Currency> getCurrencies() {
        return currencies;
    }

    @Nullable
    public static Currency getByCode(String code) {
        for (Currency currency : currencies) {
            if (currency.getCode().equalsIgnoreCase(code)) {
                return currency;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Currency currency = (Currency) o;

        if (currencyId != null ? !currencyId.equals(currency.currencyId) : currency.currencyId != null)
            return false;
        if (code != null ? !code.equals(currency.code) : currency.code != null) return false;
        if (symbol != null ? !symbol.equals(currency.symbol) : currency.symbol != null)
            return false;
        return !(descriptionEn != null ? !descriptionEn.equals(currency.descriptionEn) : currency.descriptionEn != null);
    }

    @Override
    public int hashCode() {
        int result = currencyId != null ? currencyId.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (symbol != null ? symbol.hashCode() : 0);
        result = 31 * result + (descriptionEn != null ? descriptionEn.hashCode() : 0);
        return result;
    }

    public static void update(Context context, List<Currency> items) {
        Assert.assertNotNull(context);
        Assert.assertNotNull(items);

        List<Currency> currencies = Currency.getCurrencies();

        for (Currency currency : items) {
            if (!currencies.contains(currency)) {
                currencies.add(currency);
            }
        }
    }

    @Override
    public String getName() {
        return code;
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public void actionOnSetChecked() {
        EventBus.getDefault().post(new CurrencyChangedEvent(this));
    }

    @Override
    public String getDescription() {
        return descriptionEn;
    }
}
