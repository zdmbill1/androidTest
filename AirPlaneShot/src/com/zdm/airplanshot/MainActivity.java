package com.zdm.airplanshot;

import com.zdm.airplanshot.view.MySurfaceView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 不要标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 不要状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(new MySurfaceView(this));
	}
	

}
