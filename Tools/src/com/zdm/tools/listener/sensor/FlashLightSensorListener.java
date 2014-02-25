package com.zdm.tools.listener.sensor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import com.zdm.tools.audio.BackAudioPlay;

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
	// 最近的闹钟时间
	// 只保持1/2个时间，一个是正在闹的时间，一个是最近的闹钟时间
	private List<Calendar> nextAlarmClocks = new ArrayList<Calendar>();

	private boolean pressMenuFlag = false;

	private boolean playShake = true;
	private boolean playReg = true;

	private int shakeSensitive = 0;

	private int missAlarmClock = 0;
	private int missLastHook = 0;

	private SharedPreferences sp;
	private Editor editor;

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		Log.e("fl-flSListener", "not implemmented");
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		long currectTime = System.currentTimeMillis();

		if (currectTime - lastTime > 100) {
			float suma = event.values[0] + event.values[1] + event.values[2];
			long diffTime = currectTime - lastTime;
			float speed = Math.abs(suma - oldSuma) * 1000 / diffTime;
			// Log.w("fl-sensor", "speed=" + speed + " sum=" + suma);
			lastTime = currectTime;
			oldSuma = suma;
			// 建议speed在20以上，越小越灵敏
			if (speed > (2.5 * shakeSensitive)
					&& (currectTime - shakeTime) > 1500) {
				shake();
				shakeTime = currectTime;
			}
		}
	}

	public void shake() {
		if (checkLastHookTime() && !checkClockRing()) {
			on = !on;
			if (playShake) {
				BackAudioPlay.getInstance().playBackAudio("shake");
			}
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
			Log.w("fl-flSListener",
					"next cal = " + lastHookTime.get(Calendar.YEAR) + "-"
							+ (lastHookTime.get(Calendar.MONTH) + 1) + "-"
							+ lastHookTime.get(Calendar.DAY_OF_MONTH) + " "
							+ lastHookTime.get(Calendar.HOUR_OF_DAY) + ":"
							+ lastHookTime.get(Calendar.MINUTE) + ":"
							+ lastHookTime.get(Calendar.SECOND));
			Log.w("fl-flSListener", "最后挂机时间到现在小于" + missLastHook + "秒不shake");
		}
	}

	public void regFLListener() {
		if (checkLastHookTime()) {
			sManager.registerListener(this, sShake,
					SensorManager.SENSOR_DELAY_UI);
			if (playReg) {
				BackAudioPlay.getInstance().playBackAudio("beep");
			}
			Log.w("fl-flSListener", "regFLListener");
		} else {
			Log.w("fl-flSListener",
					"next cal = " + lastHookTime.get(Calendar.YEAR) + "-"
							+ (lastHookTime.get(Calendar.MONTH) + 1) + "-"
							+ lastHookTime.get(Calendar.DAY_OF_MONTH) + " "
							+ lastHookTime.get(Calendar.HOUR_OF_DAY) + ":"
							+ lastHookTime.get(Calendar.MINUTE) + ":"
							+ lastHookTime.get(Calendar.SECOND));
			Log.w("fl-flSListener", "最后挂机时间到现在小于" + missLastHook + "秒不reg");
		}
	}

	/**
	 * 通过判断最近的alarm clock 时间来判断闹钟是否在响 暂定30秒内都表示再闹
	 * 
	 * @return true:闹钟在响
	 */
	public boolean checkClockRing() {
		Calendar nowCal = Calendar.getInstance();
		// ==0表示没有闹钟无须考虑
		if (nextAlarmClocks.size() == 0) {
			return false;
		} else {
			Calendar cal = nextAlarmClocks.get(0);
			if (nowCal.getTimeInMillis() > cal.getTimeInMillis()
					&& nowCal.getTimeInMillis() - cal.getTimeInMillis() < missAlarmClock * 1000) {
				Log.w("fl-flSListener", "checkClockRing=true");
				return true;
			} else {
				Log.w("fl-flSListener", "checkClockRing=false");
				return false;
			}
		}
	}

	/**
	 * 检测当前时间到最后挂电话时间是否小于missLastHook秒
	 * 
	 * @return true:大于 false:小于
	 */
	public boolean checkLastHookTime() {
		if (lastHookTime == null) {
			return true;
		}
		Calendar cal=Calendar.getInstance();
		long diff=cal.getTimeInMillis()-lastHookTime.getTimeInMillis();

		String tmp=cal.get(Calendar.YEAR) + "-"
				+ (cal.get(Calendar.MONTH) + 1) + "-"
				+ cal.get(Calendar.DAY_OF_MONTH) + " "
				+ cal.get(Calendar.HOUR_OF_DAY) + ":"
				+ cal.get(Calendar.MINUTE) + ":"
				+ cal.get(Calendar.SECOND);
		Log.w("fl-flSListener", "checkLastHookTime diff="+diff+" now="+tmp);
		Log.w("fl-flSListener",
				"checkLastHookTime next cal = " + lastHookTime.get(Calendar.YEAR) + "-"
						+ (lastHookTime.get(Calendar.MONTH) + 1) + "-"
						+ lastHookTime.get(Calendar.DAY_OF_MONTH) + " "
						+ lastHookTime.get(Calendar.HOUR_OF_DAY) + ":"
						+ lastHookTime.get(Calendar.MINUTE) + ":"
						+ lastHookTime.get(Calendar.SECOND));
		
		return diff > missLastHook * 1000;
	}

	public void unRegFLListener() {
		if (on) {
			camera.stopPreview(); // 关掉亮灯
			camera.release(); // 关掉照相机
			on = false;
		}

		sManager.unregisterListener(this);
		if (playReg) {
			BackAudioPlay.getInstance().playBackAudio("beep");
		}
		Log.w("fl-flSListener", "unRegFLListener");
	}

	/**
	 * 例如当灯打开，屏幕关闭时
	 */
	public void unRegFLListenerOnly() {
		sManager.unregisterListener(this);
		if (playReg) {
			BackAudioPlay.getInstance().playBackAudio("beep");
		}
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
			sp = mContext.getSharedPreferences("SP", Context.MODE_PRIVATE);
			editor = sp.edit();
		}
	}

	public boolean isOn() {
		return on;
	}

	public void setOn(boolean on) {
		this.on = on;
	}

	public boolean isInCallflag() {
		Log.w("fl-flSListener", "get inCallflag=" + inCallflag);
		return inCallflag;
	}

	public void setInCallflag(boolean inCallflag) {
		this.inCallflag = inCallflag;
		Log.w("fl-flSListener", "set inCallflag=" + inCallflag);
	}

	public Calendar getLastHookTime() {
		return lastHookTime;
	}

	public synchronized void setLastHookTime(Calendar lastHookCal) {
		this.lastHookTime = lastHookCal;
	}

	public List<Calendar> getNextAlarmClocks() {
		return nextAlarmClocks;
	}

	public void setNextAlarmClocks(List<Calendar> nextAlarmClocks) {
		this.nextAlarmClocks = nextAlarmClocks;
	}

	public void addNextAlarmClock(Calendar cal) {
		Calendar nowCal = Calendar.getInstance();
		if (!nextAlarmClocks.contains(cal)) {
			for (Calendar c : nextAlarmClocks) {
				if (nowCal.getTimeInMillis() > c.getTimeInMillis()
						&& nowCal.getTimeInMillis() - c.getTimeInMillis() < 5 * 1000) {
					nextAlarmClocks.clear();
					nextAlarmClocks.add(c);
					nextAlarmClocks.add(cal);

					break;
				}
			}
			nextAlarmClocks.clear();
			nextAlarmClocks.add(cal);
		}
	}

	public boolean isPressMenuFlag() {
		return pressMenuFlag;
	}

	public void setPressMenuFlag(boolean pressMenuFlag) {
		this.pressMenuFlag = pressMenuFlag;
	}

	public void setPlayShake(boolean playShake) {
		this.playShake = playShake;
		editor.putBoolean("playShake", playShake);
		editor.commit();
	}

	public void setPlayReg(boolean playReg) {
		this.playReg = playReg;
		editor.putBoolean("playReg", playReg);
		editor.commit();
	}

	public boolean isPlayShake() {
		return playShake;
	}

	public boolean isPlayReg() {
		return playReg;
	}

	public void setShakeSensitive(int shakeSensitive) {
		this.shakeSensitive = shakeSensitive;
		editor.putInt("shakeSensitive", shakeSensitive);
		editor.commit();
	}

	public void setMissAlarmClock(int missAlarmClock) {
		this.missAlarmClock = missAlarmClock;
		editor.putInt("missAlarmClock", missAlarmClock);
		editor.commit();
	}

	public void setMissLastHook(int missLastHook) {
		this.missLastHook = missLastHook;
		editor.putInt("missLastHook", missLastHook);
		editor.commit();
	}

	public int getShakeSensitive() {
		if (shakeSensitive == 0) {
			shakeSensitive = sp.getInt("shakeSensitive", 40);
		}
		return shakeSensitive;
	}

	public int getMissAlarmClock() {
		if (missAlarmClock == 0) {
			missAlarmClock = sp.getInt("missAlarmClock", 30);
		}
		return missAlarmClock;
	}

	public int getMissLastHook() {
		if (missLastHook == 0) {
			missLastHook = sp.getInt("missLastHook", 10);
		}
		return missLastHook;
	}

}
