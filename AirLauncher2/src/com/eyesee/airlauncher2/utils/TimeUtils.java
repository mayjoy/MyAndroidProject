package com.eyesee.airlauncher2.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.text.format.Time;
/**
 * 时间工具类
 * @author mark
 */
public class TimeUtils {
	/**
	 * 获取当前系统时间
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTime() {
		SimpleDateFormat timeFormat=new SimpleDateFormat("HH:mm");//时间格式
		String time = timeFormat.format(new Date());
		return time;
	}
	
	/**
	 * 获取当前系统日期
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getDate(){
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");//时间格式
		String date = dateFormat.format(new Date());
		return date;
	}
	
	/**
	 * 获取当前星期
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getWeekDay(){
		SimpleDateFormat weekDayFormat=new SimpleDateFormat("E");//时间格式
		String weekDay = weekDayFormat.format(new Date());
		return weekDay;
	}
	
	/**
	 * 获取系统时间,另一种格式,201504081504
	 * @return
	 */
	public static String getTimeString() {
		// 获取时间
		Time time = new Time();
		time.setToNow();
		int year = time.year;
		int month = time.month;
		int monthDay = time.monthDay;
		int hour = time.hour;
		int minute = time.minute;
		return ""+year+int2String(month)+int2String(monthDay)+int2String(hour) + int2String(minute);
	}
	
	/**
	 * 10以下的数字自动前面加0
	 */
	public static String int2String(int i) {
		String s;
		if (i < 10) {
			s = "0" + i;
		} else {
			s = "" + i;
		}
		return s;
	}
}
