package com.zdm.tools.contentobserver;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.zdm.tools.listener.sensor.FlashLightSensorListener;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

public class LastAlarmContentObserver extends ContentObserver {

	private Context mCtx;
	private Handler mHandler;

	private Map<String, Integer> weekMap = new HashMap<String, Integer>() {
		{
			put("周一", Calendar.MONDAY);
			put("周二", Calendar.TUESDAY);
			put("周三", Calendar.WEDNESDAY);
			put("周四", Calendar.THURSDAY);
			put("周五", Calendar.FRIDAY);
			put("周六", Calendar.SATURDAY);
			put("周日", Calendar.SUNDAY);
		}
	};

	public LastAlarmContentObserver(Context mCtx, Handler handler) {
		super(handler);
		this.mCtx = mCtx;
		this.mHandler = handler;
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);

		Uri uri = Settings.System
				.getUriFor(Settings.System.NEXT_ALARM_FORMATTED);

		Cursor c = mCtx.getContentResolver().query(uri, null, null, null, null);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					Log.w("fl-laCObser", "next alarm = " + c.getString(2));
					if (!c.getString(2).equals("")) {
						String[] tmp = c.getString(2).split(" ");
						Calendar cal = Calendar.getInstance();
						cal.set(Calendar.DAY_OF_WEEK, weekMap.get(tmp[0]));
						int hour = Integer.parseInt(tmp[1].split(":")[0]);
						int minute = Integer.parseInt(tmp[1].split(":")[1]);
						cal.set(Calendar.HOUR_OF_DAY, hour);
						cal.set(Calendar.MINUTE, minute);
						cal.set(Calendar.SECOND, 0);
						Log.w("fl-laCObser",
								"next cal = " + cal.get(Calendar.YEAR) + "-"
										+ (cal.get(Calendar.MONTH) + 1) + "-"
										+ cal.get(Calendar.DAY_OF_MONTH) + " "
										+ cal.get(Calendar.HOUR_OF_DAY) + ":"
										+ cal.get(Calendar.MINUTE));
						FlashLightSensorListener.getInstance()
								.addNextAlarmClock(cal);
						// mHandler.obtainMessage(1,
						// c.getString(2)).sendToTarget();
					}
				} while (c.moveToNext());
			}
		}

	}

}
