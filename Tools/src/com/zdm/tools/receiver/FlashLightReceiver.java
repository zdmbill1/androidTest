package com.zdm.tools.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zdm.tools.listener.sensor.FlashLightSensorListener;
import com.zdm.tools.services.FlashLightService;

public class FlashLightReceiver extends BroadcastReceiver {
	private FlashLightSensorListener flsl = FlashLightSensorListener
			.getInstance();

	public FlashLightReceiver() {
	}

	/*
	 * 如果不申请电源锁，service锁屏后实际是不工作的 防止某些桌面或者锁屏不是系统锁屏，主动unreg释放资源节省电量
	 */
	@Override
	public void onReceive(Context context, Intent intent) {

		flsl.setmContext(context);
		final String action = intent.getAction();

		Log.w("fl-broad", action + " is ordered " + isOrderedBroadcast());

		if (Intent.ACTION_SCREEN_ON.equals(action)) {
			Log.w("fl-broad", "screen is on...");
			if (!flsl.isInCallflag()) {
				flsl.regFLListener();
				Log.w("fl-broad", "screen is on reg");
			}
			// Toast.makeText(context, "手电筒正常工作", Toast.LENGTH_LONG).show();

		} else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
			Log.w("fl-broad", "screen is off...");

			// 屏幕关闭默认service不工作，为了屏幕打开就能摇晃，不unreg
			// 屏幕关闭时，状态不变，仅仅unreg
			// flsl.unRegFLListenerOnly();
			// startService(CloseIntent);
			// 新增判断是否按了menu键判断
		} else if (Intent.ACTION_USER_PRESENT.equals(action)) {
			Log.w("fl-broad", "ACTION_USER_PRESENT");
			if (context.getClass().getName()
					.equals(FlashLightService.class.getName())
					|| flsl.isPressHomeFlag()) {
				flsl.unRegFLListener();
			}
		}
	}
}
