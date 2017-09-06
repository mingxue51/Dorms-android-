package entity.Generic;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import org.json.simple.JSONObject;

import java.sql.SQLException;

import db.DatabaseHelperOrm;
import entity.User.User;
import entity.User.UserMapper;
import entity.User.UserProfile;
import entity.User.UserProfileMapper;
import helper.Root;
import helper.Session;

public class BaseMapper extends Root {
	
	private final String LOG_TAG = BaseMapper.class.getName();
	
	protected DatabaseHelperOrm databaseHelper = null;
	
	protected Dao<User, Integer> user = null;
    protected Dao<UserProfile, Integer> userProfile = null;
    protected Dao<Language, Integer> language = null;
    protected Dao<Gender, Integer> gender = null;
    protected Dao<Country, Integer> country = null;

	public BaseMapper(Context context) {
		super(context);
		try {
			this.getHelper();
			this.user = this.databaseHelper.getUserDao();
			this.userProfile = this.databaseHelper.getUserProfileDao();
            this.gender = this.databaseHelper.getGenderDao();
            this.country = this.databaseHelper.getCountryDao();
		} catch (SQLException e) {
			Log.e(LOG_TAG, "Database exception", e);
		}
	}

	private DatabaseHelperOrm getHelper() {
		if (databaseHelper == null) {
			databaseHelper = new DatabaseHelperOrm(super.getContext());
		}
		return databaseHelper;
	}

	public void saveToSession(String name, JSONObject json) {
		Session.setStringValue(getContext(), name, json.toJSONString());
	}

    public void clearSession() {
        Session.clear(getContext());
    }

    public void signout(){
        UserMapper mapper = new UserMapper(getContext());
        mapper.deleteAllEntities();
        mapper.clearSession();

        UserProfileMapper profileMapper = new UserProfileMapper(getContext());
        profileMapper.deleteAllEntities();
    }
	
}
