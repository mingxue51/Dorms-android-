package com.mc.youthhostels.helper;

import com.google.gson.Gson;

import helper.App;
import helper.H;
import helper.Session;

public class JsonHelper {
    public static String serializeToJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    // Deserialize to single object.
    public static Object deserializeFromJson(String jsonString, Class klass) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, klass);
    }

    public static void save(Object object) {
        String json = serializeToJson(object);
        Session.setStringValue(App.getInstance(), object.getClass().getSimpleName(), json);
    }

    public static Object restore(Class klass) {
        String json = Session.getStringValue(App.getInstance(), klass.getSimpleName());
        return deserializeFromJson(json, klass);
    }
}
