package com.zdm.tools;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.zdm.tools.intent.CloseFlashlightIS;

/**
 * @author zdm mail to :zdmbill@163.com ����start���service��������ʽ
 * test
 *         android:keepScreenOn="true"������Ļ����
 * 
 */
public class MainActivity extends Activity implements SensorEventListener {
	private SensorManager sManager;
	private Sensor sShake;

	// private boolean isopent = false;
	private Camera camera;

	private ToggleButton powerTbt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		sShake = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		if (sShake == null) {
			//��֧��shake
		} 
		
onLowMemory();
		powerTbt = (ToggleButton) findViewById(R.id.toggleButton1);

		powerTbt.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {
					Log.w("switch", "���Ѿ������ֵ�Ͳ");
					camera = Camera.open();
					Parameters params = camera.getParameters();
					params.setFlashMode(Parameters.FLASH_MODE_TORCH);
					camera.setParameters(params);
					camera.startPreview(); // ��ʼ����
				} else {
					Log.w("switch", "���Ѿ��ر����ֵ�Ͳ");
					camera.stopPreview(); // �ص�����
					camera.release(); // �ص������
				}

			}
		});

		// powerBt.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// shake();
		// }
		// });

		// �ڽ���ǰ����
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		// moveTaskToBack(true);

		final IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_USER_PRESENT);
		registerReceiver(mBatInfoReceiver, filter);

		CloseIntent = new Intent(this, CloseFlashlightIS.class);
	}

	private Intent CloseIntent;

	private boolean on = false;
	private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			final String action = intent.getAction();
			if (Intent.ACTION_SCREEN_ON.equals(action)) {

				Log.w("broad", "screen is on...");
				sManager.registerListener(MainActivity.this, sShake,
						SensorManager.SENSOR_DELAY_UI);
				on = true;
			} else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
				Log.w("broad", "screen is off...");
				on = false;
				sManager.unregisterListener(MainActivity.this);

				startService(CloseIntent);
			} else if (Intent.ACTION_USER_PRESENT.equals(action)) {
				Log.w("broad", "ACTION_USER_PRESENT");
				on = false;
				sManager.unregisterListener(MainActivity.this);
			}
		}
	};

	// TODO ��Ļ������ɫ�趨
	// TODO ����ʱ���趨

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	float[] gravity = new float[3], linear_acceleration = new float[3];

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO ҡ�γ̶��ж�&ҡ�γ̶��趨

		long currectTime = System.currentTimeMillis();

		if (currectTime - lastTime > 100) {
			float suma = event.values[0] + event.values[1] + event.values[2];
			long diffTime = currectTime - lastTime;
			float speed = Math.abs(suma - oldSuma) * 1000 / diffTime;
			// Log.w("sensor", "speed=" + speed + " sum=" + suma);
			lastTime = currectTime;
			oldSuma = suma;
			// ����speed��20���ϣ�ԽСԽ����
			if (speed > 100 && (currectTime - shakeTime) > 1500) {
				shake();
				shakeTime = currectTime;
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.w("key", "press back");
			if (!on) {
				sManager.unregisterListener(MainActivity.this);
			}
			moveTaskToBack(true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private float oldSuma = 0;
	private long lastTime = 0;
	private long shakeTime = 0;

	private void shake() {
		if (powerTbt.isChecked()) {
			Log.w("shake", "���Ѿ������ֵ�Ͳ");
			powerTbt.toggle();
		} else {
			Log.w("shake", "���Ѿ��ر����ֵ�Ͳ");
			powerTbt.toggle();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (!on) {
			Log.w("", "pause unreg");
			sManager.unregisterListener(MainActivity.this);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		sManager.registerListener(this, sShake, SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	protected void onDestroy() {
		Log.w("Tools", "destory!!");
		if (!powerTbt.isChecked()) {
			camera.stopPreview(); // �ص�����
			camera.release();
		}
		unregisterReceiver(mBatInfoReceiver);
		sManager.unregisterListener(this);
		super.onDestroy();

		// System.exit(0);
	}

}
