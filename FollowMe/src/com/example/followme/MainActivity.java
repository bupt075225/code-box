package com.example.followme;

import java.util.ArrayList;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource.OnLocationChangedListener;
import com.amap.api.maps.MapView;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;

public class MainActivity extends ActionBarActivity implements LocationSource,
	AMapLocationListener{

	private MapView mapView;
    private AMap aMap;
    private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
    private Marker marker;// 定位雷达小图标
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        //aMap = mapView.getMap();
        init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
 
	}
	
	/**
	 * 设置一些aMap的属性
	 */
	private void setUpMap()
	{
		ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point1));
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point2));
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point3));
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point4));
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point5));
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point6));
		marker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
				.icons(giflist).period(50));
		
		//自定义定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		// 设置小蓝点的图标
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));
		
		// 设置圆形的边框颜色
		myLocationStyle.strokeColor(Color.BLACK);
		// 设置圆形的填充颜色
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));
		// 设置圆形的边框粗细
		myLocationStyle.strokeWidth(0.1f);
		
		aMap.setMyLocationStyle(myLocationStyle);
		//对定位图标设置旋转角度
		aMap.setMyLocationRotateAngle(180);
		//设置定位监听
		aMap.setLocationSource(this);
		//设置默认定位按钮是否显示
		aMap.getUiSettings().setMyLocationButtonEnabled(true);
		//设置true表示显示定位层并可触发定位
		aMap.setMyLocationEnabled(true);
		//设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种 
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		mapView.onResume();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		mapView.onPause();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);		
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mapView.onDestroy();
	}
	
	/**
	 * 定位成功后回调函数
	 * 发送定位请求后，会进定位回调，返回位置信息对象 AMapLocation
	 */
	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (mListener != null && aLocation != null) 
		{
			if (aLocation.getAMapException().getErrorCode() == 0)
			{
				// 显示系统小蓝点
				mListener.onLocationChanged(aLocation);
				// 定位雷达小图标
				marker.setPosition(new LatLng(aLocation.getLatitude(), aLocation
						.getLongitude()));
				// 设置小蓝点旋转角度
				float bearing = aMap.getCameraPosition().bearing;
				aMap.setMyLocationRotateAngle(bearing);
			}			
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			//此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
            //在定位结束后，在合适的生命周期调用destroy()方法     
            //其中如果间隔时间为-1，则定位只定一次
			mAMapLocationManager.requestLocationData(
					LocationProviderProxy.AMapNetwork, 60*1000, 10, this);
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id)
		{
		    case R.id.action_settings:
		    	return true;
		    case R.id.action_to_indoor:
		    	translateToIndoorNav();
		    	return true;
		    default:
		    	return super.onOptionsItemSelected(item);
		}	
		
	}

	/**
	 * Called when the user press the indoor button
	 */
	public void translateToIndoorNav()
	{
		Intent intent = new Intent(this, IndoorAtlasActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
}
