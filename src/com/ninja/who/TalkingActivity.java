package com.ninja.who;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class TalkingActivity extends Activity
{
	private static final int MY_DATA_CHECK_CODE = 989;
	protected static final int VOICE_DONE = 50005;
	private TextToSpeech mTts;
	private Context _context;
	private boolean _shutdownTTSonFinish, _vrinitialized, parentInitilized;

	@SuppressWarnings("unused")
	private Handler _handler;

	private OnInitListener voiceSpeechInitListener;
	private OnVoicePrepared _onVoicePrepared;

	public void Initialize(final OnVoicePrepared commands)
	{
		_context = this;
		_vrinitialized = false;
		_shutdownTTSonFinish = false;
		parentInitilized = true;
		_onVoicePrepared = commands;
		SetUpHandler();
		CheckThatVRIsInstalled();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (mTts != null)
		{
			mTts.shutdown();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		parentInitilized = false;
	}

	private void CheckThatVRIsInstalled()
	{
		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
	}

	private void SetUpHandler()
	{
		_handler = new Handler()
		{
			public void handleMessage(Message msg)
			{
				if (!isFinishing())
				{
					if (msg.what == VOICE_DONE)
					{
						if (_shutdownTTSonFinish)
						{
							mTts.shutdown();
						}
					}
				}
			}
		};
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == MY_DATA_CHECK_CODE)
		{
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
			{
				SetUpVoiceSpeechInitialized();
				mTts = new TextToSpeech(_context, voiceSpeechInitListener);

			} else
			{
				Intent installIntent = new Intent();
				installIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
	}

	private void SetUpVoiceSpeechInitialized()
	{
		if (!isFinishing())
		{
			voiceSpeechInitListener = new OnInitListener()
			{
				public void onInit(int status)
				{
					if (!isFinishing())
					{
						if (TextToSpeech.SUCCESS == status)
						{
							_onVoicePrepared.voicePrepared();
							_vrinitialized = true;
						}
					}
				}
			};
		}
	}

	protected boolean VrInitialized()
	{
		return _vrinitialized;
	}

	protected void Speak(String text, boolean shutdownTTSonFinish)
	{
		if (parentInitilized)
		{
			_shutdownTTSonFinish = shutdownTTSonFinish;
			mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
		}
	}

	public interface OnVoicePrepared
	{
		void voicePrepared();
	}
}