package com.zdm.androidtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	EditText et;
	String etText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.w("MainActivity", "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btn_send = (Button) findViewById(R.id.button1);
		
		
		//直接调用destroy
//		finish();
		// 覆盖来layout里面设置的onclick方法
		/*
		 * btn_send.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Log.w("MainActivity",
		 * "clicksend");
		 * 
		 * } });
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.w("MainActivity", "onCreateOptionsMenu");
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search:
			Log.w("MainActivity", "select search menu");
			return true;
		case R.id.action_settings:
			Log.w("MainActivity", "select settings menu");
			return true;
		default:
			// TODO Auto-generated method stub
			return super.onOptionsItemSelected(item);
		}

	}

	public void sendMsg(View v) {
		Intent intent = new Intent(this, DisplayActivity.class);
		et = (EditText) findViewById(R.id.ed1);
		intent.putExtra("extraMsg", et.getText().toString());
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		Log.w("MainActivity", "onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		Log.w("MainActivity", "onPause");
		super.onPause();
	}

	@Override
	protected void onRestart() {
		Log.w("MainActivity", "onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		Log.w("MainActivity", "onResume");
		super.onResume();
	}

	@Override
	protected void onStart() {
		Log.w("MainActivity", "onStart");
		super.onStart();
	}

	@Override
	protected void onStop() {
		Log.w("MainActivity", "onStop");
		super.onStop();
		android.os.Debug.stopMethodTracing();
	}

}
