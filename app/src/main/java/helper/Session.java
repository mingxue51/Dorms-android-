package helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Session {
	private static final String SESSION_KEY = "project.session";
	
	public static final String SELECTED_LANGUAGE = "selected_language";
	public static final String SELECTED_CURRENCY_SYMBOL = "selected_currency_symbol";
	public static final String SELECTED_CURRENCY_CODE = "selected_currency_code";
	public static final String SELECTED_DISTANCE_UNIT = "selected_distance_unit";

	public static void setLongValue(Context context, String prefix, long value) {
		Editor editor = context.getSharedPreferences(SESSION_KEY, Context.MODE_PRIVATE).edit();
        editor.putLong(prefix + "_long_value", value);
        editor.commit();
	}
	public static long getLongValue(Context context, String prefix){
		return getLongValue(context, prefix, 0L);
	}
	public static long getLongValue(Context context, String prefix, long options){
    	SharedPreferences prefs = context.getSharedPreferences(SESSION_KEY, 0);
		return prefs.getLong(prefix + "_long_value", options);
	}

	public static void setStringValue(Context context, String prefix, String value) {
		Editor editor = context.getSharedPreferences(SESSION_KEY, Context.MODE_PRIVATE).edit();
        editor.putString(prefix + "_string_value", value);
        editor.commit();
	}
	public static String getStringValue(Context context, String prefix){
		return getStringValue(context, prefix, "");
	}
	public static String getStringValue(Context context, String prefix, String options){
    	SharedPreferences prefs = context.getSharedPreferences(SESSION_KEY, 0);
		return prefs.getString(prefix + "_string_value", options);
	}

	public static void setBooleanValue(Context context, String prefix, Boolean value) {
		Editor editor = context.getSharedPreferences(SESSION_KEY, Context.MODE_PRIVATE).edit();
        editor.putBoolean(prefix + "_boolean_value", value);
        editor.commit();
	}
	public static boolean getBooleanValue(Context context, String prefix){
		return getBooleanValue(context, prefix, false);
	}
	public static boolean getBooleanValue(Context context, String prefix, boolean options){
    	SharedPreferences prefs = context.getSharedPreferences(SESSION_KEY, 0);
		return prefs.getBoolean(prefix + "_boolean_value", options);
	}

	public static void clear(Context context) {
        Editor editor = context.getSharedPreferences(SESSION_KEY, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }

}
