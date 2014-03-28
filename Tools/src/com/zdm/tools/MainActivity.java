package com.zdm.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SimpleAdapter;
import android.widget.ToggleButton;

import com.zdm.tools.adapter.MyListAdapter;
import com.zdm.tools.audio.BackAudioPlay;
import com.zdm.tools.contentobserver.LastAlarmContentObserver;
import com.zdm.tools.listener.phone.FlPhoneListener;
import com.zdm.tools.listener.sensor.FlashLightSensorListener;
import com.zdm.tools.receiver.FlashLightReceiver;
import com.zdm.tools.services.FlashLightService;

//TODO 需要解决turn_on广播收到不及时的问题，off的时候不unreg也没影响？
//TODO 增加各种设置以及保存,晃动声音，感光，晃动强弱，电筒开启时间，闹钟/挂电话时间。。
//仅仅华为手机Calendar.getInstance()报java.lang.NumberFormatException: Invalid int: ""错误？
/**
 * CursorLoader会当数据更新时自动更新，类似于ContentObserver,继承LoaderManager.LoaderCallbacks<
 * Cursor> oncreate方法中初始化getLoaderManager().initLoader(0, null, this);
 * 在onLoadFinished方法中填充adapter的游标数据scAdapter.swapCursor(data);
 * 
 * @author bill
 * 
 */

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

	private SharedPreferences sp;

	private SimpleAdapter advancedAdapter;

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

		flsl.setmContext(this);

		flsl.setLastHookTime(null);

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
				flsl.clickShake();
			}
		});

		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		tm.listen(flpl, PhoneStateListener.LISTEN_CALL_STATE);

		// 获取最近的alarm的信息
		Uri uri = Settings.System
				.getUriFor(Settings.System.NEXT_ALARM_FORMATTED);
		laObser = new LastAlarmContentObserver(this, new Handler());
		getContentResolver().registerContentObserver(uri, false, laObser);

		setLv = (ListView) findViewById(R.id.setLv);
		List<String> data = new ArrayList<String>();
		data.add("测试数据1");
		data.add("测试数据2");
		data.add("测试数据3");
		data.add("测试数据4");

		ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(this,
				R.layout.setlist, data);
		setLv.setAdapter(aAdapter);

		// Cursor cur = getContentResolver().query(Phone.CONTENT_URI, null,
		// null,
		// null, Phone.DISPLAY_NAME);
		//
		// scAdapter = new SimpleCursorAdapter(this, R.layout.setlist, cur,
		// new String[] { Phone.DISPLAY_NAME, Phone.NUMBER }, new int[] {
		// R.id.textView1, R.id.checkBox1 },
		// CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		// setLv.setAdapter(scAdapter);

		sp = getSharedPreferences("SP", Context.MODE_PRIVATE);

		ArrayList<String[]> mData = new ArrayList<String[]>();
		mData.add(new String[] { "摇晃声音",
				String.valueOf(sp.getBoolean("playShake", true)) });
		mData.add(new String[] { "锁屏/解锁声音",
				String.valueOf(sp.getBoolean("playReg", false)) });

		flsl.setPlayShake(sp.getBoolean("playShake", true));
		flsl.setPlayReg(sp.getBoolean("playReg", false));

		Log.w("fl-flAct", "oncreate playReg=" + flsl.isPlayReg());
		MyListAdapter mListAdapter = new MyListAdapter(mData, this,
				R.layout.setlist);
		setLv.setAdapter(mListAdapter);

		ListView advancedSetLv = (ListView) findViewById(R.id.advancedSetLv);
		advanceData = new ArrayList<Map<String, String>>();
		createAdvanceData();
		advancedAdapter = new SimpleAdapter(this, advanceData,
				android.R.layout.simple_list_item_2, new String[] {
						"displayName", "displayValue" }, new int[] {
						android.R.id.text1, android.R.id.text2 });
		advancedSetLv.setAdapter(advancedAdapter);

		advancedSetLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				// 摇晃灵敏度 shakeSensitive
				case 0:
					shakeB.setView(createSeekBar());
					shakeSetDialog = shakeB.show();
					break;
				// 闹钟无效时间 missAlarmClock
				case 1:
					clockB.setSingleChoiceItems(new String[] { "10秒", "15秒",
							"30秒", "45秒", "60秒", "90秒", "120秒" },
							getCheckedId(),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									switch (which) {
									case 0:
										missAlarmClock = 10;
										break;
									case 1:
										missAlarmClock = 15;
										break;
									case 2:
										missAlarmClock = 30;
										break;
									case 3:
										missAlarmClock = 45;
										break;
									case 4:
										missAlarmClock = 60;
										break;
									case 5:
										missAlarmClock = 90;
										break;
									case 6:
										missAlarmClock = 120;
										break;
									}
								}
							});
					clockSetDialog = clockB.show();
					break;
				// 最后挂机无效时间 missLastHook
				case 2:
					// NumberPicker np = new
					// NumberPicker(getApplicationContext());
					// np.setMaxValue(100);
					// np.setMinValue(1);
					// np.setValue(flsl.getMissLastHook());
					// np.setWrapSelectorWheel(false);
					//
					// np.setOnValueChangedListener(new
					// NumberPicker.OnValueChangeListener() {
					//
					// @Override
					// public void onValueChange(NumberPicker picker,
					// int oldVal, int newVal) {
					// missLastHook = newVal;
					//
					// }
					// });

					// hookB.setView(np);
					// hookSetDialog = hookB.show();
					new NumberPickerDialog(MainActivity.this,
							new NumberPickerDialog.OnNumberSetListener() {

								@Override
								public void onNumberSet(int currentInt) {
									flsl.setMissLastHook(currentInt);
									createAdvanceData();
									advancedAdapter.notifyDataSetChanged();
								}
							}, flsl.getMissLastHook(), 1, 100, "最后挂机无效时间设置")
							.show();
					break;
				default:
					break;
				}

			}
		});

		BackAudioPlay.getInstance();
		// sp=new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
		// spMap.put("beep", sp.load(this, R.raw.beep, 1));

		// BackAudioPlay.getInstance().playBackAudio(R.raw.shake);

		shakeB = new Builder(this);
		shakeB.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				shakeSetDialog.dismiss();
			}
		});

		shakeB.setPositiveButton("设置", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				flsl.setShakeSensitive(shakeSensitive + 1);
				createAdvanceData();
				advancedAdapter.notifyDataSetChanged();
				shakeSetDialog.dismiss();
			}
		});
		shakeB.setCancelable(false);
		shakeB.setTitle("摇晃灵敏度设置");

		clockB = new Builder(this);
		clockB.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				clockSetDialog.dismiss();
			}
		});

		clockB.setPositiveButton("设置", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				flsl.setMissAlarmClock(missAlarmClock);
				createAdvanceData();
				advancedAdapter.notifyDataSetChanged();
				clockSetDialog.dismiss();
			}
		});
		clockB.setCancelable(false);
		clockB.setTitle("闹钟无效时间设置");

		// hookB = new Builder(this);
		// hookB.setNegativeButton("取消", new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		//
		// }
		// });
		//
		// hookB.setPositiveButton("设置", new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// flsl.setMissLastHook(missLastHook);
		// createAdvanceData();
		// advancedAdapter.notifyDataSetChanged();
		// }
		// });
		// hookB.setCancelable(false);
		// hookB.setTitle("最后挂机无效时间设置");

		flsl.regFLListener();

		// // 设定一个五秒后的时间
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTimeInMillis(System.currentTimeMillis());
		// calendar.add(Calendar.SECOND, 5);
		//
		// // 包装需要执行Service的Intent
		// Intent intent = new Intent(this, TimingService.class);
		// PendingIntent pendingIntent = PendingIntent.getService(this, 0,
		// intent,
		// PendingIntent.FLAG_ONE_SHOT);
		//
		// AlarmManager alarm = (AlarmManager)
		// getSystemService(Context.ALARM_SERVICE);
		// alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
		// pendingIntent);

		// 包装需要执行Service的Intent
//		Intent intent = new Intent(this, TimingReceiver.class);
//		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
//				intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//		AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//		alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//				pendingIntent);
	}

	public int getCheckedId() {
		switch (flsl.getMissAlarmClock()) {
		case 10:
			return 0;
		case 15:
			return 1;
		case 30:
			return 2;
		case 45:
			return 3;
		case 60:
			return 4;
		case 90:
			return 5;
		case 120:
			return 6;
		}
		return 0;
	}

	public void createAdvanceData() {
		advanceData.clear();
		Map<String, String> shakeMap = new HashMap<String, String>();

		shakeMap.put("displayName", "摇晃灵敏度");
		shakeMap.put("displayValue", flsl.getShakeSensitive() + "%");
		advanceData.add(shakeMap);

		Map<String, String> alarmClockMap = new HashMap<String, String>();
		alarmClockMap.put("displayName", "闹钟无效时间");
		alarmClockMap.put("displayValue", "闹钟响起" + flsl.getMissAlarmClock()
				+ "秒内摇晃无效");
		advanceData.add(alarmClockMap);

		Map<String, String> lastHookMap = new HashMap<String, String>();
		lastHookMap.put("displayName", "最后挂机无效时间");
		lastHookMap.put("displayValue", "电话挂机后" + flsl.getMissLastHook()
				+ "秒内摇晃无效");
		advanceData.add(lastHookMap);
	}

	public SeekBar createSeekBar() {
		SeekBar shakeSeekBar = new SeekBar(getApplicationContext());
		shakeSeekBar.setMax(99);
		shakeSeekBar.setProgress(flsl.getShakeSensitive());
		shakeSensitive = flsl.getShakeSensitive();
		shakeSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				shakeSensitive = progress;
			}
		});
		return shakeSeekBar;
	}

	// private SimpleCursorAdapter scAdapter;

	private AlertDialog shakeSetDialog;
	private AlertDialog clockSetDialog;
	// private AlertDialog hookSetDialog;
	private Builder shakeB;
	private Builder clockB;
	// private Builder hookB;

	private int shakeSensitive = 0;
	private List<Map<String, String>> advanceData;

	private int missAlarmClock = 0;

	// private int missLastHook = 0;

	// // Called when a new Loader needs to be created
	// public Loader<Cursor> onCreateLoader(int id, Bundle args) {
	// // Now create and return a CursorLoader that will take care of
	// // creating a Cursor for the data being displayed.
	// return new CursorLoader(this, Phone.CONTENT_URI,
	// null, null, null, Phone.DISPLAY_NAME);
	// }
	//
	// // Called when a previously created loader has finished loading
	// public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
	// // Swap the new cursor in. (The framework will take care of closing the
	// // old cursor once we return.)
	// scAdapter.swapCursor(data);
	// Log.w("fl-flAct", "onLoadFinished");
	// }
	//
	// // Called when a previously created loader is reset, making the data
	// unavailable
	// public void onLoaderReset(Loader<Cursor> loader) {
	// // This is called when the last Cursor provided to onLoadFinished()
	// // above is about to be closed. We need to make sure we are no
	// // longer using it.
	// scAdapter.swapCursor(null);
	// Log.w("fl-flAct", "onLoaderReset");
	// }

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }

	@Override
	protected void onPause() {
		super.onPause();
		Log.w("fl-flAct", "pause unreg");
		flsl.setPressHomeFlag(true);
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
		flsl.setPressHomeFlag(false);

	}

	@Override
	protected void onDestroy() {
		Log.w("fl-flAct", "destory!!");

		flsl.unRegFLListener();
		tm.listen(flpl, PhoneStateListener.LISTEN_NONE);
		unregisterReceiver(flReceiver);

		startService(flIntent);

		getContentResolver().unregisterContentObserver(laObser);
		flsl.setPressHomeFlag(false);

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
