package com.zdm.tools;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.zdm.tools.intentservice.FlashLightIntentService;

/**
 * @author zdm
 * mail to :zdmbill@163.com
 * 依次start多个service不是阻塞式
 */
public class MainActivity extends Activity implements SensorEventListener{
	private SensorManager sManager;
	private Sensor sLight,sShake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Log.w("Tools", Thread.currentThread().getName());
        Intent fSer=new Intent(this,FlashLightIntentService.class);
        Log.w("Tools", "0");
        startService(fSer);
        Log.w("Tools", "1");
        startService(fSer);
        Log.w("Tools", "2");
        startService(fSer);
        Log.w("Tools", "3");
//        finish();
        stopService(fSer);
        
        sManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);

        
//        sManager.getSensorList(Sensor.TYPE_GRAVITY);
        
        if(sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null){
        	Toast.makeText(this, "TYPE_ACCELEROMETER "+sManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size(), Toast.LENGTH_SHORT).show();
        }else{
        	Toast.makeText(this, "NO TYPE_ACCELEROMETER", Toast.LENGTH_SHORT).show();
        }
        
        List<Sensor> deviceSensors = sManager.getSensorList(Sensor.TYPE_ALL);
        TextView showTv=(TextView) findViewById(R.id.sensorTv);
        String sensorStr = "";
        for(Sensor s:deviceSensors){
        	sensorStr=sensorStr+s.getName()+",\n";
        }
        
        showTv.setText(sensorStr);
        Log.w("", sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER).getName()+"");
        sLight=sManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sShake=sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }


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


	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		String sName=event.sensor.getName();
		Log.w("sensor", "x="+event.values[0]+" y="+event.values[1]+" z="+event.values[2]+" sum="+(event.values[0]+event.values[1]+event.values[2]));
//		Toast.makeText(this, "现在sensor是"+sName+" value="+event.values[0]+event.sensor.getMaximumRange(), Toast.LENGTH_LONG).show();
//		 float lux = event.values[0];
//		 Toast.makeText(this, "现在光亮是"+lux, Toast.LENGTH_LONG).show();
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
//		sManager.registerListener(this, sLight, SensorManager.SENSOR_DELAY_NORMAL);
		sManager.registerListener(this, sShake, SensorManager.SENSOR_DELAY_NORMAL);
	}
    
	@Override
	protected void onDestroy() {
		Log.w("Tools", "destory!!");
		setContentView(R.layout.viewnull);
		super.onDestroy();

//		System.exit(0);
	}
	
}
