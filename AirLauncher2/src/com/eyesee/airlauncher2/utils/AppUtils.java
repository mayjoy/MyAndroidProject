package com.eyesee.airlauncher2.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.eyesee.airlauncher2.entity.AppInfo;

/**
 * 程序工具类
 * 
 * @author mark
 */
public class AppUtils implements Constants {

	/**
	 * 获取所有App
	 * 
	 * @param context
	 * @return
	 */
	public static ArrayList<AppInfo> getAllApps(Context context) {
		ArrayList<AppInfo> allApps = new ArrayList<AppInfo>();
		PackageManager pm = context.getPackageManager();
		Intent main = new Intent(Intent.ACTION_MAIN, null);
		main.addCategory(Intent.CATEGORY_LAUNCHER);
		final List<ResolveInfo> apps = pm.queryIntentActivities(main, 0);
		Collections.sort(apps, new ResolveInfo.DisplayNameComparator(pm));
		if (apps != null) {
			for (int i = 0; i < apps.size(); i++) {
				ResolveInfo info = apps.get(i);
				if (!info.activityInfo.packageName
						.equals(AIRLAUNCHER_PACKAGE_NAME)) {
					AppInfo appInfo = new AppInfo();
					appInfo.name = info.loadLabel(pm).toString();
					appInfo.setActivity(new ComponentName(
							info.activityInfo.applicationInfo.packageName,
							info.activityInfo.name),
							Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					appInfo.icon = info.activityInfo.loadIcon(pm);
					appInfo.packageName = info.activityInfo.packageName;
					allApps.add(appInfo);
				}
			}
		}
		return allApps;
	}

	/**
	 * 用来判断服务是否运行.
	 * @param context
	 * @param className 判断的服务名字
	 * @return true 在运行 false 不在运行
	 */
	public static boolean isServiceRunning(Context mContext, String className) {
		ActivityManager activityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(30);
		for (RunningServiceInfo info : serviceList) {
			Log.d("mark", info.service.getClassName());
			if (info.service.getClassName().equals(className)) {
				return true;
			}
		}
		return false;
	}
}
