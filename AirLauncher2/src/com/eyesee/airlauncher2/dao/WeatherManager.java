package com.eyesee.airlauncher2.dao;

public interface WeatherManager {
	// 根据地区编号请求并解析天气数据
	public void getWeather(String areaCode);
	// 得到地区名
	public String getArea();
	// 得到天气描述
	public String getWetherText();
	// 得到温度描述
	public String getTemp();
}
