package com.zdm.tools;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;

import com.zdm.tools.services.FlashLightService;

public class FlashLightActivity extends Activity {

	private FlashLightService flSer;
	private Intent flIntent;
	private SensorManager sManager;
	private boolean on=false;
	private Sensor sShake;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flash_light);
		flIntent=new Intent(this, FlashLightService.class);
		
		ComponentName c=startService(flIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.flash_light, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.w("flAct", "press back");
			if (!on) {
				sManager.unregisterListener(flSer);
			}
			moveTaskToBack(true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (!on) {
			Log.w("flAct", "pause unreg");
			sManager.unregisterListener(flSer);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		sManager.registerListener(flSer, sShake, SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	protected void onDestroy() {
		Log.w("flAct", "destory!!");
		stopService(flIntent);
		sManager.unregisterListener(flSer);
		super.onDestroy();

		// System.exit(0);
	}
}
