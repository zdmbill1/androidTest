package com.zdm.tools.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

/**
 * @author zdm
 * service中不单独创建thread的话，还是在主线程中
 *
 */
public class FlashLightService extends Service {
	public FlashLightService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Toast.makeText(this, "start service", Toast.LENGTH_LONG).show();
//		long endTime=System.currentTimeMillis()+10*1000;
//		synchronized (this) {
//			if(System.currentTimeMillis()<=endTime){
//				try {
//					wait(endTime-System.currentTimeMillis());
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
		Toast.makeText(this, "stop service", Toast.LENGTH_LONG).show();
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}
	
	
}
