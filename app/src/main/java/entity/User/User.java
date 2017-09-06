package entity.User;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.mc.youthhostels.entity.Booking;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.List;

import helper.App;
import helper.H;
import helper.Session;

@DatabaseTable(tableName = "users")
public class User {
	private static final String MEMBER_BOOKINGS_SESSION_KEY = "MEMBER_BOOKINGS";

	private Context mContext;
	public static final String SESSION_NAME = "user_info";
	public final static String ID_FIELD_NAME = "user_id";
	public final static String URL_FIELD_NAME = "url";

	@DatabaseField(id = true, columnName = ID_FIELD_NAME)
	private int user_id;

	@DatabaseField
	private String member_id;

    @DatabaseField
    private String session_id;

//these fields are not for db. They are part of profile
    private String password;
    private String firstName;


	public User() {
	}

	public int getUser_id() {
		return this.user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public static User getUserFromSession(Context context){
        User user = new User();
        user.setContext(context);
        user.readFromSession();
        return user;
    }

    public static boolean isLogged(Context context) {
		User user = new User();
		user.setContext(context);
		user.readFromSession();
		if(user.getMember_id() != null){
			if( user.getMember_id().length() > 0 ) return true;
		}
		return false;
	}

	public static boolean isLogged() {
		return isLogged(App.getInstance());
	}

	public void setContext(Context context) { mContext = context; }
	public void setJSON(JSONObject obj) {
		setUser_id(H.toInt(obj, "id"));
        setMember_id(H.toString(obj, "member_id"));
        setSession_id(H.toString(obj, "session_id"));
        setPassword(H.toString(obj, "password"));
        setFirstName(H.toString(obj, "firstName"));
	}

	//@SuppressWarnings("unchecked")
	public JSONObject getJSON() {
		JSONObject json = new JSONObject();
		json.put("id", getUser_id());
		json.put("member_id", getMember_id());
        json.put("session_id", getSession_id());
        json.put("password",getPassword());
        json.put("firstName",getFirstName());
		return json;
	}
	
	public void readFromSession() {
		String info = Session.getStringValue(mContext, this.SESSION_NAME);
		if (info != null && info.length() > 0) {
			try {
				setJSON((JSONObject) JSONValue.parse(info));
			} catch(Exception e) {
				H.logE("Can't init BaseEntity instance when the app reads user info from session.");
				H.logE(e.getMessage());
			}
		}
	}

	public void saveBookings(String jsonString) {
		Session.setStringValue(App.getInstance(), MEMBER_BOOKINGS_SESSION_KEY, jsonString);
		H.logD("member bookings for offline mode saved");
	}

	@Nullable
	public List<Booking> getSavedBookings() {
		String bookingsJson = Session.getStringValue(App.getInstance(), MEMBER_BOOKINGS_SESSION_KEY);
		if (TextUtils.isEmpty(bookingsJson)) {
			return null;
		}
		return Booking.factoryList(JSONValue.parse(bookingsJson));
	}
}
