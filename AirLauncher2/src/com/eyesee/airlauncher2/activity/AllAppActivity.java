package com.eyesee.airlauncher2.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.LinearLayout;

import com.eyesee.airlauncher2.R;
import com.eyesee.airlauncher2.adapter.GridViewAdapter;
import com.eyesee.airlauncher2.adapter.MyViewPagerAdapter;
import com.eyesee.airlauncher2.entity.AppInfo;
import com.eyesee.airlauncher2.utils.AppUtils;
import com.eyesee.airlauncher2.view.PointView;

/**
 * 所有应用程序界面
 * 
 * @author mark
 */
public class AllAppActivity extends Activity {

	private ViewPager vp_app;
	private Context context = this;
	// 所有应用程序列表
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
	private LinearLayout ll_point;
	private int lastPos;
	private BroadcastReceiver appChangedReceiver;//应用程序改变监听器

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app);
		findViewById();
		init();

		ib_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		//应用程序改变监听器
		appChangedReceiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if("android.intent.action.PACKAGE_ADDED".equals(action) ||
						"android.intent.action.PACKAGE_REMOVED".equals(action)){
					Log.d("mark", "应用程序改变了");
					//先清空之前的页面和小圆点,再重新设置
					viewList.clear();
					vp_app.removeAllViews();
					ll_point.removeAllViews();
					init();
				}
				
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.PACKAGE_ADDED");
		filter.addAction("android.intent.action.PACKAGE_REMOVED");
		filter.addDataScheme("package");
		registerReceiver(appChangedReceiver, filter);
		
	}

	/**
	 * 初始化 1.获取所有程序 2.计算页面个数 3.计算每个页面的程序并分配
	 */
	private void init() {
		// 获取所有app
		allApps = AppUtils.getAllApps(context);
		// 计算页面个数
		pageCount = allApps.size() % APP_NUMBERS_PER_PAGE != 0 ? ((allApps
				.size() / APP_NUMBERS_PER_PAGE) + 1)
				: (allApps.size() / APP_NUMBERS_PER_PAGE);

//		Log.d("mark", "页面个数" + pageCount);
//		Log.d("mark", allApps.toString());

		// 计算每页显示的APP
		for (int i = 0; i < pageCount; i++) {
			View view = View.inflate(context, R.layout.item_vp, null);
			GridView gv_app = (GridView) view.findViewById(R.id.gv_app);

			// 获取当前页面要显示的APP信息列表
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
			
//			Log.d("mark", perPageApps.toString());
			
			// 设置每页的gridView的适配器
			gv_app.setAdapter(new GridViewAdapter(context, perPageApps));

			// 设置每个程序的点击监听事件
			gv_app.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Log.d("mark", "点击了" + perPageApps.get(position).toString());
					Intent intent = getPackageManager()
							.getLaunchIntentForPackage(
									perPageApps.get(position).packageName);
					if (intent != null) {
						startActivity(intent);
						// 设置打开activity的动画为渐入式
						AllAppActivity.this.overridePendingTransition(
										android.R.anim.fade_in,
										android.R.anim.fade_out);
					}
				}
			});

			// 设置按下去时单元格的背景色为透明
			gv_app.setSelector(new ColorDrawable(Color.TRANSPARENT));

			// 添加到页面列表
			viewList.add(view);

			// 添加页面指示圆点
			PointView pointView = new PointView(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			params.rightMargin = 20;
			pointView.setLayoutParams(params);
			if (i != 0) {
				pointView.setDisChecked();
			}else{
				pointView.setChecked();
				lastPos = 0;
			}
			ll_point.addView(pointView);
		}

		// 设置页面ViewPager
		vp_app.setAdapter(new MyViewPagerAdapter(viewList));
		vp_app.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				((PointView) ll_point.getChildAt(position)).setChecked();
				((PointView) ll_point.getChildAt(lastPos)).setDisChecked();
				lastPos = position;
				Log.d("mark", "lastPos = "+lastPos);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	/**
	 * 获取页面控件
	 */
	private void findViewById() {
		vp_app = (ViewPager) findViewById(R.id.vp_app);
		ib_back = (ImageButton) findViewById(R.id.ib_back);
		ll_point = (LinearLayout) findViewById(R.id.ll_point);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 设置activity重新聚焦时的动画为渐入式
		AllAppActivity.this.overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(appChangedReceiver);
	}
	
}
