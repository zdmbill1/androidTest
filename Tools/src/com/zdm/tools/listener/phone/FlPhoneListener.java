package com.zdm.tools.listener.phone;

import com.zdm.tools.listener.sensor.FlashLightSensorListener;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class FlPhoneListener extends PhoneStateListener {

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		switch (state) {
		case TelephonyManager.CALL_STATE_IDLE:  
			Log.w("fl-flPListener", "手机挂机");
			FlashLightSensorListener.getInstance().setInCallflag(false);
            break;  
        case TelephonyManager.CALL_STATE_RINGING:  
        	Log.w("fl-flPListener", "手机铃声响了，来电号码:"+incomingNumber+"unreg");
        	FlashLightSensorListener.getInstance().setInCallflag(true);
        	FlashLightSensorListener.getInstance().unRegFLListener();
        	
            break;  
        case TelephonyManager.CALL_STATE_OFFHOOK: 
        	Log.w("fl-flPListener", "接听电话");        	
        default:  
            break;  
        }  
		super.onCallStateChanged(state, incomingNumber);
	}

	
}
