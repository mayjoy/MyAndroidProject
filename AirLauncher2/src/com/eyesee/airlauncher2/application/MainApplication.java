package com.eyesee.airlauncher2.application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.eyesee.airlauncher2.entity.WeatherInfo;
import com.eyesee.airlauncher2.service.WeatherService;
import com.eyesee.airlauncher2.utils.Constants;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
/**
 * Application类
 * 1.创建全局变量
 * 2.加载配置
 * 3.将城市数据库复制到指定目录
 * 4.启动天气服务
 * @author mark
 */
public class MainApplication extends Application implements Constants{
	/**
	 * 全局变量:天气信息
	 */
	public WeatherInfo weatherInfo = null;
	/**
	 * 城市数据库路径
	 */
	public String cityDbPath;
	/**
	 * 是否自动定位
	 */
	public boolean autoLocation;
	
	@Override
	public void onCreate() {
		super.onCreate();
		initDb();
		//启动天气服务
		startService(new Intent(this,WeatherService.class));
		
	}
	
	/**
	 * 初始化数据库
	 */
	public void initDb() {
		cityDbPath = "/data"
				+Environment.getDataDirectory().getAbsolutePath()
				+File.separator+getPackageName()
				+File.separator
				+"weather.db";
		final File db=new File(cityDbPath);
		
		//如果程序数据目录下不存在数据库,就从资产目录中拷贝数据库过去,采用异步线程拷贝
		if(!db.exists()){
			Log.d("mark", "城市数据库不存在,进行拷贝操作");
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						InputStream is = getAssets().open("weather.db");
						FileOutputStream fos = new FileOutputStream(db);
						//文件复制操作(边读边写)
						int len = -1;
						byte[] buffer = new byte[1024];
						while((len = is.read(buffer))!=-1){
							fos.write(buffer, 0, len);
							fos.flush();
						}
						is.close();
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}).start();
			
		}
	}
	
}
