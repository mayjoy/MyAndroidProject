package com.eyesee.sysupdate;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	@ViewInject(R.id.tv_button)
	private TextView tv_button;
	@ViewInject(R.id.ib_back)
	private ImageButton ib_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ViewUtils.inject(this);
		
	}
	
	@OnClick({R.id.ib_back,R.id.tv_button})
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.ib_back:
			finish();
			break;

		case R.id.tv_button:
			break;
		}
	}
}
