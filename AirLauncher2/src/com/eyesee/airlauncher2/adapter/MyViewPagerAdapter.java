package com.eyesee.airlauncher2.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * 所有程序页面的ViewPager适配器
 * @author mark
 */
public class MyViewPagerAdapter extends PagerAdapter {

	private ArrayList<View> viewList;

	public MyViewPagerAdapter(ArrayList<View> viewList) {
		super();
		this.viewList = viewList;
		Log.d("mark", viewList.toString());
	}

	@Override
	public int getCount() {
		return viewList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(viewList.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(viewList.get(position));
		return viewList.get(position);
	}

	
}
