package com.example.gameview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.example.gameview.R;

/**
 * 关于使用图片资源问题 在不同分辨率密度的机器上使用不同目录的资源，如果没有则系统自动对突破放大缩小 DisplayMetrics dm=new
 * DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(dm);
 * 
 * dm.densityDpi 120 --drawable-ldpi 160 --drawable-mdpi 240 --drawable-hdpi
 * 
 * @author bill
 * 
 */
public class MySurfaceView extends SurfaceView implements Callback, Runnable {

	private SurfaceHolder sh;
	private Paint paint;
	private float x = 20, y = 20;

	private boolean flag = true;
	private int screenx, screeny;

	private Canvas canvas;
	private Bitmap bm;
	private Bitmap[] fishMaps = new Bitmap[10];
	private Bitmap fish;
	private int currectFrame = 0;

	private Bitmap robot;
	private int currectRobot = 0;
	// 每次跑动的距离
	private int runlen = 0;
	private int runlenturn = 0;
	private boolean DIR_RIGHT = true;

	public MySurfaceView(Context context) {
		super(context);

		sh = getHolder();
		// 添加监听
		sh.addCallback(this);
		paint = new Paint();
		paint.setColor(Color.BLACK);
		// 设置无锯齿
		paint.setAntiAlias(true);

		for (int i = 0; i < fishMaps.length; i++) {
			fishMaps[i] = BitmapFactory.decodeResource(getResources(),
					R.drawable.fish0 + i);
		}

		bm = BitmapFactory.decodeResource(getResources(), R.drawable.water);
		fish = BitmapFactory.decodeResource(getResources(), R.drawable.fish);
		robot = BitmapFactory.decodeResource(getResources(), R.drawable.robot);
	}

	public void myDraw() {
		try {
			canvas = sh.lockCanvas();
			canvas.drawColor(Color.WHITE);
			// canvas.drawText("surface view", x, y, paint);
			// canvas.drawText("surface view", x1, y1, paint);
			// canvas.drawText("surface view", x2, y2, paint);

			// canvas.drawPoint(x, y, paint);
			// canvas.drawPoint(x1, y1, paint);
			// canvas.drawPoint(x2, y2, paint);

			// canvas.drawCircle(x, y, 5, paint);
			// canvas.drawCircle(x1, y2, 5, paint);
			// canvas.drawCircle(x2, y2, 5, paint);

			// path.lineTo(x, y);
			// path.lineTo(30, 30);
			// path.lineTo(30, 60);
			// path.close();
			// canvas.drawPath(path, paint);

			// canvas.drawLine(x1, y1, x, y, paint);

			canvas.drawText(
					"width=" + bm.getWidth() + ",height=" + bm.getHeight(),
					screenx / 2, screeny / 2, paint);
			canvas.drawText("screenx" + screenx + ",screeny=" + screeny,
					screenx / 2, screeny / 2 + 20, paint);
			// canvas.save();
			// 对画布的旋转
			// canvas.rotate(60,bm.getWidth()/2,bm.getHeight()/2);
			// canvas.drawBitmap(bm, 0, 0, paint);
			// canvas.restore();
			// canvas.clipRect(0, 0, 72, 72);
			// Matrix mx=new Matrix();
			// mx.postRotate(60,bm.getWidth()/2,bm.getHeight()/2);
			// canvas.drawBitmap(bm, mx, paint);
			// canvas.scale(2, 2,72+bm.getWidth()/2,bm.getHeight()/2);
			// canvas.drawBitmap(bm, 72, 0, paint);
			//
			// AlphaAnimation aa=new AlphaAnimation(0.2f, 1);
			// aa.setDuration(3000);
			// startAnimation(aa);

			canvas.drawBitmap(bm, x, y, paint);
			canvas.drawBitmap(fishMaps[currectFrame],
					(screenx - fishMaps[currectFrame].getWidth()) / 2, screeny
							- fishMaps[currectFrame].getHeight(), paint);

			// 剪切图
			canvas.save();
			// 显示在坐标fx,fy
			int fx = screenx / 2;
			int fy = screeny - fish.getHeight();

			canvas.clipRect(fx, fy, fx + fish.getWidth() / 10,
					fy + fish.getHeight());
			canvas.drawBitmap(fish, fx - currectFrame * fish.getWidth() / 10,
					fy, paint);
			canvas.restore();

			canvas.save();
			fx = 0;
			fy = 20;
			canvas.clipRect(fx, fy, fx + robot.getWidth() / 6,
					fy + robot.getHeight() / 2);
			canvas.drawBitmap(robot, fx - currectRobot % 6 * robot.getWidth()
					/ 6, fy - currectRobot / 6 * robot.getHeight() / 2, paint);
			canvas.restore();

			canvas.save();
			fx = screenx - robot.getWidth() / 6;
			fy = 20;
			canvas.clipRect(fx, fy, fx + robot.getWidth() / 6,
					fy + robot.getHeight() / 2);
			canvas.scale(-1, 1, fx + robot.getWidth() / 12,
					fy + robot.getHeight() / 2);
			canvas.drawBitmap(robot, fx - currectRobot % 6 * robot.getWidth()
					/ 6, fy - currectRobot / 6 * robot.getHeight() / 2, paint);
			canvas.restore();

			canvas.save();
			fx = runlen;
			fy = 20 + robot.getHeight() / 2;
			canvas.clipRect(fx, fy, fx + robot.getWidth() / 6,
					fy + robot.getHeight() / 2);
			canvas.drawBitmap(robot, fx - currectRobot % 6 * robot.getWidth()
					/ 6, fy - currectRobot / 6 * robot.getHeight() / 2, paint);
			canvas.restore();

			canvas.save();
			fx = runlenturn;
			fy = 20 + robot.getHeight();
			canvas.clipRect(fx, fy, fx + robot.getWidth() / 6,
					fy + robot.getHeight() / 2);
			if (!DIR_RIGHT) {
				canvas.scale(-1, 1, fx + robot.getWidth() / 12,
						fy + robot.getHeight() / 2);
			}
			canvas.drawBitmap(robot, fx - currectRobot % 6 * robot.getWidth()
					/ 6, fy - currectRobot / 6 * robot.getHeight() / 2, paint);
			canvas.restore();

		} catch (Exception e) {
		} finally {
			if (canvas != null) {
				sh.unlockCanvasAndPost(canvas);
			}
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// myDraw();
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// myDraw();
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		screenx = getWidth();
		screeny = getHeight();

		x = screenx - bm.getWidth();
		y = screeny - bm.getHeight();
		Thread th = new Thread(this);
		flag = true;
		runlen = -robot.getWidth() / 6;
		runlenturn = -robot.getWidth() / 6;
		// myDraw();
		th.start();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		flag = false;
	}

	private void logic() {
		x = x + 5;
		if (x > 0) {
			x = screenx - bm.getWidth();
		}
		currectFrame = currectFrame + 1;
		if (currectFrame >= fishMaps.length) {
			currectFrame = 0;
		}
		currectRobot = currectRobot + 1;
		if (currectRobot >= 12) {
			currectRobot = 0;
		}

		runlen = runlen + 5;
		if (runlen >= screenx) {
			runlen = -robot.getWidth() / 6;
		}

		if (DIR_RIGHT) {
			runlenturn = runlenturn + 5;
			if (runlenturn >= screenx) {
				runlenturn = screenx;
				DIR_RIGHT = false;
			}
		} else {
			runlenturn = runlenturn - 5;
			if (runlenturn <= -robot.getWidth() / 6) {
				runlenturn = -robot.getWidth() / 6;
				DIR_RIGHT = true;
			}
		}
	}

	@Override
	public void run() {
		while (flag) {
			long start = System.currentTimeMillis();
			myDraw();
			logic();
			long end = System.currentTimeMillis();
			try {
				if (end - start < 50) {
					Thread.sleep(50 - (end - start));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
