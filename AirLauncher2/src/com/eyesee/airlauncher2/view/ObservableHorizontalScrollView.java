package com.eyesee.airlauncher2.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class ObservableHorizontalScrollView extends HorizontalScrollView {

	private HorizontalScrollViewListener horizontalScrollViewListener = null;

	public ObservableHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public ObservableHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ObservableHorizontalScrollView(Context context) {
		super(context);
	}

	public void setScrollViewListener(
			HorizontalScrollViewListener horizontalScrollViewListener) {
		this.horizontalScrollViewListener = horizontalScrollViewListener;
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (horizontalScrollViewListener != null) {
			horizontalScrollViewListener
					.onScrollChanged(this, x, y, oldx, oldy);
		}
	}

}
