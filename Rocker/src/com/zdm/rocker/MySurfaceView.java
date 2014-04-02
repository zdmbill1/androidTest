package com.zdm.rocker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * @author bill 弧度=角度*pi/180
 * 
 */
public class MySurfaceView extends SurfaceView implements Callback, Runnable {

	private SurfaceHolder sh;
	private Paint paint;
	private Canvas canvas;
	private boolean flag = true;
	private int screenx, screeny;

	private float x = 0, y = 0;
	private double angle = 0;
	private float centerX = 0, centerY = 0;

	public MySurfaceView(Context context) {
		super(context);
		sh = getHolder();
		// 添加监听
		sh.addCallback(this);
		paint = new Paint();
		paint.setColor(Color.BLACK);
		// 设置无锯齿
		paint.setAntiAlias(true);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		screenx = getWidth();
		screeny = getHeight();
		Thread th = new Thread(this);
		flag = true;
		th.start();
		centerX = x = screenx / 2;
		centerY = y = screeny / 2;

	}

	public void myDraw() {
		try {
			canvas = sh.lockCanvas();
			canvas.drawColor(Color.WHITE);
			paint.setColor(Color.RED);
			paint.setAlpha(100);
			canvas.drawCircle(centerX, centerY, 200, paint);
			paint.setAlpha(255);
			canvas.drawCircle(x, y, 20, paint);
		} catch (Exception e) {
		} finally {
			if (canvas != null) {
				sh.unlockCanvasAndPost(canvas);
			}
		}

	}

	private void logic() {
		// angle = angle + 1;
		// if (angle >= 360) {
		// angle = 0;
		// }
		// x = centerX + (float) (Math.sin(Math.toRadians(angle)) * 200);
		// y = centerY - (float) (Math.cos(Math.toRadians(angle)) * 200);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {

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

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float touchX = event.getX();
		float touchY = event.getY();

		// 根据2点坐标计算弧度,rlen=斜边,rad=弧度
		double rlen = Math.sqrt(Math.pow(touchX - centerX, 2)
				+ Math.pow(touchY - centerY, 2));
		if (rlen <= 200) {
			x = touchX;
			y = touchY;
		} else {
			double rad = Math.asin((touchX - centerX) / rlen);
			// if (touchY > centerY) {
			// rad = -rad;
			// }
			angle = Math.toDegrees(rad);
			//分4象界考虑，1不变，2，3，4依次改变
			if (touchX > centerX && touchY >= centerY) {
				angle = 180 - angle;
			} else if (touchX <= centerX && touchY > centerY) {
				angle = 180 - angle;
			} else if (touchX < centerX && touchY <= centerY) {
				angle = 360 + angle;
			}
			Log.w("rocker", "angle=" + angle + "  rad=" + rad);
			x = centerX + (float) (Math.sin(Math.toRadians(angle)) * 200);
			y = centerY - (float) (Math.cos(Math.toRadians(angle)) * 200);
		}
		return true;
	}

}
