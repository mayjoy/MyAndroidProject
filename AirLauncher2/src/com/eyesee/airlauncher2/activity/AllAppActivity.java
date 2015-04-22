package com.eyesee.airlauncher2.activity;


import java.util.ArrayList;

import com.eyesee.airlauncher2.R;
import com.eyesee.airlauncher2.entity.AppInfo;
import com.eyesee.airlauncher2.utils.AppUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
/**
 * 所有应用程序界面
 * @author mark
 */
public class AllAppActivity extends Activity {

	private ViewPager vp_app;
	private Context context = this;
	private ArrayList<AppInfo> allApps;
	/**
	 * 每页程序个数
	 */
	public static int APP_NUMBERS_PER_PAGE = 8;
	/**
	 * 页面个数
	 */
	private int pageCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app);
		
		init();
		
	}
	
	/**
	 * 初始化
	 * 1.获取页面控件
	 * 2.获取所有程序
	 * 3.计算页面个数
	 */
	private void init() {
		vp_app = (ViewPager) findViewById(R.id.vp_app);
		allApps = AppUtils.getAllApps(context);
		pageCount = allApps.size()%APP_NUMBERS_PER_PAGE!=0?((allApps
				.size() / APP_NUMBERS_PER_PAGE) + 1)
				: (allApps.size() / APP_NUMBERS_PER_PAGE);
		
	}

}
