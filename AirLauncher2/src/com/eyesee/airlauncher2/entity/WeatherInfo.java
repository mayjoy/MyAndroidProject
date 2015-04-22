package com.eyesee.airlauncher2.entity;

import java.io.Serializable;

/**
 * 天气信息实体类
 * @author mark
 */
public class WeatherInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public String area;
	public String weatherText;
	public String temp;
	@Override
	public String toString() {
		return "WeatherInfo [area=" + area + ", weather=" + weatherText
				+ ", temp=" + temp + "]";
	}

}
