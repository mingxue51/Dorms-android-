package helper;

import android.content.Context;

public class Root {
private Context mContext;
	
	public Root(Context context) {
		if (context == null) {
			throw new NullPointerException("Context can't be NULL when you create class extended from Root class.");
		}
		mContext = context;
	}
	
	public Context getContext() { return mContext; }
	public void setContext(Context context) { mContext = context; }
}
