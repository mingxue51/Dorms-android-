package api;

import android.widget.Toast;

import org.json.simple.JSONObject;

import java.util.HashMap;

import entity.User.User;
import entity.User.UserProfile;
import entity.User.UserProfileMapper;
import helper.IGetRequest;

public class GetUserProfile extends BaseTask {
	private User mUser=null;

	public GetUserProfile(API api,IGetRequest callback) {
		super(callback);
        mUser = User.getUserFromSession(getContext());
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("member_number", mUser.getMember_id());
        params.put("session_id", mUser.getSession_id());
        createParams("member_profile?api_key="+API.API_KEY, params);
	}

	@Override
	protected void onResponse(Object response) {
		JSONObject json = (JSONObject)response;
		if (json.containsKey("profile")) {
            JSONObject responseObject = (JSONObject)json.get("profile");
            if(responseObject!=null){
                UserProfileMapper mapper = new UserProfileMapper(getContext());
                mapper.deleteAllEntities();

                UserProfile userProfile = new UserProfile();
                userProfile.setJSON(responseObject);
                userProfile.setEmail(mUser.getFirstName());
                mapper.saveEntity(userProfile);

                onSuccess(null);
                //onSuccess(parseMessage(responseObject));
            }else{
                Toast.makeText(getContext(), "Please, Try again.", Toast.LENGTH_LONG).show();
            }
		}
	}
}
