package com.eyesee.airlauncher2.activity;

import com.eyesee.airlauncher2.R;
import com.eyesee.airlauncher2.entity.WeatherInfo;
import com.eyesee.airlauncher2.utils.Constants;
import com.eyesee.airlauncher2.utils.TimeUtils;
import com.eyesee.airlauncher2.utils.WeatherUtils;
import com.eyesee.airlauncher2.view.HorizontalScrollViewListener;
import com.eyesee.airlauncher2.view.ObservableHorizontalScrollView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
/**
 * 主页面
 * @author mark
 */
public class MainActivity extends Activity implements Constants{

	private ObservableHorizontalScrollView hsv_app;
	private ImageButton ib_left;
	private ImageButton ib_right;
	
	private TextView tv_area;
	private TextView tv_weather;
	private TextView tv_temperature;
	private TextView tv_date;
	private TextView tv_time;
	private TextView tv_week;
	
	private BroadcastReceiver timeReceiver;
	private BroadcastReceiver weatherReceiver;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		/**
		 * 设置滑动监听器
		 */
		hsv_app.setScrollViewListener(new HorizontalScrollViewListener() {
			
			@Override
			public void onScrollChanged(ObservableHorizontalScrollView scrollView,
					int x, int y, int oldx, int oldy) {
//				Log.d("mark", "X:"+x);
				if(x!=0){
					ib_left.setVisibility(View.VISIBLE);
				}else{
					ib_left.setVisibility(View.GONE);
				}
				if(x!=786){
					ib_right.setVisibility(View.VISIBLE);
				}else{
					ib_right.setVisibility(View.GONE);
				}
			}
		});
		
		setClickListener();
		//注册天气更新的广播
		IntentFilter filter = new IntentFilter();
		filter.addAction(UPDATE_WEATHER);
		weatherReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if(UPDATE_WEATHER.equals(intent.getAction())){
					
					Bundle extras = intent.getExtras();
					WeatherInfo weatherInfo = (WeatherInfo) extras.getSerializable("weatherInfo");
					Log.d("mark", "收到天气更新的广播"+weatherInfo.toString());
					tv_area.setText(weatherInfo.area);
					tv_weather.setText(weatherInfo.weatherText);
					tv_temperature.setText(weatherInfo.temp);
					iv_weather.setImageResource(WeatherUtils.getWeatherImg(weatherInfo.weatherText));
				}
			}
		};
		registerReceiver(weatherReceiver, filter);
	}

	/**
	 * 设置时间日期信息
	 */
	private void setTimeAndDate() {
		tv_time.setText(TimeUtils.getTime());
		tv_date.setText(TimeUtils.getDate());
		tv_week.setText(TimeUtils.getWeekDay());
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//设置activity重新聚焦时的动画为渐入式
		this.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
		
		//设置时间日期信息
		setTimeAndDate();
		
		//注册时间改变的监听器
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_TIME_TICK);
		timeReceiver = new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if(Intent.ACTION_TIME_TICK.equals(action)){
					setTimeAndDate();
				}
			}
		};
		registerReceiver(timeReceiver, intentFilter);
	}

	/**
	 * 设置点击监听器
	 */
	private void setClickListener() {
		ClickListener l = new ClickListener();
		ib_right.setOnClickListener(l);
		ib_left.setOnClickListener(l);
		ib_apps.setOnClickListener(l);
	}
	
	private ImageButton ib_nav;
	private ImageButton ib_recorder;
	private ImageButton ib_files;
	private ImageButton ib_phone;
	private ImageButton ib_music;
	private ImageButton ib_apps;
	private ImageButton ib_set;
	private ImageView iv_weather;
	/**
	 * 初始化
	 * 1.获取页面控件
	 * 2.左键默认隐藏
	 */
	private void init() {
		hsv_app = (ObservableHorizontalScrollView) findViewById(R.id.hsv_app);
		ib_left = (ImageButton) findViewById(R.id.ib_left);
		ib_right = (ImageButton) findViewById(R.id.ib_right);
		
		tv_area = (TextView) findViewById(R.id.tv_area);
		tv_weather = (TextView) findViewById(R.id.tv_weather);
		tv_temperature = (TextView) findViewById(R.id.tv_temperature);
		tv_date = (TextView) findViewById(R.id.tv_date);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_week = (TextView) findViewById(R.id.tv_week);
		iv_weather = (ImageView) findViewById(R.id.iv_weather);
		
		ib_nav = (ImageButton) findViewById(R.id.ib_nav);
		ib_recorder = (ImageButton) findViewById(R.id.ib_recorder);
		ib_files = (ImageButton) findViewById(R.id.ib_files);
		ib_phone = (ImageButton) findViewById(R.id.ib_phone);
		ib_music = (ImageButton) findViewById(R.id.ib_music);
		ib_apps = (ImageButton) findViewById(R.id.ib_apps);
		ib_set = (ImageButton) findViewById(R.id.ib_set);
		
		ib_left.setVisibility(View.GONE);
	}
	
	/**
	 * 点击监听器
	 * @author mark
	 */
	public class ClickListener implements OnClickListener{
		private int scrollX;

		@Override
		public void onClick(View v) {
			
			switch (v.getId()) {
			case R.id.ib_right://右边点击箭头
				scrollX = hsv_app.getScrollX();
				if(scrollX<262){
					hsv_app.smoothScrollTo(262, 0);
				}else if (scrollX>=262&&scrollX<524) {
					hsv_app.smoothScrollTo(524, 0);
				}else if (scrollX>=524&&scrollX<786) {
					hsv_app.smoothScrollTo(786, 0);
				}
				break;
				
			case R.id.ib_left://左边点击箭头
				scrollX = hsv_app.getScrollX();
				if(scrollX>524){
					hsv_app.smoothScrollTo(524, 0);
				}else if (scrollX>262&&scrollX<=524) {
					hsv_app.smoothScrollTo(262, 0);
				}else if (scrollX>0&&scrollX<=262) {
					hsv_app.smoothScrollTo(0, 0);
				}
				break;
				
			case R.id.ib_apps://点击所有程序
				Log.d("mark", "点击了所有程序");
				Intent intent = new Intent(MainActivity.this,AllAppActivity.class);
				startActivity(intent);
				//设置activity重新聚焦时的动画为渐入式
				MainActivity.this.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
			}
		}
		
	}
	
}
