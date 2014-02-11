package com.zdm.tools.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;

import com.zdm.tools.services.FlashLightService;

public class FlashLightReceiver extends BroadcastReceiver {
	private SensorManager sManager;
	private Sensor sShake;
	private boolean on = false;
	private FlashLightService flSer;

	public FlashLightReceiver() {
	}

	public FlashLightReceiver(SensorManager sManager, Sensor sShake,
			boolean on, FlashLightService flSer) {
		super();
		this.sManager = sManager;
		this.sShake = sShake;
		this.on = on;
		this.flSer = flSer;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("broad", "receive");
		final String action = intent.getAction();

		if (Intent.ACTION_SCREEN_ON.equals(action)) {

			Log.w("broad", "screen is on...");
			sManager.registerListener(flSer, sShake,
					SensorManager.SENSOR_DELAY_UI);
			on = true;
		} else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
			Log.w("broad", "screen is off...");
			on = false;
			sManager.unregisterListener(flSer);

			// startService(CloseIntent);
		} else if (Intent.ACTION_USER_PRESENT.equals(action)) {
			Log.w("broad", "ACTION_USER_PRESENT");
			on = false;
			sManager.unregisterListener(flSer);
		}
	}
}
