package com.zdm.tools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.zdm.tools.audio.BackAudioPlay;
import com.zdm.tools.contentobserver.LastAlarmContentObserver;
import com.zdm.tools.listener.phone.FlPhoneListener;
import com.zdm.tools.listener.sensor.FlashLightSensorListener;
import com.zdm.tools.receiver.FlashLightReceiver;
import com.zdm.tools.services.FlashLightService;

//TODO 需要解决turn_on广播收到不及时的问题，off的时候不unreg也没影响？
//TODO 增加各种设置以及保存,晃动声音，感光，晃动强弱，电筒开启时间，闹钟/挂电话时间。。
//紧紧华为手机Calendar.getInstance()报java.lang.NumberFormatException: Invalid int: ""错误？
public class MainActivity extends Activity {

	private Intent flIntent;

	private FlashLightSensorListener flsl = FlashLightSensorListener
			.getInstance();
	private FlashLightReceiver flReceiver;

	private ToggleButton powerTbt;

	private FlPhoneListener flpl = new FlPhoneListener();
	private TelephonyManager tm;

	private LastAlarmContentObserver laObser;

	private ListView setLv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (null != flsl.getmContext()) {
			Log.w("fl-flAct", flsl.getmContext().getClass().getName());
			if (flsl.getmContext().getClass().getName()
					.equals(FlashLightService.class.getName())) {
				((FlashLightService) flsl.getmContext()).stopSelf();
			}
		}


		Calendar c = Calendar.getInstance();
		c.add(Calendar.SECOND, -5);
		flsl.setLastHookTime(c);

//		Calendar c = Calendar.getInstance();
//		c.add(Calendar.SECOND, -5);
//		flsl.setLastHookTime(c);
		
		flsl.setmContext(this);
		flsl.regFLListener();

		flIntent = new Intent(this, FlashLightService.class);

		IntentFilter filter = new IntentFilter();
		flReceiver = new FlashLightReceiver();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_USER_PRESENT);
		// filter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
		registerReceiver(flReceiver, filter);

		powerTbt = (ToggleButton) findViewById(R.id.toggleButton1);
		// 点击按钮响应click,check状态改变不响应
		powerTbt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				flsl.shake();				
			}
		});

		// powerTbt.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton arg0, boolean isChecked)
		// {
		//
		// }
		// });

		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		tm.listen(flpl, PhoneStateListener.LISTEN_CALL_STATE);

		// 获取最近的alarm的信息
		Uri uri = Settings.System
				.getUriFor(Settings.System.NEXT_ALARM_FORMATTED);
		laObser = new LastAlarmContentObserver(this, new Handler());
		getContentResolver().registerContentObserver(uri, false, laObser);

		flsl.setPressMenuFlag(false);

		setLv = (ListView) findViewById(R.id.setLv);
		List<String> data = new ArrayList<String>();
        data.add("测试数据1");
        data.add("测试数据2");
        data.add("测试数据3");
        data.add("测试数据4");         
        
		setLv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data));

		BackAudioPlay.getInstance();
//		sp=new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
//		spMap.put("beep", sp.load(this, R.raw.beep, 1));
		
//		BackAudioPlay.getInstance().playBackAudio(R.raw.shake);
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// @Override
	/*
	 * public boolean onKeyDown(int keyCode, KeyEvent event) { if (keyCode ==
	 * KeyEvent.KEYCODE_BACK) { Log.w("fl-flAct", "press back"); if (!on) {
	 * flsl.unRegFLListener(); } // moveTaskToBack(true); // return true; }
	 * return super.onKeyDown(keyCode, event); }
	 */

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		flsl.setPressMenuFlag(true);
		Log.w("fl-flAct", "press menu");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.w("fl-flAct", "pause unreg");
		flsl.unRegFLListenerOnly();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.w("fl-flAct", "resume");
		if (!flsl.isInCallflag()) {
			Log.w("fl-flAct", "resume reg");
			flsl.regFLListener();
		}
		flsl.setPressMenuFlag(false);
		
	}

	@Override
	protected void onDestroy() {
		Log.w("fl-flAct", "destory!!");

		flsl.unRegFLListener();
		tm.listen(flpl, PhoneStateListener.LISTEN_NONE);
		unregisterReceiver(flReceiver);

		startService(flIntent);

		getContentResolver().unregisterContentObserver(laObser);
		super.onDestroy();

		// System.exit(0);
	}

	// 配置android:configChanges="orientation|screenSize"后无特殊目的可以不用重写onConfigurationChanged方法
	/*
	 * @Override public void onConfigurationChanged(Configuration newConfig) {
	 * Log.w("fl-flAct", "ConfigurationChanged");
	 * super.onConfigurationChanged(newConfig); }
	 */
}
