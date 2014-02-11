package com.zdm.tools.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Camera.Parameters;
import android.os.IBinder;
import android.util.Log;

import com.zdm.tools.receiver.FlashLightReceiver;

public class FlashLightService extends Service implements SensorEventListener {

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("flSer", "create FlashLightService");

		SensorManager sManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		Sensor sShake = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		boolean on = false;
		sManager.registerListener(this, sShake, SensorManager.SENSOR_DELAY_UI);
			
		IntentFilter filter = new IntentFilter();
		FlashLightReceiver flReceiver = new FlashLightReceiver(sManager, sShake,
				on, this);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_USER_PRESENT);
		registerReceiver(flReceiver, filter);

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		Log.e("flSer", "not implemmented");
	}

	private float oldSuma = 0;
	private long lastTime = 0;
	private long shakeTime = 0;
	private boolean on=false;
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO 摇晃程度判断&摇晃程度设定

		long currectTime = System.currentTimeMillis();

		if (currectTime - lastTime > 100) {
			float suma = event.values[0] + event.values[1] + event.values[2];
			long diffTime = currectTime - lastTime;
			float speed = Math.abs(suma - oldSuma) * 1000 / diffTime;
			// Log.w("sensor", "speed=" + speed + " sum=" + suma);
			lastTime = currectTime;
			oldSuma = suma;
			// 建议speed在20以上，越小越灵敏
			if (speed > 100 && (currectTime - shakeTime) > 1500) {
				shake();
				shakeTime = currectTime;
			}
		}
	}
	
	private Camera camera;
	
	private void shake() {
		on=!on;
		if (on) {
			Log.w("flSer", "打开手电筒");
			camera = Camera.open();
			Parameters params = camera.getParameters();
			params.setFlashMode(Parameters.FLASH_MODE_TORCH);
			camera.setParameters(params);
			camera.startPreview(); // 开始亮灯
		} else {
			Log.w("flSer", "关闭手电筒");
			camera.stopPreview(); // 关掉亮灯
			camera.release(); // 关掉照相机
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("flSer", "destroy FlashLightService");
	}
}
