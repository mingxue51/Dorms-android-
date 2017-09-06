package db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import helper.H;
import helper.Root;

public abstract class BaseModel extends Root {
	private static DataBaseHelper mDH;
	private static Object mSynchObj = new Object();
	private static int mSynchCounter = 0;
	
	public static final String FIELD_ROWID = "_id";
	private String mTableName;
	
	public BaseModel(Context context, String tableName) {
		super(context);
		mTableName = tableName;
		mDH = DataBaseHelper.Instance(context);
	}
	
	public String getTableName() { return mTableName; }
	
	protected void lock() {
		try {
			 synchronized (mSynchObj) {
				 while (mSynchCounter > 0) {
					 mSynchObj.wait();
				 }
				 mSynchCounter++;
			 }
		} catch (InterruptedException  e) {
			H.logE("DB Lock", e.getMessage());
		}
	}

	protected void unlock() {
		try {
		    synchronized (mSynchObj)  {
		    	if (--mSynchCounter <= 0) {
		    		mSynchObj.notifyAll();
		    	}
		    }
		} catch (Exception  e) {
			H.logE("DB Unlock", e.getMessage());
		}
	}
	
	protected SQLiteDatabase openDataBase() {
		return mDH.openDataBase(); 
	}
	
	protected InsertHelper getIH() {
		return new InsertHelper(openDataBase(), mTableName); 
	}
	
	protected long insert(ContentValues initialValues)
	{
		long result = 0;
		SQLiteDatabase db = openDataBase();
		if (db != null){
			result = db.insert(getTableName(), null, initialValues);
		}
		
		return result;
	}
	
	protected long update(ContentValues initialValues, String whereClause, String[] whereArgs)
	{
		long result = 0;
		SQLiteDatabase db = openDataBase();
		if (db != null){
			result = db.update(getTableName(), initialValues, whereClause, whereArgs);
		}
		return result;		
	}
	
	protected boolean delete(String whereClause, String[] args)
	{
		boolean result = false;
		SQLiteDatabase db = openDataBase();
		if (db != null){
			result = db.delete(getTableName(), whereClause, args) > 0; 
		}
		return result; 
	}
	
	public boolean deleteAll()
	{
		boolean result = false;
		SQLiteDatabase db = openDataBase();
		if (db != null){
			result = db.delete(getTableName(), null, null) > 0; 
		}
		return result; 
	}
	
	protected Cursor query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
	{
		Cursor result = null;
		SQLiteDatabase db = openDataBase();
		if (db != null){
			result = db.query(getTableName(), columns, selection, selectionArgs, groupBy, having, orderBy);
		}
        if (result != null) {
        	result.moveToFirst();
        }
		return result; 		
	}
	
	protected Cursor query(String sql, String[] args) {
		Cursor result = null;
		SQLiteDatabase db = openDataBase();
		if (db != null){
			result = db.rawQuery(sql, args);
		}
		
        if (result != null) {
        	result.moveToFirst();
        }
		return result; 		
	}
	
	/*public void close() {
		//mDH.close();
	}*/
	
	public long count() {
		return count("");
	}
	
	protected long count(String where){
		long result = 0;
		SQLiteDatabase db = openDataBase();
		if (db != null){
			String sql = "select count(*) from " + getTableName();
			sql += where.trim().equalsIgnoreCase("") ?" where " + where:""; 
			SQLiteStatement lite = db.compileStatement(sql);
			result = lite.simpleQueryForLong();
		}
		return result;
	}

	protected int getIntField(String field){
		int value = 0;
		Cursor cursor = query(new String[] {FIELD_ROWID, field}, null, null, null, null, "_id ASC");
		if (cursor.getCount() > 0){
			cursor.moveToFirst();
			value = cursor.getInt(cursor.getColumnIndex(field));
		}
		cursor.close();
		return value;
	}

	protected boolean setIntField(String field, int value) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(field, value);
        return setValuesField(field, initialValues);
	}
	
	protected String getStringField(String field){
		String value = "";
		Cursor cursor = query(new String[] {FIELD_ROWID, field}, null, null, null, null, "_id ASC");
		if (cursor.getCount() > 0){
			cursor.moveToFirst();
			value = cursor.getString(cursor.getColumnIndex(field));
		}
		cursor.close();
		return value;
	}

	protected boolean setStringField(String field, String value){
        ContentValues initialValues = new ContentValues();
        initialValues.put(field, value);
        return setValuesField(field, initialValues);
	}

	protected double getDoubleField(String field, double defaultValue){
		double value = defaultValue;
		Cursor cursor = query(new String[] {FIELD_ROWID, field}, null, null, null, null, "_id ASC");
		if (cursor.getCount() > 0){
			cursor.moveToFirst();
			value = cursor.getDouble(cursor.getColumnIndex(field));
		}
		cursor.close();
		return value;
	}
	
	protected boolean setDoubleField(String field, double value){
        ContentValues initialValues = new ContentValues();
        initialValues.put(field, value);
		return setValuesField(field, initialValues);
	}
	
	protected boolean setValuesField(String field, ContentValues values){
		boolean isField = false;
		Cursor cursor = query(new String[] {FIELD_ROWID, field}, null, null, null, null, FIELD_ROWID + " ASC");
		if (cursor.getCount() <= 0){
	        if (insert(values) < 0){
	        	H.logE("Can't add field "+field+" to settings table.");
	        } else {
	        	isField = true;
	        }
		} else {
			cursor.moveToFirst();
			int rowId = cursor.getInt(cursor.getColumnIndex(FIELD_ROWID));
			if (update(values, FIELD_ROWID + " = " + rowId, null) <= 0){
				H.logE("Can't update field "+field+" to settings table.");
			} else {
				isField = true;
	        }
		}
		cursor.close();
		return isField;
	}
}
