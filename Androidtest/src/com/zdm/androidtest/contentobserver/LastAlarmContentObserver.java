package com.zdm.androidtest.contentobserver;

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
	public LastAlarmContentObserver(Context mCtx,Handler handler) {
		super(handler);
		this.mCtx=mCtx;
		this.mHandler=handler;
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);

		Uri uri = Settings.System.getUriFor(Settings.System.NEXT_ALARM_FORMATTED);

		Cursor c = mCtx.getContentResolver().query(uri, null, null, null, null);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					for (int j = 0; j < c.getColumnCount(); j++) {
						Log.w("fl-laCObser",c.getColumnName(j) + " = " + c.getString(j));
					}
					mHandler.obtainMessage(1, c.getString(2)).sendToTarget();
				} while (c.moveToNext());
			}
		}	
	
	}

	
}
