package com.eyesee.airlauncher2.utils;
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
}
