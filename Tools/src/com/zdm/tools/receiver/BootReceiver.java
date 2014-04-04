package com.zdm.tools.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zdm.tools.services.FlashLightService;

public class BootReceiver extends BroadcastReceiver {

	static final String action_boot = "android.intent.action.BOOT_COMPLETED";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(action_boot)) {
			Intent ootStartIntent = new Intent(context, FlashLightService.class);
			ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(ootStartIntent);
		}

	}

}
