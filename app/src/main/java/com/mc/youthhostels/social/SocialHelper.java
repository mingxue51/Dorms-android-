package com.mc.youthhostels.social;

import org.json.JSONException;
import org.json.JSONObject;

import entity.User.UserProfile;
import entity.User.UserProfileMapper;
import helper.App;

public class SocialHelper {
    public static void saveMemberInfoFromFacebook(JSONObject jsonObject) throws JSONException {
        final String email = jsonObject.getString("email");
        final String avatarUrl = jsonObject.getJSONObject("picture").getJSONObject("data").getString("url");

        final String lastName = jsonObject.getString("last_name");
        final String firstName = jsonObject.getString("first_name");
        final String gender = jsonObject.getString("gender");

        UserProfileMapper mapper = new UserProfileMapper(App.getInstance().getApplicationContext());

        mapper.deleteAllEntities();

        UserProfile userProfile = new UserProfile();

        userProfile.setEmail(email);
        userProfile.setImage(avatarUrl);
        userProfile.setFirst_name(firstName);
        userProfile.setLast_name(lastName);

        int genderId = gender.equals("male") ? 0 : 1;
        userProfile.setGender_id(genderId);
        mapper.saveEntity(userProfile);
    }
}
