package edu.stanford.cs231m.helloandroid;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
	private Thread mWorkerThread;
	private Handler mHandler;
	private ProgressDialog mProgress;
	
	private final static int MSG_ASYNC_TASK_STARTED = 0;
	private final static int MSG_ASYNC_TASK_COMPLETED = 1;
	
	@Override
	public void onCreate(Bundle settings) {
		
		setContentView(R.layout.main_layout);

		mLogWriter = openLogFile();
		mHandler = new Handler(mHandlerCallback);
		mProgress = new ProgressDialog(this);

		mMainText = (TextView) findViewById(R.id.txtMain);
		mButton1 = (Button) findViewById(R.id.button1);
		mButton2 = (Button) findViewById(R.id.button2);
		
		mMainText.setText("Hello Android!\n");

		mButton1.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mMainText.append("Button 1 was pressed!\n");
				mMainText.append("The square of 2 is " + square(2) + "\n");
				logMessage("Button 1 was pressed!");
				Log.i(TAG, "Button 1 was pressed!");
			}
		});
		
		mButton2.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mMainText.append("Button 2 was pressed!\n");
				logMessage("Button 2 was pressed!");
				Log.i(TAG, "Button 2 was pressed");			
				
				mWorkerThread = new Thread( new Runnable() {

					@Override
					public void run() {
						longRunningTask(6000);
					}				
				});
				
				mWorkerThread.start();
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
	
	private Handler.Callback mHandlerCallback = new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			
			long currentTime = System.currentTimeMillis();
			switch( msg.what )
			{
			case MSG_ASYNC_TASK_STARTED:
				mMainText.append("Async task started at " + currentTime + "\n");
				mProgress.setTitle("Running async task");
				mProgress.setMessage("Wait...");
				mProgress.show();
				return true;
			case MSG_ASYNC_TASK_COMPLETED:
				mMainText.append("Async task ended at " + currentTime + "\n");
				mProgress.dismiss();
				return true;
			default:
				// The message was not handled, return false
				return false;
			}
		}
	};

	private void longRunningTask( long taskDurationInMs )
	{
		long startTime = System.currentTimeMillis();
		mHandler.sendEmptyMessage(MSG_ASYNC_TASK_STARTED);
		
		long currentTime = startTime;
		do
		{
			try {
				Thread.sleep( taskDurationInMs );
			} catch (InterruptedException e) {
			}

			currentTime = System.currentTimeMillis();
			
		} while ( currentTime < startTime + taskDurationInMs );

		mHandler.sendEmptyMessage(MSG_ASYNC_TASK_COMPLETED);

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
		} catch (IOException e) {
			Log.e(TAG, "Failed to create file " + logFile.toString() );
			return null;
		}
		
		return writer;
	}
	
	private native int square(int n);
	
	static {
		System.loadLibrary("HelloAndroid");
	}
}
