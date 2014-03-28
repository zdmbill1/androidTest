package com.zdm.tools.services;

import com.zdm.tools.listener.sensor.FlashLightSensorListener;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class TimingService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.w("fl-TimingService", "create");
		if(FlashLightSensorListener.getInstance().isOn()){
			FlashLightSensorListener.getInstance().setOn(false);
			FlashLightSensorListener.getInstance().operateCamera(false);
		}
	}
}
