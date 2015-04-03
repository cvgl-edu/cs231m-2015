package edu.stanford.cs231m.camerabasics;


import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.widget.TextView;

public class CameraActivity extends Activity {
	
	
	TextView mMainTxt;
	CameraManager mCameraManager;

	@Override
	public void onCreate(Bundle settings) {

		setContentView(R.layout.main_layout);
		mMainTxt = (TextView) findViewById(R.id.txtMain);
		enumerateCameras();
		
		super.onCreate(settings);
	}
	
	private void enumerateCameras() {
		
		mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
		
		try {
			String cameraIdList[] = mCameraManager.getCameraIdList();
			
			mMainTxt.append("Number of cameras " + cameraIdList.length + "\n");
			for ( int i = 0; i < cameraIdList.length; ++i )
			{
				// Get the camera characteristics
				CameraCharacteristics properties;
				properties = mCameraManager.getCameraCharacteristics(cameraIdList[i]);

				// Which way is this camera facing?
				int lensFacing = properties.get( CameraCharacteristics.LENS_FACING );

				// Print properties
				mMainTxt.append("Camera id: " + cameraIdList[i] + "\n");
				mMainTxt.append(" -Orientation: ");
				if ( lensFacing == CameraCharacteristics.LENS_FACING_FRONT ) {
					mMainTxt.append("FRONT_FACING\n");
				}
				else if ( lensFacing == CameraCharacteristics.LENS_FACING_BACK )
				{
					mMainTxt.append("BACK_FACING\n");
				}
			}
			
		} catch( CameraAccessException e ) {
			
			
		}
		
	
	}

}
