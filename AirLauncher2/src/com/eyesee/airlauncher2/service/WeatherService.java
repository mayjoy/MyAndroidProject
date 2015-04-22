package com.eyesee.airlauncher2.service;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import com.eyesee.airlauncher2.dao.DbManager;
import com.eyesee.airlauncher2.entity.WeatherInfo;
import com.eyesee.airlauncher2.utils.Constants;
import com.eyesee.airlauncher2.utils.EnCodeUrl;
import com.eyesee.airlauncher2.utils.TimeUtils;
import com.eyesee.airlauncher2.utils.WeatherUtils;

public class WeatherService extends Service implements Constants{

	public LocationClient locationClient;
	public BDLocationListener myListener = new MyLocationListener();
	
	public int locationCount = 0;
	
	private RequestQueue requestQueue;
	private DbManager dbManager;
	private String url;
	private JsonObjectRequest request;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		//初始化百度定位
		locationClient = new LocationClient(getApplicationContext());
		locationClient.registerLocationListener(myListener);
		//设置参数
	    LocationClientOption option = new LocationClientOption();
	    option.setOpenGps(true);
	    option.setLocationMode(LocationMode.Battery_Saving);//设置定位模式,省电模式,只通过网络定位
	    option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
	    option.setScanSpan(300000);//设置发起定位请求的间隔时间为5分钟
	    option.setIsNeedAddress(true);//返回的定位结果包含地址信息
	    option.setAddrType("all");
//	    option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
	    option.setProdName("CarAir2.0");//设置产品线名称
	    
	    locationClient.setLocOption(option);//绑定参数
	    
	    //初始化网络请求
	    requestQueue = Volley.newRequestQueue(getApplicationContext());
		//初始化数据库
		dbManager = new DbManager(getApplicationContext());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		locationClient.start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		requestQueue.cancelAll(this);
		super.onDestroy();
	}
	
	/**
	 * 定位监听器
	 * @author mark
	 */
	class MyLocationListener implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation location) {
			if(location == null) return;
			//如果定位地址为空,直接返回,显示定位失败
			if(location.getAddrStr() == null){
				Log.d("mark", "定位失败,请检查网络");
				return;
			}
			Log.d("mark","省份:"+location.getProvince()+
					"\n城市:"+location.getCity()+
					"\n区域:"+location.getDistrict()+
					"\n地址:"+location.getAddrStr()+
					"\n定位时间:"+TimeUtils.getTime());
			
			String areaCode = dbManager.getAreaCode(location.getDistrict(), location.getCity());
			getWeather(areaCode);
		}
		
	}
	
	/**
	 * 查询天气
	 */
	public void getWeather(final String areaCode) {
		//根据天气编号和加密算法获取url
		String data = WEATHER_URL+"?areaid="+areaCode+"&type=forecast_v"+"&date="+TimeUtils.getTimeString()+"&appid=eb5ab7919aa518e5";
		String keyCode = EnCodeUrl.getKeyCode(data);
		url = WEATHER_URL+"?areaid="+areaCode+"&type=forecast_v"+"&date="+TimeUtils.getTimeString()+"&appid=eb5ab7"+"&key="+keyCode;
		Log.d("mark", "url:"+url);
		
		requestOfVolley(areaCode);
		
	}
	
	/**
	 * 使用Volley方式访问网络
	 * @param areaCode 城市编号
	 */
	private void requestOfVolley(final String areaCode) {
		//初始化请求
		request = new JsonObjectRequest(Method.GET, url, null, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				try {
					WeatherInfo weatherInfo = new WeatherInfo();
					//默认编码方式不对,获取的数据要进行转码
					byte[] bytes = response.toString().getBytes("ISO8859-1");
					String string = new String(bytes,"UTF-8");
					response = new JSONObject(string);
					//进行数据校验
					JSONObject c = response.getJSONObject("c");
					String c1 = c.getString("c1");
					if(c1.equals(areaCode)){
						Log.d("mark", "返回数据验证成功");
						String c3 = c.getString("c3");//当前地区
						JSONObject f = response.getJSONObject("f");
						JSONArray f1 = f.getJSONArray("f1");
						JSONObject info = (JSONObject) f1.get(0);//获得天气信息
						String fa = info.getString("fa");//白天天气现象编号
						String fb = info.getString("fb");//晚上天气现象编号
						String fc = info.getString("fc");//白天天气温度(摄氏度)
						String fd = info.getString("fd");//晚上天气温度(摄氏度)
						weatherInfo.area = c3;
						//因为白天已经过去,预报在晚上那次更新的时候白天的数据就会为空
						if(fc != null && fc!="") {
							weatherInfo.weatherText = WeatherUtils.decodeWeatherId(fa);
							weatherInfo.temp = fd+"℃~"+fc+"℃";
						}else {
							weatherInfo.weatherText = WeatherUtils.decodeWeatherId(fb);
							weatherInfo.temp = fd+"℃";
						}
						
						//发送广播
						Intent intent = new Intent();
						intent.setAction(UPDATE_WEATHER);
						intent.putExtra("weatherInfo", weatherInfo);
						sendBroadcast(intent);	
						
					}else{
						Log.d("mark", "返回数据校验失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.d("mark", "天气数据解析错误:"+response.toString());
				}
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				Log.d("mark", "天气查询失败,请检查网络");
				//查询失败5秒后继续查询
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						requestQueue.add(request);
						requestQueue.start();
					}
				}, 5000);
			}
		});
		
		//启动查询
		requestQueue.add(request);
		requestQueue.start();
	}
	
}
