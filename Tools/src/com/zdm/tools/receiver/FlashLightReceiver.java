package com.zdm.tools.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zdm.tools.sensor.listener.FlashLightSensorListener;
import com.zdm.tools.services.FlashLightService;

public class FlashLightReceiver extends BroadcastReceiver {
	private FlashLightService flS;

	private FlashLightSensorListener flsl = FlashLightSensorListener
			.getInstance();

	public FlashLightReceiver() {
	}

	public FlashLightReceiver(FlashLightService flS) {
		this.flS = flS;
	}

	/*
	 * 如果不申请电源锁，service锁屏后实际是不工作的 防止某些桌面或者锁屏不是系统锁屏，主动unreg释放资源节省电量
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.w("broad", "receive " + flsl.isOn());
		flsl.setmContext(context);
		final String action = intent.getAction();

		if (Intent.ACTION_SCREEN_ON.equals(action)) {
			Log.w("broad", "screen is on...");

			flsl.regFLListener();

		} else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
			Log.w("broad", "screen is off...");

			// 屏幕关闭时，状态不变，仅仅unreg
			flsl.unRegFLListenerOnly();
			// startService(CloseIntent);
		} else if (Intent.ACTION_USER_PRESENT.equals(action)) {
			Log.w("broad", "ACTION_USER_PRESENT");
			if (context.getClass().getName()
					.equals("com.zdm.tools.services.FlashLightService")) {
				flsl.unRegFLListener();
			}
		}
	}
}
