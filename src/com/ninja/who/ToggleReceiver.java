package com.ninja.who;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ninja.DatabaseHelper.SpeakDAL;

public class ToggleReceiver extends BroadcastReceiver
{

	private static String INTENT_TO_SEND = "android.appwidget.action.APPWIDGET_UPDATE"; 
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		System.out.println("!!TOGGLE WIDGET RCVD");
		SpeakDAL.setOn(context, !SpeakDAL.findOn(context));
		int mAppWidgetId = 0; 
		Bundle extras = intent.getExtras();
		if (extras != null) {
		    mAppWidgetId = extras.getInt(
		            AppWidgetManager.EXTRA_APPWIDGET_ID, 
		            AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		Intent i = new Intent(context, MuteWidget.UpdateService.class);
		i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		context.startService(i);
	}

}
