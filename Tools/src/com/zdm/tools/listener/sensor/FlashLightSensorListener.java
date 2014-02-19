package com.zdm.tools.listener.sensor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.ToggleButton;

import com.zdm.tools.MainActivity;
import com.zdm.tools.R;

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
	// 来电:true
	private boolean inCallflag = false;
	// 最后挂机时间
	private Calendar lastHookTime;
	//最近的闹钟时间
	private List<Calendar> nextAlarmClocks=new ArrayList<Calendar>();

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
			// Log.w("fl-sensor", "speed=" + speed + " sum=" + suma);
			lastTime = currectTime;
			oldSuma = suma;
			// 建议speed在20以上，越小越灵敏
			if (speed > 100 && (currectTime - shakeTime) > 1500) {
				shake();
				shakeTime = currectTime;
			}
		}
	}

	public void shake() {
		if (checkLastHookTime()) {
			on = !on;
			if (on) {
				Log.w("fl-flSListener", "打开手电筒");
				camera = Camera.open();
				Parameters params = camera.getParameters();
				params.setFlashMode(Parameters.FLASH_MODE_TORCH);
				camera.setParameters(params);
				camera.startPreview(); // 开始亮灯
			} else {
				Log.w("fl-flSListener", "关闭手电筒");
				camera.stopPreview(); // 关掉亮灯
				camera.release(); // 关掉照相机
			}

			if (null != mContext
					&& mContext.getClass().getName()
							.equals(MainActivity.class.getName())) {
				Activity a = (Activity) mContext;
				ToggleButton tb = (ToggleButton) a
						.findViewById(R.id.toggleButton1);
				tb.setChecked(on);
			}
		} else {
			Log.w("fl-flSListener", "最后挂机时间到现在小于5秒不shake");	
		}
	}

	public void regFLListener() {
		if (checkLastHookTime()) {
			sManager.registerListener(this, sShake,
					SensorManager.SENSOR_DELAY_UI);
			Log.w("fl-flSListener", "regFLListener");
		} else {
			Log.w("fl-flSListener", "最后挂机时间到现在小于5秒不reg");
		}
	}
	
	/**
	 * 通过判断最近的alarm clock 时间来判断闹钟是否在响
	 * @return
	 * true:闹钟在响
	 */
	public boolean checkClockRing(){
		Calendar cal=Calendar.getInstance();
		for(int i=nextAlarmClocks.size()-1;i>=0;i--){
			if(cal.getTimeInMillis()>nextAlarmClocks.get(i).getTimeInMillis()){
				if(cal.getTimeInMillis()-nextAlarmClocks.get(i).getTimeInMillis()>30*1000){
					nextAlarmClocks.remove(i);
				}else{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 检测当前时间到最后挂电话时间是否小于5秒
	 * 
	 * @return true:大于 false:小于
	 */
	public boolean checkLastHookTime() {
		return Calendar.getInstance().getTimeInMillis()
				- lastHookTime.getTimeInMillis() > 5000;
	}

	public void unRegFLListener() {
		if (on) {
			camera.stopPreview(); // 关掉亮灯
			camera.release(); // 关掉照相机
			on = false;
		}

		sManager.unregisterListener(this);
		Log.w("fl-flSListener", "unRegFLListener");
	}

	/**
	 * 例如当灯打开，屏幕关闭时
	 */
	public void unRegFLListenerOnly() {
		sManager.unregisterListener(this);
		Log.w("fl-flSListener", "unRegFLListenerOnly");
	}

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
		if (mContext != null) {
			sManager = (SensorManager) mContext
					.getSystemService(Context.SENSOR_SERVICE);
			sShake = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		}
	}

	public boolean isOn() {
		return on;
	}

	public void setOn(boolean on) {
		this.on = on;
	}

	public boolean isInCallflag() {
		Log.w("flSListener", "get inCallflag=" + inCallflag);
		return inCallflag;
	}

	public void setInCallflag(boolean inCallflag) {
		this.inCallflag = inCallflag;
		Log.w("flSListener", "set inCallflag=" + inCallflag);
	}

	public Calendar getLastHookTime() {
		return lastHookTime;
	}

	public void setLastHookTime(Calendar lastHookCal) {
		this.lastHookTime = lastHookCal;
	}

	public List<Calendar> getNextAlarmClocks() {
		return nextAlarmClocks;
	}

	public void setNextAlarmClocks(List<Calendar> nextAlarmClocks) {
		this.nextAlarmClocks = nextAlarmClocks;
	}

	public void addNextAlarmClock(Calendar cal){
		if(!nextAlarmClocks.contains(cal)){
			nextAlarmClocks.add(cal);
			Collections.reverse(nextAlarmClocks);
		}
	}
}
