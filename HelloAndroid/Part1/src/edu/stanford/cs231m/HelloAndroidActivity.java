package edu.stanford.cs231m;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class HelloAndroidActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		TextView txtView = new TextView(this);
		txtView.setText("Hello Android!");
		setContentView(txtView);
		
		super.onCreate(savedInstanceState);
	}

}
