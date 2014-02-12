package com.zdm.tools;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

import com.zdm.tools.receiver.FlashLightReceiver;
import com.zdm.tools.sensor.listener.FlashLightSensorListener;
import com.zdm.tools.services.FlashLightService;

//TODO 需要解决turn_on广播收到不及时的问题
//TODO 考虑来电，闹钟特殊情况
//TODO 增加各种设置以及保存
public class MainActivity extends Activity {

	private Intent flIntent;

	private FlashLightSensorListener flsl = FlashLightSensorListener
			.getInstance();
	private FlashLightReceiver flReceiver;

	private ToggleButton powerTbt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (null != flsl.getmContext()) {
			Log.w("flAct", flsl.getmContext().getClass().getName());
			if (flsl.getmContext().getClass().getName()
					.equals(FlashLightService.class.getName())) {
				((FlashLightService) flsl.getmContext()).stopSelf();
			}
		}

		flsl.setmContext(this);
		flsl.regFLListener();
		flIntent = new Intent(this, FlashLightService.class);

		IntentFilter filter = new IntentFilter();
		flReceiver = new FlashLightReceiver();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_USER_PRESENT);
		registerReceiver(flReceiver, filter);

		powerTbt = (ToggleButton) findViewById(R.id.toggleButton1);
		//点击按钮响应click,check状态改变不响应
		powerTbt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				flsl.shake();
				
			}
		});

//		powerTbt.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
//				
//			}
//		});
		
		
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
	 * KeyEvent.KEYCODE_BACK) { Log.w("flAct", "press back"); if (!on) {
	 * flsl.unRegFLListener(); } // moveTaskToBack(true); // return true; }
	 * return super.onKeyDown(keyCode, event); }
	 */

	@Override
	protected void onPause() {
		super.onPause();
		Log.w("flAct", "pause unreg");
		flsl.unRegFLListenerOnly();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.w("flAct", "resume reg");
		flsl.regFLListener();
	}

	@Override
	protected void onDestroy() {
		Log.w("flAct", "destory!!");
		startService(flIntent);
		flsl.unRegFLListener();
		unregisterReceiver(flReceiver);
		super.onDestroy();

		// System.exit(0);
	}

	// 配置android:configChanges="orientation|screenSize"后无特殊目的可以不用重写onConfigurationChanged方法
	/*
	 * @Override public void onConfigurationChanged(Configuration newConfig) {
	 * Log.w("flAct", "ConfigurationChanged");
	 * super.onConfigurationChanged(newConfig); }
	 */
}
