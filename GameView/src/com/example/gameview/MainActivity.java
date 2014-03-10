package com.example.gameview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.example.gameview.view.MySurfaceView;
import com.example.gameview.view.MyView;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
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
		FileOutputStream fos = null;
		String filePath = Environment.getExternalStorageDirectory().getPath()
				+ "/p.xml";

		File f = new File(filePath);
		Properties p=new Properties();
		p.setProperty("name", "zdm");
		p.setProperty("id", "1");
		
		
		try {
			if (!f.exists()) {
				f.createNewFile();
			}
			// openFileOutput模拟器有错误read only file system
			fos = new FileOutputStream(filePath);
//			fos.write("ttt".getBytes());
			p.storeToXML(fos, "test");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }

}
