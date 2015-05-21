package com.eyesee.airlauncher2.utils;

import com.eyesee.airlauncher2.R;

/**
 * 天气工具类
 * @author mark
 *
 */
public class WeatherUtils {
	/**
	 * 解析天气编号
	 */
	public static String decodeWeatherId(String id){
		if("00".equals(id)){
			return "晴";
		}else if("01".equals(id)){
			return "多云";
		}else if("02".equals(id)){
			return "阴";
		}else if("03".equals(id)){
			return "阵雨";
		}else if("04".equals(id)){
			return "雷阵雨";
		}else if("05".equals(id)){
			return "雷阵雨伴有冰雹";
		}else if("06".equals(id)){
			return "雨夹雪";
		}else if("07".equals(id)){
			return "小雨";
		}else if("08".equals(id)){
			return "中雨";
		}else if("09".equals(id)){
			return "大雨";
		}else if("10".equals(id)){
			return "暴雨";
		}else if("11".equals(id)){
			return "大暴雨";
		}else if("12".equals(id)){
			return "特大暴雨";
		}else if("13".equals(id)){
			return "阵雪";
		}else if("14".equals(id)){
			return "小雪";
		}else if("15".equals(id)){
			return "中雪";
		}else if("16".equals(id)){
			return "大雪";
		}else if("17".equals(id)){
			return "暴雪";
		}else if("18".equals(id)){
			return "雾";
		}else if("19".equals(id)){
			return "冻雨";
		}else if("20".equals(id)){
			return "沙尘暴";
		}else if("21".equals(id)){
			return "小到中雨";
		}else if("22".equals(id)){
			return "中到大雨";
		}else if("23".equals(id)){
			return "大到暴雨 ";
		}else if("24".equals(id)){
			return "暴雨到大暴雨";
		}else if("25".equals(id)){
			return "大暴雨到特大暴雨";
		}else if("26".equals(id)){
			return "小到中雪";
		}else if("27".equals(id)){
			return "中到大雪";
		}else if("28".equals(id)){
			return "大到暴雪";
		}else if("29".equals(id)){
			return "浮尘";
		}else if("30".equals(id)){
			return "扬沙";
		}else if("31".equals(id)){
			return "强沙尘暴";
		}else if("53".equals(id)){
			return "霾";
		}else if("99".equals(id)){
			return "无";
		}
		return "无";
	}
	
	/**
	 * 根据天气获取对应图片资源ID
	 */
	public static int getWeatherImg(String weatherText){
		if("晴".equals(weatherText)){
			return R.drawable.sunny;
		}else if( "多云".equals(weatherText)){
			return R.drawable.cloudy;
		}else if("阴".equals(weatherText)){
			return R.drawable.cloudy;
		}else if("阵雨".equals(weatherText)){
			return R.drawable.shower;
		}else if("雷阵雨".equals(weatherText)){
			return R.drawable.thunder_shower;
		}else if("雷阵雨伴有冰雹".equals(weatherText)){
			return R.drawable.thunder_shower;
		}else if("雨夹雪".equals(weatherText)){
			return R.drawable.light_snow;
		}else if("小雨".equals(weatherText)){
			return R.drawable.light_rain;
		}else if("中雨".equals(weatherText)){
			return R.drawable.moderate_rain;
		}else if("大雨".equals(weatherText)){
			return R.drawable.heavy_rain;
		}else if("暴雨".equals(weatherText)){
			return R.drawable.heavy_rain;
		}else if("大暴雨".equals(weatherText)){
			return R.drawable.heavy_rain;
		}else if("特大暴雨".equals(weatherText)){
			return R.drawable.heavy_rain;
		}else if("阵雪".equals(weatherText)){
			return R.drawable.light_snow;
		}else if("小雪".equals(weatherText)){
			return R.drawable.light_snow;
		}else if("中雪".equals(weatherText)){
			return R.drawable.light_snow;
		}else if("大雪".equals(weatherText)){
			return R.drawable.heavy_snow;
		}else if("暴雪".equals(weatherText)){
			return R.drawable.heavy_snow;
		}else if("雾".equals(weatherText)){
			return R.drawable.foggy;
		}else if("冻雨".equals(weatherText)){
			return R.drawable.light_rain;
		}else if("沙尘暴".equals(weatherText)){
			return R.drawable.duststorm;
		}else if("小到中雨".equals(weatherText)){
			return R.drawable.light_rain;
		}else if("中到大雨".equals(weatherText)){
			return R.drawable.heavy_rain;
		}else if("大到暴雨 ".equals(weatherText)){
			return R.drawable.heavy_rain;
		}else if("暴雨到大暴雨".equals(weatherText)){
			return R.drawable.heavy_rain;
		}else if("大暴雨到特大暴雨".equals(weatherText)){
			return R.drawable.heavy_rain;
		}else if("小到中雪".equals(weatherText)){
			return R.drawable.light_snow;
		}else if("中到大雪".equals(weatherText)){
			return R.drawable.heavy_snow;
		}else if("大到暴雪".equals(weatherText)){
			return R.drawable.heavy_snow;
		}else if("浮尘".equals(weatherText)){
			return R.drawable.duststorm;
		}else if("扬沙".equals(weatherText)){
			return R.drawable.duststorm;
		}else if("强沙尘暴".equals(weatherText)){
			return R.drawable.duststorm;
		}else if("霾".equals(weatherText)){
			return R.drawable.haze;
		}else if("99".equals(weatherText)){
			return R.drawable.cloudy;
		}
		return R.drawable.cloudy;
	}
}
