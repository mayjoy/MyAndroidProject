package com.eyesee.airlauncher2.entity;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
/**
 * 程序信息实体类
 * @author mark
 */
public class AppInfo {
	public String name;
	public Drawable icon;
	public String packageName;
	public boolean isSystemApp;
	public Intent intent;
	public final void setActivity(ComponentName className, int launchFlags) {
        intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(className);
        intent.setFlags(launchFlags);
    }
	@Override
	public String toString() {
		return "AppInfo [name=" + name + ", packageName="
				+ packageName + ", isSystemApp=" + isSystemApp +  "]";
	}

}
