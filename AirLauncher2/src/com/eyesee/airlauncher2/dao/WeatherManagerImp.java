package com.eyesee.airlauncher2.dao;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.eyesee.airlauncher2.utils.Constants;
import com.eyesee.airlauncher2.utils.EnCodeUrl;
import com.eyesee.airlauncher2.utils.TimeUtils;
import com.eyesee.airlauncher2.utils.WeatherUtils;

public class WeatherManagerImp implements WeatherManager, Constants {
	private Context context;
	private String url;
	private JsonObjectRequest request;
	private RequestQueue requestQueue;
	private String area, weatherText, temp;

	public WeatherManagerImp(Context context, RequestQueue requestQueue) {
		this.context = context;
		this.requestQueue = requestQueue;
	}

	@Override
	public String getArea() {
		return area;
	}

	@Override
	public String getWetherText() {
		return weatherText;
	}

	@Override
	public String getTemp() {
		return temp;
	}

	@Override
	public void getWeather(final String areaCode) {
		// 根据天气编号和加密算法获取url
		String data = WEATHER_URL + "?areaid=" + areaCode + "&type=forecast_v"
				+ "&date=" + TimeUtils.getTimeString()
				+ "&appid=eb5ab7919aa518e5";
		String keyCode = EnCodeUrl.getKeyCode(data);
		url = WEATHER_URL + "?areaid=" + areaCode + "&type=forecast_v"
				+ "&date=" + TimeUtils.getTimeString() + "&appid=eb5ab7"
				+ "&key=" + keyCode;
		Log.d("mark", "url:" + url);

		requestOfVolley(areaCode);

	}

	// 请求并解析天气数据
	private void requestOfVolley(final String areaCode) {
		// 初始化请求
		request = new JsonObjectRequest(Method.GET, url, null,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						try {
							// WeatherInfo weatherInfo = new WeatherInfo();
							// 默认编码方式不对,获取的数据要进行转码
							byte[] bytes = response.toString().getBytes(
									"ISO8859-1");
							String string = new String(bytes, "UTF-8");
							response = new JSONObject(string);
							// 进行数据校验
							JSONObject c = response.getJSONObject("c");
							String c1 = c.getString("c1");
							if (c1.equals(areaCode)) {
								Log.d("mark", "返回数据验证成功");
								String c3 = c.getString("c3");// 当前地区
								JSONObject f = response.getJSONObject("f");
								JSONArray f1 = f.getJSONArray("f1");
								JSONObject info = (JSONObject) f1.get(0);// 获得天气信息
								String fa = info.getString("fa");// 白天天气现象编号
								String fb = info.getString("fb");// 晚上天气现象编号
								String fc = info.getString("fc");// 白天天气温度(摄氏度)
								String fd = info.getString("fd");// 晚上天气温度(摄氏度)
								area = c3;
								// 因为白天已经过去,预报在晚上那次更新的时候白天的数据就会为空
								if (fa.equals("")) {
									weatherText = WeatherUtils
											.decodeWeatherId(fb);
									temp = fd + "℃";
								} else {
									weatherText = WeatherUtils
											.decodeWeatherId(fa);
									temp = fd + "℃~" + fc + "℃";
								}
								// 发送广播给WeatherService，进行数据更新
								Intent intent = new Intent();
								intent.setAction(GET_WEATHER_DONE);
								context.sendBroadcast(intent);
							} else {
								Log.d("mark", "返回数据校验失败");
							}
						} catch (Exception e) {
							e.printStackTrace();
							Log.d("mark", "天气数据解析错误:" + response.toString());
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						Log.d("mark", "天气查询失败,请检查网络");
						// 查询失败5秒后继续查询
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								requestQueue.add(request);
								requestQueue.start();
							}
						}, 5000);
					}
				});

		// 启动查询
		requestQueue.add(request);
		requestQueue.start();
	}

}
