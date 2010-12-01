package com.ninja.who;

import com.ninja.DatabaseHelper.SpeakDAL;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class TextReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (SpeakDAL.findOn(context))
		{
			if (SpeakDAL.findReadMessage(context)
					|| SpeakDAL.findReadName(context))
			{
				SmsMessage msg[] = getMessagesFromBundle(intent.getExtras());

				CustomSMSMessage csm = getSMSMessage(context, msg);

				Intent i = new Intent(context, TTSService.class);
				i.putExtra(IntentManagement.NameStringExtra, csm.name);
				i.putExtra(IntentManagement.MessageStringExtra, csm.body);
				context.startService(i);
			}
		}
	}

	private CustomSMSMessage getSMSMessage(Context context, SmsMessage msg[])
	{
		for (int ii = 0; ii < msg.length; ii++)
		{
			String messageBody = msg[ii].getDisplayMessageBody();

			if (messageBody != null && messageBody.length() > 0)
			{
				CustomSMSMessage cm = new CustomSMSMessage();
				String phoneNumber = msg[ii].getOriginatingAddress();
				String name = ContactUtility.getContactNameFromNumber(context,
						phoneNumber);

				cm.name = name;
				cm.body = messageBody;
				if (name == "")
				{
					cm.name = phoneNumber;

				}
				return cm;
			}
		}
		return null;
	}

	private static SmsMessage[] getMessagesFromBundle(Bundle bundle)
	{
		Object pdus[] = (Object[]) bundle.get("pdus");
		SmsMessage retMsgs[] = new SmsMessage[pdus.length];
		for (int n = 0; n < pdus.length; n++)
		{
			byte[] byteData = (byte[]) pdus[n];
			retMsgs[n] = SmsMessage.createFromPdu(byteData);
		}

		return retMsgs;
	}

	public class CustomSMSMessage
	{
		public String name;
		public String body;
	}

}
