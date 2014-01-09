package com.zdm.tools;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author zdm mail to :zdmbill@163.com 依次start多个service不是阻塞式
 * test
 */
public class MainActivity extends Activity implements SensorEventListener {
	private SensorManager sManager;
	private Sensor sLight, sShake;

	private boolean isopent = false;
	private Camera camera;
	
	private Button powerBt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Log.w("Tools", Thread.currentThread().getName());
		// Intent fSer=new Intent(this,FlashLightIntentService.class);
		// Log.w("Tools", "0");
		// startService(fSer);
		// Log.w("Tools", "1");
		// startService(fSer);
		// Log.w("Tools", "2");
		// startService(fSer);
		// Log.w("Tools", "3");
		// finish();
		// stopService(fSer);

		sManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		// sManager.getSensorList(Sensor.TYPE_GRAVITY);

		if (sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
			Toast.makeText(
					this,
					"TYPE_ACCELEROMETER "
							+ sManager.getSensorList(Sensor.TYPE_ACCELEROMETER)
									.size(), Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "NO TYPE_ACCELEROMETER", Toast.LENGTH_SHORT)
					.show();
		}

		List<Sensor> deviceSensors = sManager.getSensorList(Sensor.TYPE_ALL);
		TextView showTv = (TextView) findViewById(R.id.sensorTv);
		String sensorStr = "";
		for (Sensor s : deviceSensors) {
			sensorStr = sensorStr + s.getName() + ",\n";
		}

		showTv.setText(sensorStr);
		Log.w("", sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
				.getName() + "");
		sLight = sManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		sShake = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		powerBt = (Button) findViewById(R.id.powerBt);
		powerBt.setText("Open");

		powerBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!isopent) {
					Toast.makeText(getApplicationContext(), "您已经打开了手电筒",
							Toast.LENGTH_SHORT).show();
					powerBt.setText("Off");
					camera = Camera.open();
					Parameters params = camera.getParameters();
					params.setFlashMode(Parameters.FLASH_MODE_TORCH);
					camera.setParameters(params);
					camera.startPreview(); // 开始亮灯
					isopent = true;
				} else {
					Toast.makeText(getApplicationContext(), "关闭了手电筒",
							Toast.LENGTH_SHORT).show();
					powerBt.setText("Open");
					camera.stopPreview(); // 关掉亮灯
					camera.release(); // 关掉照相机
					isopent = false;
				}
			}
		});
	}
	//TODO 绑定锁屏/解锁
	//TODO 后台实现（用IntentService?）
	//TODO 屏幕各种颜色设定
	//TODO 开关时间设定

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	float[] gravity = new float[3], linear_acceleration = new float[3];

	@Override
	public void onSensorChanged(SensorEvent event) {
		//TODO 判断摇晃
		//TODO 摇晃程度判断&摇晃程度设定
		// In this example, alpha is calculated as t / (t + dT),
		// where t is the low-pass filter's time-constant and
		// dT is the event delivery rate.

		final float alpha = (float) 0.8;

		// Isolate the force of gravity with the low-pass filter.
		gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
		gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
		gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

		// Remove the gravity contribution with the high-pass filter.
		linear_acceleration[0] = event.values[0] - gravity[0];
		linear_acceleration[1] = event.values[1] - gravity[1];
		linear_acceleration[2] = event.values[2] - gravity[2];

		// TODO Auto-generated method stub
		String sName = event.sensor.getName();
		// Log.w("sensor", "x=" + event.values[0] + " y=" + event.values[1]
		// + " z=" + event.values[2] + " sum="
		// + (event.values[0] + event.values[1] + event.values[2]));
		Log.w("sensor", "gravity[0]=" + gravity[0] + "gravity[1]=" + gravity[1]
				+ "gravity[2]=" + gravity[2]);
		Log.w("sensor", "linear_[0]=" + linear_acceleration[0] + "linear_[1]="
				+ linear_acceleration[1] + "linear_[2]="
				+ linear_acceleration[2]);
		// Toast.makeText(this,
		// "现在sensor是"+sName+" value="+event.values[0]+event.sensor.getMaximumRange(),
		// Toast.LENGTH_LONG).show();
		// float lux = event.values[0];
		// Toast.makeText(this, "现在光亮是"+lux, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		sManager.unregisterListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// sManager.registerListener(this, sLight,
		// SensorManager.SENSOR_DELAY_NORMAL);
		// sManager.registerListener(this, sShake,
		// SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onDestroy() {
		Log.w("Tools", "destory!!");
		if (isopent) {
			camera.stopPreview(); // 关掉亮灯
			camera.release(); 
		}
		super.onDestroy();

		// System.exit(0);
	}

}
