package com.ninja.DatabaseHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Database 
{
	private static DatabaseHelper getHelper(Context context)
	{
		return DatabaseHelper.create(context);
	}
	
	public static SQLiteDatabase getWritableDatabase(Context context)
	{
		return getHelper(context).getWritableDatabase();
	}
	
	public static SQLiteDatabase getReadableDatabase(Context context)
	{
		return getHelper(context).getReadableDatabase();
	}
}

