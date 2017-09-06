package entity.Generic;


import android.content.Context;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import entity.User.User;
import helper.H;
import helper.Root;
import helper.Session;


public class BaseEntity extends Root {

	private long mId;

	public BaseEntity(Context context) {
		super(context);
		mId = -1;	// ID in local database.
	}

	public BaseEntity(Context context, JSONObject obj) {
		this(context);
		setJSON(obj);
	}
	public long getId() { return mId; }
	public void setId(long id) { this.mId = id; }
		
	public void saveToSession(String name) {
		JSONObject json = (JSONObject)this.getJSON();
		Session.setStringValue(getContext(), name, json.toJSONString());
	}
	
	public void readFromSession(String name) {
		String info = Session.getStringValue(getContext(), name);
		if (info != null && info.length() > 0) {
			try {
				setJSON((JSONObject) JSONValue.parse(info));
			} catch(Exception e) {
				H.logE("Can't init BaseEntity instance when the app reads user info from session.");
				H.logE(e.getMessage());
			}
		}
	}
	
	/*
	 * Get current logged user.
	 */
	public static User getUser(Context context) {
		User user = new User();
		//user.readFromSession(User.SESSION_NAME);
		return user;
	}
	
	public JSONObject getJSON() { return null; }
	public void setJSON(JSONObject obj) { }

}
