package com.ninja.who;

import com.ninja.DatabaseHelper.SpeakDAL;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.RemoteViews;

public class MuteWidget extends AppWidgetProvider
{
	private static final String PENDING_INTENT_STRING = "android.Widget.Toggle";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds)
	{
		// To prevent any ANR timeouts, we perform the update in a service
		context.startService(new Intent(context, UpdateService.class));
	}

	public static class UpdateService extends Service
	{
		@Override
		public void onStart(Intent intent, int startId)
		{
			if (intent != null)
			{
				// Build the widget update for today
				RemoteViews updateViews = buildUpdate(this, intent);

				// Push update for this widget to the home screen
				ComponentName thisWidget = new ComponentName(this,
						MuteWidget.class);
				AppWidgetManager manager = AppWidgetManager.getInstance(this);
				manager.updateAppWidget(thisWidget, updateViews);
			}
		}

		public RemoteViews buildUpdate(Context context, Intent intent)
		{
			RemoteViews updateViews = null;
			int mAppWidgetId = 0;
			if (SpeakDAL.findOn(context))
			{
				updateViews = new RemoteViews(context.getPackageName(),
						R.layout.widget_word);
			} else
			{
				updateViews = new RemoteViews(context.getPackageName(),
						R.layout.widget_word_selected);
			}

			Intent defineIntent = new Intent(PENDING_INTENT_STRING);

			Bundle extras = intent.getExtras();
			if (extras != null)
			{
				mAppWidgetId = extras.getInt(
						AppWidgetManager.EXTRA_APPWIDGET_ID,
						AppWidgetManager.INVALID_APPWIDGET_ID);
			}

			defineIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					mAppWidgetId);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					0, defineIntent, 0);
			updateViews.setOnClickPendingIntent(R.id.widget, pendingIntent);

			return updateViews;
		}

		@Override
		public IBinder onBind(Intent intent)
		{
			// We don't need to bind to this service
			return null;
		}
	}
}
