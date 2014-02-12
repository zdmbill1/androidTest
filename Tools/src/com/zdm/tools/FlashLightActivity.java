package com.zdm.tools;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.zdm.tools.receiver.FlashLightReceiver;
import com.zdm.tools.sensor.listener.FlashLightSensorListener;
import com.zdm.tools.services.FlashLightService;

public class FlashLightActivity extends Activity {

	private Intent flIntent;

	private FlashLightSensorListener flsl = FlashLightSensorListener
			.getInstance();
	private FlashLightReceiver flReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flash_light);
		if (null != flsl.getmContext()) {
			Log.w("flAct", flsl.getmContext().getClass().getName());
			if (flsl.getmContext().getClass().getName()
					.equals("com.zdm.tools.services.FlashLightService")) {
				((FlashLightService) flsl.getmContext()).stopSelf();
			}
		}

		flsl.setmContext(this);
		flsl.regFLListener();
		flIntent = new Intent(this, FlashLightService.class);

		IntentFilter filter = new IntentFilter();
		flReceiver = new FlashLightReceiver();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_USER_PRESENT);
		registerReceiver(flReceiver, filter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.flash_light, menu);
		return true;
	}

	// @Override
	/*
	 * public boolean onKeyDown(int keyCode, KeyEvent event) { if (keyCode ==
	 * KeyEvent.KEYCODE_BACK) { Log.w("flAct", "press back"); if (!on) {
	 * flsl.unRegFLListener(); } // moveTaskToBack(true); // return true; }
	 * return super.onKeyDown(keyCode, event); }
	 */

	@Override
	protected void onPause() {
		super.onPause();
		Log.w("flAct", "pause unreg");
		flsl.unRegFLListenerOnly();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.w("flAct", "resume reg");
		flsl.regFLListener();
	}

	@Override
	protected void onDestroy() {
		Log.w("flAct", "destory!!");
		startService(flIntent);
		flsl.unRegFLListener();
		unregisterReceiver(flReceiver);
		super.onDestroy();

		// System.exit(0);
	}

	// 配置android:configChanges="orientation|screenSize"后无特殊目的可以不用重写onConfigurationChanged方法
	/*
	 * @Override public void onConfigurationChanged(Configuration newConfig) {
	 * Log.w("flAct", "ConfigurationChanged");
	 * super.onConfigurationChanged(newConfig); }
	 */
}
