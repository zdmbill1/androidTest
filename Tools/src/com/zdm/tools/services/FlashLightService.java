package com.zdm.tools.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.zdm.tools.listener.phone.FlPhoneListener;
import com.zdm.tools.listener.sensor.FlashLightSensorListener;
import com.zdm.tools.receiver.FlashLightReceiver;

/**
 * @author bill 申请电源锁后锁屏继续工作
 * 
 */
public class FlashLightService extends Service {

	private FlashLightSensorListener flsl = FlashLightSensorListener
			.getInstance();

	private FlashLightReceiver flReceiver;

	private WakeLock mWakeLock;

	private FlPhoneListener flpl = new FlPhoneListener();
	private TelephonyManager tm;

	// 申请设备电源锁
	private void acquireWakeLock() {
		if (null == mWakeLock) {
			PowerManager pm = (PowerManager) this
					.getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK
					| PowerManager.ON_AFTER_RELEASE, "");
			if (null != mWakeLock) {
				mWakeLock.acquire();
			}
		}
	}

	// 释放设备电源锁
	private void releaseWakeLock() {
		if (null != mWakeLock) {
			mWakeLock.release();
			mWakeLock = null;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.w("fl-flSer", "create FlashLightService");

		flsl.setmContext(this);
		// flsl.regFLListener();

		IntentFilter filter = new IntentFilter();
		flReceiver = new FlashLightReceiver();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_USER_PRESENT);
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		registerReceiver(flReceiver, filter);

		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		tm.listen(flpl, PhoneStateListener.LISTEN_CALL_STATE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(flReceiver);
		flsl.setmContext(null);
		tm.listen(flpl, PhoneStateListener.LISTEN_NONE);
		Log.w("fl-flSer", "destroy FlashLightService");
	}
}
