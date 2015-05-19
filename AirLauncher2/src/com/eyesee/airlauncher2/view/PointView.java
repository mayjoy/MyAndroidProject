package com.eyesee.airlauncher2.view;

import com.eyesee.airlauncher2.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 页面指示小圆点
 * 
 * @author mark
 */
public class PointView extends ImageView {

	public PointView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public PointView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PointView(Context context) {
		super(context);
		init();
	}

	private void init() {
		this.setImageResource(R.drawable.point_no_selected);
	}

	/**
	 * 设置选中效果
	 */
	public void setChecked() {
		this.setImageResource(R.drawable.point_selected);
	}
	
	/**
	 * 设置未选中的效果
	 */
	public void setDisChecked(){
		this.setImageResource(R.drawable.point_no_selected);
	}
}
