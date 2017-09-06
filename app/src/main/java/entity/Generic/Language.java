package entity.Generic;

import android.content.Context;
import android.content.res.Configuration;
import android.widget.Spinner;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import junit.framework.Assert;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import helper.H;
import helper.Session;

@DatabaseTable(tableName = "language")
public class Language {
    public static final String DEFAULT_LANGUAGE = "en";
    public Language() {
    }

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private Integer language_id;

    @DatabaseField
    private String code_lang;

    @DatabaseField
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(Integer language_id) {
        this.language_id = language_id;
    }

    public String getCode_lang() {
        return code_lang;
    }

    public void setCode_lang(String code_lang) {
        this.code_lang = code_lang;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Language(Integer language_id, String code_lang, String description) {
        this.language_id = language_id;
        this.code_lang = code_lang;
        this.description = description;
    }

    public void setJSON(JSONObject obj) {
        setLanguage_id(H.toInt(obj, "language_id"));
        setCode_lang(H.toString(obj, "code_lang"));
        setDescription(H.toString(obj, "description"));
    }

    public static ArrayList<Language> factoryList(Object json) {
        ArrayList<Language> result = new ArrayList<Language>();
        try {
            JSONArray array = (JSONArray) json;
            for (Object key : array) {
                JSONObject obj = (JSONObject) key;
                Language entity = new Language();
                entity.setJSON(obj);
                result.add(entity);
            }
        } catch (Exception e) {
            H.logE("Can't create Currency list from JSON Array.");
            H.logE(e.getMessage());
        }
        return result;
    }

    public String getSpinnerString() {
        return this.code_lang + "-" + this.description;
    }

    @Override
    public String toString() {
        return this.getSpinnerString();
    }

    public static int getSelectionIndexById(Spinner spinner, int id) {
        int index = 0;

        List<Language> languages = Language.getLanguages();

        for (int i = 0; i < languages.size(); i++) {
            if (id == languages.get(i).getLanguage_id()) {
                String s = languages.get(i).getSpinnerString();
                if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(s)) {
                    index = i;
                    i = spinner.getCount();//will stop the loop, kind of break, by making condition false
                }
            }
        }
        return index;
    }

    public static String getLanguage(Context pContext){
        String language = Session.getStringValue(pContext, Session.SELECTED_LANGUAGE);
        if(language != null && !language.isEmpty()) {
            return language;
        }
        return  Language.getCurrentDeviceLanguage().getCode_lang();
    }

    public static void setLanguage(Context pContext, Language pLanguage){
         Session.setStringValue(pContext, Session.SELECTED_LANGUAGE, pLanguage.getCode_lang());
    }

    public static Configuration switchApplicationLanguage(String language, Context pContext){
        String languageToLoad  = language;

        String s=null;
        if(languageToLoad.equalsIgnoreCase("en")){
            s="US";
        }else if(languageToLoad.equalsIgnoreCase("de")){
            s="DE";
        }else{
            languageToLoad = "en";
            s="US";
        }
        Locale locale = new Locale(languageToLoad,s);

        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        pContext.getResources().updateConfiguration(config, pContext.getResources().getDisplayMetrics());
        return config;
    }

    public static Language getCurrentDeviceLanguage(){
        Language language = new Language();
        language.setCode_lang(Locale.getDefault().getLanguage());
        language.setDescription(Locale.getDefault().getDisplayLanguage());
        String l = Locale.getDefault().getLanguage();
        String d = Locale.getDefault().getISO3Language();
        String c = Locale.getDefault().getDisplayLanguage();
        return language;
    }

    private static final List<Language> languages = new ArrayList<Language>();

    static {
        languages.add(new Language(1, "en", "english"));
        languages.add(new Language(2, "fr", "français"));
        languages.add(new Language(3, "es", "español"));
        languages.add(new Language(4, "de", "deutsch"));
        languages.add(new Language(5, "pt", "português"));
        languages.add(new Language(6, "zh-CN", "chinese"));
        languages.add(new Language(7, "it", "italiano"));
        languages.add(new Language(8, "pl", "polish"));
        languages.add(new Language(9, "ru", "русский"));
        languages.add(new Language(10, "no", ""));
        languages.add(new Language(11, "fi", "suomalainen"));
        languages.add(new Language(12, "cs", "česky"));
        languages.add(new Language(13, "ko", "한국의"));
        languages.add(new Language(14, "ja", "日本"));
        languages.add(new Language(15, "hu", "magyar"));
    }

    public static List<Language> getLanguages() {
        return languages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Language language = (Language) o;

        if (language_id != null ? !language_id.equals(language.language_id) : language.language_id != null)
            return false;
        if (code_lang != null ? !code_lang.equals(language.code_lang) : language.code_lang != null)
            return false;
        return !(description != null ? !description.equals(language.description) : language.description != null);
    }

    @Override
    public int hashCode() {
        int result = language_id != null ? language_id.hashCode() : 0;
        result = 31 * result + (code_lang != null ? code_lang.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    public static void update(List<Language> items) {
        Assert.assertNotNull(items);

        List<Language> languages = Language.getLanguages();

        for (Language language : languages) {
            if (!languages.contains(language)) {
                languages.add(language);
            }
        }
    }
}
