package edu.stanford.cs231m.camerabasics;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CameraDevice.StateCallback;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

public class CameraActivity extends Activity {
	
	final static String TAG = "CameraActivity";
	
	TextView mMainTxt;
	SurfaceView   mCameraView;
	
	CameraManager mCameraManager;
	CameraDevice  mCameraDevice;
	CameraCaptureSession mCaptureSession;

	@Override
	protected void onPause() {
		if (mCameraDevice != null) {
			mCameraDevice.close();
		}
		
		super.onPause();
	}

	@Override
	public void onCreate(Bundle settings) {

		setContentView(R.layout.main_layout);
		
		mCameraView = (SurfaceView) findViewById(R.id.cameraView);
		mCameraView.getHolder().addCallback( new SurfaceHolder.Callback() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				holder.setFixedSize(1296, 972);
				openBackFacingCamera();
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				// TODO Auto-generated method stub
				
			}
		});

		mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);		
		super.onCreate(settings);
	}
	
	private void startCamera() {
		
		try {
			CaptureRequest.Builder builder;
			
			// Create the request builder.
			builder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
			builder.addTarget( mCameraView.getHolder().getSurface() );
			
			mCaptureSession.setRepeatingRequest( builder.build(), null, null);

		} catch( CameraAccessException e) {
			Log.e(TAG, "Camera Access Exception in call to createCaptureRequest");
		}		
	}
	
	private void createCaptureSession() {
		
		// Create the list of outputs.
		ArrayList<Surface> outputs = new ArrayList<Surface>();
		outputs.add( mCameraView.getHolder().getSurface() );
		
		try {
			mCameraDevice.createCaptureSession(
				outputs, 
				new CameraCaptureSession.StateCallback() {
	
					@Override
					public void onConfigureFailed(CameraCaptureSession session) {
						Log.e(TAG, "Failed to configure capture session");
					
					}
	
					@Override
					public void onConfigured(CameraCaptureSession session) {
						Log.i(TAG, "CaptureSession configured successfuly");
						mCaptureSession = session;
						startCamera();
					}
				
				}, 	
				null);
		
		} catch( CameraAccessException e) {
			Log.e(TAG, "Camera Access Exception in call to createCaptureSession");
		}
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
						createCaptureSession();
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

					// We have found the camera, list the available sizes.
					StreamConfigurationMap  map = mCameraManager.getCameraCharacteristics(cameraIdList[i]).
							get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
					
					android.util.Size sizes[] = map.getOutputSizes(SurfaceHolder.class);
					
					for ( int j = 0; j < sizes.length; j++ ) {
						Log.i(TAG, "- " + sizes[j].getWidth() + " x " + sizes[j].getHeight());
					}

					return cameraIdList[i]; 
				}

			}

		} catch( CameraAccessException e ) {
			
			Log.e(TAG, "CameraAccessException in enumerateCameras()");
		}
		
		return null;
	}

}
