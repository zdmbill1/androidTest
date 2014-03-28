package com.zdm.tools.receiver;

import com.zdm.tools.listener.sensor.FlashLightSensorListener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TimingReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		Log.w("fl-TimingReceiver", "create");
		if (FlashLightSensorListener.getInstance().isOn()) {
			FlashLightSensorListener.getInstance().setOn(false);
			FlashLightSensorListener.getInstance().operateCamera(false);
		}
	}

}
