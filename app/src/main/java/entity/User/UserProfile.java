package entity.User;

import android.content.Context;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.simple.JSONObject;

import java.io.Serializable;

import helper.App;
import helper.H;

@DatabaseTable(tableName = "user_profile")
public class UserProfile implements Serializable {
    private static final long serialVersionUID = 1L;

	private Context mContext;
	public static final String SESSION_NAME = "user_profile";
	public final static String ID_FIELD_NAME = "user_id";
	public final static String URL_FIELD_NAME = "url";

	@DatabaseField(id = true)
	private int id;

    @DatabaseField
    private String first_name;

	@DatabaseField
	private String last_name;

	@DatabaseField
	private String phone_number;

    @DatabaseField
    private boolean mail_subscription;

    @DatabaseField
    private String home_country;

    @DatabaseField
    private int favorite_currency;

    @DatabaseField
    private String website = "www.domain.com";

    @DatabaseField
    private int gender_id;

    @DatabaseField
    private int favorite_lang_id;

    @DatabaseField
    private String email;

    @DatabaseField
    private String image;

    //these fields are not for db
    private String password;


	public UserProfile() {
        first_name="";
        last_name="";
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public boolean getMail_subscription() {
        return mail_subscription;
    }

    public void setMail_subscription(boolean mail_subscription) {
        this.mail_subscription = mail_subscription;
    }

    public String getHome_country() {
        return home_country;
    }

    public void setHome_country(String home_country) {
        this.home_country = home_country;
    }

    public int getFavorite_currency() {
        return favorite_currency;
    }

    public void setFavorite_currency(int favorite_currency) {
        this.favorite_currency = favorite_currency;
    }

    public String getWebsite() {
        return website = "www.domain.com" ;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getGender_id() {
        return gender_id;
    }

    public void setGender_id(int gender_id) {
        this.gender_id = gender_id;
    }

    public int getFavorite_lang_id() {
        return favorite_lang_id;
    }

    public void setFavorite_lang_id(int favorite_lang_id) {
        this.favorite_lang_id = favorite_lang_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
	public void setContext(Context context) { mContext = context; }
	public UserProfile setJSON(JSONObject obj) {
		setId(H.toInt(obj, "id"));
        setFirst_name(H.toString(obj, "first_name"));
        setLast_name(H.toString(obj, "last_name"));
		setPhone_number(H.toString(obj, "phone_number"));
        setMail_subscription(H.toBoolean(obj, "mail_subscription"));
        setFavorite_currency(H.toInt(obj, "favorite_currency"));
        setHome_country(H.toString(obj, "home_country"));
        setWebsite(H.toString(obj, "website"));
        setGender_id(H.toInt(obj, "gender_id"));
        setFavorite_lang_id(H.toInt(obj, "favorite_lang_id"));
        return this;
	}

	//@SuppressWarnings("unchecked")
	public JSONObject getJSON() {
		JSONObject json = new JSONObject();
		json.put("id", getId());
		json.put("first_name", getFirst_name());
        json.put("last_name", getFirst_name());
		json.put("phone_number", getPhone_number());
        json.put("mail_subscription", getMail_subscription());
		json.put("favorite_currency", getFavorite_currency());
        json.put("website", getWebsite());
        json.put("gender_id", getGender_id());
        json.put("favorite_lang_id", getFavorite_lang_id());
		return json;
	}
	
    public static UserProfile getUserProfile() {
        UserProfileMapper mapper = new UserProfileMapper(App.getInstance());
        return mapper.getUserProfile();
    }

    public String getFullName(){
        return first_name+" "+last_name;
    }
}
