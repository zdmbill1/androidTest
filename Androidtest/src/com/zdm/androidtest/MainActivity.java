package com.zdm.androidtest;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	EditText et;
	String etText;
	TextView showTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.w("MainActivity", "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//add on local for test merge
		System.out.println("+++++++++++++++++");
		//add something on web
//		ActionBar actionBar = getActionBar();
//		actionBar.hide();

		// Button btn_send = (Button) findViewById(R.id.button1);
		// 直接调用destroy
		// finish();
		// 覆盖来layout里面设置的onclick方法
		/*
		 * btn_send.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Log.w("MainActivity",
		 * "clicksend");
		 * 
		 * } });
		 */
		
		SeekBar sk=(SeekBar) findViewById(R.id.sk);
		sk.setMax(200);
		showTv=(TextView) findViewById(R.id.showTv);
		
		sk.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				showTv.setText("完成拖动");
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				showTv.setText("开始拖动");
				
			}
			
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				showTv.setText("拖动ing:"+arg1);
				
			}
		});
		
		SearchView sv1=(SearchView) findViewById(R.id.searchView1);
		SearchManager searchManager = (SearchManager)
				 getSystemService(Context.SEARCH_SERVICE);
		sv1.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		sv1.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String arg0) {
				Toast.makeText(getApplicationContext(), arg0, Toast.LENGTH_SHORT).show();				
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String arg0) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.w("MainActivity", "onCreateOptionsMenu");
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		MenuItem mItem = menu.findItem(R.id.action_search);
//
//		MenuItemCompat.setOnActionExpandListener(mItem,
//				new OnActionExpandListener() {
//
//					@Override
//					public boolean onMenuItemActionExpand(MenuItem arg0) {
//						// TODO Auto-generated method stub
//						return false;
//					}
//
//					@Override
//					public boolean onMenuItemActionCollapse(MenuItem arg0) {
//						// TODO Auto-generated method stub
//						return false;
//					}
//				});

//		return super.onCreateOptionsMenu(menu);

//		 Inflate the options menu from XML
		 MenuInflater inflater = getMenuInflater();
		 inflater.inflate(R.menu.main, menu);
		
		 // Get the SearchView and set the searchable configuration
		 SearchManager searchManager = (SearchManager)
		 getSystemService(Context.SEARCH_SERVICE);
		 SearchView searchView = (SearchView)
		 menu.findItem(R.id.action_search).getActionView();
		 // Assumes current activity is the searchable activity
		 Log.w("MainActivity", getComponentName().toString());;
		 searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		 searchView.setIconifiedByDefault(true); 
		 searchView.setIconified(true);
		 // Do not iconify the widget; expand it by default
		
		 searchView.setSubmitButtonEnabled(true);
//		 searchView.setQueryRefinementEnabled(true);
		 return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
//		case R.id.action_search:
//			Log.w("MainActivity", "select search menu");
//
//			return super.onOptionsItemSelected(item);
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
		// startActivity(intent);
		startActivityForResult(intent, 1);
	}

	public void searchUI(View v) {
		onSearchRequested();
	}

	@Override
	public boolean onSearchRequested() {
		// 除了输入查询的值，还可额外绑定一些数据
		Bundle appSearchData = new Bundle();
		et = (EditText) findViewById(R.id.ed1);
		appSearchData.putString("demo_key", et.getText().toString());

		startSearch(null, false, appSearchData, false);
		// 必须返回true。否则绑定的数据作废
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Log.w("callback", data.getStringExtra("status"));
			setTitle("callback test");
			Toast.makeText(this, "call back", Toast.LENGTH_SHORT).show();
		}
		super.onActivityResult(requestCode, resultCode, data);
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
