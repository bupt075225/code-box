package com.example.leadme;

import android.support.v7.app.ActionBarActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.widget.TextView;

public class SensorActivity extends ActionBarActivity 
implements Orientation.Listener {

	private SensorManager sensorManager;
	private Sensor gyroscope;
	private Sensor rotationVectorSensor;
	
	private Orientation orientation;
	private RotationIndicator rotationIndicator;
	
	private TextView sensorInfo;
	
	private MediaPlayer audioPlayer;
	private float lastYaw;
	private long lastTimestamp = 0;
	private static final float NS2S = 1.0f / 1000000000.0f;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor);
		
		initUI();	
		getSensorService();
		orientation = new Orientation(sensorManager, getWindow().getWindowManager());
		audioPlayer = MediaPlayer.create(this, R.raw.turn20150615215939);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		orientation.startListening(this);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		//页面失去焦点时要注销感应器，否则电量消耗快
		orientation.stopListening();
	}
	
	/**
	 * 感应器的数据更新传递到UI对象
	 */
	@Override
	public void onOrientationChanged(float yaw, float pitch, float roll, long timestamp) {
		rotationIndicator.setAttitude(yaw, pitch, roll);
		System.out.println(">>>>>>>>>Yaw:" + yaw + "interval:" + (yaw - lastYaw));
		if(0!=lastTimestamp && 2<(timestamp - lastTimestamp)*NS2S
				&& (25 < (yaw - lastYaw) || -25 > (yaw - lastYaw)))
		{
			audioPlayer.start();
		}
		
		if(0==lastTimestamp || 2<(timestamp - lastTimestamp)*NS2S)
		{
			lastYaw = yaw;
			lastTimestamp = timestamp;
		}		
	}
	
	/**
	 * 初始化UI.
	 */
	private void initUI()
	{
		//准备显示信息的UI组件
		sensorInfo = (TextView)findViewById(R.id.showSensorInfo);
		// Initialize the calibrated gauges views
		rotationIndicator = (RotationIndicator)findViewById(R.id.orientation_indicator);

	}
	
	/**
	 *  获取传感器服务
	 */
	private void getSensorService()
	{
		//从系统服务中获取传感器管理器
		sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
				
		if(null != gyroscope)
		{
			sensorInfo.setText("检测到陀螺仪:\n" 
				+ "感应器名称:" + gyroscope.getName() + "\n"
				+ "版本:" + gyroscope.getVersion() + "\n"
				+ "供应商:" + gyroscope.getVendor() + "\n");		
		}
		else
		{
			sensorInfo.setText("没有检测到陀螺仪\n");
		}
				
		if(null!=rotationVectorSensor)
		{
			sensorInfo.append("------------\n检测到旋转感应器:\n"
				+ "感应器名称:" + rotationVectorSensor.getName() + "\n"
				+ "版本:" + rotationVectorSensor.getVersion() + "\n"
				+ "供应商:" + rotationVectorSensor.getVendor() + "\n");
		}
		else
		{
			sensorInfo.append("没有检测到旋转感应器\n");
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sensor, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
