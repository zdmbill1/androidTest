package com.zdm.tools.intentservice;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class FlashLightIntentService extends IntentService {

	public FlashLightIntentService() {
		super("MyIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.w("flISer", "start IntentService "+Thread.currentThread().getName());
		long endTime=System.currentTimeMillis()+10*1000;
		synchronized (this) {
			if(System.currentTimeMillis()<=endTime){
				try {
					wait(endTime-System.currentTimeMillis());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
//			while(true){
//			int sum=0;
//			for(int i=0;i<10000000;i++){
//				sum=sum+i;
//			}
//			Log.w("test", sum+"");
//			}
		}
		Log.w("flISer", "stop IntentService");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.w("FlashLightIntentService", "destory!!");
	}
	
	
}
