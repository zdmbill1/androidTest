package com.zdm.tools.intent;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class update intent actions and extra parameters.
 */
public class CloseFlashlightIS extends IntentService {

	public CloseFlashlightIS() {
		super("CloseFlashlightIS");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent != null) {
			final String action = intent.getAction();
			long endTime = System.currentTimeMillis() + 10 * 1000;
			Log.w("CloseFlashlightIS", "sleep start");
			synchronized (this) {
				if (System.currentTimeMillis() <= endTime) {
					try {
						wait(endTime - System.currentTimeMillis());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
			Log.w("CloseFlashlightIS", "sleep over");
		}
	}	
}
