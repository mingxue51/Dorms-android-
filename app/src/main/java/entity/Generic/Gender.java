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

@DatabaseTable(tableName = "gender")
public class Gender {
    public Gender() {
    }

    @DatabaseField(generatedId=true)
    private int id;

    @DatabaseField
    private Integer gender_id;

    @DatabaseField
    private String gender;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getGender_id() {
        return gender_id;
    }

    public void setGender_id(Integer gender_id) {
        this.gender_id = gender_id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setJSON(JSONObject obj) {
        setGender_id(H.toInt(obj, "gender_id"));
        setGender(H.toString(obj, "gender"));
    }

    public Gender(Integer gender_id, String gender) {
        this.gender_id = gender_id;
        this.gender = gender;
    }

    public static ArrayList<Gender> factoryList(Object json) {
        ArrayList<Gender> result = new ArrayList<Gender>();
        try {
            JSONArray array = (JSONArray) json;
            for (Object key : array) {
                JSONObject obj = (JSONObject) key;
                Gender entity = new Gender();
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
        return this.gender;
    }

    @Override
    public String toString() {
        return  this.getSpinnerString();
    }


    private final static List<Gender> genders = new ArrayList<Gender>();

    static {
        genders.add(new Gender(1, "Male"));
        genders.add(new Gender(2, "Female"));
    }

    public static List<Gender> getGenders() {
        return genders;
    }

    public static void update(List<Gender> items) {
        // todo handle it
        Assert.assertNotNull(items);
    }
}
