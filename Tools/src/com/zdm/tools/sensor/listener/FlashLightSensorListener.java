package com.zdm.tools.sensor.listener;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class FlashLightSensorListener implements SensorEventListener {

	private static FlashLightSensorListener instance = new FlashLightSensorListener();

	private FlashLightSensorListener() {

	}

	public static FlashLightSensorListener getInstance() {
		return instance;
	}

	private float oldSuma = 0;
	private long lastTime = 0;
	private long shakeTime = 0;
	private boolean on = false;
	private Camera camera;

	private SensorManager sManager;
	private Sensor sShake;
	private Context mContext;

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		Log.e("flSListener", "not implemmented");
	}

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

	private void shake() {
		on = !on;
		if (on) {
			Log.w("flSListener", "打开手电筒");
			camera = Camera.open();
			Parameters params = camera.getParameters();
			params.setFlashMode(Parameters.FLASH_MODE_TORCH);
			camera.setParameters(params);
			camera.startPreview(); // 开始亮灯
		} else {
			Log.w("flSListener", "关闭手电筒");
			camera.stopPreview(); // 关掉亮灯
			camera.release(); // 关掉照相机
		}
	}

	public void regFLListener() {

		sManager.registerListener(this, sShake, SensorManager.SENSOR_DELAY_UI);
	}

	public void unRegFLListener() {
		if (on) {
			camera.stopPreview(); // 关掉亮灯
			camera.release(); // 关掉照相机
			on = false;
		}
		sManager.unregisterListener(this);
	}
	
	/**
	 * 例如当灯打开，屏幕关闭时
	 */
	public void unRegFLListenerOnly(){
		sManager.unregisterListener(this);
	}

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
		sManager = (SensorManager) mContext
				.getSystemService(Context.SENSOR_SERVICE);
		sShake = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}

	public boolean isOn() {
		return on;
	}

	public void setOn(boolean on) {
		this.on = on;
	}

}
