package com.example.gameview.view;

import com.example.gameview.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements Callback, Runnable {

	private SurfaceHolder sh;
	private Paint paint;
	private float x = 20, y = 20;
	private float x1 = 20, y1 = 20;
	private float x2 = 20, y2 = 20;
	private boolean flag = true;
	private int screenx, screeny;
	private Canvas canvas;
	private Path path=new Path();

	public MySurfaceView(Context context) {
		super(context);
		sh = getHolder();
		// 添加监听
		sh.addCallback(this);
		paint = new Paint();
		paint.setColor(Color.BLACK);
		//设置无锯齿
		paint.setAntiAlias(true);
	}

	public void myDraw() {
		try {
			canvas = sh.lockCanvas();
			canvas.drawColor(Color.WHITE);
			// canvas.drawText("surface view", x, y, paint);
			// canvas.drawText("surface view", x1, y1, paint);
			// canvas.drawText("surface view", x2, y2, paint);

//			canvas.drawPoint(x, y, paint);
//			canvas.drawPoint(x1, y1, paint);
//			canvas.drawPoint(x2, y2, paint);
			
//			canvas.drawCircle(x, y, 5, paint);
//			canvas.drawCircle(x1, y2, 5, paint);
//			canvas.drawCircle(x2, y2, 5, paint);
			
//			path.lineTo(x, y);
//			path.lineTo(30, 30);
//			path.lineTo(30, 60);
//			path.close();
//			canvas.drawPath(path, paint);
			
//			canvas.drawLine(x1, y1, x, y, paint);
			
			Bitmap bm=BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
			canvas.drawText("width="+bm.getWidth()+",height="+bm.getHeight(), screenx/2, screeny/2, paint);
			canvas.save();
			//对画布的旋转
//			canvas.rotate(60,bm.getWidth()/2,bm.getHeight()/2);
//			canvas.drawBitmap(bm, 0, 0, paint);
//			canvas.restore();
			
			Matrix mx=new Matrix();
			mx.postRotate(60,bm.getWidth()/2,bm.getHeight()/2);
			canvas.drawBitmap(bm, mx, paint);
			canvas.scale(2, 2,72+bm.getWidth()/2,bm.getHeight()/2);
			canvas.drawBitmap(bm, 72, 0, paint);
			
			
		} catch (Exception e) {
		} finally {
			if (canvas != null) {
				sh.unlockCanvasAndPost(canvas);
			}
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		x2 = x1;
		y2 = y1;

		x1 = x;
		y1 = y;

		

		x = event.getX();
		y = event.getY();
		if (event.getAction() == MotionEvent.ACTION_UP) {
			x2 = x;
			y2 = y;

			x1 = x;
			y1 = y;
		}
		
		myDraw();
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
//		myDraw();
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		screenx = getWidth();
		screeny = getHeight();
		Thread th = new Thread(this);
		flag = true;
		
		
		// th.start();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		flag = false;
	}

	@Override
	public void run() {
		while (flag) {
			y = y + 10;
			if (y > screeny) {
				y = 20;
			}
			myDraw();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
