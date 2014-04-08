package com.zdm.tools.receiver;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zdm.tools.services.FlashLightService;

/**
 * @author bill 接收系统广播，每分钟检测下
 */
public class CheckReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		boolean isServiceRunning = false;
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if ("com.zdm.tools.services.FlashLightService"
					.equals(service.service.getClassName()))
			// Service的类名
			{
				isServiceRunning = true;
			}

		}
		Log.w("fl-check", "check ");
		if (!isServiceRunning) {
			Intent i = new Intent(context, FlashLightService.class);
			context.startService(i);
		}

	}

}
