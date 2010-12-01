package com.ninja.DatabaseHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper
{
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "SPEAKDB"; 
	private static final String TAG = "DatabaseHelper";
	
	public static final String MAP_ID = "_id";
	public static final String MAP_ON_OFF = "readonoff";
	public static final String MAP_READ_MESSAGE = "readmessage";
	public static final String MAP_READ_NAME = "readname";

	public static final String SPEAK_PREFERENCES_TABLE_NAME = "Contacts";
	
	private static final String SCRIPT_PREFERENCES = "create table if not exists "
			+ SpeakDAL.SPEAK_PREFERENCES_TABLE_NAME + " (" + SpeakDAL.MAP_ID
			+ " integer primary key autoincrement, "
			+ SpeakDAL.MAP_READ_MESSAGE + " string, "
			+ SpeakDAL.MAP_ON_OFF + " string, "
			+ SpeakDAL.MAP_READ_NAME + " string); ";

	private DatabaseHelper(Context context, String name,
			SQLiteDatabase.CursorFactory factory, int version)
	{
		super(context, name, factory, version);
	}

	public static DatabaseHelper create(Context context)
	{
		return DatabaseHelper.create(context, null);
	}

	public static DatabaseHelper create(Context context,
			SQLiteDatabase.CursorFactory cursorFactory)
	{
		return new DatabaseHelper(context, DATABASE_NAME, cursorFactory,
				DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// Initial Database Table Creation - Upgrade from 0 to the latest
		// version
		onUpgrade(db, 0, DATABASE_VERSION);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.i("TAG", "Starting Database Install");

		db.execSQL(SCRIPT_PREFERENCES);

		Log.i(TAG, "Finished Database Install");

	}

	public static void ensureDatabase(Context context)
	{
		DatabaseHelper helper = new DatabaseHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
		helper.getReadableDatabase();
		helper.close();
	}
}
