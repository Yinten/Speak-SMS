package com.ninja.who;

import android.content.Context;
import android.content.SharedPreferences;

public class GeoPreferences
{
	private static final String PREFS_NAME = "MyPrefsFile";

	private static final boolean READ_NAME_DEFAULT_VALUE = true;
	private static final String READ_NAME_SWITCH = "READNAMESWITCH";
	
	private static final boolean READ_MESSAGE_DEFAULT_VALUE = false;
	private static final String READ_MESSAGE_SWITCH = "READMESSAGESWITCH";

	private static final boolean ONOFF_DEFAULT_VALUE = true;
	private static final String ONOFF_MESSAGE_SWITCH = "ONOFFMESSAGESWITCH";
	
	@Deprecated 
	public static boolean getOnOffSwitchValue(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(PREFS_NAME, 0);
		boolean readName = sp.getBoolean(ONOFF_MESSAGE_SWITCH, ONOFF_DEFAULT_VALUE); 	
		return readName;
	}
	
	@Deprecated
	public static void setOnOffSwitchValue(Context context, boolean on)
	{
	      SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
	      SharedPreferences.Editor editor = settings.edit();
	      editor.putBoolean(ONOFF_MESSAGE_SWITCH, on);
	      editor.commit();
	}
	
	@Deprecated
	public static boolean getReadNameSwitchValue(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(PREFS_NAME, 0);
		boolean readName = sp.getBoolean(READ_NAME_SWITCH, READ_NAME_DEFAULT_VALUE); 	
		return readName;
	}
	
	@Deprecated
	public static void setReadNameSwitchValue(Context context, boolean readName)
	{
	      SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
	      SharedPreferences.Editor editor = settings.edit();
	      editor.putBoolean(READ_NAME_SWITCH, readName);
	      editor.commit();
	}
	
	@Deprecated
	public static boolean getReadMessageSwitchValue(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(PREFS_NAME, 0);
		boolean readName = sp.getBoolean(READ_MESSAGE_SWITCH, READ_MESSAGE_DEFAULT_VALUE); 	
		return readName;
	}
	
	@Deprecated
	public static void setReadMessageSwitchValue(Context context, boolean readMessage)
	{
	      SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
	      SharedPreferences.Editor editor = settings.edit();
	      editor.putBoolean(READ_MESSAGE_SWITCH, readMessage);
	      editor.commit();
	}

	
	
	
	
}
