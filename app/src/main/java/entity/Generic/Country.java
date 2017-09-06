package entity.Generic;

import android.widget.Spinner;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import junit.framework.Assert;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

import helper.H;

@DatabaseTable(tableName = "country")
public class Country  {
    public Country() {

    }
    @DatabaseField(generatedId=true)
    private int id;

    @DatabaseField
    private String value;

    @DatabaseField
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setJSON(JSONObject obj) {
        setText(H.toString(obj, "text"));
        setValue(H.toString(obj, "value"));
    }

    public Country(String value) {
        this.value = value;
    }

    public static ArrayList<Country> factoryList(Object json) {
        ArrayList<Country> result = new ArrayList<Country>();
        try {
            JSONArray array = (JSONArray) json;
            for (Object key : array) {
                JSONObject obj = (JSONObject) key;
                Country entity = new Country();
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
        return this.value;
    }

    @Override
    public String toString() {
        return  this.getSpinnerString();
    }

    // todo refactorings
    public static int getSelectionIndexById(Spinner spinner, String value) {
        if (value == null) {
            return 0;
        }

        int index = 0;
        for (int i=0;i< countries.size();i++){
            if(value.equalsIgnoreCase(countries.get(i).getValue())) {
                String s = countries.get(i).getSpinnerString();
                if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(s)) {
                    index = i;
                    i = spinner.getCount();//will stop the loop, kind of break, by making condition false
                }
            }
        }
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Country country = (Country) o;

        return value.equals(country.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    private static final List<Country> countries = new ArrayList<Country>();

    static {
        countries.add(new Country("Albania"));
        countries.add(new Country("Andorra"));
        countries.add(new Country("Argentina"));
        countries.add(new Country("Armenia"));
        countries.add(new Country("Australia"));
        countries.add(new Country("Austria"));
        countries.add(new Country("Azerbaijan"));
        countries.add(new Country("Bahamas"));
        countries.add(new Country("Bahrain"));
        countries.add(new Country("Bangladesh"));
        countries.add(new Country("Barbados"));
        countries.add(new Country("Belarus"));
        countries.add(new Country("Belgium"));
        countries.add(new Country("Belize"));
        countries.add(new Country("Benin"));
        countries.add(new Country("Bolivia"));
        countries.add(new Country("Bosnia-Herzegovina"));
        countries.add(new Country("Brazil"));
        countries.add(new Country("Bulgaria"));
        countries.add(new Country("Burkina Faso"));
        countries.add(new Country("Cambodia"));
        countries.add(new Country("Canada"));
        countries.add(new Country("Cape Verde"));
        countries.add(new Country("Chile"));
        countries.add(new Country("China"));
        countries.add(new Country("Colombia"));
        countries.add(new Country("Cook Islands"));
        countries.add(new Country("Costa Rica"));
        countries.add(new Country("Croatia"));
        countries.add(new Country("Cyprus"));
        countries.add(new Country("Czech Republic"));
        countries.add(new Country("Denmark"));
        countries.add(new Country("Dominica"));
        countries.add(new Country("Dominican Republic"));
        countries.add(new Country("Ecuador"));
        countries.add(new Country("Egypt"));
        countries.add(new Country("El Salvador"));
        countries.add(new Country("England"));
        countries.add(new Country("Estonia"));
        countries.add(new Country("Ethiopia"));
        countries.add(new Country("Fiji"));
        countries.add(new Country("Finland"));
        countries.add(new Country("France"));
        countries.add(new Country("French Polynesia"));
        countries.add(new Country("Gambia"));
        countries.add(new Country("Georgia"));
        countries.add(new Country("Germany"));
        countries.add(new Country("Ghana"));
        countries.add(new Country("Greece"));
        countries.add(new Country("Guadeloupe"));
        countries.add(new Country("Guatemala"));
        countries.add(new Country("Haiti"));
        countries.add(new Country("Honduras"));
        countries.add(new Country("Hong Kong"));
        countries.add(new Country("Hungary"));
        countries.add(new Country("Iceland"));
        countries.add(new Country("India"));
        countries.add(new Country("Indonesia"));
        countries.add(new Country("Ireland"));
        countries.add(new Country("Israel"));
        countries.add(new Country("Italy"));
        countries.add(new Country("Jamaica"));
        countries.add(new Country("Japan"));
        countries.add(new Country("Jordan"));
        countries.add(new Country("Kazakhstan"));
        countries.add(new Country("Kenya"));
        countries.add(new Country("Kosovo"));
        countries.add(new Country("Kyrgyzstan"));
        countries.add(new Country("Laos"));
        countries.add(new Country("Latvia"));
        countries.add(new Country("Lebanon"));
        countries.add(new Country("Liechtenstein"));
        countries.add(new Country("Lithuania"));
        countries.add(new Country("Macedonia"));
        countries.add(new Country("Madagascar"));
        countries.add(new Country("Malawi"));
        countries.add(new Country("Malaysia"));
        countries.add(new Country("Maldives"));
        countries.add(new Country("Mali"));
        countries.add(new Country("Malta"));
        countries.add(new Country("Martinique"));
        countries.add(new Country("Mauritius"));
        countries.add(new Country("Mexico"));
        countries.add(new Country("Moldova"));
        countries.add(new Country("Monaco"));
        countries.add(new Country("Mongolia"));
        countries.add(new Country("Montenegro"));
        countries.add(new Country("Morocco"));
        countries.add(new Country("Mozambique"));
        countries.add(new Country("Namibia"));
        countries.add(new Country("Nepal"));
        countries.add(new Country("Netherlands"));
        countries.add(new Country("Netherlands Antilles"));
        countries.add(new Country("New Zealand"));
        countries.add(new Country("Nicaragua"));
        countries.add(new Country("Nigeria"));
        countries.add(new Country("Northern Ireland"));
        countries.add(new Country("Norway"));
        countries.add(new Country("Oman"));
        countries.add(new Country("Panama"));
        countries.add(new Country("Paraguay"));
        countries.add(new Country("Peru"));
        countries.add(new Country("Philippines"));
        countries.add(new Country("Poland"));
        countries.add(new Country("Portugal"));
        countries.add(new Country("Puerto Rico"));
        countries.add(new Country("Qatar"));
        countries.add(new Country("Romania"));
        countries.add(new Country("Russia"));
        countries.add(new Country("Rwanda"));
        countries.add(new Country("Réunion"));
        countries.add(new Country("Saint Kitts & Nevis"));
        countries.add(new Country("Saint Martin"));
        countries.add(new Country("Samoa"));
        countries.add(new Country("San Marino"));
        countries.add(new Country("Saudi Arabia"));
        countries.add(new Country("Scotland"));
        countries.add(new Country("Senegal"));
        countries.add(new Country("Serbia"));
        countries.add(new Country("Sierra Leone"));
        countries.add(new Country("Singapore"));
        countries.add(new Country("Slovakia"));
        countries.add(new Country("Slovenia"));
        countries.add(new Country("Solomon Islands"));
        countries.add(new Country("South Africa"));
        countries.add(new Country("South Korea"));
        countries.add(new Country("Spain"));
        countries.add(new Country("Sri Lanka"));
        countries.add(new Country("St Lucia"));
        countries.add(new Country("St Vincent & The Grenadines"));
        countries.add(new Country("Swaziland"));
        countries.add(new Country("Sweden"));
        countries.add(new Country("Switzerland"));
        countries.add(new Country("Taiwan"));
        countries.add(new Country("Tanzania"));
        countries.add(new Country("Thailand"));
        countries.add(new Country("Tonga"));
        countries.add(new Country("Trinidad & Tobago"));
        countries.add(new Country("Tunisia"));
        countries.add(new Country("Turkey"));
        countries.add(new Country("USA"));
        countries.add(new Country("Uganda"));
        countries.add(new Country("Ukraine"));
        countries.add(new Country("United Arab Emirates"));
        countries.add(new Country("Uruguay"));
        countries.add(new Country("Vanuatu"));
        countries.add(new Country("Venezuela"));
        countries.add(new Country("Vietnam"));
        countries.add(new Country("Wales"));
        countries.add(new Country("Zambia"));
        countries.add(new Country("Zimbabwe"));
    }

    public static List<Country> getCountries() {
        return countries;
    }

    public static void update(List<Country> items) {
        Assert.assertNotNull(items);

        List<Country> countries = Country.getCountries();

        for (Country country : items) {
            if (!countries.contains(country)) {
                countries.add(country);
            }
        }
    }

    public static List<String> getCountriesNames() {
        List<String> names = new ArrayList<>(countries.size());
        for (Country country: countries) {
            names.add(country.getValue());
        }
        return names;
    }
}
