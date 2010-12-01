package com.ninja.who;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

import com.ninja.DatabaseHelper.SpeakDAL;

public class TTSService extends Service
{
	private Context _context;
	private TextToSpeech _mTts;
	private OnInitListener _voiceSpeechInitListener;
	//private 
	private String _name;
	private String _body;

	private AudioManager am;
	private int previousRingerMode;
	private VoiceCompletedBroadCastReceiver VoiceCompleted;
	//private boolean isScreenOnInitial; 
	
	
	@Override
	public void onCreate()
	{
		System.out.println("SERVICE CREATED");
		super.onCreate();
		_context = this;
		//PowerManager pm = (PowerManager)_context.getSystemService(Context.POWER_SERVICE); 
		//isScreenOnInitial = pm.isScreenOn(); 
		DisableNotificationRing();
		VoiceCompleted = new VoiceCompletedBroadCastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter
				.addAction(TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
		this.registerReceiver(VoiceCompleted, intentFilter);
	}

	public class VoiceCompletedBroadCastReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context arg0, Intent arg1)
		{
			stopSelf();
		}
	}

	@Override
	public void onStart(final Intent intent, int startId)
	{
		super.onStart(intent, startId);

		if (intent != null)
		{
			_name = intent.getStringExtra(IntentManagement.NameStringExtra);
			_body = intent.getStringExtra(IntentManagement.MessageStringExtra);
			String speechString = "";
			if (SpeakDAL.findReadName(_context))
			{
				speechString = _name + " Text";
			}
			if (SpeakDAL.findReadMessage(_context))
			{
				speechString = speechString + " " + _body;
			}

			System.out.println("SPEECH STRING: " + speechString);
			
			if (speechString != "")
			{
				SetUpVoiceSpeechInitialized(speechString);
				_mTts = new TextToSpeech(_context, _voiceSpeechInitListener);
			}
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if (VoiceCompleted != null)
		{
			unregisterReceiver(VoiceCompleted);
		}
		if (_mTts != null)
		{
			_mTts.shutdown();
		}
		if (am != null)
		{
			am.setRingerMode(previousRingerMode);
		}
	}

	private void DisableNotificationRing()
	{

		am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		previousRingerMode = am.getRingerMode();
		am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
	}

	private void SetUpVoiceSpeechInitialized(final String speechString)
	{
		_voiceSpeechInitListener = new OnInitListener()
		{
			public void onInit(int status)
			{
				if (TextToSpeech.SUCCESS == status)
				{	
					if(am.getMode() == AudioManager.MODE_NORMAL && previousRingerMode == AudioManager.RINGER_MODE_NORMAL)
					{
						_mTts.speak(speechString, TextToSpeech.QUEUE_ADD, null);
					}
					else
					{
						stopSelf(); 
					}
				}
			}
		};
	}

	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}
}
