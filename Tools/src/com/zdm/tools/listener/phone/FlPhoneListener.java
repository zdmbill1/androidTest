package com.zdm.tools.listener.phone;

import java.util.Calendar;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.zdm.tools.listener.sensor.FlashLightSensorListener;

public class FlPhoneListener extends PhoneStateListener {

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		switch (state) {
		case TelephonyManager.CALL_STATE_IDLE:  
			Log.w("fl-flPListener", "手机挂机");
			FlashLightSensorListener.getInstance().setInCallflag(false);
			Calendar c = Calendar.getInstance();
			//第一次启动，设置时间推迟5秒
			if(FlashLightSensorListener.getInstance().getLastHookTime()==null){
				c.add(Calendar.SECOND, -5);

				FlashLightSensorListener.getInstance().setLastHookTime(c);
			}
			FlashLightSensorListener.getInstance().setLastHookTime(c);
            break;  
        case TelephonyManager.CALL_STATE_RINGING:  
        	Log.w("fl-flPListener", "手机铃声响了，来电号码:"+incomingNumber+"unreg");
        	FlashLightSensorListener.getInstance().setInCallflag(true);
        	FlashLightSensorListener.getInstance().unRegFLListener();
        	
            break;  
        case TelephonyManager.CALL_STATE_OFFHOOK: 
        	Log.w("fl-flPListener", "接听电话");        	
        	FlashLightSensorListener.getInstance().setInCallflag(true);
        	FlashLightSensorListener.getInstance().unRegFLListener();
        default:  
            break;  
        }  
		super.onCallStateChanged(state, incomingNumber);
	}

	
}
