package com.eyesee.airlauncher2.dao;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

/**
 * 数据库工具类
 * @author mark
 *
 */
public class DbManager {
	
	public String cityDbPath;
	public Context context;
	private SQLiteDatabase db;//数据库对象
	
	public DbManager(Context context) {
		super();
		this.context = context;
		cityDbPath = "/data"
				+Environment.getDataDirectory().getAbsolutePath()
				+File.separator+context.getPackageName()
				+File.separator
				+"weather.db";
		db = context.openOrCreateDatabase(cityDbPath, Context.MODE_PRIVATE, null);
	}

	/**
	 * 获取区域ID
	 * @param distictName 区域名
	 * @param cityName	城市名
	 * @return ID
	 */
	public String getAreaCode(String distictName,String cityName){
		//直接查询试试
		Cursor c = db.rawQuery("select * from weathers where area_name like ?;", new String[]{distictName});
		if(c.getCount() == 0){//如果查不到结果,就去掉县或者市再试试
			c = db.rawQuery("select * from weathers where area_name like ?;", new String[]{parseName(distictName)});
			if(c.getCount() == 0){//如果还是没有,就直接查询上级市区名
				c = db.rawQuery("select * from weathers where area_name like ?", new String[]{parseName(cityName)});
				if(c.getCount() == 0){//如果还是没有,那就是数据库有问题了,请上报
					Log.d("mark", "查询不到当地所在编号,城市:"+cityName+",地区:"+distictName);
					return null;
				}
			}
		}
		if(c.moveToFirst()){
			return c.getString(c.getColumnIndex("weather_id"));
		}
		return null;
	}
	
	/**
	 * 去掉市或县
	 * @param district
	 * @return
	 */
	public String parseName(String district) {
		if(district != null){
			if (district.endsWith("市")) {// 如果为空就去掉市字再试试
				String subStr[] = district.split("市");
				district = subStr[0];
			} else if (district.endsWith("县")) {// 或者去掉县字再试试
				String subStr[] = district.split("县");
				district = subStr[0];
			} 
			return district;
		}
		return null;
	}
}
