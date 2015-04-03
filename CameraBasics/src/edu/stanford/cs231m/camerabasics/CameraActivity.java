package edu.stanford.cs231m.camerabasics;


import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraDevice.StateCallback;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class CameraActivity extends Activity {
	
	final static String TAG = "CameraActivity";
	
	TextView mMainTxt;
	CameraManager mCameraManager;
	CameraDevice  mCameraDevice;

	@Override
	public void onCreate(Bundle settings) {

		setContentView(R.layout.main_layout);
		mMainTxt = (TextView) findViewById(R.id.txtMain);
		
		mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
		
		enumerateCameras();
		openBackFacingCamera();
		
		super.onCreate(settings);
	}
	

	private void openBackFacingCamera()
	{
		String cameraId = findBackFacingCamera();
		
		try
		{
			mCameraManager.openCamera(
				
				cameraId,
				
				new StateCallback() {

					@Override
					public void onDisconnected(CameraDevice arg0) {
						Log.e(TAG, "Camera was disconnected\n");
					}

					@Override
					public void onError(CameraDevice arg0, int arg1) {
						Log.e(TAG, "Error opening camera");
					}

					@Override
					public void onOpened(CameraDevice arg0) {
						Log.i(TAG, "Opened camera!");
						mCameraDevice = arg0;
					}
			
				}
			
				, null);
		}
		catch ( CameraAccessException e)
		{
			
		}
	}

	private String findBackFacingCamera()
	{
		try {
			String cameraIdList[] = mCameraManager.getCameraIdList();
			
			for ( int i = 0; i < cameraIdList.length; ++i )
			{
				// Get the camera characteristics
				CameraCharacteristics properties;
				properties = mCameraManager.getCameraCharacteristics(cameraIdList[i]);

				if ( properties.get( CameraCharacteristics.LENS_FACING ) == 
						CameraCharacteristics.LENS_FACING_BACK ) {
					
					return cameraIdList[i]; 
				}

			}
		} catch( CameraAccessException e ) {
			
			Log.e(TAG, "CameraAccessException in enumerateCameras()");
		}
		
		return null;
	}

	private void enumerateCameras() {
		
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
