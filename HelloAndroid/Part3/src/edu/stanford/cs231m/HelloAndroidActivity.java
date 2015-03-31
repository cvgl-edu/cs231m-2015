package edu.stanford.cs231m;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HelloAndroidActivity extends Activity {
	
	protected static final String TAG = "HelloAndroid";
	private TextView mMainText;
	private Button mButton1;
	private Button mButton2;
	private BufferedWriter mLogWriter;
	
	
	@Override
	public void onCreate(Bundle settings) {
		
		setContentView(R.layout.main_layout);
		mLogWriter = openLogFile();

		mMainText = (TextView) findViewById(R.id.txtMain);
		mButton1 = (Button) findViewById(R.id.button1);
		mButton2 = (Button) findViewById(R.id.button2);
		
		mMainText.setText("Hello Android!\n");

		mButton1.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mMainText.append("Button 1 was pressed!\n");
				Log.i(TAG, "Button 1 was pressed");
				logMessage("Button 1 was pressed");
			}
		});
		
		mButton2.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mMainText.append("Button 2 was pressed!\n");
				Log.i(TAG, "Button 2 was pressed");
				logMessage("Button 2 was pressed");
			}
		});

		super.onCreate(settings);
	}

	private void logMessage( String message )
	{
		if ( mLogWriter != null )
		{
			try {
				mLogWriter.write(message);
				mLogWriter.newLine();
				mLogWriter.flush();
			} catch (IOException e) {
				Log.e(TAG, "Failed to write to log file");
			}
		
		}
	}
	
	private BufferedWriter openLogFile()
	{

		File appExternalDir = new File( Environment.getExternalStorageDirectory(), 
				"HelloAndroid");
		
		if ( !appExternalDir.exists() )
		{
			if ( appExternalDir.mkdirs() )
			{
				Log.i(TAG, "External storage directory created: " + appExternalDir.toString() );
			}
			else
			{
				Log.e(TAG, "Failed to create directory " + appExternalDir.toString() );
				return null;
			}
		}

		File logFile = new File( appExternalDir, "log.txt");
		
		BufferedWriter writer = null;
		try {
			 writer = new BufferedWriter( new FileWriter(logFile));
			 Log.i(TAG, "Created log file" + logFile);
		} catch (IOException e) {
			Log.e(TAG, "Failed to create file " + logFile.toString() );
			return null;
		}
		
		return writer;
	}


}