package com.eyesee.airlauncher2.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;

public class OrientatorService extends Service {
	private static final String TAG = "OrientatorService";
	private View mView = null;
	private WindowManager mWM = null;

	public OrientatorService() {

	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			initialize();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private void initialize() {		
		WindowManager.LayoutParams lp = null;
		mView = new View(getApplicationContext());
		mWM = ((WindowManager) getSystemService(Context.WINDOW_SERVICE));
		lp = new WindowManager.LayoutParams(-2, -2, 2010, 40, -2);
		lp.screenOrientation = 0;
		lp.width = 1;
		lp.height = 1;
		lp.screenBrightness = -1.0F;
		lp.gravity = 51;
		mWM.addView(mView, lp);
	}
}