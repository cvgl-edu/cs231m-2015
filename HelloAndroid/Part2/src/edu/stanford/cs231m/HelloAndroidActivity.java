package edu.stanford.cs231m;

import android.app.Activity;
import android.os.Bundle;
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
	
	
	@Override
	public void onCreate(Bundle settings) {
		
		setContentView(R.layout.main_layout);

		mMainText = (TextView) findViewById(R.id.txtMain);
		mButton1 = (Button) findViewById(R.id.button1);
		mButton2 = (Button) findViewById(R.id.button2);
		
		mMainText.setText("Hello Android!\n");

		mButton1.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mMainText.append("Button 1 was pressed!\n");
				Log.i(TAG, "Button 1 was pressed");
			}
		});
		
		mButton2.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mMainText.append("Button 2 was pressed!\n");
				Log.i(TAG, "Button 2 was pressed");
			}
		});

		super.onCreate(settings);
	}

}