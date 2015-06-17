package com.example.leadme;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;

/** 
 * @author ccliu
 * Orientation类
 * 从系统服务中获取旋转感应器，监听感应器采样数据,将获取到的数据更新到处理UI刷新的对象
 */
public class Orientation implements SensorEventListener{
	public interface Listener {
	    void onOrientationChanged(float yaw, float pitch, float roll, long timestamp);
	}

	private static final int SENSOR_DELAY_MICROS = 50 * 1000; // 50ms

	private final SensorManager mSensorManager;
	private final Sensor mRotationSensor;
	private final WindowManager mWindowManager;

	private int mLastAccuracy;
	private Listener mListener;

	public Orientation(SensorManager sensorManager, WindowManager windowManager) {
	    mSensorManager = sensorManager;
	    mWindowManager = windowManager;

	    // Can be null if the sensor hardware is not available
	    mRotationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
	}

	public void startListening(Listener listener) {
	    if (mListener == listener) {
	        return;
	    }
	    mListener = listener;
	    if (mRotationSensor == null) {
	        Log.w("Orientation class", "Rotation vector sensor not available; will not provide orientation data.");
	        return;
	    }
	    mSensorManager.registerListener(this, mRotationSensor, SENSOR_DELAY_MICROS);
	}

	public void stopListening() {
	    mSensorManager.unregisterListener(this);
	    mListener = null;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	    if (mLastAccuracy != accuracy) {
	        mLastAccuracy = accuracy;
	    }
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		Sensor sensor = event.sensor;
		int type = sensor.getType();
		
	    if (mListener == null) {
	        return;
	    }
	    if (mLastAccuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
	        return;
	    }    
		
		switch(type)
		{
			case Sensor.TYPE_GYROSCOPE:
				break;
			case Sensor.TYPE_ROTATION_VECTOR:
				updateOrientation(event.values, event.timestamp);
				break;
			default:
				return;
		}	
	}

	/**
	 * @param rotationVector
	 * 更新感应器数据到UI对象
	 */
	@SuppressLint("NewApi")
	private void updateOrientation(float[] rotationVector, long timestamp) {
	    float[] rotationMatrix = new float[9];
	    SensorManager.getRotationMatrixFromVector(rotationMatrix, rotationVector);

	    // By default, remap the axes as if the front of the
	    // device screen was the instrument panel.
	    int worldAxisForDeviceAxisX = SensorManager.AXIS_X;
	    int worldAxisForDeviceAxisY = SensorManager.AXIS_Z;

	    // Adjust the rotation matrix for the device orientation
	    int screenRotation = mWindowManager.getDefaultDisplay().getRotation();
	    if (screenRotation == Surface.ROTATION_0) {
	        worldAxisForDeviceAxisX = SensorManager.AXIS_X;
	        worldAxisForDeviceAxisY = SensorManager.AXIS_Z;
	    } else if (screenRotation == Surface.ROTATION_90) {
	        worldAxisForDeviceAxisX = SensorManager.AXIS_Z;
	        worldAxisForDeviceAxisY = SensorManager.AXIS_MINUS_X;
	    } else if (screenRotation == Surface.ROTATION_180) {
	        worldAxisForDeviceAxisX = SensorManager.AXIS_MINUS_X;
	        worldAxisForDeviceAxisY = SensorManager.AXIS_MINUS_Z;
	    } else if (screenRotation == Surface.ROTATION_270) {
	        worldAxisForDeviceAxisX = SensorManager.AXIS_MINUS_Z;
	        worldAxisForDeviceAxisY = SensorManager.AXIS_X;
	    }

	    float[] adjustedRotationMatrix = new float[9];
	    SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisForDeviceAxisX,
	        worldAxisForDeviceAxisY, adjustedRotationMatrix);

	    // Transform rotation matrix into azimuth/pitch/roll
	    float[] orientation = new float[3];
	    SensorManager.getOrientation(adjustedRotationMatrix, orientation);

	    // Convert radians to degrees
	    float yaw = orientation[0] * -57;
	    float pitch = orientation[1] * -57;
	    float roll = orientation[2] * -57;

	    mListener.onOrientationChanged(yaw, pitch, roll, timestamp);
	  }
}
