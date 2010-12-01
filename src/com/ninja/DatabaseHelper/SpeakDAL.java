package com.ninja.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ninja.who.GeoPreferences;

public class SpeakDAL
{

	public static final String MAP_ID = "_id";
	public static final String MAP_ON_OFF = "readonoff";
	public static final String MAP_READ_MESSAGE = "readmessage";
	public static final String MAP_READ_NAME = "readname";

	public static final String SPEAK_PREFERENCES_TABLE_NAME = "Contacts";

	
	public static void setReadName(Context context, boolean fieldValue)
	{
		UpdatePreference(context, SpeakDAL.MAP_READ_NAME, fieldValue);
	}
	
	public static boolean findReadName(Context context)
	{
		return FindPreference(context, SpeakDAL.MAP_READ_NAME);
	}
	

	public static void setReadMessage(Context context, boolean fieldValue)
	{
		UpdatePreference(context, SpeakDAL.MAP_READ_MESSAGE, fieldValue);
	}
	
	public static boolean findReadMessage(Context context)
	{
		return FindPreference(context, SpeakDAL.MAP_READ_MESSAGE); 
	}
	
	
	public static void setOn(Context context, boolean fieldValue)
	{
		UpdatePreference(context, SpeakDAL.MAP_ON_OFF, fieldValue);
	}
	
	public static boolean findOn(Context context)
	{
		return FindPreference(context, SpeakDAL.MAP_ON_OFF);
	}
	
	private static void UpdatePreference(Context mCtx, String fieldName, boolean fieldValue)
	{
		SQLiteDatabase db = Database.getWritableDatabase(mCtx);
		ContentValues preferenceValues = MakeContentValues(fieldName, fieldValue);
		
		try
		{
			System.out.println("UPDATE"); 
			System.out.println("fieldName: " + fieldName);
			System.out.println("fieldValue: " + fieldValue);
			System.out.println("fieldValueConverted: " + Boolean.toString(fieldValue));
			db.update(SpeakDAL.SPEAK_PREFERENCES_TABLE_NAME, preferenceValues, null, null);
		}
		catch(Exception e)
		{
			e.printStackTrace(); 
		}
		finally
		{
			if (db != null)
				db.close();
		}
	}
	
	

	private static boolean FindPreference(Context context, String fieldName)
	{
		SQLiteDatabase db = Database.getWritableDatabase(context);
		Cursor c = null;
		boolean retVal = false;
		try
		{
			c = db.query(false, SPEAK_PREFERENCES_TABLE_NAME, null, null, null,
					null, null, null, null);

			if (c.moveToFirst())
			{
				String preConvertRetVal = c.getString(c.getColumnIndex(fieldName));
				System.out.println("fieldName: " + fieldName);
				System.out.println("retVal Preconvedrt: " + preConvertRetVal);
				
				
				if(preConvertRetVal.equalsIgnoreCase("true"))
					retVal = true; 
				
				System.out.println("fieldValueConverted: " + retVal);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace(); 
		}
		finally
		{
			if (db != null)
				db.close();
			if (c != null)
				c.close();
		}

		return retVal;	
	}
	
	private static ContentValues MakeContentValues(String fieldName, boolean fieldValue)
	{
		ContentValues exercisevalues = new ContentValues();
		exercisevalues.put(fieldName, Boolean.toString(fieldValue));
		return exercisevalues;		
	}
	
	/*
	 * Initial setup
	 */
	public static boolean hasPreferences(Context context)
	{
		SQLiteDatabase db = Database.getWritableDatabase(context);
		Cursor c = null;
		boolean retVal = false;
		try
		{
			c = db.query(false, SPEAK_PREFERENCES_TABLE_NAME, null, null, null,
					null, null, null, null);

			if (c.moveToFirst())
			{
				retVal = true;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace(); 
		}
		finally
		{
			if (db != null)
				db.close();
			if (c != null)
				c.close();
		}

		return retVal;
	}

	public static void initializeDatabase(Context context)
	{
		System.out.println("INITIALIZE DATABASE");
		if (!hasPreferences(context))
		{
			
			System.out.println("INITIALIZE DATABASE - true");
			SQLiteDatabase db = Database.getWritableDatabase(context);
			
			boolean preferencesOnOff = GeoPreferences.getOnOffSwitchValue(context);
			boolean preferencesReadMessage = GeoPreferences.getReadMessageSwitchValue(context);
			boolean preferencesReadName = GeoPreferences.getReadNameSwitchValue(context); 
			
			ContentValues values = new ContentValues();
			values.put(MAP_ON_OFF, Boolean.toString(preferencesOnOff));
			values.put(MAP_READ_MESSAGE,Boolean.toString(preferencesReadMessage));
			values.put(MAP_READ_NAME, Boolean.toString(preferencesReadName));
			
			try
			{
				db.insert(SPEAK_PREFERENCES_TABLE_NAME, null, values);
			} 
			catch(Exception e)
			{
				e.printStackTrace(); 
			}
			finally
			{
				if (db != null)
					db.close();
			}
		}

	}

}
