package com.ninja.who;

import com.ninja.DatabaseHelper.SpeakDAL;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.Toast;

public class WhoActivity extends TalkingActivity
{
	private ImageView _globeImage;
	private CheckedTextView _speakNameCTV;
	private CheckedTextView _speakMessageCTV;
	private Button _saveButton, _goToPreferencesButton;
	private Context _context;
	private static final String _testMessage = "hello, how is your day?";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
		setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.main);
		_context = this;
		SpeakDAL.initializeDatabase(_context);
		AttachViews();
		
		BindPreferences();
		BindCheckBoxClicks(); 
		BindGlobeOnClickListener();
		BindSaveOnClickListener();
		BindPreferencesOnClickListener(); 
	}

	private void BindPreferencesOnClickListener()
	{
		 _goToPreferencesButton.setOnClickListener(new OnClickListener(){

			public void onClick(View v)
			{
				Intent i = new Intent(); 
				i.setPackage("com.android.settings");
				i.setClassName("com.android.settings", "com.android.settings.TextToSpeechSettings");
				startActivity(i);
				
			}});
		
	}

	private void BindCheckBoxClicks()
	{
		SetCheckListenerOverOnClickListener(_speakNameCTV);
		SetCheckListenerOverOnClickListener(_speakMessageCTV);
	}
	
	private void SetCheckListenerOverOnClickListener(final CheckedTextView ctv)
	{
		ctv.setOnClickListener(new OnClickListener(){

			public void onClick(View v)
			{
				ctv.setChecked(!ctv.isChecked());
				
			}}); 			
	}
	
	private void BindSaveOnClickListener()
	{
		_saveButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				SpeakDAL.setReadName(_context, _speakNameCTV
						.isChecked());
				SpeakDAL.setReadMessage(_context,
						_speakMessageCTV.isChecked());
				Toast.makeText(_context, "Settings are saved.", Toast.LENGTH_SHORT).show();
				finish(); 
			}
		});
	}

	private void BindGlobeOnClickListener()
	{
		_globeImage.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Initialize(new OnVoicePrepared()
				{

					public void voicePrepared()
					{
						Toast.makeText(_context, "You should hear a voice..if not try volume controls", Toast.LENGTH_SHORT).show(); 
						speak();
					}
				});

			}
		});
	}

	private void BindPreferences()
	{
		_speakNameCTV.setChecked(SpeakDAL.findReadName(_context));
		_speakMessageCTV.setChecked(SpeakDAL.findReadMessage(_context));
	}

	private void AttachViews()
	{
		_globeImage = (ImageView) findViewById(R.id._main_speak_test_img);
		_speakNameCTV = (CheckedTextView) findViewById(R.id._main_speak_name_chk);
		_speakMessageCTV = (CheckedTextView) findViewById(R.id._main_speak_sender_chk);
		_saveButton = (Button) findViewById(R.id._main_save_btn);
		_goToPreferencesButton = (Button)findViewById(R.id._main_voice_btn); 
	}

	private void speak()
	{
		super.Speak(_testMessage, false);
	}
	
	
}
