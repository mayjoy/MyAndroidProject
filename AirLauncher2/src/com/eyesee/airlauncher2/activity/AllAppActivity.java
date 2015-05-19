package com.eyesee.airlauncher2.activity;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.eyesee.airlauncher2.R;
import com.eyesee.airlauncher2.adapter.GridViewAdapter;
import com.eyesee.airlauncher2.adapter.MyViewPagerAdapter;
import com.eyesee.airlauncher2.entity.AppInfo;
import com.eyesee.airlauncher2.utils.AppUtils;
/**
 * 所有应用程序界面
 * @author mark
 */
public class AllAppActivity extends Activity {

	private ViewPager vp_app;
	private Context context = this;
	//所有应用程序列表
	private ArrayList<AppInfo> allApps;

	/**
	 * 每页程序个数
	 */
	public static int APP_NUMBERS_PER_PAGE = 8;
	/**
	 * 页面个数
	 */
	private int pageCount;
	private ArrayList<View> viewList = new ArrayList<View>();
	private ImageButton ib_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app);
		
		init();
		
		ib_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	/**
	 * 初始化
	 * 1.获取页面控件
	 * 2.获取所有程序
	 * 3.计算页面个数
	 */
	private void init() {
		vp_app = (ViewPager) findViewById(R.id.vp_app);
		ib_back = (ImageButton) findViewById(R.id.ib_back);
		
		//获取所有app
		allApps = AppUtils.getAllApps(context);
		//计算页面个数
		pageCount = allApps.size()%APP_NUMBERS_PER_PAGE!=0?((allApps.size() / APP_NUMBERS_PER_PAGE) + 1)
				: (allApps.size() / APP_NUMBERS_PER_PAGE);
		
		Log.d("mark", "页面个数"+pageCount);
		Log.d("mark", allApps.toString());
		
		//计算每页显示的APP
		for (int i = 0; i < pageCount; i++) {
			View view = View.inflate(context, R.layout.item_vp, null);
			GridView gv_app = (GridView) view.findViewById(R.id.gv_app);
			
			//获取当前页面要显示的APP信息列表
			final ArrayList<AppInfo> perPageApps = new ArrayList<AppInfo>();
			if ((i != (pageCount - 1))) {
				for (int j = i * APP_NUMBERS_PER_PAGE; j < APP_NUMBERS_PER_PAGE
						+ i * APP_NUMBERS_PER_PAGE; j++) {
					perPageApps.add(allApps.get(j));
				}
			} else {
				for (int j = i * APP_NUMBERS_PER_PAGE; j < allApps.size(); j++) {
					perPageApps.add(allApps.get(j));
				}
			}
			Log.d("mark", perPageApps.toString());
			//设置每页的gridView的适配器
			gv_app.setAdapter(new GridViewAdapter(context,perPageApps));
			
			//设置每个程序的点击监听事件
			gv_app.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Log.d("mark", "点击了"+perPageApps.get(position).toString());
					Intent intent = getPackageManager().getLaunchIntentForPackage(perPageApps
							.get(position).packageName);
					if (intent != null) {
						startActivity(intent);
					}
				}
			});
			
			//设置按下去时单元格的背景色为透明
			gv_app.setSelector(new ColorDrawable(Color.TRANSPARENT));
			
			//添加到页面列表
			viewList.add(view);
		}

		//设置页面ViewPager
		vp_app.setAdapter(new MyViewPagerAdapter(viewList));
		vp_app.setCurrentItem(0);
		vp_app.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}

}
