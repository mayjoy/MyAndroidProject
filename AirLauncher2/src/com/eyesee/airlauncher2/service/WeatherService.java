package com.eyesee.airlauncher2.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import com.eyesee.airlauncher2.application.MainApplication;
import com.eyesee.airlauncher2.dao.DbManager;
import com.eyesee.airlauncher2.dao.WeatherManager;
import com.eyesee.airlauncher2.dao.WeatherManagerImp;
import com.eyesee.airlauncher2.entity.WeatherInfo;
import com.eyesee.airlauncher2.utils.Constants;
import com.eyesee.airlauncher2.utils.TimeUtils;

public class WeatherService extends Service implements Constants {

	public LocationClient locationClient;
	public BDLocationListener myListener = new MyLocationListener();

	public int locationCount = 0;

	private RequestQueue requestQueue;
	private DbManager dbManager;
//	private String url;
//	private JsonObjectRequest request;

	private MainApplication application = null;
	private WeatherManager weatherManager = null;
	private WeatherServiceReceiver networkReceiver = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		// 初始化百度定位
		locationClient = new LocationClient(getApplicationContext());
		locationClient.registerLocationListener(myListener);
		// 设置参数
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setLocationMode(LocationMode.Battery_Saving);// 设置定位模式,省电模式,只通过网络定位
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(300000);// 设置发起定位请求的间隔时间为5分钟
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setAddrType("all");
		// option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
		option.setProdName("CarAir2.0");// 设置产品线名称

		locationClient.setLocOption(option);// 绑定参数

		// 初始化网络请求
		requestQueue = Volley.newRequestQueue(getApplicationContext());
		// 初始化数据库
		dbManager = new DbManager(getApplicationContext());

		application = (MainApplication) getApplication();
		// 注册广播接收器
		if (networkReceiver == null) {
			networkReceiver = new WeatherServiceReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			filter.addAction(GET_WEATHER_DONE);
			registerReceiver(networkReceiver, filter);
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		locationClient.start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		requestQueue.cancelAll(this);
		unregisterReceiver(networkReceiver);
		super.onDestroy();
	}

	/**
	 * 定位监听器
	 * 
	 * @author mark
	 */
	class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			// 如果定位地址为空,直接返回,显示定位失败
			if (location.getAddrStr() == null) {
				Log.d("mark", "定位失败,请检查网络");
				return;
			}
			Log.d("mark",
					"省份:" + location.getProvince() + "\n城市:"
							+ location.getCity() + "\n区域:"
							+ location.getDistrict() + "\n地址:"
							+ location.getAddrStr() + "\n定位时间:"
							+ TimeUtils.getTime());

			String areaCode = dbManager.getAreaCode(location.getDistrict(),
					location.getCity());
			// getWeather(areaCode);
			weatherManager = new WeatherManagerImp(WeatherService.this,
					requestQueue);
			// 访问网络获取天气，当获取到天气数据时，会发送广播过来进行数据更新
			weatherManager.getWeather(areaCode);
		}

	}

	// 更新全局数据，并刷新显示
	private void updateWeatherInfo() {
		WeatherInfo weatherInfo = new WeatherInfo();
		weatherInfo.area = weatherManager.getArea();
		weatherInfo.weatherText = weatherManager.getWetherText();
		weatherInfo.temp = weatherManager.getTemp();
//		application.weatherInfo = weatherInfo;
		// 发送广播，更新显示
		Intent intent = new Intent();
		intent.setAction(UPDATE_WEATHER);
		intent.putExtra("weatherInfo", weatherInfo);
		sendBroadcast(intent);
	}

	/**
	 * 查询天气
	 */
	// public void getWeather(final String areaCode) {
	// //根据天气编号和加密算法获取url
	// String data =
	// WEATHER_URL+"?areaid="+areaCode+"&type=forecast_v"+"&date="+TimeUtils.getTimeString()+"&appid=eb5ab7919aa518e5";
	// String keyCode = EnCodeUrl.getKeyCode(data);
	// url =
	// WEATHER_URL+"?areaid="+areaCode+"&type=forecast_v"+"&date="+TimeUtils.getTimeString()+"&appid=eb5ab7"+"&key="+keyCode;
	// Log.d("mark", "url:"+url);
	//
	// requestOfVolley(areaCode);
	//
	// }
	//
	// private MainApplication myApp;
	//
	// /**
	// * 使用Volley方式访问网络
	// * @param areaCode 城市编号
	// */
	// private void requestOfVolley(final String areaCode) {
	// //初始化请求
	// request = new JsonObjectRequest(Method.GET, url, null, new
	// Listener<JSONObject>() {
	//
	// @Override
	// public void onResponse(JSONObject response) {
	// try {
	// WeatherInfo weatherInfo = new WeatherInfo();
	// //默认编码方式不对,获取的数据要进行转码
	// byte[] bytes = response.toString().getBytes("ISO8859-1");
	// String string = new String(bytes,"UTF-8");
	// response = new JSONObject(string);
	// //进行数据校验
	// JSONObject c = response.getJSONObject("c");
	// String c1 = c.getString("c1");
	// if(c1.equals(areaCode)){
	// Log.d("mark", "返回数据验证成功");
	// String c3 = c.getString("c3");//当前地区
	// JSONObject f = response.getJSONObject("f");
	// JSONArray f1 = f.getJSONArray("f1");
	// JSONObject info = (JSONObject) f1.get(0);//获得天气信息
	// String fa = info.getString("fa");//白天天气现象编号
	// String fb = info.getString("fb");//晚上天气现象编号
	// String fc = info.getString("fc");//白天天气温度(摄氏度)
	// String fd = info.getString("fd");//晚上天气温度(摄氏度)
	// weatherInfo.area = c3;
	// //因为白天已经过去,预报在晚上那次更新的时候白天的数据就会为空
	// if(fa.equals("")) {
	// weatherInfo.weatherText = WeatherUtils.decodeWeatherId(fb);
	// weatherInfo.temp = fd+"℃";
	// }else {
	// weatherInfo.weatherText = WeatherUtils.decodeWeatherId(fa);
	// weatherInfo.temp = fd+"℃~"+fc+"℃";
	// }
	// myApp = (MainApplication) getApplication();
	// myApp.weatherInfo = weatherInfo;
	// //发送广播
	// Intent intent = new Intent();
	// intent.setAction(UPDATE_WEATHER);
	// intent.putExtra("weatherInfo", weatherInfo);
	// sendBroadcast(intent);
	//
	// }else{
	// Log.d("mark", "返回数据校验失败");
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// Log.d("mark", "天气数据解析错误:"+response.toString());
	// }
	// }
	// }, new ErrorListener() {
	//
	// @Override
	// public void onErrorResponse(VolleyError arg0) {
	// Log.d("mark", "天气查询失败,请检查网络");
	// //查询失败5秒后继续查询
	// new Handler().postDelayed(new Runnable() {
	// @Override
	// public void run() {
	// requestQueue.add(request);
	// requestQueue.start();
	// }
	// }, 5000);
	// }
	// });
	//
	// //启动查询
	// requestQueue.add(request);
	// requestQueue.start();
	// }

	private class WeatherServiceReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			String action = arg1.getAction();
			if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
				ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = manager.getActiveNetworkInfo();
				if (networkInfo != null && networkInfo.isConnected()) {// 网络已连接上
					if (locationClient != null) {
						if (locationClient.isStarted()) {// 定位服务已经开启
							switch (locationClient.requestLocation()) {// 异步获取当前位置
							case 0:// 正常发起了定位
								Log.d("mark", "正常发起了定位");
								break;
							case 1:// 服务没有启动
								Log.d("mark", "服务没有启动");
								break;
							case 2:// 没有监听函数
								Log.d("mark", "没有监听函数");
								break;
							case 6:// 请求间隔过短， 前后两次请求定位时间间隔不能小于1s
								Log.d("mark", "请求时间过短");
								break;
							default:
								break;
							}
						} else {
							locationClient.start();
						}
					}
				}
			} else if (GET_WEATHER_DONE.equals(action)) {// 获取天气数据完毕广播
				updateWeatherInfo();// 刷新
			}
		}

	}

}
