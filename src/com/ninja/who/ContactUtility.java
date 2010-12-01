package com.ninja.who;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.PhoneLookup;

public class ContactUtility
{
	public static final String TAG = "Contact Utility";

	public static String getContactNameFromNumber(Context context, String number)
	{
		if (number == null || number.equals(""))
			return "";

		Uri lookupUri1 = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				number);

		String name = getStoredValue(context, lookupUri1, null, null,
				CommonDataKinds.Phone.DISPLAY_NAME);

		return name;

	}

	public static String getStoredValue(Context context, Uri uri,
			String selection, String[] selectionArgs, String column)
	{
		String value = "";
		Cursor c = context.getContentResolver().query(uri, new String[]
		{ column }, selection, selectionArgs, null);
		try
		{
			if (c.moveToFirst())
			{
				value = c.getString(c.getColumnIndex(column));
			}
		} finally
		{
			c.close();
		}
		return value;
	}

}
